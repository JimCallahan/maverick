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
                           ::opt [::start-location ::end-location ::end-stamp]))

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

;; Whether a there is a piece at the given location which can be moved.
#_(s/def ::can-move? (s/fspec :args (s/cat :db map? :loc location?)
                            :ret boolean?))

;; The legal locations the piece at the given location can be moved to.
#_(s/def ::destinations (s/fspec :args (s/cat :db map? :loc location?)
                               :ret (s/every location? :kind vector?)))

;; The legal results of a completed game.
#_(def game-result? #{::white-win ::black-win ::draw})

;; The result of the game, if complete.
#_(s/def game-result (s/fspec :args (s/cat :db map?)
                            :ret (s/or :going nil?
                                       :over game-result?)))

;; The required functions which implement the chess variant rules.
#_(s/def ::rules (s/keys :req [::can-move? ::destinations ::game-result]))


(s/def ::rules #{::classic ::funky})

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


;;------------------------------------------------------------------------------
;; Initialization. 
;;------------------------------------------------------------------------------

(defn place
  [i j color kind]
  [[i j] {::color color ::kind kind}])

;; Classic 
(def classic-start-position
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
    {::locations (merge (majors ::black 7)
                        (pawns ::black 6)
                        (pawns ::white 1)
                        (majors ::white 0))})) 

(def classic-board
  {::cols 8 ::rows 8})

(def classic-setup 
  {::board classic-board
   ::rules ::classic
   ::current-position classic-start-position})

;; Funky, just for testing...
(def funky-setup
  {::board {::cols 10 ::rows 8}
   ::rules ::funky
   ::current-position classic-start-position})

(def game-setups
  {::classic classic-setup
   ::funky funky-setup})


;; Game initialization.
(def game-start
  {::current-move {::move-number 1
                   ::color ::white
                   ::start-stamp (.now js/Date)}
   ::moves []
   ::positions []})


;; App startup database state.
(def default-db
  (merge {::look {::board-size ::medium}}
         game-start
         classic-setup))
