#!/bin/bash

cat ./log/*adjusted.log > ./log/total.log
sort -k 3 ./log/total.log > ./ssdd.log
