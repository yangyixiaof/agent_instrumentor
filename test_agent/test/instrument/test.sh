#/bin/bash

# Add all jars to classpath
echo -e "\033[32m	config classpath...\033[0m"
for jar in /sdcard/app_test/lib/*;
do
    export CLASSPATH=$jar:$CLASSPATH
done

# Add the config of jacoco to classpath
export CLASSPATH=/sdcard/app_test/jacoco_agent.properties:$CLASSPATH

echo -e "\033[32m	run app...\033[0m"
if [ -n "$1" ];
then
    am start -n $1
else
    echo "You need to provide [package name]/.[Main activity] of the app"
fi

