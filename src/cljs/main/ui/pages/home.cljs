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

            [main.ui.components.messages :refer [Messages]]
            [main.ui.components.state-button :refer [StateButton]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :timeout)
        status (data :status)]

    (d/div {:class "container" :style {:margin-top "60px"} :id "home"}

      (d/div {:class "row"}
        (d/div {:class "col"}
          "home"))

      (d/div {:class "row"}
        (d/div {:class "col"}
          ($ StateButton {:class "timeout" :status status :icon faUser :label (str "status::" status) :onClick (fn [event] (dispatch props :timeout :on-click {:status "active"}))})))

      (d/div {:class "row"}
        (d/div {:class "col"}
          ($ Messages {:messages (let [now (.getTime (js/Date.))] [{:text "test success message" :status "success" :timestamp now}
                                                                   {:text "test warning message" :status "warning" :timestamp now}
                                                                   {:text "test error message" :status "error" :timestamp now}])})))
      )))

(def Home (with-keechma Container))













