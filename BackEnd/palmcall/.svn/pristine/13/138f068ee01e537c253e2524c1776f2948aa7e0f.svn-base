cd /userdata1/jenkins_workspaces/palmcall

rm -rf src* target* pom.xml
tar -xzf source.tar.gz
rm -rvf source.tar.gz

export JAVA_HOME=/userdata1/jdk1.8.0_65
PATH=$PATH:$HOME/bin:/userdata1/jdk1.8.0_65/bin
export PATH

/userdata1/apache-maven-3.3.9/bin/mvn -U clean package -Dmaven.test.skip=true

cp -v /userdata1/jenkins_workspaces/palmcall/target/ROOT.war /userdata1/apache-tomcat-palmcall/webapps/ROOT.war.back
cp -v /userdata1/jenkins_workspaces/palmcall/target/ROOT.war /userdata1/apache-tomcat-palmcall2/webapps/ROOT.war.back

rm -rf /userdata1/jenkins_workspaces/palmcall/target/
