# 🍔 Food Delivery Backend

A scalable and secure **Food Delivery REST API** built with **Spring Boot**, **MongoDB**, **Spring Security (JWT)**, **Razorpay**, and **Cloudinary**. This backend powers a food ordering platform with authentication, food management, cart management, order processing, payment integration, and role-based authorization.

---

## 🚀 Features

### 🔐 Authentication & Authorization
- User Registration
- User Login
- JWT Authentication
- Role-Based Authorization (ADMIN & USER)
- Password Encryption using BCrypt
- Stateless Authentication

### 👤 User Management
- Register New User
- Update User Profile
- Delete User
- Fetch Logged-in User Details

### 🍕 Food Management
- Add Food Item
- Upload Food Images to Cloudinary
- Get All Foods
- Get Food By ID
- Delete Food Item
- Category-wise Food Listing

### 🛒 Cart Management
- Add Food to Cart
- Remove Food from Cart
- View User Cart
- Clear Cart

### 📦 Order Management
- Create Order
- Razorpay Payment Integration
- Verify Payment
- View User Orders
- View All Orders (Admin)
- Update Order Status
- Delete Order

### 💳 Payment Integration
- Razorpay Order Creation
- Secure Payment Verification
- Signature Validation
- Automatic Order Status Update

### 📖 API Documentation
- Swagger UI
- OpenAPI 3 Documentation

---

# 🛠 Tech Stack

| Technology | Usage |
|------------|-------|
| Java 21 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Security | Authentication & Authorization |
| JWT | Secure Authentication |
| MongoDB | Database |
| Spring Data MongoDB | ORM |
| Razorpay | Payment Gateway |
| Cloudinary | Image Storage |
| Maven | Dependency Management |
| Swagger OpenAPI | API Documentation |
| Lombok | Boilerplate Reduction |

---

# 📂 Project Structure

```
src
├── controller
├── service
├── serviceImpl
├── repository
├── model
├── io
├── config
├── security
├── util
├── exception
└── FoodDeliveryBackendApplication.java
```

---

# ⚙️ Installation

## Clone Repository

```bash
git clone https://github.com/yourusername/FoodDeliveryBackend.git
```

```bash
cd FoodDeliveryBackend
```

---

## Configure Environment

Update your `application.properties`

```properties
# MongoDB
spring.data.mongodb.uri=YOUR_MONGODB_URI

# JWT
jwt.secret.key=YOUR_SECRET_KEY

# Cloudinary
cloudinary.cloud.name=YOUR_CLOUD_NAME
cloudinary.api.key=YOUR_API_KEY
cloudinary.api.secret=YOUR_API_SECRET

# Razorpay
razorpay.key.id=YOUR_KEY_ID
razorpay.key.secret=YOUR_KEY_SECRET
```

---

## Build Project

```bash
mvn clean install
```

---

## Run Application

```bash
mvn spring-boot:run
```

Server starts at

```
http://localhost:8080
```

---

# 📚 API Documentation

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI Docs

```
http://localhost:8080/v3/api-docs
```

---

# 🔑 Authentication

After login, copy the generated JWT token.

Click **Authorize** in Swagger.

```
Bearer <your_jwt_token>
```

Example

```
Bearer eyJhbGciOiJIUzI1NiJ9....
```

---

# 📌 API Endpoints

## Authentication

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/login` | User Login |

---

## User APIs

| Method | Endpoint |
|---------|----------|
| POST | `/users/register` |
| GET | `/users/profile` |
| PUT | `/users/profile` |
| DELETE | `/users/{id}` |

---

## Food APIs

| Method | Endpoint |
|---------|----------|
| POST | `/api/foods` |
| GET | `/api/foods` |
| GET | `/api/foods/{id}` |
| DELETE | `/api/foods/{id}` |

---

## Cart APIs

| Method | Endpoint |
|---------|----------|
| POST | `/api/cart` |
| GET | `/api/cart` |
| DELETE | `/api/cart` |
| POST | `/api/cart/remove` |

---

## Order APIs

| Method | Endpoint |
|---------|----------|
| POST | `/order/create` |
| POST | `/order/verify` |
| GET | `/order` |
| GET | `/order/all` |
| PATCH | `/order/status/{orderId}` |
| DELETE | `/order/{orderId}` |

---

# 🖼 Image Upload

Food images are uploaded using **Cloudinary**.

Supported formats:

- JPG
- JPEG
- PNG
- WEBP

---

# 💳 Payment Flow

```
Create Order
      │
      ▼
Generate Razorpay Order
      │
      ▼
User Completes Payment
      │
      ▼
Verify Payment Signature
      │
      ▼
Update Order Status
```

---

# 🔒 Security

- JWT Authentication
- BCrypt Password Encoding
- Spring Security
- Role-Based Access Control
- Stateless Sessions
- Secure REST APIs

---

# 📦 Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Starter Test
- Spring Data MongoDB
- JWT (JJWT)
- Razorpay Java SDK
- Cloudinary SDK
- Lombok
- SpringDoc OpenAPI

---

# 🧪 Testing

Use:

- Swagger UI
- Postman

---

# 📄 License

This project is licensed under the MIT License.

---

# 👨‍💻 Author

**Harsh Sahu**

- GitHub: https://github.com/Harshsahu11

---

⭐ If you found this project helpful, consider giving it a star on GitHub!
