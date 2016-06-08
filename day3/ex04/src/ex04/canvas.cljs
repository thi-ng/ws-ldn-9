(ns ex04.canvas
  (:require-macros
   [cljs-log.core :refer [debug info warn severe]])
  (:require
   [reagent.core :as reagent]
   [thi.ng.geom.gl.webgl.animator :as anim]
   [thi.ng.color.core :as col]))

(defn canvas-component
  [props]
  (reagent/create-class
   {:component-did-mount
    (fn [this]
      (reagent/set-state this {:active true})
      ((:init props) this)
      (anim/animate ((:loop props) this)))
    :component-will-unmount
    (fn [this]
      (debug "unmount GL")
      (reagent/set-state this {:active false}))
    :reagent-render
    (fn [_]
      [:canvas
       (merge
        {:width  (.-innerWidth js/window)
         :height (.-innerHeight js/window)}
        props)])}))

(defn canvas
  [route]
  [canvas-component
   {:width 640
    :height 480
    :init (fn [comp] (debug "init canvas"))
    :loop (fn [comp]
            (fn []
              (let [canvas (reagent/dom-node comp)
                    ctx (.getContext canvas "2d")]
                (set! (.-fillStyle ctx) (-> (col/random-rgb) (col/as-css) deref))
                (set! (.-font ctx) "32px Arial")
                (.fillRect ctx 0 0 (.-width canvas) (.-height canvas))
                (set! (.-fillStyle ctx) (-> (col/random-rgb) (col/as-css) deref))
                (.fillText ctx "Epilepsy warning" 100 100)
                (:active (reagent/state comp)))))}])
