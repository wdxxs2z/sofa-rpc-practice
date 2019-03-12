mvn -U eclipse:clean eclipse:eclipse -DdownloadSources=true -DdownloadJavadocs=false  -DfailIfNoTests=false -Dmaven.test.skip=true   -f ./pom.xml
