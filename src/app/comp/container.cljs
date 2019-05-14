
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
       (let [template-name (:template-name options)
             state-path (:state-path options)
             mutate! (fn [x] (d! :states [state-path x]))
             this-state (get-in states (conj state-path :data))]
         (if (string/starts-with? (name op) "-")
           (case template-name
             "header"
               (case op
                 :-input (mutate! (assoc this-state :draft (:value options)))
                 :-keydown (println "keydown")
                 :-submit
                   (let [draft (:draft this-state)]
                     (when-not (string/blank? draft) (d! :submit draft) (mutate! nil)))
                 (println "template op not handled:" op template-name))
             (do (println "Not handled in template:" template-name)))
           (case op
             :input (d! :input (:value options))
             :clear (d! :clear nil)
             :archive (d! :archive nil)
             :toggle (d! :toggle param)
             :remove (d! :remove param)
             :keydown
               (cond
                 (= 13 (.-keyCode (:event options)))
                   (when-not (string/blank? (:input store)) (d! :submit nil))
                 :else (js/console.log "keydown" (:event options)))
             (do (println "Unknown op:" op)))))))
    (comp-inspect "model" store {:bottom 0})
    (when dev? (cursor-> :reel comp-reel states reel {})))))
