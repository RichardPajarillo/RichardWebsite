# RichardWebsite - Online Bookstore

Welcome to RichardWebsite! This is a comprehensive online bookstore built with Spring Boot, providing a complete e-commerce experience for customers and administrators. The application allows users to browse, search, and purchase books while administrators can manage inventory, users, and orders.

## Features

### For Customers:
- **User Registration & Authentication**: Secure account creation and login
- **Book Browsing**: Paginated book catalog with detailed information
- **Search & Filtering**: Search by title or filter by genre
- **Shopping Cart**: Add, remove, and update book quantities
- **Checkout Process**: Secure order placement
- **Order History**: View past orders and order details

### For Administrators:
- **Book Management**: Add, edit, and delete books from inventory
- **User Management**: View, create, update roles, and delete user accounts
- **Order Management**: View all orders, update order status, and delete orders
- **Dashboard**: Centralized admin interface

## Technology Stack

- **Backend**: Java 26, Spring Boot 4.0.5
- **Web Framework**: Spring MVC with Thymeleaf templating
- **Security**: Spring Security for authentication and authorization
- **Database**: MySQL 8.0+ with Spring Data JPA
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Frontend**: HTML5, CSS3, Bootstrap 5, JavaScript

## Prerequisites

- Java 26 or higher
- Maven 3.6+
- MySQL 8.0+

## Installation & Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/RichardPajarillo/RichardWebsite.git
   cd RichardWebsite
   ```

2. **Database Setup**:
   - Create a MySQL database named `bookstore_database`
   - Update database credentials in `src/main/resources/application.properties` if needed (default: username `root`, password `admin`)

3. **Run the Application**:
   ```bash
   # Linux/Mac
   ./mvnw spring-boot:run

   # Windows
   mvnw.cmd spring-boot:run
   ```

4. **Access the Application**:
   - Open browser to `http://localhost:8080`
   - Register a new account or login with admin credentials

### Default Admin Account
- Username: `admin`
- Password: `admin`

## Project Structure

```
src/
├── main/
│   ├── java/org/example/richardwebsite/
│   │   ├── RichardWebsiteApplication.java    # Main application class
│   │   ├── config/
│   │   │   ├── SecurityConfig.java           # Security configuration
│   │   │   └── CustomAccessDeniedHandler.java # Access denied handler
│   │   ├── controller/
│   │   │   ├── AuthController.java           # Authentication
│   │   │   ├── BookController.java           # Book browsing
│   │   │   ├── CartController.java           # Shopping cart
│   │   │   ├── CheckoutController.java       # Checkout process
│   │   │   ├── OrderController.java          # Order management
│   │   │   ├── AdminBookController.java      # Book admin
│   │   │   ├── AdminUserController.java      # User admin
│   │   │   ├── AdminController.java          # Admin dashboard
│   │   │   └── InfoController.java           # Info pages
│   │   ├── model/
│   │   │   ├── User.java                     # User entity
│   │   │   ├── Book.java                     # Book entity
│   │   │   ├── Cart.java                     # Shopping cart
│   │   │   ├── CartItem.java                 # Cart items
│   │   │   ├── Order.java                    # Orders
│   │   │   ├── OrderItem.java                # Order items
│   │   │   └── OrderStatus.java              # Order status enum
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── BookRepository.java
│   │   │   ├── CartRepository.java
│   │   │   ├── CartItemRepository.java
│   │   │   └── OrderRepository.java
│   │   └── service/
│   │       ├── UserService.java
│   │       ├── BookService.java
│   │       ├── CartService.java
│   │       ├── OrderService.java
│   │       ├── CheckoutService.java
│   │       ├── SecurityService.java
│   │       └── CustomUserDetailsService.java
│   └── resources/
│       ├── application.properties            # App configuration
│       ├── application-test.properties       # Test configuration
│       ├── messages.properties               # Internationalization
│       ├── static/
│       │   ├── css/                          # Stylesheets
│       │   └── js/                           # JavaScript files
│       └── templates/                        # Thymeleaf templates
│           ├── index.html                    # Home page
│           ├── login.html                    # Login page
│           ├── register.html                 # Registration
│           ├── cart.html                     # Shopping cart
│           ├── checkout.html                 # Checkout
│           ├── orders.html                   # Order history
│           ├── admin-dashboard.html          # Admin dashboard
│           ├── admin-books.html              # Book management
│           ├── admin-users.html              # User management
│           ├── admin-orders.html             # Order management
│           └── fragments/                    # Reusable components
└── test/
    └── java/org/example/richardwebsite/      # Test classes
```

## Architecture

The application follows a layered architecture:

1. **Presentation Layer**: Thymeleaf templates with Bootstrap styling
2. **Controller Layer**: Spring MVC controllers handling HTTP requests
3. **Service Layer**: Business logic and transaction management
4. **Repository Layer**: Data access using Spring Data JPA
5. **Database Layer**: MySQL database with JPA entities

## Security

- **Authentication**: Form-based login with Spring Security
- **Authorization**: Role-based access control (USER/ADMIN)
- **Password Encoding**: NoOpPasswordEncoder (development only - use BCryptPasswordEncoder in production)
- **Protected Routes**: Admin functions require ROLE_ADMIN, cart/order operations require authentication

## Testing

Run tests with:
```bash
./mvnw test
```

Test coverage includes:
- Controller tests with MockMvc
- Service layer testing with Mockito
- Repository integration tests
- Security configuration tests

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /login` - Login page
- `POST /login` - Process login
- `GET /register` - Registration page
- `POST /register` - Process registration
- `GET /about` - About page
- `GET /news` - News page
- `GET /contact` - Contact page

### Customer Endpoints (Authenticated)
- `GET /cart` - View cart
- `POST /cart/add/{id}` - Add to cart
- `GET /cart/remove/{id}` - Remove from cart
- `POST /cart/update` - Update quantity
- `GET /checkout` - Checkout page
- `POST /checkout` - Process checkout
- `GET /orders` - Order history
- `GET /orders/{id}` - Order details

### Admin Endpoints (ROLE_ADMIN)
- `GET /admin` - Admin dashboard
- `GET /admin/books` - Manage books
- `GET /admin/books/new` - Add book form
- `POST /saveBook` - Save book
- `GET /admin/books/update/{id}` - Edit book
- `POST /admin/books/delete/{id}` - Delete book
- `GET /admin/users` - Manage users
- `GET /admin/users/add` - Add user form
- `POST /admin/users/add` - Create user
- `POST /admin/users/updateRole` - Change role
- `POST /admin/users/delete/{id}` - Delete user
- `GET /admin/orders` - Manage orders
- `POST /admin/orders/update-status` - Update status
- `POST /admin/orders/delete/{id}` - Delete order

## Database Schema

The application uses JPA with automatic schema generation. Key tables:

- **users**: User accounts and roles
- **books**: Book inventory
- **carts**: Shopping carts
- **cart_items**: Cart contents
- **orders**: Order records
- **order_items**: Order contents

## Development

### Running in Development
```bash
./mvnw spring-boot:run
```

### Building for Production
```bash
./mvnw clean package
java -jar target/*.jar
```

### IDE Setup
- Import as Maven project
- Ensure Java 26 SDK is configured
- Run tests via IDE or Maven

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Submit a pull request

## License

This project is for educational purposes.

## Documentation

- [HELP.md](HELP.md) - Quick start guide
- [HOW_IT_WORKS.md](HOW_IT_WORKS.md) - Detailed application flow
- [literally_everything_explained.md](literally_everything_explained.md) - Comprehensive technical details
- [CAPSTONE_PRESENTATION.md](CAPSTONE_PRESENTATION.md) - Presentation script</content>
<parameter name="filePath">c:\Users\szepeda\IdeaProjects\RichardWebsite\README.md