
(ns app.updater (:require [respo.cursor :refer [mutate]]))

(defn updater [store op op-data op-id op-time]
  (case op
    :states (update store :states (mutate op-data))
    :hydrate-storage op-data
    :input (assoc store :input op-data)
    :submit
      (-> store
          (update
           :records
           (fn [records]
             (conj records {:id op-id, :time op-time, :done? false, :text (:input store)})))
          (assoc :input ""))
    :toggle
      (update
       store
       :records
       (fn [records]
         (->> records
              (map
               (fn [record] (if (= op-data (:id record)) (update record :done? not) record)))
              vec)))
    :remove
      (update
       store
       :records
       (fn [records] (->> records (filter (fn [record] (not= op-data (:id record)))) vec)))
    :archive
      (update
       store
       :records
       (fn [records] (->> records (filter (fn [record] (not (:done? record)))) vec)))
    :clear (assoc store :records [])
    (do (println "unknown op" op) store)))
