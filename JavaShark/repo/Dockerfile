FROM ubuntu:22.04
RUN apt update && apt -y install openjdk-17-jdk openjdk-17-jre tesseract-ocr git maven curl ssh
RUN mkdir -p /var/www/baby-sharks/
run curl -O /var/www/baby-sharks/CatsScreenDataHashes.properties https://baby-sharks.hackedyour.info/CatsScreenDataHashes.properties
