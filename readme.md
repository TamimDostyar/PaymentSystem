This is a payment system application with very basic routings and necessity a simple applications can have, like Venmo and CashApp. The software is developed mainly on Java and containerized on Docker. This software is using sprinboot for web server access, and it stores crucial information in SQLite. This program is mainly built for practice purpose and if needed, it can be well-developed.

A few things to remember about this application:

    1 - Program is built on Java 23
    2 - The program is using Gradle for package management
    3 - Docker is used for containerization

Run this on your computer:

    1 - If you are using Docker, try:
        1 - `Docker-compose build && docker-compose up`
    2 - If only want to use Gradle, try:
        1 - `./gradlew build && gradlew BootRun`

    3 - Access the swagger on:
        1 -  `http://localhost:8080/swagger-ui/index.html#/`

This backend has a frontend developed in Swift and can be accessed with the link below:

    `https://github.com/TamimDostyar/PaymentSystem-Frontend`

Any improvement is appreciated, and please feel free to clone and commit your change and make a pull-request. 

Here’s an image showing the interior view:


![Home Interior Screenshot](home-int.png)

CopyRights:
MIT
