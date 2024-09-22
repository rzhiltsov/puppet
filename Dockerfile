FROM openjdk:21-jdk

COPY build/libs/puppet.jar puppet.jar

CMD [ "java", "-jar", "puppet.jar"]