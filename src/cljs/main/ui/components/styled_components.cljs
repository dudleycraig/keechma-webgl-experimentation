(ns main.ui.components.styled-components
  (:require [helix.core :as hx :refer [$ <>]]
            [helix.dom :as d]

            [keechma.next.helix.core :refer [with-keechma use-sub dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.helix.lib :refer [defnc]]))

(defclassified NavBar :div "fixed top-0 right-0 left-0 z-40 flex flex-wrap sm:flex-row items-center justify-between sm:justify-start py-1 px-4 bg-gray-900")

(defclassified NavBarBrand :a "inline-block py-1 mr-4 text-xl text-gray-100 hover:text-gray-100")

(defclassified NavBarNav :ul "flex flex-col pl-0 mb-0 list-none mr-auto sm:flex-row")

(defclassified NavBarItem :li "mx-2 px-2")

(defclassified NavBarLink :a "text-gray-200 hover:text-gray-500 sm:px-2")
