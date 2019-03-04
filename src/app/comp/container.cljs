
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
            [respo-composer.core :refer [render-markup extract-templates]]
            [shadow.resource :refer [inline]]
            [cljs.reader :refer [read-string]]
            [app.updater :refer [model-updater]]
            ["shortid" :as shortid]
            [cumulo-util.core :refer [unix-time!]]
            [respo.comp.inspect :refer [comp-inspect]]))

(defcomp
 comp-container
 (reel)
 (let [store (:store reel)
       states (:states store)
       templates (extract-templates (read-string (inline "composed/composer.edn")))]
   (div
    {:style (merge ui/global ui/row)}
    (render-markup
     (get templates "container")
     {:data (:model store), :templates templates, :level 1}
     (fn [d! op props op-data]
       (println op props op-data)
       (let [op-id (.generate shortid)
             op-time (unix-time!)
             next-store (model-updater (:model store) op props op-data op-id op-time)]
         (d! :model next-store))))
    (comp-inspect "model" (:model store) {:bottom 0})
    (when dev? (cursor-> :reel comp-reel states reel {})))))
