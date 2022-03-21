import React from 'react';
import * as THREE from 'three';
import { Canvas } from 'react-three-fiber';
import Scene from './Scene';

/**
http://functional.org.za/engine/console
The virtual engine projects the real-time state of a 3D printed physical engine, primarily using react-three-fiber and zustand.
The 3D components were designed on Blender and exported as gltf's (glb) and composed. Animation is purely trigonometric calculations on the current state (rotational angle).

http://functional.org.za/weather
GIS representation of real-time weather on a 3D map primarily using react-three-fiber and zustand.
The map was generated via BlenderGIS and exported as a gltf (glb). The projected attribute data, regional boundaries, were collated in QGis and exported as GeoJSON to MongoDB. I then used Apollo to stitch these attributes with a real-time, restful weather API (afrigis). The data-set is then, via GraphQL queries, projected on the 3D map.
 **/

export default ({ staticContext, ...props }) => {
  const initialCameraPosition = [40000, 0, 0];

  return (
    <Canvas
      id="console"
      gl={{ alpha: true }}
      onCreated={({ scene, camera }) => {
        // camera.up = new THREE.Vector3(0, 0, 1);
        // scene.translateY(50);
      }}
      camera={{ fov: 30, aspect: 0.2, near: 1, far: 50000000, position: initialCameraPosition, zoom: 2 }}
      style={{ position: 'absolute', width: '100vw', height: '100vh', left: '0px', top: '0px', zIndex: 20 }}
      concurrent
      pixelRatio={window.devicePixelRatio}
      {...props}
    >
      <Scene />
    </Canvas>
  );
};
