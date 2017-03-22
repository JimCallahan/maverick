(ns maverick.css
  (:require [garden.def :refer [defstyles]]))

(def piece
  {:stroke-miterlimit 10
   :stroke-width "6px"})

(def detail
  {:stroke-width "0px"})

(defstyles screen
  [:.lite-squares {:fill "#d9e6f2"}]
  [:.dark-squares {:fill "#9fbfdf"}]
  [:.white (merge piece
                  {:fill "#ffffff"
                   :stroke "#000000"})]
  [:.black (merge piece
                  {:fill "#000000"
                   :stroke "#ffffff"})]
  [:.white-detail (merge detail {:fill "#000000"})]
  [:.black-detail (merge detail {:fill "#ffffff"})])
                     
