# Accenture assessment

This project is for the Accenture assessment that utilizes the [https://gitlab.com/restcountries/restcountries] API.

## Running the project
### Prerequisites

- Java Development Kit (JDK)
- Maven 

### 1. Build the Project

1. Open a terminal or command prompt.
2. Navigate to the root directory of the project where the `pom.xml` file is located.
3. To build the project, execute the following Maven command:

   ```bash
   mvn clean install
   ```
### 2. Run the application
1. Navigate to the directory containing the JAR file in `target` using the terminal.
2. Run the application using the following command:

    ```bash
   java -jar Assessment-1.0.0.jar
   ```
3. The application is now running.

## Using the application.
The application consist of two endpoints.

### List countries by population density
Navigate to `localhost:8080/country/density`.

### List countries in a region with the most neighbouring countries in a different region
Navigate to `localhost:8080/country/neighbours?region={region}`.
Substitute `{region}` with one of the following:
1. `Europe`
2. `Africa`
3. `Oceania`
4. `Asia`
5. `Americas`

Alternatively, when `?region` is omitted from the URL, the default region is `Asia`. 