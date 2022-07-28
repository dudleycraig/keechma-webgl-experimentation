(ns main.ui.pages.home
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@heroicons/react/outline" :refer [HomeIcon CubeIcon MailIcon UserIcon ShareIcon ExclamationIcon CheckCircleIcon]]

            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]

            [main.ui.components.styled-components :as styled]
            [main.ui.components.messages :refer [Messages]]
            [main.ui.components.state-button :refer [StateButton]]
            [main.ui.components.button :refer [Button]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :contrived)
        {:keys [status messages]} data]

    (d/section {:class "flex flex-col items-center w-3/4 h-64 bg-white rounded-lg m-3 p-3"}
               (d/div {:class "flex flex-nowrap space-x-4"}
                      ($ StateButton {:class "contrived"
                                      :status status
                                      :icon UserIcon
                                      :label "contrive status" 
                                      :onClick (fn [event] (dispatch props :contrived :init))})
                      ($ Button {:disabled (not= status "error")
                                 :onClick (fn [event] (dispatch props :contrived :reset))}
                         "reset status"))
               (d/div {:class "flex flex-nowrap space-x-4"}
                      ($ Messages {:messages (let [now (.getTime (js/Date.))] messages)})))))

(def Home (with-keechma Container))
