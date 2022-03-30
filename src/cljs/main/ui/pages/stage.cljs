(ns main.ui.pages.stage
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faUser faShare faSpinner faExclamation faCheck]]

            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :contrived)]

    (d/div {:class "container" :style {:margin-top "60px"} :id "stage"}

      (d/div {:class "row mb-2"}
        (d/div {:class "col"}
          "stage")))))

(def Stage (with-keechma Container))
