FROM clojure

COPY . /app
WORKDIR /app

RUN lein deps
RUN lein uberjar

CMD ["/bin/sh", "/app/docker_start.sh"]
