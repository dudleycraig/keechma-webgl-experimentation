![dynamic screenshot](https://raw.githubusercontent.com/dudleycraig/keechma-webgl-experimentation/master/public/images/contrived-status.screenshot.gif)

[experimentation](http://functional.org.za/keechma-webgl-experimentation)

## installation and running

> git clone https://github.com/dudleycraig/experimentation.git functional 

> npm install

> npm run frontend

## fsm (finite state machine) tutorial 

This is a quick overview on applying a state machine to a keechma controller.

The example is of button triggering a state cascade, from an "inert" state, transitioning to "active", "success", and finally transitioning back to "inert".
```
inert -> active |-> success -> inert
                |
                |-> warning -> inert
                |
                |-> error -> reset -> inert
```

During the "active" state, a contrived promise is created from a simple setTimeout within the pipeline and has a weighted chance of returning either a status of "success", "warning", or "error".  

The basic flow of the controller can be sumarised as follows ...

```
:inert
  handle ":init"
    update state
    trigger "::init"
  handle ":on-init-response"
    transition ":active"
```
```
:active
  handle enter
    trigger "::active" event
  handle ":on-active-response"
    transition ":success"
  handle ":on-active-warning"
    transition ":warning"
  handle ":on-active-error"
    transition ":error"
```
```
:success
  handle enter
    update state
    trigger "::success"
  handle ":on-success-response"
    update state
    transition ":inert"
```
```
:warning
  handle enter
    update state
    trigger "::warning"
  handle ":on-warning-response"
    update state
    transition ":inert"
```
```
:error
  handle enter
    update state from exception
  handle ":reset"
    update state
    transition ":inert"
```
  





