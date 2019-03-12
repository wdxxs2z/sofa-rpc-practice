mvn clean:clean 

mvn -U package -Dtest -DfailIfNoTests=false -Dmaven.test.skip=true 
