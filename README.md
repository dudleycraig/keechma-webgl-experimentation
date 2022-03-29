![dynamic screenshot](https://raw.githubusercontent.com/dudleycraig/keechma-webgl-experimentation/master/public/images/contrived-status.screenshot.gif)

[experimentation](http://functional.org.za/keechma-webgl-experimentation)

## installation and running

> git clone https://github.com/dudleycraig/experimentation.git functional 

> npm install

> npm run frontend

## fsm (finite state machine) tutorial 

This is a quick overview on applying a state machine to a keechma controller.

The example is of button triggering a state cascade, 
from an "inert" state, transitioning to "active", "success", and finally transitioning back to "inert".

inert -> active |-> success -> inert
                |
                |-> warning -> inert
                |
                |-> error -> reset -> inert

during the "active" state, a contrived promise is created from a simple setTimeout within the pipelines and has a weighted chance of returning either a status of "success", "warning", or "error".  
```
(defn contrived-async-promise
  "an asyncronous process meant to mimic an ajax transaction 
  returns a promise"
  []
  (promesa/create
   (fn [resolve reject]
     (js/setTimeout
      (fn []
        (case (wrand [6 3 3])
          0 (do (js/console.log "success triggered") (resolve {:status "success" :messages [{:status "success" :timestamp (.getTime (js/Date.)) :text "A contrived success message."}]}))
          1 (do (js/console.warn "warning triggered") (resolve {:status "warning" :messages [{:status "warning" :timestamp (.getTime (js/Date.)) :text "A contrived warning message."}]}))
          2 (do (js/console.error "error triggered") (reject {:status "error" :messages [{:status "error" :timestamp (.getTime (js/Date.)) :text "A contrived error message."}]}))
          (do (js/console.error "default triggered") (reject {:status "error" :messages [{:status "error" :timestamp (.getTime (js/Date.)) :text "Failed retrieving a contrived message."}]}))))
      1500))))
```
