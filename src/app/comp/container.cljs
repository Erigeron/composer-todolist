
(ns app.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.core
             :refer
             [defcomp cursor-> action-> mutation-> <> div button textarea span]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]
            [respo-md.comp.md :refer [comp-md]]
            [app.config :refer [dev?]]
            [composer.core :refer [render-markup extract-templates]]
            [shadow.resource :refer [inline]]
            [cljs.reader :refer [read-string]]
            ["shortid" :as shortid]
            [cumulo-util.core :refer [unix-time!]]
            [respo.comp.inspect :refer [comp-inspect]]
            [clojure.string :as string]))

(defcomp
 comp-container
 (reel)
 (let [store (:store reel)
       states (:states store)
       templates (extract-templates (read-string (inline "composer.edn")))]
   (div
    {:style (merge ui/global ui/row)}
    (render-markup
     (get templates "container")
     {:data store,
      :templates templates,
      :level 1,
      :template-name "container",
      :state-path [],
      :states states,
      :state-fns {"header" (fn [data state] (or state {:draft ""}))}}
     (fn [d! op param options]
       (println op param (pr-str options))
       (case op
         :input (d! :input (:value options))
         :submit (when-not (string/blank? (:input store)) (d! :submit nil))
         :clear (d! :clear nil)
         :archive (d! :archive nil)
         :toggle (d! :toggle param)
         :remove (d! :remove param)
         :keydown
           (cond
             (= 13 (.-keyCode (:event options)))
               (when-not (string/blank? (:input store)) (d! :submit nil))
             :else (js/console.log "keydown" (:event options)))
         (do (println "Unknown op:" op)))))
    (comp-inspect "model" (:model store) {:bottom 0})
    (when dev? (cursor-> :reel comp-reel states reel {})))))
