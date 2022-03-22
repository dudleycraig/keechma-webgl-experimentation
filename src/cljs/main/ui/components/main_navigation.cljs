(ns main.ui.components.main-navigation
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

(defnc NavItem [{:keys [node-key active href label]}]
  (d/li {:key node-key :class "nav-item" :active active}
        (d/a {:class "nav-link" :href href} 
             label)))

(defnc NavBar [{:keys [nav-items]}]
  (d/nav {:class "navbar navbar-expand-md navbar-dark bg-primary display-flex justify-content-center fixed-top"}
    (d/a {:class "navbar-brand" :href "#"} "Experimentation")
    (d/div {:class "collapse navbar-collapse"}
      (d/ul {:class "navbar-nav mr-auto"}
        (into
         []
         (map-indexed
          (fn [index nav-item] ($ NavItem {:node-key (str "nav-item-" index) & nav-item}))
          nav-items))))))

(def MainNavigation (with-keechma NavBar))



