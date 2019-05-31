#!/bin/bash
# inject config data into container
#@author jimin.huang@nx-engine.com

set -e
if [ -f /usr/share/zoneinfo/Asia/Shanghai ];then
  echo "tz set to Asia/Shanghai"
  ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
  export TZ="Asia/Shanghai"
else
  echo "warn: no tzdata"
fi


mkdir -p /cfg/
if [ -f /cfg/env.txt ]; then
    echo "/cfg/env.txt mounted"
    set -a # automatically export all variables
    . /cfg/env.txt
    set +a
    echo "import  $(wc -l /cfg/env.txt) env vars from /cfg/env.txt done"
else
    echo "/cfg/env.txt not found!"
fi
if [ -f /docker/initdata/init.sh ]; then
    echo "/docker/initdata/init.sh"
    sh "/docker/initdata/init.sh"
fi
if [ -z ${HOSTNAME} ];then
    HOSTNAME=no-name-service
fi

K8S_NS_FILE="/var/run/secrets/kubernetes.io/serviceaccount/namespace"
if [ -f ${K8S_NS_FILE} ];then
K8S_NS=`head -n 1 ${K8S_NS_FILE}`
SVC_NAME=`echo ${K8S_NS}.${HOSTNAME} | rev | cut -d'-'  -f 3- | rev`
MY_K8S_NS=${K8S_NS}
MY_K8S_SVC_NAME=$(echo ${SVC_NAME} | awk -F. '{print $NF}')
else
K8S_NS="cant-get-ns"
fi
echo "SVC_NAME=${SVC_NAME}"
echo "MY_K8S_NS=${MY_K8S_NS}"
echo "MY_K8S_SVC_NAME=${MY_K8S_SVC_NAME}"


echo -n '
#!/bin/sh
if [ -f /cfg/env.txt ];then  
    # first run
    if [ ! -f /tmp/env.pre.md5 ];then
        md5sum  /cfg/env.txt > /tmp/env.pre.md5
        exit 0
    else
        md5sum  /cfg/env.txt > /tmp/env.now.md5
        diff /tmp/env.pre.md5 /tmp/env.now.md5
        # cfg change
        ret=$?
        if [ ${ret} -ne 0 ];then
            exit 1
        fi
        /bin/cp -f /tmp/env.now.md5 /tmp/env.pre.md5
     fi
fi
' > /tmp/check_env_change.sh
chmod +x /tmp/check_env_change.sh

ls /
echo "MY_K8S_SVC_NAME=${MY_K8S_SVC_NAME}"
echo ":thunderbolt-debug thunderbolt-ev-gb-center thunderbolt-ev-gb-uploader thunderbolt-gateway-dispatcher thunderbolt-gateway-server thunderbolt-gateway-tcu"
if [ ! -z "$(which java)" ];then
    JAVA_OPTS="${JAVA_OPTS} ${PINPOINT_OPTS} -XX:+UseG1GC -XX:G1ReservePercent=20 -Xloggc:/gc.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=2M -XX:-PrintGCDetails -XX:+PrintGCDateStamps -XX:-PrintTenuringDistribution "
#    case $MY_K8S_SVC_NAME in
#    　　thunderbolt-global-dispatcher)
#        jar=$(find / -maxdepth 1 -name "thunderbolt-global-dispatcher*jar" 2>/dev/null |head -n 1)
#        echo jar=${jar}
#    　　;;
#    　　thunderbolt-global-api)
#        jar=$(find / -maxdepth 1 -name "thunderbolt-global-api*jar" 2>/dev/null |head -n 1)
#    　　;;
#    　　one-app-core)
#        jar=$(find / -maxdepth 1 -name "one-app-core*jar" 2>/dev/null |head -n 1)
#        ;;
#    　　*)
#        echo "not support ${MY_K8S_SVC_NAME}"
#    　　exit 0
#    　　;;
#    esac
    jar=$(find / -maxdepth 1 -name "${MY_K8S_SVC_NAME}*jar" 2>/dev/null| egrep -v tests.jar|egrep -v sources.jar|head -n 1)
    echo "jar=${jar}"
    echo "JAVA_OPTS=${JAVA_OPTS}"
    if [ -n "${jar}" ];then
      echo "run ${jar}"
      exec java $JAVA_OPTS  -jar ${jar}
    else
      echo "cant detect /app.jar, will exit"
      sleep 20; 
      exit 1
    fi
fi
