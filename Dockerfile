FROM openjdk:17-oracle
WORKDIR /home/kakao_backend
ARG JAR_PATH=./build/libs
COPY ${JAR_PATH}/kakao_backend-0.0.1-SNAPSHOT.jar ${JAR_PATH}/app.jar
ENTRYPOINT ["java", "-jar", "./build/libs/app.jar"]