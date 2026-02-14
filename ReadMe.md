// Order Service Local setup instructions

# Order Service Local setup instructions

## Prerequisites
- Docker
- Kafka
- MySQL
- Java 17
- Maven
- Postman (for testing)
- Git
- IntelliJ IDEA (or any Java IDE)

## Steps to set up the Order Service locally

1. Clone the repository: git clone https://github.com/ShaikMerge/order-service.git
2. Start Docker compose file to set up Kafka
3. Start MySQL server and create a database named `order_db`
4. Update the `application.properties` file with your MySQL credentials and Kafka configurations
5. Build the project using Maven: mvn clean install
6. Run the Order Service application: mvn spring-boot:run
7. Use Postman to test the endpoints:
- Create Order: POST http://localhost:8081/api/v1/orders/create