// WARNING: DO NOT EDIT!
// THIS FILE WAS GENERATED BY SHADOW-CLJS AND WILL BE OVERWRITTEN!

var ALL = {};
ALL["@react-three/fiber"] = require("@react-three/fiber");
ALL["three"] = require("three");
ALL["@fortawesome/react-fontawesome"] = require("@fortawesome/react-fontawesome");
ALL["react-dom"] = require("react-dom");
ALL["@react-three/drei"] = require("@react-three/drei");
ALL["react"] = require("react");
ALL["@fortawesome/free-solid-svg-icons"] = require("@fortawesome/free-solid-svg-icons");
global.shadow$bridge = function shadow$bridge(name) {
  var ret = ALL[name];

  if (ret === undefined) {
     throw new Error("Dependency: " + name + " not provided by external JS. Do you maybe need a recompile?");
  }

  return ret;
};

shadow$bridge.ALL = ALL;