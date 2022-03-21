import * as THREE from 'three';

export const appendCartesian3D = (position, radius = 6371010) => {
  if (position['c3d']) return position;
  if (position['c2d']) throw new Error('Cannot create Cartesian 3D: type 2D Cartesian translation not implemented.');
  if (position['dms']) throw new Error('Cannot create Cartesian 3D: type Degrees, Minutes, and Seconds translation not implemented.');
  if (position['rd']) return { ...position, c3d: new THREE.Vector3().setFromSphericalCoords(radius, position.rd.latitude, position.rd.longitude) };
  if (position['dd']) return { ...position, c3d: new THREE.Vector3().setFromSphericalCoords(radius, THREE.MathUtils.degToRad(position.dd.latitude), THREE.MathUtils.degToRad(position.dd.longitude)) };
  throw new Error('Cannot create Cartesian 3D: valid translation type not found.');
};

export const offsetPosition = (position, offset) => {
  if (!position['c3d']) return;
  if (!offset['c3d']) return;

  const vectorX = position.c3d.x - offset.c3d.x;
  const vectorY = position.c3d.y - offset.c3d.y;
  const vectorZ = position.c3d.z - offset.c3d.z;

  console.log('  offset', offset['c3d']);
  console.log('position', position['c3d']);
  console.log('  result', { x: vectorX, y: vectorY, z: vectorZ });

  // return { ...position, offset: new THREE.Vector3().set(0, -vectorX, vectorY) };
  return { ...position, offset: new THREE.Vector3().set(vectorX, vectorY, vectorZ) };
};
