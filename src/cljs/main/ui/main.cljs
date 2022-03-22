(ns main.ui.main
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
            [main.ui.components.main-navigation :refer [MainNavigation]]
            [main.ui.pages.home :refer [Home]]))


(def nav-items 
  [{:label "Home" :active true :href "#"}
   {:label "Stage" :active false :href "#"}
   {:label "Contact" :active false :href "#"}])

(defnc Stage [props]
  (suspense
    {:fallback (d/div "Loading ...")}
    (let [{:keys [page]} (use-sub props :router)]
      (<>
        ($ MainNavigation {:nav-items nav-items})

        (suspense {:fallback (d/main {:class "container" :role "main"} "Loading ...")}
                  (case page
                    "home" ($ Home)
                    (d/div "404")))

        (d/nav {:class "navbar navbar-dark navbar-expand-lg bg-primary fixed-bottom"
                :style {:border "none"}}
               (d/div {:class "navbar-navbar d-none d-md-inline"
                       :style {:padding "5px"}}
                      "© 2018 Very Big Things"))))))

(def Main (with-keechma Stage))













