.

📝 Test Portal

A web-based Test Portal that allows students to attempt quizzes and view their results, while admins can manage courses, questions, and users.

🚀 Features
👩‍🎓 Student Module

Register/Login securely

Attempt MCQ-based tests

Instant result calculation with score display

View performance & ranking

👨‍💻 Admin Module

Admin authentication (Spring Security)

Add / Edit / Delete questions

Organize questions by Course, Branch, Year, Subject

View and manage student results

🛠️ Tech Stack

Frontend: HTML, CSS, JavaScript, Bootstrap

Backend: Java Spring Boot (REST APIs + MVC)

Database: MySQL

📂 Project Structure
TestPortal/
│-- frontend/              # Bootstrap + HTML/CSS/JS files
│   │-- index.html
│   │-- login.html
│   │-- test.html
│   │-- results.html
│
│-- backend/               # Spring Boot backend
│   │-- src/main/java/com/testportal
│   │-- src/main/resources/application.properties
│   │-- pom.xml
│
│-- database/              # MySQL scripts
│   │-- schema.sql
│   │-- data.sql
│
│-- README.md

⚙️ Installation & Setup
1️⃣ Clone Repository
git clone https://github.com/yourusername/TestPortal.git
cd TestPortal

2️⃣ Setup Database (MySQL)

Create a database named test_portal

Import the schema file

CREATE DATABASE test_portal;
USE test_portal;
SOURCE database/schema.sql;


Configure database in application.properties (Spring Boot backend):

spring.datasource.url=jdbc:mysql://localhost:3306/test_portal
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

3️⃣ Run Backend (Spring Boot)
cd backend
./mvnw spring-boot:run

4️⃣ Run Frontend (Bootstrap UI)

Open frontend/index.html in your browser

Or serve via Apache/Tomcat/Nginx

🎯 Future Enhancements

Timer-based quizzes ⏳

Export results in PDF/Excel 📊

Bulk upload of questions via Excel/CSV 📂

Leaderboard & analytics 📈

📸 Screenshots

(Add images later, example 👇)

![Login Page](screenshots/login.png)  
![Dashboard](screenshots/dashboard.png)  
![Test Page](screenshots/test.png)  
