;; documentation on WGS84 and ECEF
;; https://danceswithcode.net/engineeringnotes/geodetic_to_ecef/geodetic_to_ecef.html
;; https://gist.github.com/govert/1b373696c9a27ff4c72a
;; https://www.unoosa.org/documents/pdf/icg/2020/ait-gnss2020/AIT2020_05.pdf
;; http://wiki.gis.com/wiki/index.php/Geodetic_system#Step_1:_Convert_WGS-84_to_ECEF
;; http://wiki.gis.com/wiki/index.php/Geodetic_system
;; https://en.wikipedia.org/wiki/Geographic_coordinate_conversion#From_geodetic_to_ECEF_coordinates

(ns main.lib.gis
  (:require [clojure.string :as str]))

(defn deg-to-rad 
  "convert decimal degrees to radians "
  [degrees] 
  (* degrees (/ js/Math.PI 180)))

(defn rad-to-deg 
  "convert radians to decimal degrees"
  [radians]
  (* radians (/ 180 js/Math.PI)))

(defn wgs84-to-ecef
  "convert WGS84 spherical coordinates (latitude, longitude, altitude) to ECEF cartesian coordinates (x, y, z)
   expects latitude and longitude as decimal degrees, altitude as meters [latitude, longitude, altitude]
   outputs cartesian coordinates [x, y, z]"
  [coords]
  (let [latitude (deg-to-rad (get coords 0))
        longitude (deg-to-rad (get coords 1))
        altitude (get coords 2)
        semi-major-axis 6378137.0
        first-eccentric-squared 6.6943799901377997e-3
        n (/ semi-major-axis (js/Math.sqrt (- 1 (* first-eccentric-squared (js/Math.sin latitude) (js/Math.sin latitude)))))
        x (* (+ n altitude) (js/Math.cos latitude) (js/Math.cos longitude))
        y (* (+ n altitude) (js/Math.cos latitude) (js/Math.sin longitude))
        z (* (+ (* n (- 1 first-eccentric-squared)) altitude) (js/Math.sin latitude))]
    [x y z]))

(comment
  (wgs84-to-ecef [37.8043722 -122.2708026 0.0])) ;; => [-2694044.4111565403, -4266368.805493665, 3888310.602276871]
