(ns ex04.state
  (:require-macros
   [reagent.ratom :refer [reaction]]
   [cljs-log.core :refer [debug info warn]])
  (:require
   [reagent.core :as reagent]))

(defonce app (reagent/atom {}))

(defn set-state!
  "Helper fn to reset app state for given key. Key can be single
  keyword or path vector."
  [key val]
  (debug key val)
  (swap! app (if (sequential? key) assoc-in assoc) key val))

(defn update-state!
  "Helper fn to update app state value for given key. Key can be single
  keyword or path vector. Behaves like clojure.core/update or update-in"
  [key f & args]
  (debug key args)
  (swap! app #(apply (if (sequential? key) update-in update) % key f args)))

(defn subscribe
  "Helper fn to create a reagent reaction for given app state key/path."
  [key]
  (if (sequential? key)
    (reaction (get-in @app key))
    (reaction (@app key))))

(defn nav-change
  [route]
  (set-state! :curr-route route))

(defn init-app
  []
  (swap! app merge
         {:inited true}))
