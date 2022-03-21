(ns main.core
  (:require ["react" :as react]
            ["react-dom" :as rdom]

            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]

            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]

            [main.app :refer [app]]))

(defonce app-instance* (atom nil))

(defnc HomePage [props]
    (d/div 
      {:style {:width "100%" :height "30px" :background "#cfcfcf"}}
      "home"))

(def Home (with-keechma HomePage))

(defnc Stage [props]
    (suspense
      {:fallback (d/div "Loading ...")}
      (let [{:keys [page]} (use-sub props :router)]
        (d/div
          (suspense {:fallback (d/div {:class "container"} "Loading ...")}
                    (case page
                      "home" ($ Home)
                      (d/div "404")))))))

(def Main (with-keechma Stage))

(defn render 
  []
  (when-let [app-instance @app-instance*] (keechma/stop! app-instance))
  (let [app-instance (keechma/start! app)]
    (reset! app-instance* app-instance)
    (rdom/render ($ react/StrictMode
                   ($ KeechmaRoot {:keechma/app app-instance} ($ Main)))
      (js/document.getElementById "experimentation"))))

(defn ^:dev/before-load before-load
  "shadow-cljs hook before browser reload"
  []
  (js/console.clear))

(defn ^:dev/after-load after-load 
  "shadow-cljs hook after browser reload"
  []
  (rdom/unmountComponentAtNode (js/document.getElementById "experimentation"))
  (render))

(defn ^:export init "application entry point." [] (render))


