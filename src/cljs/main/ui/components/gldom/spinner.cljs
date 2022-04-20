(ns main.ui.components.gldom.spinner
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

(defnc Spinner []
  (let [groupref (hooks/use-ref nil)
        radius 16
        divisions 9
        radians (. (. THREE -MathUtils) degToRad (/ 360 8))]
    (useFrame (fn [] (if (. groupref -current) 
                       (set! ^js (.. groupref -current -rotation -z) (+ (.. groupref -current -rotation -z) 0.061)))))
    ($ :group {:ref groupref :position #js[0 0 0]}
      (map (fn [index]
             (let [x (* radius (. js/Math cos (* radians index)))
                   y (* radius (. js/Math sin (* radians index)))]
               (if (not= index 1)
                 ($ :mesh
                   {:key (str "ball-" index)
                    :visible true
                    :position #js[x y 0]
                    :rotation #js[0 0 0]}
                   ($ :sphereGeometry
                     {:attach "geometry"
                      :args #js [5 20 20]})
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
                      :shininess 100})))))
           (range 1 divisions)))))
