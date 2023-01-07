FROM ubuntu:22.04
RUN apt update && apt -y install openjdk-17-jdk openjdk-17-jre tesseract-ocr git maven curl ssh
RUN mkdir -p /var/www/baby-sharks/
RUN curl -O /var/www/baby-sharks/CatsScreenDataHashes.properties https://baby-sharks.hackedyour.info/CatsScreenDataHashes.properties
RUN /usr/bin/curl -fsSL https://code-server.dev/install.sh | /usr/bin/sh


#docker build -t adamoutler/ubuntu11tessmaven:2 .
#docker image push adamoutler/ubuntu11tessmaven:3