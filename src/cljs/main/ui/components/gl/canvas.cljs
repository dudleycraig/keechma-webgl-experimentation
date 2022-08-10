(ns main.ui.components.gl.canvas
  (:require ["core-js/stable"]
            ["regenerator-runtime/runtime"]
            ["react" :refer [Suspense useRef] :as react]
            [three]
            ["three/examples/jsm/controls/OrbitControls" :refer [OrbitControls]]
            [cljs.pprint :as pprint]
            [helix.core :as hx :refer [$ defnc suspense <>]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]))

(defnc Container [{:keys [antialias alpha output-encoding scale camera children] 
                   :or {antialias true alpha true output-encoding (. three -sRGBEncoding) scale 5.6
                        camera {:fov 75 :near 0.1 :far 1000}}
                   :as props}]
  (let [{:keys [active-states data]} (use-sub props :stage)
        {:keys [status messages]} data
        [state set-state] (hooks/use-state {:renderer nil :scene nil :animation-frame nil})
        canvas-ref* (hooks/use-ref nil)]
    (hooks/use-effect
      []
      (let [{:keys [fov near far]} camera] 
        (when (not= @canvas-ref* nil)
          (let [canvas @canvas-ref*
                width (. canvas -clientWidth)
                height (. canvas -clientHeight)
                aspect (/ width height)
                camera (three/PerspectiveCamera. fov aspect near far)
                scene (three/Scene.)
                geometry (three/BoxGeometry. 1 1 1)
                material (three/MeshBasicMaterial. #js {"color" 0x00ff00})
                cube (three/Mesh. geometry material)
                renderer (three/WebGLRenderer. #js {"alpha" alpha "antialias" antialias})]
            (set-state merge state {:renderer renderer :scene scene})
            (set! (. renderer -outputEncoding) output-encoding)
            (. renderer setClearColor 0xffffff 1)
            (. renderer setPixelRatio (. js/window -devicePixelRatio))
            (. renderer setSize width height)
            (. canvas appendChild (. renderer -domElement))
            (. scene add cube)
            (set! (.. camera -position -z) 5)
            (defn render []
              (set-state assoc :animation-frame (js/requestAnimationFrame render))
              (set! (.. cube -rotation -x) (+ (.. cube -rotation -x) 0.001))
              (set! (.. cube -rotation -y) (+ (.. cube -rotation -y) 0.001))
              (.render renderer scene camera))
            (render)
            (fn [] 
              (js/cancelAnimationFrame (:animation-frame state))
              (set-state assoc :animation-frame nil)
              (. (:renderer state) dispose))))
        nil))
    #_(when 
      (not= (:renderer state) nil)
      (do
        (println "frame: " (.. (:renderer state) -info -render -frame))
        (println "scene: " (. js/JSON stringify (:scene state)))))
    (d/div
      {:id "canvas"
       :ref canvas-ref*
       :style {:width "100%" :height "100%"}}
      children)))

(def Canvas (with-keechma Container))
