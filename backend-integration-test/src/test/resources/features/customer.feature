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
