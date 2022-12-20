(ns bsless.keys
  (:refer-clojure :exclude [select-keys]))

(defn select-key
  [acc m k]
  (let [v (get m k ::not-found)]
    (if (identical? ::not-found v)
      acc
      (assoc acc k v))))

(defn select-key!
  {:inline-arities #{3}
   :inline
   (fn [acc m k]
     `(let [acc# ~acc k# ~k
            v# (get ~m k# ::not-found)]
        (if (identical? ::not-found v#)
          acc#
          (assoc! acc# k# v#))))}
  ([m]
   (fn [acc k]
     (select-key! acc m k)))
  ([acc k]
   (fn [m]
     (select-key! acc m k)))
  ([acc m k]
   (let [v (get m k ::not-found)]
     (if (identical? ::not-found v)
       acc
       (assoc! acc k v)))))

(defn select-into*
  "Like [[select-keys]] but the provided keys don't have to be provided as a vector.
  Aggressively in-lines iteration."
  {:inline
   (fn
     ([to] to)
     ([to m & ks]
      (let [g (gensym)]
        `(let [~g ~m]
           (-> ~to transient ~@(map (fn [k] `(select-key! ~g ~k)) ks) persistent!)))))}
  ([to _] to)
  ([to m k0] (-> to transient (select-key! m k0) persistent!))
  ([to m k0 k1]
   (-> to transient (select-key! m k0) (select-key! m k1) persistent!))
  ([to m k0 k1 k2]
   (-> to transient (select-key! m k0) (select-key! m k1) (select-key! m k2) persistent!))
  ([to m k0 k1 k2 k3]
   (-> to transient (select-key! m k0) (select-key! m k1) (select-key! m k2) (select-key! m k3) persistent!))
  ([to m k0 k1 k2 k3 k4]
   (-> to transient (select-key! m k0) (select-key! m k1) (select-key! m k2) (select-key! m k3) (select-key! m k4) persistent!))
  ([to m k0 k1 k2 k3 k4 k5]
   (-> to transient (select-key! m k0) (select-key! m k1) (select-key! m k2) (select-key! m k3) (select-key! m k4) (select-key! m k5) persistent!))
  ([to m k0 k1 k2 k3 k4 k5 k6]
   (-> to transient (select-key! m k0) (select-key! m k1) (select-key! m k2) (select-key! m k3) (select-key! m k4) (select-key! m k5) (select-key! m k6) persistent!))
  ([to m k0 k1 k2 k3 k4 k5 k6 k7]
   (-> to transient (select-key! m k0) (select-key! m k1) (select-key! m k2) (select-key! m k3) (select-key! m k4) (select-key! m k5) (select-key! m k6) (select-key! m k7) persistent!)))

(defn select*
  "Like [[select-keys]] but the provided keys don't have to be provided as a vector.
  Aggressively in-lines iteration."
  {:inline (fn [& args] `(select-into* {} ~@args))}
  ([_] {})
  ([m k0] (select-into* {} m k0))
  ([m k0 k1] (select-into* {} m k0 k1))
  ([m k0 k1 k2] (select-into* {} m k0 k1 k2))
  ([m k0 k1 k2 k3] (select-into* {} m k0 k1 k2 k3))
  ([m k0 k1 k2 k3 k4] (select-into* {} m k0 k1 k2 k3 k4))
  ([m k0 k1 k2 k3 k4 k5] (select-into* {} m k0 k1 k2 k3 k4 k5))
  ([m k0 k1 k2 k3 k4 k5 k6] (select-into* {} m k0 k1 k2 k3 k4 k5 k6))
  ([m k0 k1 k2 k3 k4 k5 k6 k7] (select-into* {} m k0 k1 k2 k3 k4 k5 k6 k7)))

(defn- init-select [init _] (transient init))
(defn- rf-select [f' k]
  (fn [acc m]
    (select-key! (f' acc m) m k)))

(defn- go-select [f]
  (fn go
    ([m] (persistent! (f {} m)))
    ([init m] (persistent! (f init m)))))

(defn select
  "Like [[clojure.core/select-keys]] but faster.
  If called without a map, returns a function with two arities which
  will select the provided keys.
  The returned function is operationally equivalent to:
  ((select ks) m) == (select-keys m ks)
  ((select ks) init m) == (merge init (select-keys m ks))"
  ([ks]
   (->> ks (reduce rf-select init-select) go-select))
  ([m ks]
   (->> ks (reduce (select-key! m) (transient {})) persistent!)))

(comment
  (select {:a 1 :b 2} [:a :c])
  (select* {:a 1 :b 2} :a :c)
  ((select [:a :c]) {:a 1 :b 2})
  ((select [:a :c]) {:x 3} {:a 1 :b 2}))


(defn select-key-as!
  {:inline-arities #{4}
   :inline
   (fn [acc m k k']
     `(let [acc# ~acc
            v# (get ~m ~k ::not-found)]
        (if (identical? ::not-found v#)
          acc#
          (assoc! acc# ~k' v#))))}
  ([acc m k k']
   (let [v (get m k ::not-found)]
     (if (identical? ::not-found v)
       acc
       (assoc! acc k' v)))))

(defn select-into-as*
  "Like [[select-as-keys]] but the keys are provided as rest arguments.
  Aggressively in-lines iteration."
  {:inline
   (fn
     ([to] to)
     ([to m & ks]
      (assert (even? (count ks)))
      (let [g (gensym)]
        `(let [~g ~m]
           (-> ~to transient ~@(map (fn [[k k']] `(select-key-as! ~g ~k ~k')) (partition 2 ks)) persistent!)))))}
  ([to] to)
  ([to m k0 k0'] (-> to transient (select-key-as! m k0 k0') persistent!))
  ([to m k0 k0' k1 k1']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') persistent!))
  ([to m k0 k0' k1 k1' k2 k2']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') (select-key-as! m k2 k2') persistent!))
  ([to m k0 k0' k1 k1' k2 k2' k3 k3']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') (select-key-as! m k2 k2') (select-key-as! m k3 k3') persistent!))
  ([to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') (select-key-as! m k2 k2') (select-key-as! m k3 k3') (select-key-as! m k4 k4') persistent!))
  ([to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') (select-key-as! m k2 k2') (select-key-as! m k3 k3') (select-key-as! m k4 k4') (select-key-as! m k5 k5') persistent!))
  ([to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') (select-key-as! m k2 k2') (select-key-as! m k3 k3') (select-key-as! m k4 k4') (select-key-as! m k5 k5') (select-key-as! m k6 k6') persistent!))
  ([to m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6' k7 k7']
   (-> to transient (select-key-as! m k0 k0') (select-key-as! m k1 k1') (select-key-as! m k2 k2') (select-key-as! m k3 k3') (select-key-as! m k4 k4') (select-key-as! m k5 k5') (select-key-as! m k6 k6') (select-key-as! m k7 k7') persistent!)))

(defn select-as*
  "Like [[select-as-keys]] but the keys are provided as rest arguments.
  Aggressively in-lines iteration."
  {:inline (fn [& args] `(select-into-as* {} ~@args))}
  ([_] {})
  ([m k0 k0'] (select-into-as* {} m k0 k0'))
  ([m k0 k0' k1 k1'] (select-into-as* {} m k0 k0' k1 k1'))
  ([m k0 k0' k1 k1' k2 k2'] (select-into-as* {} m k0 k0' k1 k1' k2 k2'))
  ([m k0 k0' k1 k1' k2 k2' k3 k3'] (select-into-as* {} m k0 k0' k1 k1' k2 k2' k3 k3'))
  ([m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4'] (select-into-as* {} m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4'))
  ([m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5'] (select-into-as* {} m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5'))
  ([m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6'] (select-into-as* {} m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6'))
  ([m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6' k7 k7'] (select-into-as* {} m k0 k0' k1 k1' k2 k2' k3 k3' k4 k4' k5 k5' k6 k6' k7 k7')))

(defn- rf-select-as [f' k k']
  (fn [acc m]
    (select-key-as! (f' acc m) m k k')))

(defn select-as
  "Like [[clojure.core/select-keys]] but faster and renames keys
  according to kmap as if by [[clojure.set/rename-keys]].
  If called without a map, returns a function with two arities which
  will select-as the provided keys.
  The returned function is operationally equivalent to:
  ((select-as kmap) m) == (select-as m kmap)
  ((select-as kmap) init m) == (merge init (select-as m kmap))"
  ([kmap]
   (->> kmap (reduce-kv rf-select-as init-select) go-select))
  ([m kmap]
   (->> kmap (reduce-kv (fn [acc k k'] (select-key-as! acc m k k')) (transient {})) persistent!)))

(comment
  (select-as {:a 1 :b 2} {:a :c :x :x})
  (select-as* {:a 1 :b 2} :a :c :x :x)
  (select-into-as* {:to "me"} {:a 1 :b 2} :a :c :x :x)
  ((select-as {:a :c :x :x}) {:a 1 :b 2})
  ((select-as {:a :c :x :x}) {:x 3} {:a 1 :b 2}))

(defn rename-key!
  {:inline
   (fn [m from to]
     `(let [m# ~m
            from# ~from
            v# (get m# from# ::not-found)]
        (if (identical? ::not-found v#)
          m#
          (-> m# (dissoc! from#) (assoc! ~to v#)))))}
  [m from to]
  (let [v (get m from ::not-found)]
    (if (identical? ::not-found v)
      m
      (-> m (dissoc! from) (assoc! to v)))))

(defn rename
  "Like [[clojure.set/rename-keys]] but faster.
  If called without a map, returns a function that will rename keys according to kmap."
  ([kmap]
   (let [f (reduce-kv
            (fn [f from to]
              (let [f' (fn [m] (rename-key! m from to))]
                (fn [m]
                  (f' (f m)))))
            transient
            kmap)]
     (fn [m] (persistent! (f m)))))
  ([m kmap]
   (->> kmap (reduce-kv rename-key! (transient m)) persistent!)))

(comment
  ((rename {:a :b}) {:a 1 :x 2}))

(defn rename*
  ([m] m)
  ([m k0 k0'] (-> m transient (rename-key! k0 k0') persistent!))
  ([m k0 k0' k1 k1'] (-> m transient (rename-key! k0 k0') (rename-key! k1 k1') persistent!))
  ([m k0 k0' k1 k1' k2 k2'] (-> m transient (rename-key! k0 k0') (rename-key! k1 k1') (rename-key! k2 k2') persistent!))
  ([m k0 k0' k1 k1' k2 k2' k3 k3'] (-> m transient (rename-key! k0 k0') (rename-key! k1 k1') (rename-key! k2 k2') (rename-key! k3 k3') persistent!)))
