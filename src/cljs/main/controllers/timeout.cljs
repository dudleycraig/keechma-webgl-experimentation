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

(def state-machine
  [:fsm/root

   [:fsm/state#inert
    [:fsm/transition
     #:fsm.transition {:event :on-click
                       :target :b}]]

   [:fsm/state#b
    {:fsm.on/enter #(println "B")}
    [:fsm/transition #:fsm.transition {:event :on-click :target :c}]]

   [:fsm/state#c
    {:fsm.on/enter #(println "C")}]]

  #_[:fsm/root
     [:fsm/state#inert
      [:fsm/transition
       #:fsm.transition
        {:event :on-click
         :target :active
         :fsm/on (fn [fsm {:keys [data]}]
                   (println "foo")
                   #_(state-machete/assoc-in-data fsm [:status] data))}]]

     [:fsm/state#active
      [:fsm.on/enter (fn [fsm {:keys [data]}]
                       (let [payload (state-machete/get-data fsm)]
                         (state-machete/send fsm {:fsm/event ::active})))]]

     [:fsm/state#success
      [:fsm.on/enter (fn [fsm {:keys [data]}]
                       (println "here" data))]]])

(defmethod keechma-controller/prep :timeout [controller]
  (register
   controller
   {:on-click
    (pipeline! [payload {:keys [state*]}]
               (pipelines/swap! state* (fn [state payload] (assoc state :status payload)) payload)
               "active")

    #_(::active
       (pipeline! [payload {:keys [state*]}]
                  (pipelines/swap! state* (fn [state payload] (assoc state :status payload)))
                  (promesa/delay 3000)
                  "success")

       ::success
       (pipeline! [payload {:keys [state*]}]
                  (pipelines/swap! state* (fn [state payload] (assoc state :status payload)))
                  (promesa/delay 750)
                  "inert"))}))

(defmethod keechma-controller/start :timeout [_ _ _]
  (state-machete/start (state-machete/compile state-machine) {}))

(defmethod keechma-controller/handle :timeout [controller event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :timeout [_ state _]
  {:active-states (state-machete/get-active-states state)
   :data (state-machete/get-data state)})



