/userdata1/apache-tomcat-palmcall2/bin/shutdown.sh

sleep 3

kill $(ps aux | grep '/userdata1/apache-tomcat-palmcall2/conf/logging.properties' | awk '{print $2}')

rm -rf /userdata1/apache-tomcat-palmcall2/webapps/ROOT/ /userdata1/apache-tomcat-palmcall2/webapps/ROOT.war

cp -v /userdata1/apache-tomcat-palmcall2/webapps/ROOT.war.back /userdata1/apache-tomcat-palmcall2/webapps/ROOT.war

/userdata1/apache-tomcat-palmcall2/bin/startup.sh
