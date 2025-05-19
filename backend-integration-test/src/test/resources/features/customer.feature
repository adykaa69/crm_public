Feature: Customer API requests

  Scenario: Get a customer by ID
    Given a new customer is created
    When the customer is retrieved by ID
    Then the response should contain the customer's details
    And the status code should be 200

  Scenario: Get all customers
    Given 3 new customers are created
    When all customers are retrieved
    Then the response should contain all customer's details
    And the status code should be 200

  Scenario: Create a customer
    Given the customer database is empty
    When a new customer is created
    Then the response should contain the customer's details
    And the status code should be 201

  Scenario: Delete a customer by ID
    Given a new customer is created
    When the created customer is deleted
    Then the response should contain the customer's details
    And the status code should be 200
    And the created customer should no longer exist in the database

  Scenario: Update a customer by ID
    Given a new customer is created
    When the created customer's details are updated
    Then the response should contain the updated customer's details
    And the status code should be 200

  Scenario: Get a non-existent customer
    When the customer with ID "aaaaaaaa-1111-2222-4444-bbbbbbbbbbbb" is requested
    Then the response should contain the error message: "Customer not found"
    And the status code should be 404

  Scenario: Create a customer without relationship
    When a new customer is created without relationship
    Then the response should contain the error message: "Relationship is required."
    And the status code should be 400

  Scenario: Create a customer without first name and nickname
    When a new customer is created without first name and nickname
    Then the response should contain the error message: "At least one of First Name or Nickname is required."
    And the status code should be 400
    
  Scenario: Create a customer with invalid email
    When a new customer is created with invalid email
    Then the response should contain the error message: "Invalid email format"
    And the status code should be 400

  Scenario: Delete a non-existent customer
    When the customer with ID "aaaaaaaa-1111-2222-4444-bbbbbbbbbbbb" is requested to be deleted
    Then the response should contain the error message: "Customer not found"
    And the status code should be 404

  Scenario: Update a customer with no relationship set
    Given a new customer is created
    When the created customer's details are updated without relationship
    Then the response should contain the error message: "Relationship is required."
    And the status code should be 400

  Scenario: Update a customer with no first name and nickname set
    Given a new customer is created
    When the created customer's details are updated without first name and nickname
    Then the response should contain the error message: "At least one of First Name or Nickname is required."
    And the status code should be 400

  Scenario: Update a customer with an invalid email
    Given a new customer is created
    When the created customer's details are updated with an invalid email
    Then the response should contain the error message: "Invalid email format"
    And the status code should be 400