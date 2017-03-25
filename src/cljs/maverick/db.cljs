(ns maverick.db
  (:require [clojure.spec :as s]))

;;------------------------------------------------------------------------------
;; Specs.
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

;; The possible colors for players and pieces.
(def color? #{::white ::black})

;; The color of a player or piece.
(s/def ::color color?)

;; The orientation of the board with respect to a player color.
(s/def ::orient color?)

;; The number of squares horizontally direction.
(s/def ::cols pos-int?)

;; The number of squares in the vertical direction.
(s/def ::rows pos-int?)

;; The description of the board layout.
(def board? (s/keys :req [::rows ::cols ::orient]))


;;
;; Position
;;

;; The possible kinds of chess pieces.
(def kind? #{::pawn ::knight ::bishop ::rook ::hawk ::elephant ::queen ::king})

(s/def ::kind kind?)

;; A chess piece.
(def piece? (s/keys :req [::color ::kind]))

;; A non-negative integer.
(def whole? (s/and int? (partial <= 0)))

;; The location of a chess piece on the board.
(def location? (s/tuple whole? whole?))

;; The map of all pieces on the board by their locations.
(s/def ::locations (s/map-of location? piece?))

;; The move number.
(s/def ::move-number pos-int?)

;; The position currently on the board.
(s/def ::current-position (s/keys :req [::locations]))

;; The position of all pieces after a particular move has been played.
(def position? (s/keys ::req [::move-number ::color ::locations]))

;; Every position in the game from newest to oldest.
(s/def ::positions (s/every position? :kind vector?))

  
;;
;; Move.
;;

;; The location a piece was in at the start of a move.
(s/def ::start-location location?)

;; The location a piece was in at the end of a move.
(s/def ::end-location location?)

;; The time stamp of when a move began (millis since 1970).
(s/def ::start-stamp pos-int?)

;; The time stamp of when a move ended (millis since 1970).
(s/def ::start-stamp pos-int?)

;; A partially completed move.
(def partial-move? (s/keys ::req [::move-number ::color ::start-stamp]
                           ::opt [::kind ::end-stamp
                                  ::start-location ::end-location]))

;; The move which is currently in progress.
(s/def ::current-move partial-move?)
  
;; A completed move.
(def move? (s/keys ::req [::move-number ::color ::kind 
                          ::start-location ::end-location
                          ::start-stamp ::end-stamp]))

;; All previously completed moves. 
(s/def ::moves (s/every move? :kind vector?))
  

;;
;; Interaction Feedback.
;;

;; The board location currently under the mouse.
(s/def ::hover-location location?)

(def ::feedback (s/keys ::opt [::hover-location]))


;;
;; Rules.
;;

(s/def ::rules #{::classic})


;;
;; Database.
;;

(def database? (s/keys :req [::look ::board ::rules
                             ::current-move ::moves
                             ::current-position ::positions]
                       ::opt [::feedback]))


;;------------------------------------------------------------------------------
;; Validation. 
;;------------------------------------------------------------------------------

(defn valid-database?
  "validate the given db, writing any problems to console.error"
  [db]
  (if-not (s/valid? database? db)
    (.error js/console "Database is invalid!")))

