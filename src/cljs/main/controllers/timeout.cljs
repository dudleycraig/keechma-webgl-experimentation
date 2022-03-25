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

(def state-machine
  [:fsm/root

   [:fsm/state#inert
    [:fsm/transition
     #:fsm.transition
      {:event :on-click
       :fsm/on (fn [state-machine {:keys [data]}]
                 (fsm/send (fsm/update-data state-machine assoc :status "active") {:fsm/event ::init}))}]
    [:fsm/transition
     #:fsm.transition
      {:event :on-init-response
       :target :active}]]

   [:fsm/state#active
    {:fsm.on/enter
     (fn [state-machine {:keys [data]}]
       (fsm/send state-machine {:fsm/event ::active}))}
    [:fsm/transition
     #:fsm.transition
      {:event :on-active-response
       :target :success}]]

   [:fsm/state#success
    {:fsm.on/enter
     (fn [state-machine {:keys [data]}]
         (fsm/send (fsm/update-data state-machine assoc :status "success") {:fsm/event ::success}))}
    [:fsm/transition
     #:fsm.transition
      {:event :on-success-response
       :target :inert
       :fsm/on (fn [state-machine {:keys [data]}]
                 (fsm/update-data state-machine assoc :status "inert"))}]]])

(defmethod keechma-controller/prep :timeout [controller]
  (register
   controller
   {::init
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)]
         (println "pipeline::init, data: " data)))
     :init)

    ::active
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (promesa/delay 3000)
       (let [data (fsm/get-data @state*)]
         (println "pipeline::active, data: " data)))
     :active)

    ::success
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (promesa/delay 750)
       (let [data (fsm/get-data @state*)]
         (println "pipeline::success, data: " data)))
     :success)}))

(defmethod keechma-controller/start :timeout [_ _ _]
  (fsm/start (fsm/compile state-machine) {:status "inert"}))

(defmethod keechma-controller/handle :timeout [controller event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :timeout [_ state _]
  {:active-states (fsm/get-active-states state)
   :data (fsm/get-data state)})

