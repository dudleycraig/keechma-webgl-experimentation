(ns main.controllers.timeout
  (:require ["@fortawesome/free-solid-svg-icons" :refer [faUser]]

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

(derive :timeout ::controllers-pipelines/controller)

(defn wrand [slices]
  "weighed random generator. 
  Given a vector of integers, 
  and using those integer values as weights, 
  returns random index from vector"
  (let [total (reduce + slices)
        r (rand total)]
    (loop [i 0 sum 0]
      (if
       (< r (+ (slices i) sum))
        i
        (recur (inc i) (+ (slices i) sum))))))

(defn contrived-async-promise []
  (promesa/create
   (fn [resolve reject]
     (js/setTimeout
      (fn [] (case (wrand [6 3 3])
               0 (do (js/console.log "success triggered") (resolve {:status "success"}))
               1 (do (js/console.warn "warning triggered") (resolve {:status "warning"}))
               2 (do (js/console.error "error triggered") (reject {:status "error"}))
               (do (js/console.error "default triggered") (reject {:status "error"}))))
      1500))))

(def state-machine
  [:fsm/root

   ;; inert state
   [:fsm/state#inert
    [:fsm/transition #:fsm.transition
                      {:event :on-click
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (let [{:keys [status]} data]
                                   (fsm/send (fsm/update-data state-machine assoc :status status) {:fsm/event ::init})))}]

    [:fsm/transition #:fsm.transition
                      {:event :on-init-response
                       :target :active}]]

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
                       :target :error
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (let [{::keys [error-msg]} (ex-data data)]
                                   (cond-> state-machine
                                     true (fsm/assoc-in-data [:error] {:title (ex-message data)
                                                                       :body (-> data ex-data :message)})
                                     error-msg (fsm/assoc-in-data [:error-msg] error-msg))))}]]

   ;; success state
   [:fsm/state#success
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (let [{:keys [status]} data]
                       (fsm/send (fsm/update-data state-machine assoc :status status) {:fsm/event ::success})))}

    [:fsm/transition #:fsm.transition
                      {:event :on-success-response
                       :target :inert
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (let [{:keys [status]} data]
                                   (fsm/update-data state-machine assoc :status status)))}]]

   ;; warning state
   [:fsm/state#warning
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (let [{:keys [status]} data]
                       (fsm/send (fsm/update-data state-machine assoc :status status) {:fsm/event ::warning})))}

    [:fsm/transition #:fsm.transition
                      {:event :on-warning-response
                       :target :inert
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (let [{:keys [status]} data]
                                   (fsm/update-data state-machine assoc :status status)))}]]

   ;; error state
   [:fsm/state#error
    {:fsm.on/exit (fn [state-machete {:keys [data]}]
                    (let [{:keys [status]} data]
                      (fsm/update-data state-machine assoc :status status)))}]])

(defmethod keechma-controller/prep :timeout [controller]
  (register
   controller
   {::init
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)] data))
     :init)

    ::active
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)]
         (-> (contrived-async-promise)
             (promesa/then (fn [response] response))
             (promesa/catch (fn [error] error))))
       (println "payoad: " payload))
     :active)

    ::success
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (promesa/delay 750)
       (let [data (fsm/get-data @state*)] data))
     :success)

    ::warning
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (promesa/delay 750)
       (let [data (fsm/get-data @state*)] data))
     :warning)}))

(defmethod keechma-controller/start :timeout [_ _ _]
  (fsm/start (fsm/compile state-machine) {:status "inert"}))

(defmethod keechma-controller/handle :timeout [controller event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :timeout [_ state _]
  {:active-states (fsm/get-active-states state)
   :data (fsm/get-data state)})

