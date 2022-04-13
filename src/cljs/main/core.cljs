(ns main.core
  (:require ["core-js/stable"]
            ["regenerator-runtime/runtime"]
    
            ["react" :as react]
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
            [main.app :refer [app]]
            [main.ui.main :refer [Main]]))

(defonce app-instance* (atom nil))

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


