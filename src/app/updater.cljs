
(ns app.updater (:require [respo.cursor :refer [mutate]]))

(defn model-updater [model op props op-data op-id op-time]
  (case op
    :input (let [event-obj op-data, text (:text op-data)] (assoc model :input text))
    :submit
      (-> model
          (update
           :records
           (fn [records]
             (conj records {:id op-id, :time op-time, :done? false, :text (:input model)})))
          (assoc :input ""))
    :toggle
      (update
       model
       :records
       (fn [records]
         (->> records
              (map
               (fn [record] (if (= op-data (:id record)) (update record :done? not) record)))
              vec)))
    :remove
      (update
       model
       :records
       (fn [records] (->> records (filter (fn [record] (not= op-data (:id record)))) vec)))
    :archive
      (update
       model
       :records
       (fn [records] (->> records (filter (fn [record] (not (:done? record)))) vec)))
    :clear (assoc model :records [])
    (do (println "unknown op" op) model)))

(defn updater [store op op-data op-id op-time]
  (case op
    :states (update store :states (mutate op-data))
    :content (assoc store :content op-data)
    :hydrate-storage op-data
    :model (assoc store :model op-data)
    store))
