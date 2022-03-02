FROM openjdk:11.0.9-jdk-slim as extract
WORKDIR /extract
COPY ./target/store-0.0.1-SNAPSHOT.jar application.jar
RUN jar xvf application.jar && rm application.jar

FROM openjdk:11.0.9-jre-slim
MAINTAINER Carlos R. B. Meneghette <carlosrb.work@gmail.com>
RUN addgroup --system spring && adduser --system --group spring
COPY src/test/resources/.camel/jsonmsg.json /input/jsonmsg.json
RUN chown -R spring:spring /input
USER spring:spring
WORKDIR /application
COPY --from=extract /extract/ /application/
ENV case.input.from=file:///input?autoCreate=true&delay=10000&delete=false
ENV case.rabbitmq.addresses=192.168.50.53:5672
ENV case.rabbitmq.username=guest
ENV case.rabbitmq.password=guest
ENV ssl-protocol=teste
#ENV ssl-protocal=TLS

ENTRYPOINT ["java", "-Duser.timezone=UTC", "org.springframework.boot.loader.JarLauncher"]
