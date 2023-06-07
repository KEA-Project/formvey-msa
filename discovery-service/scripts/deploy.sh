# 가동중인 awsstudy 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=discovery-service" | grep -q . && docker stop discovery-service && docker rm discovery-service | true

# 기존 이미지 삭제
sudo docker rmi seunghee98/discovery-service:0.0.1

# 도커허브 이미지 pull
sudo docker pull seunghee98/discovery-service:0.0.1

# 도커 run
docker run --name discovery-service -it -d -p 8761:8761 seunghee98/discovery-service:0.0.1

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않습니다.
docker rmi -f $(docker images -f "dangling=true" -q) || true