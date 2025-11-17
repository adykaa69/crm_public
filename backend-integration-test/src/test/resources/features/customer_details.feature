Feature: CustomerDetails API requests

  Scenario: Get a customer document by ID
    Given a new customer document is created
    When the customer document is retrieved by ID
    Then the response should contain the customer document's details
    And the status code should be 200

  Scenario: Get all customer documents by customer ID
    Given 3 new customer documents are created
    When all customer documents are retrieved by customer ID
    Then the response should contain all customer documents' details
    And the status code should be 200

  Scenario: Create a customer document
    Given the customer document database is empty
    When a new customer document is created
    Then the response should contain the customer document's details
    And the status code should be 201

  Scenario: Delete a customer document by ID
    Given a new customer document is created
    When the created customer document is deleted
    Then the response should contain the customer document's details
    And the status code should be 200
    And the created customer document should no longer exist in the database

  Scenario: Update a customer document by ID
    Given a new customer document is created
    When the created customer document's details are updated
    Then the response should contain the updated customer document's details
    And the status code should be 200

  Scenario: Get a non-existent customer document
    When the customer document with ID "aaaaaaaa-1111-2222-4444-bbbbbbbbbbbb" is requested
    Then the response should contain the error message: "Customer details not found"
    And the status code should be 404

  Scenario: Create a customer document without a note
    When a new customer document is created without a note
    Then the response should contain the error message: "Note is required"
    And the status code should be 400