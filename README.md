# RichardWebsite - Online Bookstore

Welcome to RichardWebsite! This is a simple online bookstore where you can buy books, manage your shopping cart, and place orders. It's built with Spring Boot (a Java framework) and uses a database to store information.

## What This App Does

### For Regular Users (Customers):
- **Sign up and log in**: Create an account to start shopping
- **Browse books**: Look at all available books, search by title, or filter by genre (like Fantasy, Romance, etc.)
- **Add to cart**: Pick books you want to buy and add them to your shopping cart
- **Checkout**: Pay for your books and place an order
- **View orders**: See your past orders and what you bought

### For Admins (Store Managers):
- **Manage books**: Add new books, update book info, or remove books from the store
- **View all orders**: See what everyone has ordered and update order status (like "Shipped" or "Delivered")

## How to Run the App

### Requirements:
- Java (version 26 or higher)
- Maven (for building the project)

### Steps:
1. Open a terminal/command prompt
2. Go to the project folder: `cd RichardWebsite`
3. Run: `./mvnw spring-boot:run` (or `mvnw.cmd spring-boot:run` on Windows)
4. Open your browser and go to `http://localhost:8080`
5. Register a new account or log in with existing one

### Default Admin Account:
- Username: `admin`
- Password: `admin`

## Project Structure (Simple Explanation)

### Backend (The Brain - Java Code):
- **Main.java**: Starts the whole application
- **Config/**: Security settings (who can access what)
- **Controller/**: Handles web requests (like when you click buttons)
- **Model/**: Data classes (User, Book, Order, etc.)
- **Repository/**: Talks to the database (saves/loads data)
- **Service/**: Business logic (calculates totals, checks rules)

### Frontend (What You See - HTML Pages):
- **index.html**: Home page with book list
- **login.html**: Login page
- **register.html**: Sign up page
- **cart.html**: Your shopping cart
- **checkout.html**: Pay for your books
- **orders.html**: Your order history
- **admin-books.html**: Admin page to manage books
- And more pages for details, about, contact, etc.

### Tests (Quality Checks):
- Tests for each part to make sure everything works
- Like testing that adding to cart actually adds the book

## Technologies Used
- **Java**: The programming language
- **Spring Boot**: Makes building web apps easy
- **Thymeleaf**: Creates the web pages
- **MySQL**: Database for storing data
- **JUnit**: For testing the code

## For Developers
If you want to change the code:
- Backend classes are in `src/main/java/org/example/richardwebsite/`
- HTML templates are in `src/main/resources/templates/`
- Tests are in `src/test/java/`

Run tests with: `./mvnw test`

## Need Help?
Check out HELP.md for quick start guide, HOW_IT_WORKS.md for how the app flows, and literally_everything_explained.md for detailed explanations of every part.
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