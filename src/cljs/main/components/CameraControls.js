import React, { useRef } from 'react';
import { useFrame, useThree, extend } from 'react-three-fiber';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import * as THREE from 'three';

extend({ OrbitControls });
export default (props) => {
  const {
    gl: { domElement },
    camera,
  } = useThree();
  const controls = useRef();

  camera.rotation.x = THREE.MathUtils.degToRad(180);

  useFrame(() => controls.current.update());

  return <orbitControls ref={controls} args={[camera, domElement]} autoRotate={false} enableZoom={true} {...props} />;
};
