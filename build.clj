(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b] ; for b/git-count-revs
            [org.corfield.build :as bb]))

(def lib 'io.github.bsless/keys)
(defn -version [s] (format "0.0.%s" s))
(def version (-version (b/git-count-revs nil)))
(def snapshot (-version (str (b/git-count-revs nil) "-SNAPSHOT")))

(defn ->opts
  [opts]
  (-> opts
      (assoc :lib lib)
      (assoc :version (if (:snapshot opts) snapshot version))))

(defn test "Run the tests." [opts]
  (bb/run-tests opts))

(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
  (-> opts
      ->opts
      (bb/run-tests)
      (bb/clean)
      (bb/jar)))

(defn install "Install the JAR locally." [opts]
  (-> opts
      ->opts
      (bb/install)))

(defn deploy "Deploy the JAR to Clojars." [opts]
  (-> opts
      ->opts
      (bb/deploy)))
