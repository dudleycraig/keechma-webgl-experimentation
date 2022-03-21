(ns main.ui.pages.home
  (:require ["react" :as react]
   ["react-dom" :as rdom]
   ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
   ["@fortawesome/free-solid-svg-icons" :refer [faEnvelope faImages faHome faUser]]
   [helix.core :as hx :refer [$ <> suspense]]
   [helix.dom :as d]
   [helix.hooks :as hooks]
   [keechma.next.core :as keechma]
   [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub KeechmaRoot]]
   [keechma.next.helix.lib :refer [defnc]]
   [keechma.next.controllers.pipelines :refer [throw-promise!]]))

(defnc HomePage [props] 
  (d/div 
    {:id "home" :class "container"} 
    (d/div 
      {:class "row"} 
      (d/div {:class "col"} "home"))))

(def Home (with-keechma HomePage))
