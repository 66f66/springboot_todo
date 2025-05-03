# Stage 1: Build (Gradle을 사용해 JAR 생성)
FROM gradle:8.6-jdk21 AS builder

WORKDIR /app

# 빌드 캐시 최적화를 위해 gradle 파일 먼저 복사
COPY build.gradle settings.gradle ./
COPY src ./src

# Gradle 빌드 (의존성 캐시 활용)
RUN gradle bootJar --no-daemon

# Stage 2: Run (경량 JRE 기반 실행)
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Stage 1에서 빌드된 JAR 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 타임존 설정 (선택 사항)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
