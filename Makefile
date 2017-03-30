doc:
	mvn -f sporamonitor/pom.xml clean compile test-compile jxr:jxr checkstyle:checkstyle org.pitest:pitest-maven:mutationCoverage
	rm -rf dokumentaatio/checkstyle/* dokumentaatio/pit/*
	cp -R sporamonitor/target/site/* dokumentaatio/checkstyle
	cp -R sporamonitor/target/pit-reports/**/* dokumentaatio/pit
