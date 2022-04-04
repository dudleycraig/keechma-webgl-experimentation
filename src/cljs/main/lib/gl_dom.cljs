(ns main.lib.gl-dom
  (:require-macros [main.lib.gl])
  (:require ["three" :as THREE]
            [helix.impl.props]
            [applied-science.js-interop :as j]))

(def gl-group "group")
(def gl-scene "scene")

(def gl-mesh "mesh")
(def gl-instanced-mesh "instancedMesh")

(def gl-object-3d "object3D")
(def gl-points "points")
(def gl-fog "fog")

(def gl-box-buffer-geometry "boxBufferGeometry")
(def gl-buffer-geometry "bufferGeometry")
(def gl-sphere-geometry "sphereGeometry")
(def gl-text-geometry "textGeometry")
(def gl-plane-buffer-geometry "planeBufferGeometry")
(def gl-shape-buffer-geometry "shapeBufferGeometry")

(def gl-buffer-attribute "bufferAttribute")

(def gl-color "color")

(def gl-mesh-basic-material "meshBasicMaterial")
(def gl-mesh-standard-material "meshStandardMaterial")
(def gl-mesh-normal-material "meshNormalMaterial")
(def gl-mesh-phong-material "meshPhongMaterial")
(def gl-mesh-lambert-material "meshLambertMaterial")
(def gl-shadow-material "shadowMaterial")
(def gl-points-material "pointsMaterial")

(def gl-ambient-light "ambientLight")
(def gl-point-light "pointLight")
(def gl-spot-light "spotLight")
(def gl-hemisphere-light "hemisphereLight")

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
  (j/call-in three [.-Math .-degToRad] n))

(def pcf-soft-shadow-map THREE/PCFSoftShadowMap)

(def shader-material-class THREE/ShaderMaterial)

(def back-side THREE/BackSide)

(defn play-animation-action! [action]
  (.play action))

(defn get-clip-animation-action
  ([mixer clip] (.clipAction mixer clip))
  ([mixer clip root] (.clipAction mixer clip root)))

(defn compute-bounding-box! [geom]
  (.computeBoundingBox geom))

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

(defn camera-look-at! [camera x y z]
  (.lookAt camera x y z))
