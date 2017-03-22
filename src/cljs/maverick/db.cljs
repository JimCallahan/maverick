(ns maverick.db
  (:require [clojure.spec :as s]))            

;;------------------------------------------------------------------------------
;; Specs
;;------------------------------------------------------------------------------

;;
;; Look and Feel
;;

;; The possible board sizes.
(def board-size? #{::small ::medium ::large})

;; The visible size of the playing board. 
(s/def ::board-size board-size?)

;; Appearance related configuration.
(def look? (s/keys ::req [::board-size]))


;;
;; Board 
;;

;; The number of squares horizontally direction.
(s/def ::cols pos-int?)

;; The number of squares in the vertical direction.
(s/def ::rows pos-int?)

;; The description of the board layout.
(def board? (s/keys :req [::rows ::cols]))


;;
;; Position
;;

;; The possible colors for players and pieces.
(def color? #{::white ::black})

;; The color of a player or piece.
(s/def ::color color?)

;; The possible kinds of chess pieces.
(def kind? #{::pawn ::knight ::bishop ::rook ::hawk ::elephant ::queen ::king})

(s/def ::kind kind?)


;;
;; Database.
;;

(def database? (s/keys :req [::look ::board ::position]))


;;------------------------------------------------------------------------------
;; Initialization 
;;------------------------------------------------------------------------------

(defn place
  [i j color kind]
  [[i j] {::color color ::kind kind}])

(def classic-position
  (let [pawns (fn [c j]
                (into {}
                      (for [i (range 0 8)]
                        (place i j c ::pawn))))
        majors (fn [c j]
                 (into {}
                       [(place 0 j c ::rook)
                        (place 1 j c ::knight)
                        (place 2 j c ::bishop)
                        (place 3 j c ::queen)
                        (place 4 j c ::king)
                        (place 5 j c ::bishop)
                        (place 6 j c ::knight)
                        (place 7 j c ::rook)]))]    
    (merge (majors :black 7)
           (pawns :black 6)
           (pawns :white 1)
           (majors :white 0))))

(def classic-board
  {::cols 8 ::rows 8})

(def default-db
  {::look {::board-size ::medium}
   ::board classic-board
   ::position classic-position})
