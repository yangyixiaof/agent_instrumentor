#/bin/bash

# Add all jars to classpath
# echo -e "\033[32m	config classpath...\033[0m"
# for jar in /sdcard/app_test/lib/*;
# do
#     dd if=$jar of=/data/app/com.tsinghua.date.sample-haaKBOw5DBaJd-F6UaycbA==/lib/`basename $jar`
#     # export CLASSPATH=/data/app/com.tsinghua.date.sample-haaKBOw5DBaJd-F6UaycbA==/lib/`basename $jar`:$CLASSPATH
# done

# dd if=/sdcard/app_test/lib/jacocoagent.jar of=/vendor/lib/jacocoagent.jar

# Add the config of jacoco to classpath
export CLASSPATH=/sdcard/app_test/:$CLASSPATH
echo $CLASSPATH >> /sdcard/app_test/classpath
echo $CLASSPATH


echo -e "\033[32m	run app...\033[0m"
if [ -n "$1" ];
then
    am start -n $1
else
    echo "You need to provide [package name]/.[Main activity] of the app"
fi

