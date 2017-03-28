#!/bin/bash

case "$1" in
  ("start-maverick")
    java -cp '/opt/maverick/jars/*:/opt/maverick/maverick.jar' \
	 maverick.core.main;;
    
    (*)
      exec "$@";;
esac

