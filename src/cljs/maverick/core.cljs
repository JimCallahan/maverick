(ns maverick.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as rf]
              [re-frisk.core :refer [enable-re-frisk!]]
              [maverick.events :as events]
              [maverick.subs]
              [maverick.views :as views]
              [maverick.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
