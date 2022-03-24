(ns main.ui.components.state-button
  (:require ["react" :as react]
   ["react-dom" :as rdom]
   ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
   ["@fortawesome/free-solid-svg-icons" :refer [faHome faUser faShare faSpinner faExclamation faCheck]]

   [helix.core :as hx :refer [$ <> suspense]]
   [helix.dom :as d]
   [helix.hooks :as hooks]

   [keechma.next.core :as keechma]
   [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub KeechmaRoot]]
   [keechma.next.helix.lib :refer [defnc]]
   [keechma.next.controllers.pipelines :refer [throw-promise!]]))

(defnc StateButton
  [{:keys [status onClick icon label class]
    :or {status "inert" onClick (fn [] nil) icon faShare label "submit" class ""}
    :as props}]

  (def status-to-icon-attributes
    {:inert {}
     :warning {:icon faExclamation}
     :active {:icon faSpinner :spin true}
     :success {:icon faCheck}
     :error {:icon faExclamation}})

  (def status-to-variant
    {:inert "primary"
     :warning "warning"
     :active "secondary"
     :success "success"
     :error "danger"})

  (d/button
    {:class (str "status-button " class " btn btn-outline-" (status-to-variant (keyword status)) " d-inline-flex flex-row justify-content-center align-items-center m-0")
     :type "button"
     :disabled (contains? '("active" "success") status)
     :onClick onClick}

    ($ FontAwesomeIcon {& (merge {:icon icon :size "1x" :spin false} (status-to-icon-attributes (keyword status)))})

    (d/span 
      {:class "ml-2"} 
      label)))













