(ns clojure-quest.core
  (:require [datomic.api :as d])
  (:gen-class))

;;questory: portmanteu of quest and history.
;;                  or of query and story
;;  it works on so many levels, just go with it
(def uri "datomic:mem://questory")

(d/create-database uri)
(def conn (d/connect uri))
;;ever attribute must have at least:
;;                               :db/ident :<namespace>/<name>
;;                               :db/valueType :db.type/<type>
;;
(def starter-schema [{:db/id #db/id [:db.part/db]
                     :db/ident :quester/name
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/fulltext true
                     :db/index true
                     :db.install/_attribute :db.part/db}])

(def spell-schema [{:db/id #db/id [:db.part/db]
                    :db/ident :quester/spells
                    :db/valueType :db.type/string
                    :db/cardinality :db.cardinality/many
                    :db.install/_attribute :db.part/db}])
(def db (d/db conn))
(first starter-schema)

@(d/transact conn starter-schema)
(defn add-quester [name]
  (@(d/transact conn [{:db/id #db/id
                              [:db.part/db ]
                      :quester/name name}])))

(def foo (d/q '[:find ?e ?name
       :where [?e :quester/name ?name]]
              db))
(ffirst foo)

@(d/transact conn [[:db/add 63 :quester/name "foo"]])
( (d/q '[:find ?attr-name
       :where [?attr :db/ident ?attr-name]]
     db))
(defn find-questers [] (d/q '[:find ?name
                              :where [?e :quester/name ?name]] db2))
(find-questers)
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;; work around dangerous default behaviour in Clojure

  (alter-var-root #'*read-eval* (constantly false))
  (println "Hello, World!"))

(d/touch (d/entity db 63))
(add-quester "bar")
