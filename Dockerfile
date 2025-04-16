# Use an official Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS builder
ARG HOME=/workflow-engine
ENV HOME=$HOME
RUN mkdir -p $HOME

WORKDIR $HOME

RUN curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs

ADD pom.xml $HOME
COPY src ./src
COPY frontend ./frontend

RUN mvn clean package -Pproduction -DskipTests -Dvaadin.frontend.nodeDownload=false

FROM eclipse-temurin:17.0.6_10-jre-alpine


ARG HOME=/workflow-engine
WORKDIR $HOME

ENV JAVA_OPTS=""
ENV HOME=$HOME

COPY --from=builder /workflow-engine/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]


