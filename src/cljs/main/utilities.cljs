(ns main.utilities
  (:require [applied-science.js-interop :as j]
            [clojure.string :as str]
            ["@heroicons/react/outline" :refer [HomeIcon UserIcon ShareIcon ExclamationIcon CheckCircleIcon]]
            [main.ui.components.spinner :refer [Spinner] :rename {Spinner SpinnerIcon}]))

(defn class-to-vector
  "returns a vector when given either a string or vector of a css class attribute value"
  [class-attribute-values]
  (if (string? class-attribute-values) (str/split class-attribute-values #"\s") class-attribute-values))

(defn merge-classes
  "takes multiple css classes as either strings or vectors and merges them"
  [& classes]
  (reduce (fn [accumulator class-value] (distinct (concat (class-to-vector class-value) accumulator))) [] classes))

(def status-to-icon
  {:warning  ExclamationIcon
   :active SpinnerIcon
   :success CheckCircleIcon
   :error ExclamationIcon})

(def status-to-color
  {:warning "text-orange-500" 
   :active "text-inherit" 
   :success "text-green-500" 
   :error "text-red-500"})
