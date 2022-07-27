(ns main.lib.gl-dom
  (:require-macros [main.lib.gl])
  (:require ["three" :as THREE]
            [helix.impl.props]
            [applied-science.js-interop :as j]))

(def group "group")
(def scene "scene")

(def mesh "mesh")
(def instanced-mesh "instancedMesh")

(def object-3d "object3D")
(def points "points")
(def fog "fog")

(def box-buffer-geometry "boxBufferGeometry")
(def buffer-geometry "bufferGeometry")
(def sphere-geometry "sphereGeometry")
(def text-geometry "textGeometry")
(def plane-buffer-geometry "planeBufferGeometry")
(def shape-buffer-geometry "shapeBufferGeometry")

(def buffer-attribute "bufferAttribute")

(def color "color")

(def mesh-basic-material "meshBasicMaterial")
(def mesh-standard-material "meshStandardMaterial")
(def mesh-normal-material "meshNormalMaterial")
(def mesh-phong-material "meshPhongMaterial")
(def mesh-lambert-material "meshLambertMaterial")
(def shadow-material "shadowMaterial")
(def points-material "pointsMaterial")

(def ambient-light "ambientLight")
(def point-light "pointLight")
(def spot-light "spotLight")
(def hemisphere-light "hemisphereLight")

; -- wrappers ---------------------------------------------------------------------------------------------------------------

(def linear-filter THREE/LinearFilter)

(def texture-loader THREE/TextureLoader)

(defn create-texture-loader
  ([] (texture-loader.))
  ([manager] (texture-loader. manager)))

(def webgl-render-target-class THREE/WebGLRenderTarget)

(defn create-webgl-render-target
  ([width height] (webgl-render-target-class. width height))
  ([width height options] (webgl-render-target-class. width height options)))

(def object-3d-class THREE/Object3D)

(defn create-object-3d []
  (object-3d-class.))

(def font-loader THREE/FontLoader)

(defn create-font-loader
  ([] (font-loader.))
  ([manager] (font-loader. manager)))

(defn create-vector3
  ([] (THREE/Vector3.))
  ([x y z] (THREE/Vector3. x y z)))

(def animation-mixer-class THREE/AnimationMixer)

(defn create-animation-mixer
  ([] (animation-mixer-class.))
  ([root] (animation-mixer-class. root)))

(defn update-mixer [mixer delta-time]
  (.update mixer delta-time))

(def catmull-rom-curve3-class THREE/CatmullRomCurve3)

(defn create-catmull-rom-curve3
  ([points] (catmull-rom-curve3-class. points)))

(defn deg-to-rad [n]
  (j/call-in THREE [.-Math .-degToRad] n))

(def pcf-soft-shadow-map THREE/PCFSoftShadowMap)

(def shader-material-class THREE/ShaderMaterial)

(def back-side THREE/BackSide)

(defn play-animation-action! [action]
  (.play action))

;; (defn get-clip-animation-action
;;   ([mixer clip] (.clipAction mixer clip))
;;   ([mixer clip root] (.clipAction mixer clip root)))

;; (defn compute-bounding-box! [geom]
;;   (.computeBoundingBox geom))

(defn get-geometry [o]
  (.-geometry o))

(defn export-bounding-box-size! [geom out-size]
  (j/call-in geom [.-boundingBox .-getSize] out-size))

(defn get-x [o]
  (.-x o))

(defn get-y [o]
  (.-y o))

(defn get-z [o]
  (.-z o))

(defn vector3-add! [v d]
  (.add v d))

(defn vector3-clone [v]
  (.clone v))

;; (defn camera-look-at! [camera x y z]
;;   (.lookAt camera x y z))
