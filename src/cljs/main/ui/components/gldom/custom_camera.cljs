(ns main.ui.components.gldom.custom-camera
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
            [helix.hooks :as hooks]))

(defnc CustomCamera [{:keys [position]}]
  (let [cameraref (hooks/use-ref nil)
        set-state (useThree (fn [state] (. state -set)))]
    (hooks/use-effect
      :once
      (if
        (. cameraref -current)
        (set-state #js{:camera (. cameraref -current)})))
    ($ :perspectiveCamera 
       {:ref cameraref 
        :position position 
        :fov 30 
        :aspect 0.2 
        :near 1 
        :far 50000000 
        :zoom 1})))
