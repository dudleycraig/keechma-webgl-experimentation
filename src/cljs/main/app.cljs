(ns main.app
  (:require ["react-dom" :as rdom]

            [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]

            [main.controllers.contrived]
            [main.controllers.stage]))

(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates
   :keechma/controllers

   {:router
    #:keechma.controller
     {:params true
      :type :keechma/router
      :keechma/routes [["" {:page "home"}] ":page" ":page/:subpage"]}

    ;; :dataloader
    ;; #:keechma.controller
    ;; {:params true
    ;;  :type :keechma/dataloader}

    ;; :entitydb
    ;; #:keechma.controller
    ;;  {:params true
    ;;   :type :keechma/entitydb
    ;;   :keechma.entitydb/schema {}}

    :contrived
    #:keechma.controller
     {:params {:status "inert" :messages []}}

    :stage
    #:keechma.controller
     {:params {:status "inert" :messages [] :container (. js/document getElementById "canvas")} 
      :deps [:router]}}})

