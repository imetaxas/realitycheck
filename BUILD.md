Build
-------
mvn package

Deploy to Sonatype
-------------------
mvn deploy -P sign,release [--settings ../../.m2/settings2.xml]

Release to Maven Central
-------------------------
**Prepare:**

mvn release:clean release:prepare

**If it fails:**

mvn release:rollback

mvn release:clean

**Else Perform:**

mvn release:perform

**Prepare the bundle manually**
https://stackoverflow.com/questions/28846802/how-to-manually-publish-jar-to-maven-central

cd target
jar -cvf bundle.jar realitycheck-*
Upload to https://oss.sonatype.org/#staging-upload
Release from https://oss.sonatype.org/#stagingRepositories
