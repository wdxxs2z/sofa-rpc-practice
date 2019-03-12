mvn clean:clean 
mvn package -Dtest assembly:assembly -DfailIfNoTests=false 
mvn install -Dmaven.test.skip=true 
