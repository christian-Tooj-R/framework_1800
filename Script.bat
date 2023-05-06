cd framework/WEB-INF/classes/   
jar cf Framework.jar ./*
move Framework.jar ../../../test_framework/WEB-INF/lib/
cd ../../../
cd test_framework/WEB-INF/classes/ 
javac  -cp ../lib/Framework.jar -d . *.java 
cd ../../../
cd test_framework/
jar cvf testFramework.war ./*
move testFramework.war ../../
pause
