FROM bellsoft/liberica-openjdk-alpine-musl:11

COPY ./build/libs/project-flow.jar /app/app.jar
WORKDIR /app

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=Europe/Moscow","-Dmail.mime.charset=UTF-8","-Dfile.encoding=UTF-8","-Xmx256M -Xms256M -XX:MaxDirectMemorySize=256M", "-jar", "app.jar"]

