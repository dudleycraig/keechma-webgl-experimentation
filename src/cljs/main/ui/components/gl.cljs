(ns main.ui.components.gl
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

(def initial-camera-position #js[4000 0 0])

(defnc Spinner []
  (let [groupref (hooks/use-ref nil)
        radius 16
        divisions 9
        radians (. (. THREE -MathUtils) degToRad (/ 360 8))]
    (useFrame (fn [] (if (. groupref -current) 
                       (set! ^js (.. groupref -current -rotation -z) (+ (.. groupref -current -rotation -z) 0.061)))))
    ($ :group {:ref groupref}
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

(defnc Scene []
  (let [canvas (useThree)
        camera (. canvas -camera)
        domElement (.. canvas -gl -domElement)
        torchref (hooks/use-ref nil)
        groupref (hooks/use-ref nil)
        element (.. canvas -gl -domElement)
        controls (OrbitControls. camera element)]
    (useFrame (fn [] 
                (if (. torchref -current)
                  (do 
                      (set! ^js (.. torchref -current -position -x) (.. camera -position -x))
                      (set! ^js (.. torchref -current -position -y) (.. camera -position -y))
                      (set! ^js (.. torchref -current -position -z) (.. camera -position -z))))))
    [($ :spotLight
        {:key "stage-spotlight"
         :position initial-camera-position 
         :lookAt #js[0 0 0]
         :color 0xCCCCCC
         :intensity 1
         :penumbra 0.5})
     ($ :spotLight
        {:key "camera-spotlight"
         :lookAt #js[0 0 0]
         :color 0xCCAAAA
         :intensity 2
         :penumbra 0
         :ref torchref})
     ($ Spinner {:key "spinner"})]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)]
    ($ Canvas
       {:id "gl-dom"
        :gl {:alpha true}
        :onCreated (fn [gl scene camera] nil)
        :camera {:fov 30 :aspect 0.2 :near 1 :far 50000000 :position initial-camera-position :zoom 2}
        :concurrent true
        :pixelRatio (. js/window -devicePixelRatio)}
       ($ Scene))))

(def GL (with-keechma Container))
