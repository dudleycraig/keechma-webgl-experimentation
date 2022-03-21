(ns main.app
  (:require [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]
            ["react-dom" :as rdom]))

(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates
   :keechma/controllers

   {:router #:keechma.controller 
    {:params true 
     :type :keechma/router 
     :keechma/routes [["" {:page "home"}] ":page" ":page/:subpage"]}

    :dataloader #:keechma.controller 
    {:params true 
     :type :keechma/dataloader}

    :entitydb #:keechma.controller 
    {:params true 
     :type :keechma/entitydb 
     :keechma.entitydb/schema {}}}})
