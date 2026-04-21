# RichardWebsite

RichardWebsite is a full-stack web application built with Spring Boot that serves as an online bookstore. It allows users to browse books, add them to a cart, place orders, and manage their accounts. Administrators can manage the book inventory and view orders.

## Features

### For Users:
- **Browse Books**: View a paginated list of books with search by title and filter by genre.
- **User Authentication**: Register and login to access personalized features.
- **Shopping Cart**: Add books to cart, view cart contents, and proceed to checkout.
- **Order Management**: Place orders and view order history and details.
- **Responsive Design**: Works on desktop and mobile devices.

### For Administrators:
- **Book Management**: Add, update, and delete books from the inventory.
- **Order Oversight**: View all orders and their details.

## How It Works

### Architecture
- **Backend**: Spring Boot with Spring MVC for handling requests, Spring Security for authentication, and Spring Data JPA for database interactions.
- **Frontend**: Thymeleaf templates for server-side rendering, with custom CSS and JavaScript for styling and interactivity.
- **Database**: MySQL database to store users, books, carts, and orders.

### User Flow
1. **Registration/Login**: New users register with a username, password, and role (USER or ADMIN). Existing users log in.
2. **Home Page**: Displays a list of books. Users can search by title or filter by genre. Pagination is supported.
3. **Book Details**: Click on a book to view more details (though this might be handled in templates).
4. **Add to Cart**: From the home page or book details, add books to the cart with a specified quantity.
5. **Cart**: View items in cart, see total price, and proceed to checkout.
6. **Checkout**: Enter shipping details and confirm the order.
7. **Orders**: View past orders and their details.
8. **Admin Panel**: Admins can access dashboards to manage books and view orders.

### Security
- Spring Security handles authentication and authorization.
- Passwords are stored securely (assuming BCrypt is used in the service layer).
- Role-based access: USER for customers, ADMIN for administrators.

### Database Schema
- **Users**: Stores user information including username, password, and role.
- **Books**: Stores book details like title, author, genre, price, quantity, and cover image URL.
- **Carts**: Links users to their cart items.
- **CartItems**: Items in the cart with book reference and quantity.
- **Orders**: Order records with user, date, and status.
- **OrderItems**: Items in each order.

## Technologies Used
- **Java 26**
- **Spring Boot 4.0.5**
- **Spring MVC**: For web layer.
- **Spring Security**: For authentication and authorization.
- **Spring Data JPA**: For ORM and database operations.
- **Thymeleaf**: For templating.
- **MySQL**: Database.
- **Maven**: Build tool.
- **CSS/JavaScript**: For frontend styling and interactivity.

## Prerequisites
- Java 26 or higher
- Maven 3.6+
- MySQL 8.0+

## Setup and Installation

1. **Clone the Repository**:
   ```
   git clone https://github.com/RichardPajarillo/RichardWebsite.git
   cd RichardWebsite
   ```

2. **Set Up MySQL Database**:
   - Create a database named `bookstore_database`.
   - Update `src/main/resources/application.properties` with your MySQL credentials if different from default (username: root, password: admin).

3. **Build the Project**:
   ```
   mvn clean install
   ```

4. **Run the Application**:
   ```
   mvn spring-boot:run
   ```

5. **Access the Application**:
   - Open a browser and go to `http://localhost:8080`
   - Register a new user or login with existing credentials.
   - For admin access, register with role "ADMIN" or use an existing admin account.

## Project Structure
- `src/main/java/org/example/richardwebsite/`: Java source code
  - `config/`: Security configuration
  - `controller/`: Web controllers for handling requests
  - `model/`: JPA entities
  - `repository/`: Data access layer
  - `service/`: Business logic
- `src/main/resources/`: Configuration and static resources
  - `templates/`: Thymeleaf HTML templates
  - `static/css/`: Stylesheets
  - `static/js/`: JavaScript files
- `pom.xml`: Maven configuration

## Contributing
Feel free to fork the repository and submit pull requests for improvements or bug fixes.

## License
This project is licensed under the MIT License.</content>
<parameter name="filePath">c:\Users\szepeda\IdeaProjects\RichardWebsite\README.md