
(ns app.container
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
            [clojure.string :as string]
            [app.vm :as vm]))

(defcomp
 comp-container
 (reel)
 (let [store (:store reel)
       states (:states store)
       templates (extract-templates (read-string (inline "composer.edn")))
       view-model (vm/get-view-model store)]
   (div
    {:style (merge ui/global ui/row)}
    (render-markup
     (get templates "container")
     {:data view-model,
      :templates templates,
      :level 1,
      :template-name "container",
      :state-path [],
      :states states,
      :state-fns (->> vm/states-manager
                      (map (fn [[alias manager]] [alias (:init manager)]))
                      (into {}))}
     (fn [d! op context options]
       (vm/on-action d! op (dissoc context :templates :state-fns) options view-model states)))
    (comp-inspect "model" store {:bottom 0})
    (when dev? (cursor-> :reel comp-reel states reel {})))))
