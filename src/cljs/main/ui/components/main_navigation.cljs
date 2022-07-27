(ns main.ui.components.main-navigation
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faEnvelope faHome faUser]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.controllers.router :as router]
            [main.ui.components.styled-components :as styled]))

(defnc Component [{:keys [nav-items] :as props}]
  (let [current-page (:page (use-sub props :router))]
    ($ styled/NavBar 
           ($ styled/NavBarBrand {:className "px-3" :href (router/get-url props :router {:page "home"})} "Experimentation")
           ($ styled/NavBarNav
                 (into
                   []
                   (map-indexed
                     (fn [index {:keys [route title icon] :as link}]
                       ($ styled/NavBarItem {:key (str "nav-item-" index)}
                             ($ styled/NavBarLink {:className [(when (= current-page (:page route)) "active")] :href (router/get-url props :router route)}
                                  ($ FontAwesomeIcon {:icon icon :size "1x" :style {:min-width "16px"}})
                                  (d/span {:class ["ml-1" "d-md-inline" "d-none"]} title))))
                     nav-items))))))

(def MainNavigation (with-keechma Component))
