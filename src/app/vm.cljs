
(ns app.vm (:require [clojure.string :as string]))

(defn get-view-model [store] store)

(def state-footer
  {:init (fn [props state] state),
   :update (fn [d! op context options state mutate!]
     (case op :clear (d! :clear) :archive (d! :archive) (println "Unhandled op" op)))})

(def state-header
  {:init (fn [data state] (or state {:draft ""})),
   :update (fn [d! op context options state mutate!]
     (case op
       :input (mutate! (assoc state :draft (:value options)))
       :keydown
         (cond
           (= 13 (.-keyCode (:event options)))
             (when-not (string/blank? (:draft state)) (d! :submit nil) (mutate! {:draft ""}))
           :else (do))
       :submit
         (let [draft (:draft state)]
           (when-not (string/blank? draft) (d! :submit draft) (mutate! nil)))
       (println "op not handled:" op)))})

(def state-task
  {:init (fn [props state] state),
   :update (fn [d! op context options state mutate!]
     (case op
       :toggle (d! :toggle (:param options))
       :remove (d! :remove (:param options))
       :edit (d! :edit {:id (get-in context [:data :id]), :text (:value options)})
       :local-edit (mutate! (:value options))
       (do (println "Unknown op:" op))))})

(def states-manager {"header" state-header, "footer" state-footer, "task" state-task})

(defn on-action [d! op context options view-model states]
  (let [param (:param options)
        template-name (:template-name context)
        state-path (:state-path context)
        mutate! (fn [x] (d! :states [state-path x]))
        this-state (get-in states (conj state-path :data))]
    (println op context (pr-str options))
    (if (contains? states-manager template-name)
      (let [on-action (get-in states-manager [template-name :update])]
        (on-action d! op context options this-state mutate!))
      (println "Unhandled template:" template-name))))
