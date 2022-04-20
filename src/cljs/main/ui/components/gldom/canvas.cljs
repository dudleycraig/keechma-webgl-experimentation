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

(def initial-camera-position #js[0 0 -50])

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)]
    ($ Canvas
       {:id "gldom"
        :gl {:alpha true}
        ;; :onCreated (fn [canvas] (set! ^js (. (. canvas -camera) -position) initial-camera-position))
        :camera {:fov 30 :aspect 0.2 :near 1 :far 50000000 :position initial-camera-position :zoom 2}
        :concurrent true
        :pixelRatio (. js/window -devicePixelRatio)}
       ($ Scene {:initial-camera-position initial-camera-position}))))

(def Canvas3D (with-keechma Container))
