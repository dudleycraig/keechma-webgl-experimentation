![dynamic screenshot](https://raw.githubusercontent.com/dudleycraig/keechma-webgl-experimentation/master/public/images/contrived-status.screenshot.gif)

[experimentation](http://functional.org.za/keechma-webgl-experimentation)

## installation and running

> git clone https://github.com/dudleycraig/keechma-webgl-experimentation.git functional 

> npm install

> npm run frontend

At this point you'll should receive a compile error something along the lines of ...
```
[dev] Closure compilation failed with 2 errors
[dev] --- node_modules/three-stdlib/helpers/PositionalAudioHelper.js:18
[dev] cannot access this before calling super()
[dev] --- node_modules/three-stdlib/helpers/RectAreaLightHelper.js:17
[dev] cannot access this before calling super()
```
As of writing this, it's a es6 compilation bug regarding pmndrs/three-stdlib
Currently the files at fault can be found at 
node_modules/three-stdlib/helpers/LightProbeHelper.js
node_modules/three-stdlib/helpers/PositionalAudioHelper.js
node_modules/three-stdlib/helpers/RectAreaLightHelper.js

To resolve the issue, 
* edit similar class definitions to the following  
```
class LightProbeHelper extends Mesh {
  constructor(lightProbe, size) {
    this.lightProbe = lightProbe;
    this.size = size;
    ...
```
* removing destructured arguments and replacing with an umbrella props argument.
* add "super(props)" as first line of constructor
* and either destructure props as per removed arguments or access arguments directly from props object as follows 
```
class LightProbeHelper extends Mesh {
  constructor(props) {
    super(props);
    this.lightProbe = props.lightProbe;
    this.size = props.size;
    ...
```

## fsm (finite state machine) tutorial 

This is a quick overview on applying a state machine to a keechma controller.

The example is of button triggering a state cascade, from an "inert" state, transitioning to "active", "success", and finaly transitioning back to "inert".
```
            inert
              |
            active
              |
    +---------+---------+
 success   warning    error
    |         |      [reset]
    +---------+---------+
              |
            inert
```

During the "active" state, a contrived promise is created from a simple setTimeout within the pipeline and has a weighted chance of returning either a status of "success", "warning", or "error".  

The basic flow of the controller (mapped out in /src/cljs/main/controllers/contrived.cljs) can be summarised as follows ...
```
:inert
  handle ":init"
    update state
    trigger "::init" (Event handled by pipeline. On completion, triggers response and error events)
  handle ":on-init-response" (event triggered by pipeline)
    transition ":active"
```
```
:active
  handle enter
    trigger "::active" (Event handled by pipeline. On completion, triggers response and error events)
  handle ":on-active-response" (event triggered by pipeline)
    transition ":success"
  handle ":on-active-warning" (event triggered by pipeline)
    transition ":warning"
  handle ":on-active-error" (event triggered by pipeline)
    transition ":error"
```
```
:success
  handle enter
    update state
    trigger "::success" (Event handled by pipeline. On completion, triggers response and error events)
  handle ":on-success-response" (event triggered by pipeline)
    update state
    transition ":inert"
```
```
:warning
  handle enter
    update state
    trigger "::warning" (Event handled by pipeline. On completion, triggers response and error events)
  handle ":on-warning-response" (event triggered by pipeline)
    update state
    transition ":inert"
```
```
:error
  handle enter
    update state
  handle ":reset"
    update state
    transition ":inert"
```

  





