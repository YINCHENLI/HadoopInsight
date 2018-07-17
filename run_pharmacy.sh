#!/bin/bash

# test the hadoop cluster
cd src

# delete hdfs
hdfs dfs -rm -r /output
hdfs dfs -rm -r /intermediate

# go to the directory
cd InsightDataPharmacy/src/main/java

# compile
hadoop com.sun.tools.javac.Main *.java
jar cf Data-1.0-SNAPSHOT.jar *.class
hadoop jar Data-1.0-SNAPSHOT.jar Driver /input /output

# show result
echo -e "results are: "
hadoop fs -cat /output/part-r-00000
