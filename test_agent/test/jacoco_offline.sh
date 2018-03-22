#!/bin/bash

LIBS=/home/ren/.m2/repository/junit/junit/4.12/junit-4.12.jar:/home/ren/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/home/ren/.m2/repository/org/ow2/asm/asm/5.2/asm-5.2.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.core/0.8.0/org.jacoco.core-0.8.0.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.report/0.8.0/org.jacoco.report-0.8.0.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.agent/0.8.0/org.jacoco.agent-0.8.0.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.agent/0.8.0/org.jacoco.agent-0.8.0-runtime.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.ant/0.8.0/org.jacoco.ant-0.8.0.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.ant/0.8.0/org.jacoco.ant-0.8.0-nodeps.jar:/home/ren/.m2/repository/org/jacoco/org.jacoco.cli/0.8.0/org.jacoco.cli-0.8.0-nodeps.jar:/home/ren/.m2/repository/args4j/args4j/2.0.28/args4j-2.0.28.jar
TARGET_JAR_PATH=$1
CONFIG_PATH=/home/ren/Software/jacoco-0.8.0/lib/

CLASSPATH=$LIBS:$TARGET_JAR_PATH:$CONFIG_PATH:$CLASSPATH

INST_CLASS=$2

java -cp $CLASSPATH $INST_CLASS


