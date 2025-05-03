#### Base64 시크릿 키

./gradlew bootjar

docker build -t <project_name>:latest .
docker rmi <project_name>:latest

docker tag <local_image_name>:<local_tag_name> <username>/<repo_name>:<tag>
docker push <username>/<repo_name>:<tag>
