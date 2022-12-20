# Keys

Select and rename keys as fast as possible with idiomatic Clojure

## Usage


```clojure
(require '[bsless.keys :as keys])
```

This library provides a few functions for selecting and renaming keys,
operationally equivalent to the following clojure.core functions:

| keys                                   | clojure.core                                                                |
|:---------------------------------------|:----------------------------------------------------------------------------|
| `(select m ks)`                        | `(select-keys m ks)`                                                        |
| `((select ks) m)`                      | `(select-keys m ks)`                                                        |
| `((select ks) to m)`                   | `(merge to (select-keys m ks))`                                             |
| `(select* m k0 k1 k2)`                 | `(select-keys m [k0 k1 k2])`                                                |
| `(select-into* to m k0 k1 k2)`         | `(merge to (select-keys m [k0 k1 k2]))`                                     |
| `(select-as m kmap)`                   | `(-> m (select-keys (keys kmap)) (set/rename-keys kmap))`                   |
| `((select-as kmap) m)`                 | `(-> m (select-keys (keys kmap)) (set/rename-keys kmap))`                   |
| `((select-as kmap) to m)`              | `(merge to (-> m (select-keys (keys kmap)) (set/rename-keys kmap)))`        |
| `(select-as* m k0 k0' k1 k1')`         | `(-> m (select-keys [k0 k1]) (set/rename-keys {k0 k0' k1 k1'}))`            |
| `(select-into-as* to m k0 k0' k1 k1')` | `(merge to (-> m (select-keys [k0 k1]) (set/rename-keys {k0 k0' k1 k1'})))` |
| `(rename m kmap)`                      | `(set/rename-keys m kmap)`                                                  |
| `((rename kmap) m)`                    | `(set/rename-keys m kmap)`                                                  |
| `(rename* m k0 k0' k1 k1')`            | `(set/rename-keys m {k0 k0' k1 k1'})`                                       |


All implementations provide better performance than the core implementation.

The implementations which don't take a source map return a function
which closes over all operations, in a similar manner to operators
fusion.

## Performance

```clojure
(quick-bench (select-keys {:a 1 :b 2 :c 3} [:a :b :c])) ;; => "244.746154 ns"
(quick-bench (keys/select {:a 1 :b 2 :c 3} [:a :b :c])) ;; => "97.587060 ns"
(quick-bench (apply keys/select {:a 1 :b 2 :c 3} [[:a :b :c]])) ;; => "195.571249 ns"
(def f (keys/select [:a :b :c]))
(quick-bench (f {:a 1 :b 2 :c 3})) ;; => "106.211750 ns"
(quick-bench (keys/select* {:a 1 :b 2 :c 3} :a :b :c)) ;; => "72.448903 ns"
(quick-bench (apply keys/select* {:a 1 :b 2 :c 3} [:a :b :c])) ;; => "183.127248 ns"
```

## License

Copyright © 2022 Ben Sless

Distributed under the Eclipse Public License version 1.0.
