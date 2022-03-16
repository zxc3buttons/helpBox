FROM openjdk:11
ADD /target/helpBox-0.0.1-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "-Dserver.port=$PORT", "backend.jar"]