(ns maverick.core 
  (:require [ring.middleware.resource :refer [wrap-resource]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [compojure.core :refer [defroutes routes]]))

(defn init []
  (println "Maverick is Starting..."))

(defn destroy []
  (println "Maverick is Shutting Down..."))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes app-routes)
      (handler/site)))
