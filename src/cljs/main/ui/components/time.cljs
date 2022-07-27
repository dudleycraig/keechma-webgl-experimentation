(ns main.ui.components.time
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faUser faShare faSpinner faExclamation faCheck]]

            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]))

(defnc Time [{:keys [timestamp style]
              :or {timestamp (.getTime (js/Date.)) style {}}
              :as props}]
  (let [date (js/Date. timestamp)
        hours (take-last 2 (str "0" (.getHours date)))
        minutes (take-last 2 (str "0" (.getMinutes date)))
        seconds (take-last 2 (str "0" (.getSeconds date)))
        milliseconds (take-last 3 (str "00" (.getMilliseconds date)))]

    (d/span {:class "time" :style style}
      (d/span {:class "hours" :key "hours"} hours)
      (d/span {:class "separator"} ":")
      (d/span {:class "minutes" :key "minutes"} minutes)
      (d/span {:class "separator"} ":")
      (d/span {:class "seconds" :key "seconds"} seconds)
      (d/span {:class "separator"} ".")
      (d/span {:class "milliseconds" :key "milliseconds"} milliseconds))))

