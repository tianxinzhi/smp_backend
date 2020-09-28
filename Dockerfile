FROM maven

MAINTAINER chankamlam <919125189@qq.com>

EXPOSE 8080

RUN mkdir backend

WORKDIR /backend

COPY . .

RUN mvn install:install-file -Dfile=ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -Dpackaging=jar -DgeneratePom=true



ENTRYPOINT java -jar /backend/target/backend-0.0.1-SNAPSHOT.jar
    