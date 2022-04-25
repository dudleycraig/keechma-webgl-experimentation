(ns main.ui.components.gldom.edge
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
            [keechma.next.controllers.pipelines :refer [throw-promise!]]))

(defnc Edge [props]
  ($ :mesh
     {:visible true
      :position #js[0 0 0]
      :rotation #js[0 0 0]}
     ($ :sphereGeometry
        {:attach "geometry"
         :args #js[2 10 10]})
     ($ :meshPhongMaterial
        {:attach "material"
         :depthTest true
         :depthWrite true
         :side (. THREE -FrontSide)
         :color 0xff7700
         :reflectivity 0
         :flatShading false
         :roughness 0.8
         :metalness 0.2
         :emissive 0x101010
         :specular 0x101010
         :shininess 100})))
