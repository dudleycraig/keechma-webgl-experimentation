(ns main.ui.components.gldom.canvas
  (:require ["core-js/stable"]
            ["regenerator-runtime/runtime"]

            ["react" :refer [Suspense useRef] :as react]
            ["react-dom" :as rdom]
            ["@react-three/fiber" :refer [Canvas useFrame useThree extend] :as gdom]
            ["three/examples/jsm/controls/OrbitControls" :refer [OrbitControls]]
            ["three" :as THREE]

            [goog.object :as gobj]
            [applied-science.js-interop :as js-interop]

            [helix.core :as hx :refer [$ defnc suspense <>]]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            
            [main.ui.components.gldom.scene :refer [Scene]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)
        initial-camera-position (get-in data [:scene :camera :position])]
    ($ Canvas
      {:id "gldom"
       :frameloop "always"
       :gl {:alpha true}
       :onCreated
       (fn [state]
         (do
           ;; (.. state -gl (setPixelRatio 1.5))
           ;; (.. state -gl (setClearColor 0x17191b 0.2))
         ))
       ;; :pixelRatio (. js/window -devicePixelRatio)
       ;; :dpr (. js/Math (min (. js/window -devicePixelRatio) 2))
       ;; :dpr #js[1 2] 
       :concurrent true}
      ($ Scene {:initial-camera-position initial-camera-position}))))

(def Canvas3D (with-keechma Container))
