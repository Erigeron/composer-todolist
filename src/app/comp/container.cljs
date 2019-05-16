
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
            [clojure.string :as string]
            [app.vm :as vm]))

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
      :state-fns (->> vm/states-manager
                      (map (fn [[alias manager]] [alias (:init manager)]))
                      (into {}))}
     (fn [d! op context options]
       (println op (dissoc context :templates :state-fns) (pr-str options))
       (let [param (:param options)
             template-name (:template-name context)
             state-path (:state-path context)
             mutate! (fn [x] (d! :states [state-path x]))
             this-state (get-in states (conj state-path :data))]
         (if (contains? vm/states-manager template-name)
           (let [on-action (get-in vm/states-manager [template-name :update])]
             (on-action d! op context options this-state mutate!))
           (println "Unhandled template:" template-name)))))
    (comp-inspect "model" store {:bottom 0})
    (when dev? (cursor-> :reel comp-reel states reel {})))))
