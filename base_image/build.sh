#!/bin/sh

/bin/cp -pf ../project.clj .

docker build --rm -t uochan/panpan-base .
