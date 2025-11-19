Feature: Task API requests

  Scenario: Get a task by ID
    Given a new task is created
    When the task is retrieved by ID
    Then the response should contain the task's details
    And the status code should be 200

  Scenario: Get all tasks
    Given 3 new tasks are created
    When all tasks are retrieved
    Then the response should contain all tasks' details
    And the status code should be 200

  Scenario: Get all tasks by customer ID
    Given 3 new tasks are created
    When all tasks are retrieved by customer ID
    Then the response should contain all tasks' details
    And the status code should be 200

  Scenario: Create a task
    Given the task database is empty
    When a new task is created
    Then the response should contain the task's details
    And the status code should be 201

  Scenario: Delete a task by ID
    Given a new task is created
    When the created task is deleted
    Then the response should contain the task's details
    And the status code should be 200
    And the created task should no longer exist in the database

  Scenario: Update a task by ID
    Given a new task is created
    When the created task's details are updated
    Then the response should contain the updated task's details
    And the status code should be 200

  Scenario: Get a non-existent task
    When the task with ID "aaaaaaaa-1111-2222-4444-bbbbbbbbbbbb" is requested
    Then the response should contain the error message: "Task not found"
    And the status code should be 404

  Scenario: Create a task with no title
    When a new task is created with no title
    Then the response should contain the error message: "Title is required"
    And the status code should be 400