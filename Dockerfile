FROM public.ecr.aws/docker/library/amazoncorretto:21
COPY country-server/target/country-server-0.0.1.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]