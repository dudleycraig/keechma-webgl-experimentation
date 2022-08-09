(ns main.ui.components.gldom.scene
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

            [main.ui.components.gldom.custom-camera :refer [CustomCamera]]
            [main.ui.components.gldom.custom-orbit-controls :refer [CustomOrbitControls]]
            [main.ui.components.gldom.spinner :refer [Spinner]]
            [main.ui.components.gldom.ball :refer [Ball]]
            [main.ui.components.gldom.node :refer [Node]]))

(defnc Scene [{:keys [initial-camera-position]
               :or {initial-camera-position #js[0 0 0]}
               :as props}]
  (let [canvas (useThree)
        camera (. canvas -camera)
        torchref (hooks/use-ref nil)
        groupref (hooks/use-ref nil)
        element (.. canvas -gl -domElement)]
    (useFrame (fn []
                (if (. torchref -current)
                  (do ;; set torch to follow camera
                      (set! ^js (.. torchref -current -position -x) (.. camera -position -x))
                      (set! ^js (.. torchref -current -position -y) (.. camera -position -y))
                      (set! ^js (.. torchref -current -position -z) (.. camera -position -z))))))
    [($ CustomCamera
        {:key "custom-camera" 
         :position initial-camera-position})
     ($ CustomOrbitControls
        {:key "custom-orbit-controls"})
     ($ :spotLight
        {:key "camera-torch" 
         :position initial-camera-position 
         :lookAt #js[0 0 0] 
         :color 0xEEEEEE
         :intensity 0.8 
         :penumbra 0.01 
         :ref torchref})
     ($ :spotLight
        {:key "stage-spotlight" 
         :position #js[100 100 0] 
         :lookAt #js[0 0 0] 
         :color 0xAAAAAA 
         :intensity 0.4
         :penumbra 0.01})
     (suspense
       {:key "stage-floor" :fallback ($ Spinner)}
       ($ Node))]))
