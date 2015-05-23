#!/bin/sh

export PORT=80
export REDISCLOUD_URL="redis://$REDIS_PORT_6379_TCP_ADDR:$REDIS_PORT_6379_TCP_PORT"

java $JVM_OPTS -cp target/panpan-standalone.jar clojure.main -m panpan.core --name p --adapter slack --brain redis
