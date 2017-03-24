(ns maverick.css
  (:require [garden.def :refer [defstyles]]))

(def piece
  {:stroke-miterlimit 10
   :stroke-width "6px"
   :pointer-events "none"})

(def detail
  {:stroke-width "0px"})

(def bracket
   {:stroke-miterlimit 10
    :stroke-width "15px"
    :fill "none"
    :pointer-events "none"})

(defstyles screen
  [:.lite-squares {:fill "#d9e6f2"}]
  [:.dark-squares {:fill "#9fbfdf"}]
  [:.white (assoc piece
                  :fill "#ffffff"
                  :stroke "#000000")]  
  [:.black (assoc piece
                  :fill "#000000"
                  :stroke "#ffffff")]
  [:.white-detail (assoc detail :fill "#000000")]
  [:.black-detail (assoc detail :fill "#ffffff")]
  [:.hover (assoc bracket :stroke "#ffff00")]
  [:.move (assoc bracket :stroke "#00ff00")])
