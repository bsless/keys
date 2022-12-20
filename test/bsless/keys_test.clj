(ns bsless.keys-test
  (:require
   [clojure.set :as set]
   [clojure.test :as t]
   [bsless.keys :as keys]))

(t/deftest select-test
  (t/is (= (select-keys {:a 1 :b 2 :c 3 :e 5} [:A :b :c :d])
           (keys/select {:a 1 :b 2 :c 3 :e 5} [:A :b :c :d])
           (apply keys/select {:a 1 :b 2 :c 3 :e 5} [[:A :b :c :d]])
           ((keys/select [:A :b :c :d]) {:a 1 :b 2 :c 3 :e 5})
           (keys/select* {:a 1 :b 2 :c 3 :e 5} :A :b :c :d)
           (apply keys/select* {:a 1 :b 2 :c 3 :e 5} [:A :b :c :d]))))

(defn select-as
  [m kmap]
  (-> m (select-keys (keys kmap)) (set/rename-keys kmap)))

(t/deftest select-as-test
  (t/is (= (select-as {:a 1 :b 2 :c 3 :e 5} {:x :A :b :B :c :C :d :D})
           (keys/select-as {:a 1 :b 2 :c 3 :e 5} {:x :A :b :B :c :C :d :D})
           (apply keys/select-as {:a 1 :b 2 :c 3 :e 5} [{:x :A :b :B :c :C :d :D}])
           ((keys/select-as {:x :A :b :B :c :C :d :D}) {:a 1 :b 2 :c 3 :e 5})
           (keys/select-as* {:a 1 :b 2 :c 3 :e 5} :x :A :b :B :c :C :d :D)
           (apply keys/select-as* {:a 1 :b 2 :c 3 :e 5} [:x :A :b :B :c :C :d :D]))))
