(ns main.ui.components.gldom.custom-orbit-controls
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

(defnc CustomOrbitControls [props]
  (let [canvas (useThree)
        camera (. canvas -camera)
        element (.. canvas -gl -domElement)
        controls (OrbitControls. camera element)]
    (useFrame (fn [] (. controls (update))))
    (set! ^js (. controls -autoRotate) false)
    (set! ^js (. controls -enableZoom) true)
    (set! ^js (.. camera -rotation -x) (.. THREE -MathUtils (degToRad 180)))))
