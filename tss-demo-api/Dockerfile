FROM eclipse-temurin:17

#フォントのインストール
RUN apt-get update &&\
 apt-get install -y wget &&\
 apt-get install -y zip unzip &&\
 apt-get install -y fontconfig
RUN wget https://moji.or.jp/wp-content/ipafont/IPAexfont/IPAexfont00301.zip
RUN unzip IPAexfont00301.zip
RUN mkdir -p /usr/share/fonts/ipa
RUN cp IPAexfont00301/*.ttf /usr/share/fonts/ipa
RUN fc-cache -fv