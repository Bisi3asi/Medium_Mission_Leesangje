# GraalVm을 기반 이미지로 사용
FROM ghcr.io/graalvm/graalvm-community:17

# 작업 디렉토리 설정
WORKDIR /app

# 호스트 시스템의 build/libs 디렉토리 내의 모든 .jar 파일을
# 컨테이너의 /app 디렉토리로 복사
COPY build/libs/*.jar /app/app.jar

#실행할 JAR 파일 지정
#여러 파일이 있는 경우 실행할 특정 파일을 지정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]