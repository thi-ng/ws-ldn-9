(ns ex04.canvas
  (:require
   [reagent.core :as reagent]
   [thi.ng.geom.gl.webgl.animator :as anim]))

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
        {:width (or (:width props) (.-innerWidth js/window))
         :height (or (:height props) (.-innerHeight js/window))}
        props)])}))
