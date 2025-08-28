
# 📝 Test Portal  

A simple, secure, and student-friendly **quiz & test management platform**. Students can register, attempt MCQ-based tests, check results instantly, and admins can manage questions, courses, and student records.  

---

## ✨ Features  

* 👤 **Auth**: Student/Teacher registration & login (role-based access)  
* 📝 **Quizzes/Tests**: MCQ-based test system with instant scoring  
* 🗂️ **Organized Questions**: Course → Branch → Year → Subject categories  
* 🔎 **Search & Filters**: Filter tests by course/subject/date  
* 📊 **Results & Ranking**: Students can view score & rank  
* 🛡️ **Admin Panel**: Manage questions, view results, manage students  

---

## 🧱 Architecture (High-Level)  


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
## Application properties
# Server
server.port=8080

# DB
spring.datasource.url=jdbc:mysql://localhost:3306/test_portal?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASS
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Security
spring.security.user.name=admin
spring.security.user.password=admin123
---

## 🤝 Contributing

1. Fork the repo
2. Create a feature branch: `git checkout -b feat/your-feature`
3. Commit changes: `git commit -m "feat: add your feature"`
4. Push: `git push origin feat/your-feature`
5. Open a Pull Request

---

## 📫 Contact

* **Author:** (Rajkumar Gupta)
* **Email:** (mailto:kumarguptaraj825@gmail.com)
* **Project:**(https://github.com/raj8292/Testportal)

