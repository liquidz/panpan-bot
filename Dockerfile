FROM uochan/panpan-base
MAINTAINER uochan

COPY . /app
WORKDIR /app

RUN lein deps && lein uberjar

CMD ["/bin/sh", "/app/docker_start.sh"]
