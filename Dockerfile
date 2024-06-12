# Вибір базового образу
FROM openjdk:17-jdk-slim

# Встановлення змінної середовища для визначення місця розташування jar файлу
ENV APP_HOME /app

# Створення робочої директорії
WORKDIR $APP_HOME

# Копіювання jar файлу у контейнер
COPY target/AutomationTestPlatform-1-0.0.1.jar app.jar

# Відкриття порту для додатку
EXPOSE 8080

# Команда для запуску додатку
ENTRYPOINT ["java", "-jar", "app.jar"]
