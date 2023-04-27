#!/bin/bash

cat ../log/*.log > ../log/total.log
sort -k 3 ../log/total.log > ../ssdd.log
