#!/bin/bash
 
 #add-apt-repository ppa:libreoffice/libreoffice-7-0
apt update

# 安装 LibreOffice 7.47
apt install -y libreoffice

./gradlew build
java -jar build/libs/tss-demo-0.0.1-SNAPSHOT.jar

