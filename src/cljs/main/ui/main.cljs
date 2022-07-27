(ns main.ui.main
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
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [main.ui.components.main-navigation :refer [MainNavigation]]
            [main.ui.components.main-footer :refer [MainFooter]]
            [main.ui.pages.home :refer [Home]]
            [main.ui.pages.stage :refer [Stage]]
            [main.ui.pages.contact :refer [Contact]]))

;; (def nav-items 
;;   [{:label "Home" :active true :href "home"}
;;    {:label "Stage" :active false :href "stage"}
;;    {:label "Contact" :active false :href "contact"}])

(def nav-items
  [{:route {:page "home"} :title "Home" :icon faHome}
   {:route {:page "stage"} :title "Stage" :icon faCube}
   {:route {:page "contact"} :title "Contact" :icon faEnvelope}])

(defnc Container [props]
  (suspense
    {:fallback (d/div "Loading ...")}
    (let [{:keys [page subpage] :as router} (use-sub props :router)]
      (<>
        ($ MainNavigation {:nav-items nav-items})

        (suspense {:fallback (d/main {:class "container" :role "main"} "Loading ...")}
                  (case page
                    "home" ($ Home)
                    "stage" ($ Stage)
                    "contact" ($ Contact)
                    (d/div "404")))
        ($ MainFooter)))))

(def Main (with-keechma Container))













