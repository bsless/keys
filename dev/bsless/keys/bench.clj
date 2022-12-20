(ns bsless.keys.bench
  (:require
   [bsless.keys :as keys]
   [criterium.core :as cc]))

(defmacro quick-bench
  [expr]
  `(let [ret# (cc/quick-benchmark ~expr nil)
         mean# (first (:mean ret#))
         [factor# unit#] (cc/scale-time mean#)]
     (cc/format-value mean# factor# unit#)))

(quick-bench (select-keys {:a 1 :b 2 :c 3} [:a :b :c])) ;; => "244.746154 ns"
(quick-bench (keys/select {:a 1 :b 2 :c 3} [:a :b :c])) ;; => "97.587060 ns"
(quick-bench (apply keys/select {:a 1 :b 2 :c 3} [[:a :b :c]])) ;; => "195.571249 ns"
(def f (keys/select [:a :b :c]))
(quick-bench (f {:a 1 :b 2 :c 3})) ;; => "106.211750 ns"
(quick-bench (keys/select* {:a 1 :b 2 :c 3} :a :b :c)) ;; => "72.448903 ns"
(quick-bench (apply keys/select* {:a 1 :b 2 :c 3} [:a :b :c])) ;; => "183.127248 ns"
