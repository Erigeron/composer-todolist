
(ns app.vm (:require [clojure.string :as string]))

(def state-footer
  {:init (fn [props state] state),
   :update (fn [d! op param options state mutate!]
     (case op :clear (d! :clear) :archive (d! :archive) (println "Unhandled op" op)))})

(def state-header
  {:init (fn [data state] (or state {:draft ""})),
   :update (fn [d! op param options state mutate!]
     (case op
       :input (mutate! (assoc state :draft (:value options)))
       :keydown
         (cond
           (= 13 (.-keyCode (:event options)))
             (when-not (string/blank? (:draft state)) (d! :submit nil) (mutate! {:draft ""}))
           :else (js/console.log "keydown->>>" (:event options) (pr-str (:data options))))
       :submit
         (let [draft (:draft state)]
           (when-not (string/blank? draft) (d! :submit draft) (mutate! nil)))
       (println "op not handled:" op)))})

(def state-task
  {:init (fn [props state] state),
   :update (fn [d! op param options state mutate!]
     (case op
       :toggle (d! :toggle param)
       :remove (d! :remove param)
       :edit (d! :edit {:id (get-in options [:data :id]), :text (:value options)})
       :local-edit (mutate! (:value options))
       (do (println "Unknown op:" op))))})

(def states-manager {"header" state-header, "footer" state-footer, "task" state-task})
