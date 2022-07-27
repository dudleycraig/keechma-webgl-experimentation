(ns main.ui.components.main-footer
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faCube faEnvelope]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]))

(defnc Container [{:keys [nav-items] :as props}]
  (d/nav {:class "navbar navbar-dark navbar-expand-lg bg-primary fixed-bottom"
          :style {:border "none"}}
         (d/div {:class "navbar-navbar d-none d-md-inline"
                 :style {:padding "5px"}}
                "© 2018 Very Big Things")))

(def MainFooter (with-keechma Container))
