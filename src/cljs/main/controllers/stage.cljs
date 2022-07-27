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

(def initial-data
  {:status "inert"
   :messages []
   :canvas {:dimensions {:width 600 :height 600}}
   :scene {:camera {:position #js[0 0 100]}}})

(defn graph
  ([]
   (graph {}))
  ([options]
   {:options options
    :node-set {}
    :nodes []
    :edges []
    :layout nil}))

(defmethod keechma-controller/prep :stage [controller]
  (register
   controller
   {::init
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       (let [data (fsm/get-data @state*)] data)
       {:status "active" :messages []})
     :init)}))

(defmethod keechma-controller/start :stage [_ _ _]
  (fsm/start 
    (fsm/compile state-machine) initial-data))

(defmethod keechma-controller/handle :stage [controller event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :stage [_ state _]
  {:active-states (fsm/get-active-states state)
   :data (fsm/get-data state)})

