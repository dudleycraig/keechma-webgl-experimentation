(ns main.lib.fsm
  (:require [keechma.next.controllers.pipelines :as pipelines]
            [com.verybigthings.state-machete.core :as fsm]
            [promesa.core :as p]
            [keechma.next.controller :as ctrl]
            [keechma.pipelines.core :refer-macros [pipeline!]]))

(defn make-response-event-name
  ([ev]
   (make-response-event-name ev false))
  ([ev is-error]
   (let [ev-ns (namespace ev)
         suffix (if is-error "error" "response")
         ev-name (str "on-" (name ev) "-" suffix)]
     (if ev-ns
       (keyword ev-ns ev-name)
       (keyword ev-name)))))

(defn wrap-pipeline [pipeline-name pipeline]
  (let [on-response-event (make-response-event-name pipeline-name)
        on-error-event (make-response-event-name pipeline-name true)]
    (pipeline! [value ctrl]
      pipeline
      (ctrl/dispatch-self ctrl on-response-event value)
      (rescue! [error]
        (ctrl/dispatch-self ctrl on-error-event error)))))

(defn handle [{:keys [state*] :as ctrl} ev payload]
  (let [state @state*
        next-state (fsm/trigger state {:fsm/event ev :data payload})
        outbound-events (fsm/get-events next-state)]

    (reset! state* next-state)

    (doseq [{:fsm/keys [event] :keys [data]} outbound-events]
      (->> (pipelines/handle ctrl event data)
           (p/error (constantly nil))))))

(defn register [ctrl pipelines]
  (let [pipelines' (->> pipelines
                        (map (fn [[pipeline-name pipeline]] [pipeline-name (wrap-pipeline pipeline-name pipeline)]))
                        (into {}))]
    (pipelines/register ctrl pipelines')))

