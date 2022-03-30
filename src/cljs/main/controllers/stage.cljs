(ns main.controllers.stage
  (:require [promesa.core :as promesa]

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

(def state-machine
  [:fsm/root [:fsm/state#inert]])

(defmethod keechma-controller/prep :contrived [controller]
  (register
   controller
   {::init
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)] data)
       {:status "active" :messages []})
     :init)}))

(defmethod keechma-controller/start :contrived [_ _ _]
  (fsm/start (fsm/compile state-machine) {:status "inert" :messages []}))

(defmethod keechma-controller/handle :contrived [controller event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :contrived [_ state _]
  {:active-states (fsm/get-active-states state)
   :data (fsm/get-data state)})

