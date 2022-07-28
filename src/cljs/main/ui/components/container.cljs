(ns main.ui.components.container
  (:require ["react" :as react]
            [helix.dom :as d]
            [keechma.next.helix.lib :refer [defnc]]
            [main.utilities :refer [merge-classes]]))

(defnc Container [{:keys [class children]
                   :or {class []}
                   :as props}]
  (d/div {:class (merge-classes "container-md" class)}
         children))
