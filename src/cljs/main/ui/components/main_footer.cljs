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
  (d/footer {:class "w-screen fixed bottom-0 bg-gray-800"}
            (d/div {:class "max-w-7xl mx-auto px-2 sm:px-6 lg:px-8"}
                   (d/div {:class "relative flex items-center justify-between h-10"}
                          (d/div {:class "flex-1 flex items-center justify-center sm:items-stretch sm:justify-start"}
                                 (d/span {:class "text-white"} 
                                         "© 2018 Very Big Things"))))))

(def MainFooter (with-keechma Container))
