(ns maverick.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
  [:.lite-squares {:fill "#d9e6f2"}]
  [:.dark-squares {:fill "#9fbfdf"}]
  [:.piece {:stroke-miterlimit 10
            :stroke-width "6px"}]
  [:.piece.white {:fill "#ffffff"
                  :stroke "#000000"}]
  [:.piece.black {:fill "#000000"
                  :stroke "#ffffff"}]
  [:.white :.detail {:stroke-width "0px"
                     :fill "#000000"}]
  [:.black :.detail {:stroke-width "0px"
                     :fill "#ffffff"}])
                     
