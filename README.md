# API Automation Framework with RestAssured

A robust API automation framework built using Java, RestAssured, TestNG, and ExtentReports for comprehensive API testing.

## Features

- REST API testing with RestAssured
- Parallel test execution
- Data-driven testing
- Comprehensive reporting with ExtentReports
- Test grouping (smoke and regression)
- Custom test listeners
- Retry mechanism for flaky tests
- Support for various HTTP methods (GET, POST, PUT, DELETE)
- Authentication testing (Basic and Bearer Token)
- Request/Response logging
- Custom headers and query parameters testing
- Gzip compression testing
- Request timeout verification

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, etc.)

## Project Structure

```
API-RestAssured-Java/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── reqres/
│   │               ├── base/
│   │               │   └── BaseTest.java
│   │               ├── config/
│   │               │   └── Config.java
│   │               ├── models/
│   │               │   └── User.java
│   │               └── utils/
│   │                   ├── ApiUtils.java
│   │                   ├── RetryAnalyzer.java
│   │                   ├── TestDataProvider.java
│   │                   └── TestListener.java
│   └── test/
│       └── java/
│           └── com/
│               └── reqres/
│                   └── tests/
│                       └── HttpBinTests.java
├── reports/
│   └── extent-reports/
│       └── Test-Report-*.html
├── src/test/resources/
│   └── testdata/
├── pom.xml
├── testng.xml
└── README.md
```

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```

2. Navigate to the project directory:
   ```bash
   cd API-RestAssured-Java
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Groups
```bash
# Run smoke tests
mvn test -Dgroups=smoke

# Run regression tests
mvn test -Dgroups=regression
```

### Run Tests in Parallel
Tests are configured to run in parallel by default. The number of threads can be configured in `pom.xml`.

## Test Reports

After test execution, reports are generated in the `reports/extent-reports` directory. The report includes:
- Test execution status
- Test duration
- Test parameters
- System information
- Error logs (if any)

Each report is named with a timestamp (e.g., `Test-Report-2024.04.30.13.54.30.html`).

## Configuration

The framework can be configured through the following files:

- `Config.java`: API endpoints and test configuration
- `pom.xml`: Dependencies and build configuration
- `testng.xml`: Test suite configuration

## Adding New Tests

1. Create a new test class in `src/test/java/com/reqres/tests/`
2. Extend `BaseTest` class
3. Use `@Test` annotation with appropriate groups and description
4. Use `ApiUtils` for common API operations
5. Use `TestDataProvider` for data-driven tests

Example:
```java
@Test(description = "Test GET request", groups = {"smoke", "regression"})
public void testGetRequest() {
    Response response = ApiUtils.get(Config.GET_ENDPOINT);
    Assert.assertEquals(response.getStatusCode(), 200);
}
```

## Best Practices

1. Always add meaningful test descriptions
2. Group tests appropriately (smoke/regression)
3. Use data providers for multiple test scenarios
4. Implement proper error handling
5. Add assertions for all important response elements
6. Use logging for better debugging
7. Follow naming conventions for test methods

## Troubleshooting

1. If tests fail with 401 errors:
   - Check authentication credentials in `Config.java`
   - Verify API endpoints are correct

2. If ExtentReports are not generated:
   - Check if the `reports/extent-reports` directory exists
   - Verify write permissions
   - Check for any exceptions in the console

3. If parallel tests are failing:
   - Check thread count in `pom.xml`
   - Ensure tests are thread-safe
   - Use `ThreadLocal` for thread-specific data

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 