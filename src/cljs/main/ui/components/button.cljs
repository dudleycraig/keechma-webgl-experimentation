(ns main.ui.components.button
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@heroicons/react/outline" :refer [HomeIcon CubeIcon MailIcon UserIcon ShareIcon ExclamationIcon CheckCircleIcon]]

            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]

            [keechma.next.core :as keechma]
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.pipelines :refer [throw-promise!]]

            [main.utilities :refer [merge-classes]]))

(defnc Container [{:keys [onClick disabled class type children]
                   :or {onClick (fn [event] nil)
                        type "button"
                        disabled false
                        class []}
                   :as props}]
  (d/button {:class (merge-classes "relative flex items-center justify-between h-12 px-3 rounded-lg bg-gray-900 disabled:opacity-75 text-white" class)
             :type type 
             :disabled disabled
             :onClick onClick}
            children))

(def Button (with-keechma Container))

