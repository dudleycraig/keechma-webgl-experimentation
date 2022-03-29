(ns main.ui.components.messages
  (:require ["react" :as react]
            ["react-dom" :as rdom]
            ["@fortawesome/react-fontawesome" :refer [FontAwesomeIcon]]
            ["@fortawesome/free-solid-svg-icons" :refer [faHome faUser faShare faSpinner faExclamation faCheck]]

            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            
            [keechma.next.helix.core :refer [with-keechma use-sub use-meta-sub dispatch KeechmaRoot]]
            [keechma.next.helix.lib :refer [defnc]]

            [main.ui.components.time :refer [Time]]))

(defnc Messages [{:keys [messages class]
                  :or {messages [] class ""}
                  :as props}]

  (def status-to-variant
    {:inert "primary"
     :warning "warning"
     :active "secondary"
     :success "success"
     :error "danger"})

  (d/ul {:class (str "messages list-group" class)}
    (into
     []
     (map-indexed
       (fn [index {:keys [text status timestamp]
                   :or {text "Undefined error." status "error" timestamp (.getTime (js/Date.))}
                   :as message}]
         (d/li {:class (str "message my-1 rounded list-group-item list-group-item-" (status-to-variant (keyword status))) :key (str "message-" index)}
           ($ Time timestamp)
           (d/span {:class "text pl-2"} text)))
       messages))))

