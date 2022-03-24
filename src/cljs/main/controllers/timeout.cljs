(ns main.controllers.timeout
  (:require ["@fortawesome/free-solid-svg-icons" :refer [faUser]]

            [promesa.core :as promesa]

            [keechma.next.core :as keechma]
            [keechma.next.controller :as keechma-controller]
            [keechma.next.controllers.pipelines :as controllers-pipelines]
            [keechma.next.controllers.entitydb :as entitydb]
            [keechma.next.controllers.dataloader :as controllers-dataloader]
            [keechma.pipelines.core :as pipelines :refer-macros [pipeline!]]))

(derive :timeout ::controllers-pipelines/controller)

(defmethod keechma-controller/prep :timeout [controller]
  (controllers-pipelines/register
   controller
   {:inc
    (pipeline! [payload {:keys [state*]}]
      (pipelines/swap! state* (fn [state payload] (assoc state :status payload)) payload)
      (promesa/delay 5000)
      (pipelines/swap! state* (fn [state payload] (assoc state :status "success"))))}))

(defmethod keechma-controller/start :timeout [_ _ _] 
  {:status "inert"})

;; (defmethod keechma-controller/handle :timeout [{:keys [state*] :as controller} event payload]
;;   (case event
;;     :inc (swap! state* (fn [state payload] (assoc state :status payload)) payload)
;;     nil))



