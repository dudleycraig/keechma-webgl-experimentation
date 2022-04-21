(ns main.ui.pages.stage
  (:require ["core-js/stable"]
            ["regenerator-runtime/runtime"]

            ["react" :refer [Suspense useEffect useRef] :as react]
            ["react-dom" :as rdom]
            ["three" :as THREE]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faUser faShare faSpinner faExclamation faCheck]]

            [helix.core :as hx :refer [$ suspense]]
            [helix.dom :as hdom]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.helix.lib :refer [defnc]]
            
            [main.ui.components.gldom.canvas :refer [Canvas3D]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)]

    (hdom/div
      {:class "container" :style {:margin-top "60px"} :id "stage"}

      (hdom/div
        {:class "row mb-2"}
        (hdom/div {:class "col"}
          "stage"))

      (hdom/div
        {:class "row mb-2"}
        (hdom/div
          {:class "col"}
          (hdom/div
            {:class ""
             :style
             {:position "relative"
              :margin "0 auto"
              :left 0
              :top 0
              :z-index 20
              :width "600px"
              :height "600px"
              :background-color "#3a3f44"
              :border-style "solid"
              :border-color "#17191b"
              :border-width "1px"
              :border-radius "5px"}}
            ($ Canvas3D)))))))

(def Stage (with-keechma Container))
