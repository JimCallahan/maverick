(ns maverick.rules.classic
  (:require [maverick.db :as db]
            [maverick.rules.protocol :as proto]))

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

    (can-move? [_ db loc]
      (let [color (-> db ::db/current-move ::db/color)
            plocs (-> db ::db/current-position ::db/locations)
            piece (get plocs loc)]

        ;; placeholder
        
        (= color (::db/color piece))))


    (destinations [this db loc]
      (when (proto/can-move? this db loc)
        (let [plocs (-> db ::db/current-position ::db/locations)
              {:keys [::db/kind ::db/color]} (get plocs loc)
              [i j] loc]
          (case kind
            ::db/pawn [[i (inc j)] [i (+ j 2)]]        ;; <----
            nil))))

    (game-result [this db]

      ;; placeholder
      
      nil)))