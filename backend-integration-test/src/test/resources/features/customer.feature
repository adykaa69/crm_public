Feature: Customer API testing

  Scenario: Get customer by ID
    Given I have a new customer with first name "Naruto", last name "Uzumaki", nickname "Narubaba", email "naruto.uzumaki@konoha.jp", phone number "123456789", relationship "hokage"
    When I send request to create the customer
    And I request customer with the created ID
    Then return 200 status code

  Scenario: Add a new customer
    Given I have a new customer with first name "Naruto", last name "Uzumaki", nickname "Narubaba", email "naruto.uzumaki@konoha.jp", phone number "123456789", relationship "hokage"
    When I send request to create the customer
    Then return 201 status code
    And the response should contain the customer

  Scenario: Add a new customer without relationship
    Given I have a new customer with first name "Krúbi", last name "Krr", nickname "Krumpli", email "crewbi.krr@banana.com", phone number "69420"
    When I send request to create the customer
    Then return 500 status code

  Scenario: Add a new customer with invalid email
    Given I have a new customer with first name "Krúbi", last name "Krr", nickname "Krumpli", email "crewbi.krr", phone number "69420", relationship "rapper"
    When I send request to create the customer
    Then return 500 status code

