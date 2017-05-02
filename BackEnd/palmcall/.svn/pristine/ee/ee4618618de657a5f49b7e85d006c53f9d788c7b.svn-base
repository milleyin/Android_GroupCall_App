#!/bin/bash

#ssh -i /Users/jialechan/key/Singapore_test/Singapore_test root@172.17.20.13 'bash -s' < /Users/jialechan/idesWorkspaces/palmcall/shell/restartTomcatCode.sh
ssh -i /home/liang/palmchat/developer/Singapore_test root@172.17.20.13 'bash -s' < /home/liang/intellijIDEA_workspace/palmchat/palmcall/shell/restartTomcatCode.sh

sleep 8

curl -X GET 'http://52.76.173.126:30000/fetchCallableList?afid=a1010701'
sleep 3
curl -v -X GET 'http://172.17.20.13:20000/fetchCallableList?afid=a1010701'
sleep 3
curl -v -X GET 'http://172.17.20.13:30001/fetchCallableList?afid=a1010701'
