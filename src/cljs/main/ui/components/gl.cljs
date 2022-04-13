(ns main.ui.components.gl
  (:require ["core-js/stable"]
            ["regenerator-runtime/runtime"]

            ["react" :refer [Suspense] :as react]
            ["react-dom" :as rdom]
            ;; ["@react-three/cannon" :refer [Physics usePlane useBox useCylinder useRaycastVehicle]]
            ["@react-three/fiber" :refer [Canvas useFrame useThree extend] :as gdom]
            ["@react-three/drei" :refer [OrbitControls]]
            ;; ["three-stdlib" :refer [OrbitControls]]
            ["three" :as THREE]

            [goog.object :as gobj]

            [helix.core :as hx :refer [$ suspense]]
            ;; [helix.dom :as hdom]
            [helix.hooks :as hooks]
            ;; [cljs-bean.core :as bean]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.helix.lib :refer [defnc]]
            ))

(defnc CameraControls []
  (let [canvas (useThree)
        camera (. canvas -camera)
        domElement (.. canvas -gl -domElement)
        cameraref (hooks/use-ref nil)]
    (useFrame #(if 
                 (. cameraref -current) 
                 (.. cameraref -current update)))
    (set! ^js (.. camera -rotation -x) (.. THREE -MathUtils (degToRad 180)))
    ($ OrbitControls 
       {:ref cameraref 
        :args [camera, domElement] 
        :autoRotate false 
        :enableZoom true})))

(defnc Spinner []
  (let [groupref (hooks/use-ref nil)
        radius 16
        divisions 9
        radians (. (. THREE -MathUtils) degToRad (/ 360 8))]
    (useFrame (fn [] (if (. groupref -current) (set! ^js (.. groupref -current -rotation -z) (+ (.. groupref -current -rotation -z) 0.061)))))
    ($ :group {:ref groupref}
      (map (fn [index]
             (let [x (* radius (. js/Math cos (* radians index)))
                   y (* radius (. js/Math sin (* radians index)))]
               (if (not= index 1)
                 ($ :mesh
                   {:key (str "ball-" index)
                    :visible true
                    :position #js [x y 0]
                    :rotation #js [0 0 0]}
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
        torchref (hooks/use-ref nil)
        groupref (hooks/use-ref nil)]
    (useFrame #(if (. torchref -current)
                 (do (set! ^js (.. torchref -current -position -x) (.. camera -position -x))
                     (set! ^js (.. torchref -current -position -y) (.. camera -position -y))
                     (set! ^js (.. torchref -current -position -z) (.. camera -position -z)))))
    [($ CameraControls)
     ($ :spotLight
       {:key "camera-spotlight"
        :position #js [0 400 0]
        :lookAt #js [0 0 0]
        :color 0xCCCCCC
        :intensity 1
        :penumbra 0.5})
     ($ :spotLight
       {:key "static-spotlight"
        :lookAt #js [0 0 0]
        :color 0xCCAAAA
        :intensity 2
        :penumbra 0
        :ref torchref})
     ($ Spinner {:key "spinner"})]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)]
    ($ Canvas
      {:id "console"
       :gl {:alpha true}
       :onCreated (fn [scene camera] nil)
       :camera {:fov 30 :aspect 0.2 :near 1 :far 50000000 :position #js [40000 0 0] :zoom 2}
       :style {:position "absolute" :width "100%" :height "100%"
               :z-index 20}
       :concurrent true
       :pixelRatio (.-devicePixelRatio js/window)}
      ($ Scene))
    ))

(def GL (with-keechma Container))
