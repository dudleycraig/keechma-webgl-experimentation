import React from 'react';
import * as THREE from 'three';

export default (props) => {
  return (
    <mesh {...props}>
      <sphereGeometry attach="geometry" args={[100, 16, 16]} />
      <meshPhongMaterial
        attach="material"
        depthTest={true}
        depthWrite={true}
        side={THREE.FrontSide}
        color={0xff7700}
        reflectivity={0}
        flatShading={false}
        roughness={0.8}
        metalness={0.2}
        emissive={0x101010}
        specular={0x101010}
        shininess={100}
      />
    </mesh>
  );
};
