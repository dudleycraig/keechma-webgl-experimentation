(ns main.app
  (:require ["react-dom" :as rdom]

            [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]

            [main.controllers.timeout]))

(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates
   :keechma/controllers

   {:router
    #:keechma.controller
     {:params true
      :type :keechma/router
      :keechma/routes [["" {:page "home"}] ":page" ":page/:subpage"]}

    :dataloader
    #:keechma.controller
     {:params true
      :type :keechma/dataloader}

    :entitydb
    #:keechma.controller
     {:params true
      :type :keechma/entitydb
      :keechma.entitydb/schema {}}


;;      :signin/credentials
;;      #:keechma.controller
;;       {:params (fn [{:keys [signin]}]
;;                  (let [{:keys [active-states data]} signin]
;;                    {:credentials data}))
;;        :deps [:signin]
;;        :type :auth/credentials
;;        :_/on-submit (event/to-dispatch :signin :on-submit)}

    :timeout
    #:keechma.controller
     {:params true}}})



