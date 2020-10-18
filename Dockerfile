FROM openjdk:11.0.8-buster
WORKDIR /app
COPY . /app/
RUN apt update
RUN apt install maven -y
EXPOSE 8080