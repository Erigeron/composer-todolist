
(ns app.updater (:require [respo.cursor :refer [update-states]]))

(defn updater [store op op-data op-id op-time]
  (case op
    :states (update-states store op-data)
    :hydrate-storage op-data
    :submit
      (-> store
          (update
           :records
           (fn [records]
             (conj records {:id op-id, :time op-time, :done? false, :text op-data}))))
    :toggle
      (update
       store
       :records
       (fn [records]
         (->> records
              (map
               (fn [record] (if (= op-data (:id record)) (update record :done? not) record)))
              vec)))
    :edit
      (update
       store
       :records
       (fn [records]
         (->> records
              (map
               (fn [record]
                 (if (= (:id op-data) (:id record))
                   (assoc record :text (:text op-data))
                   record)))
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
