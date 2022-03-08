FROM bellsoft/liberica-openjdk-alpine-musl:11

COPY ./build/libs/project-flow.jar /app/app.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]
