Feature:
  Scenario: Add customer successfully
    Given a customer
    When add to database
    Then return "201" status code