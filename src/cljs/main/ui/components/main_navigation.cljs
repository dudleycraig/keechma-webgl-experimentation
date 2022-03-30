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
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.controllers.router :as router]))

(defnc NavBar [{:keys [nav-items] :as props}]
  (let [current-page (:page (use-sub props :router))]
    (d/nav {:class "main-navigation navbar navbar-expand-md navbar-dark bg-primary display-flex justify-content-center fixed-top"}
           (d/a {:class "navbar-brand" :href "#"} "Experimentation")
           (d/div {:class "collapse navbar-collapse"}
                  (d/ul {:class "navbar-nav mr-auto"}
                        (into
                          []
                          (map-indexed
                            (fn [index {:keys [route title icon] :as link}]
                              (d/li {:key (str "nav-item-" index) :class "nav-item"}
                                    (d/a {:class ["nav-link" (when (= current-page (:page route)) "active")] 
                                          :href (router/get-url props :router route)}
                                         title)))
                            nav-items)))))))

(def MainNavigation (with-keechma NavBar))



