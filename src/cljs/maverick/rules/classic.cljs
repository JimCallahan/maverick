(ns maverick.rules.classic
  (:require [maverick.db :as db]
            [maverick.rules.protocol :as proto]))

;;------------------------------------------------------------------------------
;; Helpers. 
;;------------------------------------------------------------------------------

(defn- add
  "2D vector addition."
  [[ai aj] [bi bj]]
  [(+ ai bi) (+ aj bj)])

;;
;; Piece Threats.
;;

(defn- pawn-threats
  [rules db loc]
  (let [plocs (-> db ::db/current-position ::db/locations)
        attack-color (-> db ::db/current-move ::db/color) 
        [start-j step] (case attack-color
                         ::db/white [1 [0 1]]
                         ::db/black [6 [0 -1]])
        sloc (add loc step)
        dloc (add sloc step)
        rloc (add sloc [1 0])
        lloc (add sloc [-1 0])
        [single double right left]
        (->> [sloc dloc lloc rloc]
             (map (fn [lc] [lc (-> (get plocs lc)
                                  (assoc ::db/depth 0))])))
        takes (->> [right left]
                   (filter (fn [[_ {:keys [::db/color]}]]
                             (and color (not= color attack-color))))
                   (map (fn [[lc p]] [lc (assoc p ::db/depth 0)]))
                   (into {}))
        moves (when-not (-> single second ::db/kind)
                (if (or (-> double second ::db/kind)
                        (not= start-j (second loc)))
                  (into {} [single])
                  (into {} [single double])))]
    (merge moves takes)))

(defn- knight-threats
  [rules db loc]
  (let [plocs (-> db ::db/current-position ::db/locations)]
    (->> [[1 2] [1 -2] [-1 2] [-1 -2]
          [2 1] [-2 1] [2 -1] [-2 -1]]
         (map (partial add loc))
         (filter (partial proto/in-bounds? rules db))
         (map (fn [lc]
                [lc (assoc (get plocs lc) ::db/depth 0)]))
         (into {}))))


;;------------------------------------------------------------------------------
;; Rules Protocol Implementation.
;;------------------------------------------------------------------------------

(def rules
  (reify proto/Rules
    (init-db [_]
      (let [place (fn [i j color kind]
                    [[i j] {::db/color color ::db/kind kind}])

            pawns (fn [c j]
                    (into {} (for [i (range 0 8)]
                               (place i j c ::db/pawn))))
            majors (fn [c j]
                     (into {}
                           [(place 0 j c ::db/rook)
                            (place 1 j c ::db/knight)
                            (place 2 j c ::db/bishop)
                            (place 3 j c ::db/queen)
                            (place 4 j c ::db/king)
                            (place 5 j c ::db/bishop)
                            (place 6 j c ::db/knight)
                            (place 7 j c ::db/rook)]))]
        {::db/look {::db/board-size ::db/medium}
         ::db/board {::db/cols 8
                     ::db/rows 8
                     ::db/orient ::db/white}
         ::db/rules ::db/classic
         ::db/current-move {::db/move-number 1
                            ::db/color ::db/white
                            ::db/start-stamp (.now js/Date)}
         ::db/moves []
         ::db/current-position {::db/locations (merge (majors ::db/black 7)
                                                      (pawns ::db/black 6)
                                                      (pawns ::db/white 1)
                                                      (majors ::db/white 0))}
         ::db/positions []}))
  
    (in-bounds? [_ db [i j]]
      (let [{:keys [::db/board]} db
            {:keys [::db/rows ::db/cols]} board]
        (and i j
             (and (<= 0 i) (< i cols))
             (and (<= 0 j) (< j rows)))))

    (threats [this db loc]
      (let [{:keys [::db/kind ::db/color]}
            (-> db ::db/current-position ::db/locations (get loc))
            attack-color (-> db ::db/current-move ::db/color)]
        (when (= attack-color color)
          (case kind
            ::db/pawn (pawn-threats this db loc)
            ::db/knight (knight-threats this db loc)
            nil))))

    (game-result [this db]

      ;; placeholder
      
      nil)))
