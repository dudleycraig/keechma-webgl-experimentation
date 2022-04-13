(ns main.ui.pages.stage
  (:require ["core-js/stable"]
            ["regenerator-runtime/runtime"]

            ["react" :refer [Suspense useEffect useRef] :as react]
            ["react-dom" :as rdom]
            ["three" :as THREE]
            ;; ["@react-three/cannon" :refer [Physics usePlane useBox useCylinder useRaycastVehicle]]
            ;; ["@react-three/drei" :refer [OrbitControls useGLTF]]
            ;; ["@react-three/fiber" :refer [Canvas useFrame useThree extend] :as gdom]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faUser faShare faSpinner faExclamation faCheck]]

            [helix.core :as hx :refer [$ suspense]]
            [helix.dom :as hdom]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.helix.lib :refer [defnc]]
            
            ;; [main.lib.gl :refer [gl-canvas use-ref]] ;;  defnc $
            ;; [main.lib.gl-r3f :refer [use-frame]]
            ;; [main.lib.gl-dom :refer [gl-mesh gl-group gl-spot-light]]
            ;; [main.lib.gl-dom :as gdom]

            [main.ui.components.gl :refer [GL]]
            ))

;; (defnc CameraControls []
;;   (let [canvas (useThree)
;;         camera (. canvas -camera)
;;         domElement (.. canvas -gl -domElement)
;;         cameraref (useRef)])
;;   (useFrame #(if 
;;                (. cameraref -current) 
;;                (.. cameraref -current update)))
;;   (set! ^js (.. camera -rotation -x) (.. THREE -MathUtils (degToRad 180)))
;;   ($ OrbitControls 
;;      {:ref cameraref 
;;       :args [camera, domElement] 
;;       :autoRotate false 
;;       :enableZoom true}))

;; (defnc Spinner []
;;   (let [groupref (useRef)
;;         radius 16
;;         divisions 9 
;;         radians (. (. THREE -MathUtils) degToRad (/ 360 8))]
;;     (useFrame #(if (. groupref -current) ( set! ^js (.. groupref -current -rotation -z) (+ (.. groupref -current -rotation -z) 0.061))))
;;     (:group {:ref groupref}
;;        (map (fn [index] 
;;               (let [x (* radius (. js/Math cos (* radians index)))
;;                     y (* radius (. js/Math sin (* radians index)))]
;;                 (if (not= index 1)
;;                   ($ :mesh
;;                      {:key (str "ball-" index)
;;                       :visible true
;;                       :position [x y 0]
;;                       :rotation [0 0 0]}
;;                      ($ :sphereGeometry
;;                         {:attach "geometry" 
;;                          :args [5 20 20]})
;;                      ($ :meshPhongMaterial
;;                         {:attach "material"
;;                          :depthTest true
;;                          :depthWrite true
;;                          :side (. THREE -FrontSide)
;;                          :color 0xff7700
;;                          :reflectivity 0
;;                          :flatShading false
;;                          :roughness 0.8
;;                          :metalness 0.2
;;                          :emissive 0x101010
;;                          :specular 0x101010
;;                          :shininess 100})
;;                      ))))
;;             (range 1 divisions)))))

;; (defnc Scene []
;;   (let [canvas (useThree)
;;         camera (. canvas -camera)
;;         torchref (useRef)
;;         groupref (useRef)]
;;     (useFrame #(if (. torchref -current)
;;                  (do (set! ^js (.. torchref -current -position -x) (.. camera -position -x))
;;                      (set! ^js (.. torchref -current -position -y) (.. camera -position -y))
;;                      (set! ^js (.. torchref -current -position -z) (.. camera -position -z)))))
;;     [ 
;;       ;; ($ CameraControls)
;;       (:spotLight
;;          {:key "camera-spotlight" 
;;           :position [0 400 0]
;;           :lookAt [0 0 0]
;;           :color 0xCCCCCC
;;           :intensity 1
;;           :penumbra 0.5})
;;       (:spotLight
;;          {:key "static-spotlight"
;;           :lookAt [0 0 0]
;;           :color 0xCCAAAA
;;           :intensity 2
;;           :penumbra 0
;;           :ref torchref})
;;       ;; ($ Spinner)
;;     ]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)]

    (hdom/div
      {:class "container" :style {:margin-top "60px"} :id "stage"}

      (hdom/div
        {:class "row mb-2"}
        (hdom/div {:class "col"}
                  "stage"))

      (hdom/div
        {:class "row mb-2"}
        (hdom/div
          {:class "col" 
           :style {:width "1000px" :height "600px" :background-color "#3a3f44" :border-style "solid" :border-color "#17191b" :border-width "1px" :border-radius "5px"}}
          ($ GL))))))

(def Stage (with-keechma Container))
