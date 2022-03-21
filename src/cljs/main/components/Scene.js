import React, { Suspense, useRef } from 'react';
import { useFrame, useThree } from 'react-three-fiber';
import * as THREE from 'three';
import { appendCartesian3D, offsetPosition } from '../Utils';
import CameraControls from './CameraControls';
import Loading from './Loading';
import Marker from './Marker';
import CapeTown from './CapeTown';

const Log = () => (console.log('testing'), null);

export default () => {
  const { camera, scene } = useThree();
  const torchRef = useRef();

  useFrame(({}, delta) => {
    torchRef.current.position.x = camera.position.x;
    torchRef.current.position.y = camera.position.y;
    torchRef.current.position.z = camera.position.z;
  });

  const worldRadiusInMeters = 6371010;
  const map = {
    position: { dd: { latitude: -33.91775, longitude: 18.40292, altitude: 0 } },
    size: { x: 34244, y: 19568, z: 1365 },
    radius: 6371000, // earth radius in meters
    scale: 14,
  };

  const home = {
    position: { dd: { latitude: -33.919545175839765, longitude: 18.38205572274231, altitude: 0 } },
    name: 'home',
    description: 'Where the MAGIC happens.',
    graphics: { pixelSize: 100 },
  };

  const center = {
    position: { dd: { latitude: -33.91775, longitude: 18.40292, altitude: 0 } },
    name: 'center',
  };

  map.position = appendCartesian3D(map.position);
  home.position = offsetPosition(appendCartesian3D(home.position), map.position);
  center.position = offsetPosition(appendCartesian3D(center.position), map.position);

  return (
    <>
      <CameraControls />
      <spotLight position={[0, 400, 0]} lookAt={[0, 0, 0]} color={0xcccccc} intensity={1} penumbra={0.5} />
      <spotLight ref={torchRef} lookAt={[0, 0, 0]} color={0xccaaaa} intensity={2} penumbra={0} />
      <Suspense fallback={<Loading />}>
        <group rotation={[THREE.MathUtils.degToRad(0), THREE.MathUtils.degToRad(180), THREE.MathUtils.degToRad(90)]}>
          <CapeTown />
          <Marker position={[center.position.offset.x, center.position.offset.y, center.position.offset.z]} />
          <Marker position={[home.position.offset.x, home.position.offset.y, home.position.offset.z]} />
        </group>
      </Suspense>
      <axesHelper args={20000} />
    </>
  );
};
