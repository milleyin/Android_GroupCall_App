/userdata1/apache-tomcat-palmcall/bin/shutdown.sh

sleep 3

kill $(ps aux | grep '/userdata1/apache-tomcat-palmcall/conf/logging.properties' | awk '{print $2}')

rm -rf /userdata1/apache-tomcat-palmcall/webapps/ROOT/ /userdata1/apache-tomcat-palmcall/webapps/ROOT.war

cp -v /userdata1/apache-tomcat-palmcall/webapps/ROOT.war.back /userdata1/apache-tomcat-palmcall/webapps/ROOT.war

/userdata1/apache-tomcat-palmcall/bin/startup.sh
