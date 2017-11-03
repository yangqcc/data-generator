#!/usr/bin/env bash
if [ $# -eq 1 ]; then
    app_name="$1"
    if [ "$(hyperkube kubectl get rc -l name=${app_name}|grep ${app_name}|awk '{{ print $1 }}')" != "" ]; then
        hyperkube kubectl delete -f ${app_name}-rc.yml
    fi
    sleep 5
    if [ "$(hyperkube kubectl get svc -l name=${app_name}|grep ${app_name}|awk '{{ print $1 }}')" = "" ]; then
        hyperkube kubectl create -f ${app_name}-svc.yml
    fi
    hyperkube kubectl create -f ${app_name}-rc.yml
fi