#!/bin/bash

echo "--------------- 서버 배포 시작 -----------------"
docker stop kampus-server || true
docker rm kampus-server || true
docker rmi -f 440744221393.dkr.ecr.ap-northeast-2.amazonaws.com/kampus-dev:latest || true
docker login -u AWS -p $(aws ecr get-login-password --region ap-northeast-2) 440744221393.dkr.ecr.ap-northeast-2.amazonaws.com
docker pull --no-cache 440744221393.dkr.ecr.ap-northeast-2.amazonaws.com/kampus-dev:latest
docker run -d --name kampus-server -p 8080:8080 440744221393.dkr.ecr.ap-northeast-2.amazonaws.com/kampus-dev:latest
echo "--------------- 서버 배포 끝 -----------------"