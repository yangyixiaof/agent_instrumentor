#/bin/bash

# parse the JVM jars to Dalvik jars in lib/ by using dx from sdk
#echo -e "\033[32mparse the JVM jars to Dalvik jars...\033[0m"
#LIBS=(/home/ren/Software/jacoco-0.8.0/lib/org.jacoco.agent-0.8.0.201801022044.jar /home/ren/Software/jacoco-0.8.0/lib/org.jacoco.core-0.8.0.201801022044.jar /home/ren/Software/jacoco-0.8.0/lib/org.jacoco.report-0.8.0.201801022044.jar /home/ren/Software/jacoco-0.8.0/lib/jacococli.jar /home/ren/Software/jacoco-0.8.0/lib/jacocoagent.jar /home/ren/.m2/repository/junit/junit/4.12/junit-4.12.jar /home/ren/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar /home/ren/.m2/repository/org/ow2/asm/asm/5.2/asm-5.2.jar /home/ren/.m2/repository/args4j/args4j/2.0.28/args4j-2.0.28.jar)

#for jar in ${LIBS[@]};
#do
#    echo "`dirname $0`/lib/`basename $jar` success"
#    bash $AOSP_PATH/prebuilts/sdk/tools/dx --dex --output=`dirname $0`/lib/`basename $jar` $jar
#done

# # push the jars into android device
#echo -e "\033[32mpush jars to device...\033[0m"
#adb push lib /sdcard/app_test/lib
adb push jacoco_agent.properties /sdcard/app_test/
adb push test.sh /sdcard/app_test/

# install the apk
echo -e "\033[32minstall apk on device...\033[0m"
if [ `adb shell pm list package | grep "$2"` ];
then
    adb uninstall $2
fi
adb install $1

# run test
echo -e "\033[32mtest begin...\033[0m"
adb shell sh /sdcard/app_test/test.sh $2/.$3
