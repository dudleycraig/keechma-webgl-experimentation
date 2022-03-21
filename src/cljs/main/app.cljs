(ns main.app
  (:require [keechma.next.controllers.router]
            [keechma.next.controllers.subscription]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]
            ["react-dom" :as rdom]))

(defn page-eq? [page] (fn [{:keys [router]}] (= page (:page router))))

(defn role-eq? [role] (fn [deps] (= role (:role deps))))

(defn slug [{:keys [router]}] (:slug router))

(def homepage? (page-eq? "home"))

(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates,

   :keechma/controllers
   {:router #:keechma.controller
    {:params true,
     :type :keechma/router,
     :keechma/routes
     [["" {:page "home"}] ":page"]},
    :dataloader #:keechma.controller
    {:params true, :type :keechma/dataloader},
    :entitydb #:keechma.controller
    {:params true,
     :type :keechma/entitydb,
     :keechma.entitydb/schema
     {}}}})
