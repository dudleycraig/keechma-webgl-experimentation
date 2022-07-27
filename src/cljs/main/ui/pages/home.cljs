(ns main.ui.pages.home
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
            [keechma.next.controllers.pipelines :refer [throw-promise!]]

            [main.ui.components.styled-components :as styled]
            [main.ui.components.messages :refer [Messages]]
            [main.ui.components.state-button :refer [StateButton]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :contrived)
        {:keys [status messages]} data]

    (d/div {:class "container" :style {:margin-top "60px"} :id "home"}

      (d/div {:class "row mb-2"}
        (d/div {:class "col"}
          "home"))

      (d/div {:class "row mb-2"}
        (d/div {:class "col"}
          ($ StateButton {:class "contrived"
                          :status status
                          :icon faUser
                          :label "contrive status" 
                          :onClick (fn [event] 
                                     (dispatch props :contrived :init))})
          (d/button {:class ["btn" "btn-outline-secondary" "d-inline-flex" "flex-row" "justify-content-center" "align-items-center" "ml-2"]
                     :disabled (not= status "error")
                     :onClick (fn [event] 
                                (dispatch props :contrived :reset))}
            "reset status")))

      (d/div {:class "row mb-2"}
        (d/div {:class "col"}
          ($ Messages {:messages (let [now (.getTime (js/Date.))] messages)}))))))

(def Home (with-keechma Container))













