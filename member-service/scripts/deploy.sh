# 가동중인 도커 중단 및 삭제
sudo docker ps -a -q --filter "name=member-service" | grep -q . && docker stop member-service && docker rm member-service | true

# 기존 이미지 삭제
sudo docker rmi seunghee98/member-service:0.0.1

# 도커허브 이미지 pull
sudo docker pull seunghee98/member-service:0.0.1

# 도커 run
docker run --name member-service -it -d -p 8081:8081 seunghee98/member-service:0.0.1

# 사용하지 않는 불필요한 이미지 삭제 -> 현재 컨테이너가 물고 있는 이미지는 삭제되지 않습니다.
docker rmi -f $(docker images -f "dangling=true" -q) || true