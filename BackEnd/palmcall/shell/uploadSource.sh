#!/bin/bash

#cd /Users/jialechan/idesWorkspaces/palmcall
cd /home/liang/intellijIDEA_workspace/palmchat/palmcall

rm -f source.tar.gz

tar -czvf source.tar.gz pom.xml src

#scp -i /Users/jialechan/key/Singapore_test/Singapore_test source.tar.gz root@172.17.20.13:/userdata1/jenkins_workspaces/palmcall/
scp -i /home/liang/palmchat/developer/Singapore_test source.tar.gz root@172.17.20.13:/userdata1/jenkins_workspaces/palmcall/

rm -f source.tar.gz

#ssh -i /Users/jialechan/key/Singapore_test/Singapore_test root@172.17.20.13 'bash -s' < /Users/jialechan/idesWorkspaces/palmcall/shell/uploadSourceCode.sh
ssh -i /home/liang/palmchat/developer/Singapore_test root@172.17.20.13 'bash -s' < /home/liang/intellijIDEA_workspace/palmchat/palmcall/shell/uploadSourceCode.sh

