#!/bin/bash

javac -d bin -sourcepath src src/core/*.java
javac -d bin -sourcepath src src/math/*.java
javac -d bin -sourcepath src src/objects/*.java
javac -d bin -sourcepath src src/MRay.java