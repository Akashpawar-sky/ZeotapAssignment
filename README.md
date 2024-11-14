
# Rule Engine Application

## Overview

This project is a simple 3-tier rule engine application designed to determine user eligibility based on attributes like age, department, income, spend, etc. The application leverages an Abstract Syntax Tree (AST) to represent conditional rules, allowing for dynamic creation, combination, and modification of these rules.

## Project Structure

```plaintext
rule_engine/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── ruleengine/
│   │   │               ├── RuleEngineApplication.java     # Main application file
│   │   │               ├── controller/
│   │   │               │   └── RuleController.java        # REST API controller
│   │   │               ├── model/
│   │   │               │   └── Node.java
|   |   |               |    |---RuleRequest               # Model for AST nodes
│   │   │               ├── repository/
│   │   │               │   └── NodeRepository.java    # Repository for database operations
|   |    |              |---Security
|   |    |              |    |___Corsconfig.java
|   |    |              |    |___SecurityConfig.java
|   |   |               |
│   │   │               └── service/
│   │   │                   └── RuleEngineService.java           # Core logic for rule creation, combination, and evaluation
│   │   └── resources/
│   │       ├── application.properties                     # Configuration properties
│   │       └── static/                                    # Static files (if any)
│   │       └── templates/                                 # HTML templates (if any)
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── ruleengine/
│                       └── RuleEngineApplicationTests.java # Unit tests
├── pom.xml                                                # Maven configuration file
└── README.md                                              # Project description and user manual
```

## Features

- **Rule Creation**: Generate individual rules from a given string format and represent them as an AST.
- **Rule Combination**: Combine multiple rules into a single AST to optimize processing and minimize redundant checks.
- **Rule Evaluation**: Evaluate the combined rule AST against user data to determine eligibility based on specified conditions.

## Data Structure

The Abstract Syntax Tree (AST) structure uses a `Node` class, with the following attributes:

- `type`: String indicating the node type ("operator" for AND/OR, "operand" for conditions).
- `left`: Reference to another `Node` (left child).
- `right`: Reference to another `Node` (right child for operators).
- `value`: Optional value for operand nodes (e.g., age, department, salary).

## Database

The application uses a relational database (e.g., PostgreSQL, MySQL) to store rules and application metadata. Here’s an example schema:

- **Rule**:
  - `id` (Primary Key)
  - `rule_expression` (String)
  - `ast_structure` (JSON/BLOB to represent the AST structure)

Sample rules in the AST format:
- Rule1: `"((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)"`
- Rule2: `"((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)"`

## API Design

### Endpoints

1. **Create Rule**  
   - **Endpoint**: `/api/rules/create`
   - **Method**: `POST`
   - **Description**: Parses a rule string and converts it into an AST `Node` representation.
   - **Input**: JSON `{ "ruleString": "<rule_string>" }`
   - **Output**: AST representation in JSON format.

2. **Combine Rules**  
   - **Endpoint**: `/api/rules/combine`
   - **Method**: `POST`
   - **Description**: Takes a list of rule strings, combines them into a single AST, and returns the root node.
   - **Input**: JSON `{"rules": ["<rule_string1>", "<rule_string2>", ...]}`
   - **Output**: Combined AST root node in JSON format.

3. **Evaluate Rule**  
   - **Endpoint**: `/api/rules/evaluate`
   - **Method**: `POST`
   - **Description**: Evaluates the AST against a user data JSON and returns a boolean result.
   - **Input**: JSON `{"data": {"age": 35, "department": "Sales", "salary": 60000, "experience": 3}}`
   - **Output**: `true` or `false` based on eligibility.

### Sample Input/Output

1. **Create Rule**
   ```json
   POST /api/rules/create
   {
     "ruleString": "age > 30 AND department = 'Sales'"
   }
   ```
   **Response**:
   ```json
   {
     "type": "operator",
     "left": { "type": "operand", "value": "age > 30" },
     "right": { "type": "operand", "value": "department = 'Sales'" }
   }
   ```

2. **Combine Rules**
   ```json
   POST /api/rules/combine
   {
     "rules": ["age > 30 AND department = 'Sales'", "salary > 50000 OR experience > 5"]
   }
   ```
   **Response**:
   ```json
   {
     "type": "operator",
     "left": { ... },
     "right": { ... }
   }
   ```

3. **Evaluate Rule**
   ```json
   POST /api/rules/evaluate
   {
     "data": {
       "age": 35,
       "department": "Sales",
       "salary": 60000,
       "experience": 3
     }
   }
   ```
   **Response**:
   ```json
   true
   ```

## Getting Started

### Prerequisites

- Java JDK 11 or above
- Maven 3.6+
- PostgreSQL/MySQL (or other supported RDBMS)

### Installation

1. Clone the repository:
   ```bash
   git clone https....
   cd folder
   ```

2. Update `application.properties` with your database configuration.

3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Running Tests

To run the tests:

mvn test


## User Guide

1. **Creating Rules**: Use `/api/rules/create` endpoint with a rule string.
2. **Combining Rules**: Use `/api/rules/combine` endpoint with multiple rules.
3. **Evaluating Rules**: Use `/api/rules/evaluate` with user data to check eligibility.

## Error Handling

- **Invalid Rule Strings**: The API returns an error if the rule string format is incorrect.
- **Unsupported Data Types**: The evaluation will fail if data attributes don’t match required types (e.g., `age` should be numeric).

## Future Enhancements

- Add support for user-defined functions in rule expressions.
- Allow modification of rules within existing ASTs.
- Implement caching for frequently used rules for performance optimization.

