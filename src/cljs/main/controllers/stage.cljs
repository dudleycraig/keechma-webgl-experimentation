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

(defn initialize-stage []
  (promesa/create (fn [resolve reject] (resolve))))

(def state-machine
  [:fsm/root

   ;; loading state
   [:fsm/state#loading
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (fsm/send (fsm/update-data state-machine merge data) {:fsm/event ::loading}))}
    [:fsm/transition #:fsm.transition
                      {:event :on-loading-response
                       ;; :target :active
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (fsm/update-data state-machine merge data))}]]

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
                                 (fsm/update-data state-machine merge {:status "inert" :messages []}))}]]
   
   ;; inert state
   [:fsm/state#inert
    {:fsm.on/enter (fn [state-machine {:keys [data]}]
                     (fsm/send (fsm/update-data state-machine merge data) {:fsm/event ::init}))}
    [:fsm/transition #:fsm.transition
                      {:event :on-init-response
                       :target :loading
                       :fsm/on (fn [state-machine {:keys [data]}]
                                 (fsm/update-data state-machine merge data))}]]])

(defmethod keechma-controller/prep :stage [controller]
  (register
    controller
    {::loading
     (pipelines/set-queue
       (pipeline! [payload {:keys [state*]}]
                  (let [data (fsm/get-data @state*)
                        {:keys [container]} data]
                    (-> (initialize-stage)
                        (promesa/then (fn [response] (merge {:status "active"} response)))
                        (promesa/catch (fn [error] (throw (ex-info (:message error) error)))))))
       :loading)

     ::active
     (pipelines/set-queue
       (pipeline! [payload {:keys [state*]}])
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
       :warning)

     ::error
     (pipelines/set-queue
       (pipeline! [payload {:keys [state*]}]
                  (promesa/delay 750)
                  {:status "error"})
       :error)

     ::init
     (pipelines/set-queue
       (pipeline!
         [payload {:keys [state*]}]
         (let [data (fsm/get-data @state*)]
           (merge data {:status "loading" :messages []})))
       :init)}))

(defmethod keechma-controller/start :stage [_ payload {:keys [router]} _]
  (fsm/start (fsm/compile state-machine) payload))

(defmethod keechma-controller/handle :stage [{:keys [state*] :as controller} event payload]
  (handle controller event payload))

(defmethod keechma-controller/derive-state :stage [_ state _]
  {:active-states (fsm/get-active-states state)
   :data (fsm/get-data state)})
