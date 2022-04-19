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
           :style {:position "relative" :width "800px" :height "400px" :background-color "#3a3f44" :border-style "solid" :border-color "#17191b" :border-width "1px" :border-radius "5px"}}
          ($ GL))))))

(def Stage (with-keechma Container))
