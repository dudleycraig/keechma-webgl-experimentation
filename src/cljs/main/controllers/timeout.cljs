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
            [com.verybigthings.state-machete.core :as state-machete]
            
            [main.lib.fsm :refer [handle register]]))

(derive :timeout ::controllers-pipelines/controller)

(def fsm
  [:state-machete/state#timeout

   [:state-machete/state#inert
    [:state-machete/transition
     #:state-machete.transition
     {:event :on-click
      :target :active
      :state-machete/on (fn [fsm {:keys [data]}]
                          (state-machete/assoc-in-data fsm [:status] data))}]]

   [:state-machete/state#active
    [:state-machete.on/enter (fn [fsm {:keys [data]}]
                               (let [payload (state-machete/get-data fsm)]
                                 (state-machete/send fsm {:state-machete/event ::active})))]]

   [:state-machete/state#success
    [:state-machete.on/enter (fn [fsm {:keys [data]}] 
                               ;; transition inert state
                               )]]])

(defmethod keechma-controller/prep :timeout [controller]
  (register
   controller
   {:on-click
    (pipeline! [payload {:keys [state*]}]
      (pipelines/swap! state* (fn [state payload] (assoc state :status payload)) payload)
      "active")

    ::active
    (pipeline! [payload {:keys [state*]}]
      (pipelines/swap! state* (fn [state payload] (assoc state :status payload)))
      (promesa/delay 3000)
      "success")

    ::success
    (pipeline! [payload {:keys [state*]}]
      (pipelines/swap! state* (fn [state payload] (assoc state :status payload)))
      (promesa/delay 750)
      "inert")}))

(defmethod keechma-controller/start :timeout [_ _ _]
  (state-machete/start {} (state-machete/compile fsm)))

(defmethod keechma-controller/handle :timeout [controller event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :timeout [_ state _]
  {:active-states (state-machete/get-active-states state)
   :data (state-machete/get-data state)})



