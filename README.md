.

# 📝 Test Portal  

A **web-based Test Management System** where students can attempt quizzes and view results, while admins manage questions and performance reports.  

---

## 📌 Table of Contents  
- [Features](#-features)  
- [Tech Stack](#-tech-stack)  
- [Project Structure](#-project-structure)  
- [Setup Instructions](#-setup-instructions)  
- [Future Enhancements](#-future-enhancements)  
- [Screenshots](#-screenshots)  

---

## 🚀 Features  

### 👩‍🎓 Student Module  
- User registration & login  
- Attempt **MCQ-based tests**  
- Instant scoring & result display  
- View past performance & ranks  

### 👨‍💻 Admin Module  
- Secure **Admin login**  
- Add / Update / Delete questions  
- Categorize questions by **Course, Branch, Year, Subject**  
- View student results & maintain records  

---

## 🛠 Tech Stack  
- **Frontend:** HTML, CSS, JavaScript, Bootstrap  
- **Backend:** Spring Boot (Java, REST APIs, MVC)  
- **Database:** MySQL  
- **Security:** Spring Security (for admin authentication)  

---
🎯 Future Enhancements

Quiz timer with auto-submit ⏳

Export results in PDF/Excel 📑

Bulk question upload via CSV/Excel 📂

Leaderboard with analytics 📊

## 📂 Project Structure  


👨‍💻 Admin Module

Admin authentication (Spring Security)

Add / Edit / Delete questions

Organize questions by Course, Branch, Year, Subject

View and manage student results

🛠️ Tech Stack

Frontend: HTML, CSS, JavaScript, Bootstrap

Backend: Java Spring Boot (REST APIs + MVC)

Database: MySQL


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
