(ns main.ui.pages.stage
  (:require ["react" :refer [Suspense useEffect useRef] :as react]
            ["react-dom" :as rdom]
    
            ["core-js/stable"]
            ["regenerator-runtime/runtime"]

            ["three" :as THREE]
            ["@heroicons/react/outline" :refer [HomeIcon CubeIcon MailIcon]]

            [helix.core :as hx :refer [$ suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]
            [keechma.next.helix.lib :refer [defnc]]))

(defnc Container [props]
  (let [{:keys [active-states data]} (use-sub props :stage)
        {:keys [width height]} (get-in data [:canvas :dimensions])]
    (d/section {:class "flex flex-col justify-center items-center w-3/4 h-64 bg-white rounded-lg m-3 p-3" :id "canvas"})))

(def Stage (with-keechma Container))
