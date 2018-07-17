#!/bin/bash

# test the hadoop cluster

#unpack the file
echo -e "unpack the file"
tar -xvf  HadoopInsight.tar

#go to the directory
cd HadoopInsight

#make an input directory
echo -e "making an input directory"
hdfs dfs -mkdir /input

#put the data.txt file to hdfs input file
echo -e "putting the file to input directory"
hdfs dfs -put input/* /input 

# delete hdfs if previously created
hdfs dfs -rm -r /output
hdfs dfs -rm -r /intermediate

# go to the directory
cd src/main/java

# compile
hadoop com.sun.tools.javac.Main *.java
jar cf Data-1.0-SNAPSHOT.jar *.class
hadoop jar Data-1.0-SNAPSHOT.jar Driver /input /output

# show result
echo -e "results are: "
hadoop fs -cat /output/part-r-00000
