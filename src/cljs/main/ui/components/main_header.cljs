(ns main.ui.components.main-header
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            [helix.core :as hx :refer [$]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [keechma.next.helix.core :refer [with-keechma use-sub]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]))

(defnc NavItem [{:keys [title icon active? href] :as props}]
  (d/li 
    (d/a {:href href :class (str "relative flex items-center justify-between h-10 px-3 rounded-lg " (if active? "bg-gray-900 text-white" "text-gray-300 hover:bg-gray-700 hover:text-white")) :aria-current (if active? "page" nil)}
         (d/span {:class "flex-1 flex items-center justify-center sm:items-stretch sm:justify-start"}
                 ($ icon {:className "self-center w-5 h-5 shrink-0 mr-1"})
                 title))))

(defnc NavItems [{:keys [nav-items current-page app-props]}]
  (d/ul {:class "flex space-x-4"}
        (into []
              (map-indexed
                (fn [index {:keys [route title icon]}] 
                  ($ NavItem {:key (str "nav-item-" index) :title title :icon icon :active? (= current-page (:page route)) :href (router/get-url app-props :router route)}))
                nav-items))))

(defnc Brand [] 
  (d/div {:class "flex-shrink-0 flex items-center mr-6"}
    (d/span {:class "text-white"} 
      "Experimentation")))

(defnc Bar [{:keys [nav-items app-props current-page active?]}]
  (d/div {:class "max-w-7xl mx-auto px-2 sm:px-6 lg:px-8"}
         (d/div {:class "relative flex items-center justify-between h-12"}
                (d/div {:class "flex-1 flex items-center justify-center sm:items-stretch sm:justify-start"}
                       ($ Brand) 
                       (d/nav 
                         ($ NavItems {:nav-items nav-items :current-page current-page :app-props app-props}))))))

(defnc Container [{:keys [nav-items] :as props}]
  (let [current-page (:page (use-sub props :router))]
    (d/header {:class "w-screen top-0 bg-gray-800"}
              ($ Bar {:nav-items nav-items :current-page current-page :app-props props}))))

(def MainHeader (with-keechma Container))
