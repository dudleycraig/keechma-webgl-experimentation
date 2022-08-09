(ns main.controllers.stage
  (:require [three]

            [promesa.core :as promesa]

            [keechma.next.core :as keechma]
            [keechma.next.event :as event]
            [keechma.next.controller :as keechma-controller]
            [keechma.next.controllers.pipelines :as controllers-pipelines]
            [keechma.next.controllers.entitydb :as entitydb]
            [keechma.next.controllers.dataloader :as controllers-dataloader]
            [keechma.pipelines.core :as pipelines :refer-macros [pipeline!]]
            [com.verybigthings.state-machete.core :as fsm]

            [main.lib.fsm :refer [handle register]]))

(derive :stage ::controllers-pipelines/controller)

(def config
  {:canvas {:dimensions {:width 600 :height 600}}
   :renderer {:antialias true}
   :scene {:camera {:fov 30 :aspect 0.2 :near 1 :far 50000000 :zoom 1 :position #js[0 0 100]}}})

(defn initialize-stage [config container]
  (promesa/create
   (fn [resolve reject]
     (let [{{{:keys [width height] :or {width 600 height 600}} :dimensions} :canvas} config
           {{{:keys [fov aspect near far] :or {fov 30 aspect 0.2 near 1 far 50000000}} :camera} :scene} config
           {{:keys [antialias] :or {antialias true}} :renderer} config
           ;; scene (three/Scene.)
           ;; camera (three/PerspectiveCamera. fov aspect near far)
           ;; renderer (three/WebGLRenderer. #js {"antialias" antialias})
           ;; geometry (three/BoxGeometry. 100 100 100)
           ;; material (three/MeshBasicMaterial. #js {"color" 0x00ff00})
           ;; cube (three/Mesh. geometry material)
       ]
       ;; (. renderer setPixelRatio (. js/window -devicePixelRatio))
       ;; (. renderer setSize width height)
       ;; (. container appendChild (. renderer -domElement))
       ;; (.add scene cube)
       ;; (set! (.. camera -position -z) 120)
       ;; (defn animate []
       ;;   (js/requestAnimationFrame animate)
       ;;   (set! (.. cube -rotation -x) (+ (.. cube -rotation -x) 0.01))
       ;;   (set! (.. cube -rotation -y) (+ (.. cube -rotation -y) 0.01))
       ;;   (.render renderer scene camera))
       ;; (animate)
       (resolve)))))

(def state-machine
  [:fsm/root

   ;; inert state
   [:fsm/state#inert
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (fsm/send (fsm/update-data state-machine merge data) {:fsm/event ::init}))}
    [:fsm/transition #:fsm.transition
                      {:event :on-init-response
                       :target :active
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (fsm/update-data state-machine merge data))}]]

   ;; active state
   [:fsm/state#active
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (fsm/send state-machine {:fsm/event ::active}))}
    [:fsm/transition #:fsm.transition
                      {:event :on-active-response
                       :target :success}]
    [:fsm/transition #:fsm.transition
                      {:event :on-active-warning
                       :target :warning}]
    [:fsm/transition #:fsm.transition
                      {:event :on-active-error
                       :target :error}]]

   ;; success state
   [:fsm/state#success
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (fsm/send (fsm/update-data state-machine merge data) {:fsm/event ::success}))}
    [:fsm/transition #:fsm.transition
                      {:event :on-success-response
                       :target :inert
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (fsm/update-data state-machine merge data))}]]

   ;; warning state
   [:fsm/state#warning
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (fsm/send (fsm/update-data state-machine merge data) {:fsm/event ::warning}))}
    [:fsm/transition #:fsm.transition
                      {:event :on-warning-response
                       :target :inert
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (fsm/update-data state-machine merge data))}]]

   ;; error state
   [:fsm/state#error
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (let [exception-data (ex-data data)]
                       (fsm/update-data state-machine merge exception-data)))}
    [:fsm/transition #:fsm.transition
                      {:event :reset
                       :target :inert
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (fsm/update-data state-machine merge {:status "inert" :messages []}))}]]])

(defmethod keechma-controller/prep :stage [controller]
  (register
   controller
   {::init
    (pipelines/set-queue
     (pipeline!
       [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)]
         (merge data {:status "active" :messages []})))
     :init)

    ::active
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)
             {:keys [container]} data]
         (-> (initialize-stage config container)
             (promesa/then (fn [response] (merge {:status "active"} response)))
             (promesa/catch (fn [error] (throw (ex-info (:message error) error)))))))
     :active)

    ::success
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (promesa/delay 750)
       {:status "inert"})
     :success)

    ::warning
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (promesa/delay 750)
       {:status "inert"})
     :warning)}))

(defmethod keechma-controller/start :stage [_ payload {:keys [router]} _]
  (fsm/start (fsm/compile state-machine) payload))

(defmethod keechma-controller/handle :stage [{:keys [state*] :as controller} event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :stage [_ state _]
  {:active-states (fsm/get-active-states state)
   :data (fsm/get-data state)})
