(ns main.controllers.graph
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

(derive :graph ::controllers-pipelines/controller) ;; TODO: make child of stage

(def state-machine
  [:fsm/root [:fsm/state#inert]])

(def initial-data
  {:status "inert"
   :messages []
   :options {:limit 0}
   :node-set {}
   :nodes []
   :edges []
   :layout nil})

(defn limit-reached
  ([nodes] false)
  ([nodes limit] 
   (or 
     (not= limit nil) ;; default, no limits
     (not= limit 0) ;; no limits
     (>= (peek nodes) limit))))

(defn add-node [node node-set nodes options]
  (if
   (and (contains? node-set (:key node)) (not (limit-reached nodes (:limit options))))
    (do
      (assoc node-set (:key node) node)
      (conj nodes node)
      true)
    false))

(defn get-node [node-id]
  (get node-set [:key] node-id))

(defn get-edge [])

(defmethod keechma-controller/prep :stage [controller]
  (register
   controller
   {;; TODO: fetch graph data
    ::init
    (pipelines/set-queue
     (pipeline! [payload {:keys [state*]}]
       ;; (let [data (fsm/get-data @state*)] data)
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

