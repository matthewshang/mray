#!/bin/bash

rm -r bin/*
touch sources.txt
find . -name "*.java" > sources.txt
javac -d bin @sources.txt
rm sources.txt