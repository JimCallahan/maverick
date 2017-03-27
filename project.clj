(defproject maverick "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.2.391"]
                 [reagent "0.6.0"]
                 [reagent-utils "0.2.1"]
                 [re-frame "0.9.2"]
                 [re-com "2.0.0"]
                 [re-frisk "0.3.2"]
                 [garden "1.3.2"]
                 [ns-tracker "0.3.0"]
                 [compojure "1.5.2"]
                 [ring-server "0.4.0"]]

  :plugins [[lein-ring "0.11.0"]
            [lein-cljsbuild "1.1.4"]
            [lein-garden "0.2.8"]]

  :hooks [leiningen.cljsbuild]
  
  :ring {:handler maverick.core/app
         :init maverick.core/init
         :destroy maverick.core/destroy}
  
  :min-lein-version "2.7.1"
  
  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :garden {:builds [{:id "screen"
                     :source-paths ["src/clj"]
                     :stylesheet maverick.css/screen
                     :compiler {:output-to "resources/public/css/screen.css"
                                :pretty-print? true}}]}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev {:dependencies [[binaryage/devtools "0.8.2"]
                        [figwheel-sidecar "0.5.9"]
                        [com.cemerick/piggieback "0.2.1"]]
         :plugins [[lein-figwheel "0.5.9"]]}
   :uberjar {:aot :all}
   :production {:ring
                {:open-browser? false
                 :stacktraces? false
                 :auto-reload? false}}}

  :open-file-command "emacsclient"
  
  :cljsbuild
  {:builds
   [{:id "min"
     :source-paths ["src/cljs"]
     :compiler {:main maverick.core
                :output-to "resources/public/js/compiled/app.js"
                :optimizations :advanced
                :closure-defines {goog.DEBUG false}
                :pretty-print false}
     :jar true}

    {:id "dev"
     :source-paths ["src/cljs"]
     :figwheel {:on-jsload "maverick.core/mount-root"}
     :compiler {:main maverick.core
                :output-to "resources/public/js/compiled/app.js"
                :output-dir "resources/public/js/compiled/out"
                :asset-path "js/compiled/out"
                :source-map-timestamp true
                :preloads [devtools.preload]
                :external-config {:devtools/config
                                  {:features-to-install :all}}}}]})
