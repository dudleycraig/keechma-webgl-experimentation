(ns main.ui.components.state-button
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@heroicons/react/outline" :refer [ShareIcon]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [main.utilities :refer [status-to-color status-to-icon merge-classes]]
            [main.ui.components.button :refer [Button]]))

(defnc StateButton
  [{:keys [status onClick icon label class type]
    :or {status "inert" 
         icon ShareIcon 
         onClick (fn [event] nil) 
         type "button"
         label "Submit" 
         class []}
    :as props}]

  ($ Button {:class (merge-classes (status-to-color (keyword status)) class) :type type :disabled (some #(= status %) ["active" "success" "error"]) :onClick onClick}
     ($ (get status-to-icon (keyword status) icon) {:className "self-center w-4 h-4 shrink-0"})
     (d/span {:class "ml-2"} label)))
