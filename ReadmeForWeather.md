
# Weather Monitoring Application

A real-time weather monitoring application built with **Spring Boot** and **Thymeleaf**. This application fetches weather data for multiple cities from the OpenWeatherMap API, stores it in a PostgreSQL database, and displays it on a web interface.

## Features
- **Real-Time Weather Data**: Fetches and stores weather data for selected cities every 5 minutes.
- **Daily Summaries**: Supports calculating daily temperature summaries and dominant weather conditions (extendable).
- **Alerting**: Capable of setting temperature-based alerts (extendable).
- **Data Visualization**: Displays current weather data on a web dashboard.

## Technologies Used
- **Backend**: Java, Spring Boot (Spring MVC, Spring Data JPA, Spring Scheduling)
- **Frontend**: Thymeleaf (for server-side rendering)
- **Database**: PostgreSQL
- **External API**: OpenWeatherMap API for live weather data

## Prerequisites
- **Java JDK 17 or later**
- **Maven** (for dependency management)
- **PostgreSQL** (for database storage)
- **OpenWeatherMap API Key** (sign up [here](https://openweathermap.org/) to obtain a free API key)

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/weather-monitoring-app.git
cd weather-monitoring-app
```

### 2. Configure the Application
Open the `src/main/resources/application.properties` file and configure the following:

```properties
# PostgreSQL Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/weather_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# OpenWeatherMap API Key
openweathermap.api.key=your_api_key

# Thymeleaf Settings
spring.thymeleaf.cache=false
```
- Replace `your_username` and `your_password` with your PostgreSQL credentials.
- Replace `your_api_key` with your OpenWeatherMap API key.

### 3. Set Up the Database
1. Make sure PostgreSQL is installed and running.
2. Create a new database called `weather_db`:
   ```sql
   CREATE DATABASE weather_db;
   ```

### 4. Install Dependencies and Build the Project
Run the following Maven command to install dependencies and build the project:
```bash
mvn clean install
```

### 5. Run the Application
Start the Spring Boot application:
```bash
mvn spring-boot:run
```

### 6. Access the Web Application
Open a browser and go to `http://localhost:8080/weather` to view the Weather Dashboard, which will display weather data for all specified cities.

## Project Structure

```plaintext
weather-monitoring-app/
├── src/
│   ├── main/
│   │   ├── java/com/yourproject/
│   │   │   ├── controllers/          # Contains WeatherController.java
│   │   │   ├── models/               # Contains Weather.java entity class
│   │   │   ├── repositories/         # Contains WeatherRepository.java
│   │   │   ├── services/             # Contains WeatherService.java
│   │   │   └── WeatherMonitoringApplication.java  # Main application class
│   │   ├── resources/
│   │   │   ├── templates/            # Contains weather.html Thymeleaf template
│   │   │   ├── application.properties  # Application configuration
│   │   │   └── static/               # Static resources (e.g., CSS)
├── pom.xml                           # Maven configuration
└── README.md                         # Project documentation
```

## How It Works
1. **Data Fetching**: The application fetches weather data for each city every 5 minutes using the OpenWeatherMap API.
2. **Data Storage**: Weather data is stored in PostgreSQL, with each city’s data saved as a separate entry.
3. **Display**: Weather data is displayed on a Thymeleaf-based HTML page, showing current conditions for each city.
4. **Daily Summaries (Extendable)**: Optionally, you can calculate daily summaries and set alerts based on temperature thresholds.



## Testing

1. **JUnit Tests**: Add JUnit tests in `src/test/java/com/yourproject/` for the service and repository layers to verify functionality.
2. **API Testing**: Test the `/weather` endpoint using a tool like Postman to confirm data is correctly fetched and displayed.

## Troubleshooting


- **OpenWeatherMap API Key**: If data fetching fails, ensure your API key is valid and that you’re not exceeding usage limits.


## Future Enhancements
- **Daily Summaries**: Add logic to calculate daily temperature summaries and dominant weather conditions.
- **Alerting**: Implement alerting based on temperature thresholds.
- **Visualization Enhancements**: Integrate additional front-end components to display historical data trends.

