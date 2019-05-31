#!/bin/sh
# healthy checker
if [ -f /tmp/check_env_change.sh ];then
    sh /tmp/check_env_change.sh
    if [ $? -ne 0 ];then
        exit 1
    fi
    
fi
