(ns main.ui.main
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@heroicons/react/outline" :refer [HomeIcon CubeIcon MailIcon]]
            [clojure.core.match :refer-macros [match]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [main.ui.components.main-header :refer [MainHeader]]
            [main.ui.components.container :refer [Container] :rename {Container MainBody}]
            [main.ui.components.main-footer :refer [MainFooter]]
            [main.ui.pages.home :refer [Home] :rename {Home HomePage}]
            [main.ui.pages.stage :refer [Stage] :rename {Stage StagePage}]
            [main.ui.pages.contact :refer [Contact] :rename {Contact ContactPage}]))

(def nav-items
  [{:route {:page "home"} :title "Home" :icon HomeIcon}
   {:route {:page "stage"} :title "Stage" :icon CubeIcon}
   {:route {:page "contact"} :title "Contact" :icon MailIcon}])

(defnc Container [props]
  (suspense
    {:fallback (d/div "Loading ...")}
    (let [{:keys [page] :as router} (use-sub props :router)]
      [
       ($ MainHeader {:key "main-header" :nav-items nav-items })
       (d/main {:key "main-content" :class "w-screen h-screen flex flex-grow justify-center items-top" :role "main" }
               (suspense {:fallback (d/div "Loading ...")}
                         (match [page]
                                ["home"] ($ HomePage)
                                ["stage"] ($ StagePage)
                                ["contact"] ($ ContactPage)
                                :else (d/section "404"))))
       ($ MainFooter {:key "main-footer"})])))

(def Main (with-keechma Container))
