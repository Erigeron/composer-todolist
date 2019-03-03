
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
    (textarea
     {:value (:content store),
      :placeholder "Content",
      :style (merge ui/flex ui/textarea {:height 320}),
      :on-input (action-> :content (:value %e))})
    (=< "8px" nil)
    (div
     {:style ui/flex}
     (comp-md "This is some content with `code`")
     (=< "8px" nil)
     (button
      {:style ui/button,
       :inner-text (str "run"),
       :on-click (fn [e d! m!] (println (:content store)))})
     (render-markup
      (get templates "container")
      {:data (:model store), :templates templates, :level 1}
      (fn [d! op props op-data]
        (println op props op-data)
        (let [op-id (.generate shortid)
              op-time (unix-time!)
              next-store (model-updater (:model store) op props op-data op-id op-time)]
          (d! :model next-store)))))
    (comp-inspect "model" (:model store) {:bottom 0})
    (when dev? (cursor-> :reel comp-reel states reel {})))))
