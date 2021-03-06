(ns maverick.pieces
  (:require [re-frame.core :as rf]
            [maverick.db :as db]
            [maverick.events :as events]
            [maverick.subs :as subs :refer [listen]]
            [maverick.geom :as geom :refer [path]]))

(defn pawn-elems [_]
  [[:rect {:x 72
           :y 297
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   (path ["M 128.31 198"
          "H 126"
          "a 9 9 0 0 0 -9 9"
          "h 0"
          "a 9 9 0 0 0 9 9"
          "H 234"
          "a 9 9 0 0 0 9 -9"
          "h 0"
          "a 9 9 0 0 0 -9 -9"
          "h -2.31"
          "a 63 63 0 1 0 -103.38 0"
          "Z"])
   (path ["M 225 225H135"
          "c 0 22.14 -11.11 41.16 -27 49.49"
          "V 288"
          "H 252"
          "V 274.49"
          "C 236.11 266.16 225 247.14 225 225"
          "Z"])])

(defn bishop-elems [_]
  [[:rect {:x 72
           :y 297
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   (path ["M 225 225"
          "H 135"
          "c 0 22.14 -11.11 41.16 -27 49.49"
          "V 288"
          "H 252"
          "V 274.49"
          "C 236.11 266.16 225 247.14 225 225"
          "Z"])
   (path ["M 236 198.23a9 9 0 0 1 7 8.77"
          "h 0"
          "a 9 9 0 0 1 -9 9"
          "H 126a9 9 0 0 1 -9 -9"
          "h 0"
          "a 9 9 0 0 1 7 -8.77"
          "S 108 170.14 108 153"
          "c 0 -21.74 10.76 -49.31 26.41 -69.83l28.37 64.26"
          "a 9 9 0 0 0 11.87 4.6"
          "h 0"
          "a 9 9 0 0 0 4.6 -11.87"
          "l -31.56 -71.5"
          "c 2.25 -2 6.93 -5.48 6.93 -5.48"
          "A 29.09 29.09 0 0 1 153 54"
          "a 27 27 0 1 1 52.38 9.23"
          "S 252 122.78 252 153"
          "A 71.68 71.68 0 0 1 236 198.23"
          "Z"])])

(defn knight-elems [color]
  [[:rect {:x 72
           :y 296.19
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   (path ["M 108 207"
          "l 15.63 80.19"
          "H 234"
          "c 0 -24 16.37 -46.44 38.36 -53.22"
          "C 282 197.66 288 159.74 285.43 128.8 281.2 77.82 258.22 60 226.59 56"
          "c -5.84 -5.39 -13.7 -8.77 -22.82 -9.55"
          "C 197.79 34.78 192.36 27 181.16 27"
          "c -5.9 0 -7.32.84 -11.6 3.88 5.24 2.68 14.29 14.57 5.65 23.62"
          "L 151.28 75.06"
          "l -4.17 7.41 -48 41.2"
          "a 11.85 11.85 0 0 0 -1.22 16.77"
          "l 13.75 15.73"
          "a 12.11 12.11 0 0 0 16.93 1.2"
          "l 11.83 -10.15"
          "c 9 -6.68 16.27 -1.79 25.22 -7.33 5.41 -3.35 16.49 -11.07 16.49 -11.07"
          "C 182.13 177.34 148 207 108 207"
          "Z"])
   (path (str color "-detail")
         ["M 175.42 76.55 172 78.07"
          "a 3.52 3.52 0 1 1 -5.37 2.4"
          "L 163 82.07"
          "a 7 7 0 1 0 12.39 -5.52"
          "Z"])])

(defn rook-elems [_]
  [[:rect {:x 72
           :y 297
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   (path ["M 126 117"
          "a 9 9 0 0 0 -9 9"
          "h 0"
          "a 9 9 0 0 0 9 9"
          "H 234"
          "a 9 9 0 0 0 9 -9"
          "h 0"
          "a 9 9 0 0 0 -9 -9"
          "h 15"
          "a 12 12 0 0 0 12 -12"
          "V 39"
          "a 12 12 0 0 0 -12 -12"
          "H 229.5"
          "V 45"
          "h -18"
          "V 27"
          "H 189"
          "V 45"
          "H 171"
          "V 27"
          "H 148.5"
          "V 45"
          "h -18"
          "V 27"
          "H 111"
          "A 12 12 0 0 0 99 39"
          "v 66"
          "a 12 12 0 0 0 12 12"
          "Z"])
   (path ["M 225 144"
          "H 135"
          "c 0 50.61 -11.11 94.08 -27 113.13"
          "V 288"
          "H 252"
          "V 257.13"
          "C 236.11 238.08 225 194.61 225 144"
          "Z"])])

(defn hawk-elems [color]
  [[:rect {:x 72
           :y 297
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   (path ["M 109.7 99"
          "a 40.79 40.79 0 0 1 -6.07.45"
          "c -7.45 0 -14.05 -2 -18.12 -5"
          "h 0"
          "a 26.36 26.36 0 0 0 -7.32 18.08"
          "s 0 .09 0 .14"
          "a 29.74 29.74 0 0 1 -6.13 -18.08"
          "c 0 -15.42 11.82 -28.17 27.18 -30.24 4.76 -20.42 28 -33.9 59.39 -37.06"
          "C 191.78 24 202.9 45.21 209 71.48"
          "c 4 17.19 28.22 40.3 33.38 99.17 1.34 15.33 -.92 36.43 -4.27 53.36"
          "a 36.17 36.17 0 0 0 1.95 20.67"
          "A 55.25 55.25 0 0 0 270 274.49"
          "V 288"
          "H 90"
          "V 274.49"
          "c 13.21 -5.54 35.7 -27.08 33.57 -47.82"
          "C 122.07 212 111.08 183.1 113.4 159"
          "c 1.44 -15 7.48 -31.09 5.44 -43.31 -.76 -4.56 -4.82 -10.27 -9.13"
          " -16.64"
          "Z"])
   (path (str color "-detail")
         ["M 140.46 56.58l -4.66 1.09"
          "a 4.5 4.5 0 1 1 -7.32 1.71"
          "l -4.91 1.15"
          "a 9 9 0 1 0 16.88 -3.94"
          "Z"])])

(defn elephant-elems [_]
  [[:rect {:x 72
           :y 297
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   (path ["M 134.47 167.35"
          "c 0 50.61 -10.57 70.73 -26.47 89.78"
          "V 288"
          "H 252"
          "V 257.13"
          "c -10.9 -13.07 -19.56 -16.64 -23.91 -47.12 1.39 -5.09 2.79 -9.87 4.32"
          " -14.13 2.54 -7.1 2.69 8.67 10.66 5.8 9.09 -3.27 44.58 -61.75 44.58"
          " -72.12 0 -10.77 -23.61 -42.89 -39.26 -49.63 -21.47 -9.25 -56.5 "
          " -21.58"
          " -68.63 -18.44 -8.3 2.15 -9.39 4.47 -18.75 7.73 -22.87 8 -18.11 24.75"
          " -45.07 37.64 -22 10.49 -29.13 11.19 -38.59 3.07 -4.85 -4.17 -7.92"
          " -11.28 -7 -19.42.67 -6.18 5 -14.21 10.17 -22.45 7.73 -12.4 6.12"
          " -20.22 1.44 -28 -3.8 -6.28 -12.65 -12.87 -17.54 -13.17"
          "l -.87 6.51 -6.22 3.13"
          "c 2.49 3.75 8 8 10.57 12.4 2.72 4.64 -.65 9.49 -4 13.73 -5.78 7.42"
          " -11.22 17.57 -12.53 28.93"
          "a 41.42 41.42 0 0 0 13 35.62"
          "C 72.27 134.74 81.11 139 91 140.19"
          "c -23.05 5.46 -45.55 2.59 -62.38 -6.39 -.83 -.44 -2 .82 -1.23 1.49 17"
          " 15.25 45.29 21.71 74.44 14.81 15.21 -3.6 29.64 -8.9 40.13 -17.54.63"
          " -.45 1.83 0 1.81.8 -.08 2.51 -15.17 15.47 -27.86 20.54 -2 .8 -2.08"
          " 2.46 1.34 3.59"
          "C 123.85 159.66 132 161.93 134.47 167.35"
          "Z"])
   [:circle {:cx 164
             :cy 96
             :r 6}]])

(defn queen-elems [_]
  [[:rect {:x 72
           :y 270
           :width 216
           :height 36
           :rx 12
           :ry 12}]
   [:rect {:x 72
           :y 316
           :width 216
           :height 18
           :rx 12
           :ry 12}]
   (path ["M 261 243"
          "h 18"
          "a 9 9 0 0 1 9 9"
          "h 0"
          "a 9 9 0 0 1 -9 9"
          "H 81"
          "a 9 9 0 0 1 -9 -9"
          "h 0"
          "a 9 9 0 0 1 9 -9"
          "H 99"
          "L 55.62 100.82"
          "h 0"
          "a 10.09 10.09 0 0 1 -3 .43"
          "A 10.13 10.13 0 1 1 63 91.13"
          "a 10 10 0 0 1 -4.37 8.26"
          "l 0 0 57 80.55"
          "L 107.4 74.22l -.79 0"
          "A 10.13 10.13 0 1 1 117 64.13"
          "a 10.1 10.1 0 0 1 -5.78 9.07"
          "h 0l49.4 97.8"
          "L 177.06 65.14"
          "a 10.39 10.39 0 1 1 3.11 0"
          "L 196.62 171 246 73.2"
          "a 10.11 10.11 0 0 1 -5.78 -9.08 10.39 10.39 0 1 1"
          " 10.38 10.13 10.74 10.74 0 0 1 -1.45 -.1"
          "L 234 179.58"
          "l 64.14 -80.52"
          "a 10 10 0 0 1 -3.93 -7.93 10.39 10.39 0 1 1"
          " 10.38 10.13 10.65 10.65 0 0 1 -2.84 -.38"
          "Z"])])

(defn king-elems [_]
  [[:rect {:x 72
           :y 270
           :width 216
           :height 36
           :rx 12
           :ry 12}]
    [:rect {:x 72
           :y 316
           :width 216
           :height 18
           :rx 12
            :ry 12}]
   (path ["M 265.41 243"
          "H 279"
          "a 9 9 0 0 1 9 9"
          "h 0"
          "a 9 9 0 0 1 -9 9"
          "H 81"
          "a 9 9 0 0 1 -9 -9"
          "h 0"
          "a 9 9 0 0 1 9 -9"
          "H 94.15"
          "A 169.6 169.6 0 0 1 82 224.21"
          "C 51.56 169.67 58.44 111.69 97.35 94.71"
          "c 18.8 -8.21 41.63 -5.34 63.65 6 3 -.54 6.83 -1 10 -1.24"
          "V 63"
          "H 153"
          "V 45"
          "h 18"
          "V 27"
          "h 18"
          "V 45"
          "h 18"
          "V 63"
          "H 189"
          "V 99.36"
          "c 3.08.25 6.05.63 9 1.12 22.22 -11.15 45.23 -13.94 64.19 -5.78 39.46"
          " 17 46.44 75 15.59 129.5"
          "A 169.44 169.44 0 0 1 265.41 243"
          "Z"])
   [:polygon
    {:points "195 217.86 180 187.66 165 217.86 180.75 235.47 195 217.86"}]])

(def piece-fns
  {::db/pawn     pawn-elems
   ::db/knight   knight-elems
   ::db/bishop   bishop-elems
   ::db/rook     rook-elems
   ::db/hawk     hawk-elems
   ::db/elephant elephant-elems
   ::db/queen    queen-elems
   ::db/king     king-elems})

(defn piece [color kind loc]
  (let [cs (name color)]
    (into (geom/loc-group color loc)
          ((kind piece-fns) cs))))
