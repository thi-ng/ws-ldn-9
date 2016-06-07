(ns ex03.core
  (:require
   [org.httpkit.server :as http]
   [compojure.core :refer :all]
   [compojure.route :as route]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [html5 include-js include-css]]
   [hiccup.element :as el]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
   [ring.middleware.reload :refer [wrap-reload]]
   [ring.middleware.stacktrace :refer [wrap-stacktrace]]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.response :as resp]
   [ring.util.mime-type :refer [default-mime-types]]))

(defonce state (atom {}))

(def html-head
  (html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
    [:meta {:name "viewport" :content "width=device-width,initial-scale=1.0"}]
    [:meta {:name "author" :content ""}]
    [:meta {:name "description" :content ""}]
    [:meta {:name "keywords" :content ""}]
    [:title "ws-ldn-9"]
    (include-css "//fonts.googleapis.com/css?family=Roboto" "/css/main.css")]))

(defroutes app-routes
  (GET "/" [:as req]
       (html5
        {:lang "en"}
        html-head
        [:body
         [:h1 "Hello world!!!"]]))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-stacktrace)
      (wrap-reload)))

(defn start!
  []
  (swap! state assoc :server (http/run-server #'app {:port 3000})))

(defn stop!
  []
  (when-let [server (:server @state)]
    (server :timeout 100)
    (swap! state dissoc :server))
  (Thread/sleep 1100))

(defn restart!
  []
  (stop!)
  (start!))

(defn -main [& args] (start!))
