(ns main.ui.components.spinner
  (:require ["react" :as react]
            [helix.dom :as d]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]))

(defnc Spinner []
  (d/div {:style {:border-top-color "transparent"} 
          :class "w-4 h-4 border-2 border-white border-dashed rounded-full animate-spin"}))
