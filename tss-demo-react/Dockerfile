#FROM node:19.7-alpine
FROM node:18.16.0-alpine

RUN [ "mkdir", "-p", "/usr/src/app" ]

WORKDIR /usr/src/app

ENV PATH /usr/src/app/node_modules/.bin:$PATH

COPY package.json /usr/src/app/package.json

COPY yarn.lock /usr/src/app/yarn.lock

RUN [ "yarn", "install", "--production" ]

#フォントのインストール
RUN apk add --no-cache curl fontconfig &&\
 curl -O https://moji.or.jp/wp-content/ipafont/IPAexfont/IPAexfont00301.zip && \
 mkdir -p /usr/share/fonts/ipa && \
 mkdir -p /temp && \
 unzip IPAexfont00301.zip -d /temp && \
 cp /temp/IPAexfont00301/*.ttf /usr/share/fonts/ipa/ && \
 rm IPAexfont00301.zip
RUN rm -rf /temp && \
 fc-cache -fv

COPY . /usr/src/app
