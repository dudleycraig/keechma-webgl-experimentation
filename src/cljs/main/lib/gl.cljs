(ns main.lib.gl
  (:require-macros [main.lib.gl])
  (:require ["react" :refer [Suspense useEffect useRef useContext createContext] :as react]
            ["react-dom" :as rdom]
            ["three" :as THREE]
            ["@react-three/fiber" :refer [Canvas Dom useFrame useThree extend] :as r3f]
            ;; ["@react-three/drei" :refer [OrbitControls useGLTF]]

            [helix.core]
            [helix.hooks]
            [helix.dom]))

(defn get-react [] react)

(def gl-canvas Canvas)
(def gl-dom Dom)

(defn render! [react-el dom-el]
  (rdom/render react-el dom-el))

(defn unmount! [dom-el]
  (rdom/unmountComponentAtNode dom-el))

(def create-context createContext)
(def use-context useContext)

(defn use-ref
  ([] (helix.hooks/use-ref nil))
  ([x] (helix.hooks/use-ref x)))

; from uix
(deftype StateHook [value set-value]
  Object
  (equiv [this other]
    (-equiv this other))

  IHash
  (-hash [o] (goog/getUid o))

  IDeref
  (-deref [o]
    value)

  IReset
  (-reset! [o new-value]
    (set-value new-value))

  ISwap
  (-swap! [o f]
    (set-value f))
  (-swap! [o f a]
    (set-value #(f % a)))
  (-swap! [o f a b]
    (set-value #(f % a b)))
  (-swap! [o f a b xs]
    (set-value #(apply f % a b xs)))

  IPrintWithWriter
  (-pr-writer [o writer opts]
    (-write writer "#object [StateHook ")
    (pr-writer {:val value} writer opts)
    (-write writer "]")))

(defn use-state [value]
  (let [[value set-value] (react/useState value)
        sh (react/useMemo #(StateHook. value set-value) #js [])]
    (react/useMemo (fn []
                     (set! (.-value sh) value)
                     (set! (.-set-value sh) set-value)
                     sh)
                   #js [value set-value])))
