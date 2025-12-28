Payment System Application

A simple backend payment system application that demonstrates the core routing and functionality of basic payment platforms such as Venmo and Cash App. This project is primarily built for practice and learning purposes, but its structure allows for further expansion into a more robust system.

The application is developed in Java, containerized with Docker, and uses Spring Boot for web server access. Essential data is stored using SQLite.

Tech Stack:

    Java 23
    Spring Boot
    Gradle (dependency & package management)
    SQLite (database)
    Docker & Docker Compose (containerization)

A few things to remember about this application:

    1 - Program is built on Java 23
    2 - The program is using Gradle for package management
    3 - Docker is used for containerization

You can run the application either with Docker or directly using Gradle.

1 - If you are using Docker, try(Recommended):

        Docker-compose build && docker-compose up

2 - If only want to use Gradle, try:

        ./gradlew build && gradlew BootRun
3 - Access the swagger on:

        http://localhost:8080/swagger-ui/index.html#/

This backend has a frontend developed in Swift and can be accessed with the link below:

    https://github.com/TamimDostyar/PaymentSystem-Frontend


Here’s an image showing the interior view:

![Home Interior Screenshot](home-int.png)


Contributions

Improvements are welcome! Feel free to clone the repository, make your changes, and submit a pull request.

License

MIT License
