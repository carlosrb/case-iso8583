FROM openjdk:11.0.9-jdk-slim as extract
WORKDIR /extract
COPY ./target/transaction-0.0.1-SNAPSHOT.jar application.jar
RUN jar xvf application.jar && rm application.jar

FROM openjdk:11.0.9-jre-slim
MAINTAINER Carlos R. B. Meneghette <carlosrb.work@gmail.com>
RUN addgroup --system spring && adduser --system --group spring
USER spring:spring
WORKDIR /application
COPY --from=extract /extract/ /application/
ENTRYPOINT ["java", "-Duser.timezone=UTC", "org.springframework.boot.loader.JarLauncher"]
