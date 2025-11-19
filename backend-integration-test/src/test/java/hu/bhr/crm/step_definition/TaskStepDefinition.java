package hu.bhr.crm.step_definition;

import com.fasterxml.jackson.core.type.TypeReference;
import hu.bhr.crm.api.ApiResponseParser;
import hu.bhr.crm.api.CustomerApiClient;
import hu.bhr.crm.api.TaskApiClient;
import hu.bhr.crm.assertions.HttpAssertions;
import hu.bhr.crm.context.GlobalTestContext;
import hu.bhr.crm.context.TaskContext;
import hu.bhr.crm.step_definition.dto.CustomerRequest;
import hu.bhr.crm.step_definition.dto.CustomerResponse;
import hu.bhr.crm.step_definition.dto.PlatformResponse;
import hu.bhr.crm.step_definition.dto.TaskRequest;
import hu.bhr.crm.step_definition.dto.TaskResponse;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;

public class TaskStepDefinition {

    private final TaskContext taskContext;
    private final GlobalTestContext globalTestContext;
    private final TaskApiClient taskApiClient = new TaskApiClient();
    private final CustomerApiClient customerApiClient = new CustomerApiClient();

    public TaskStepDefinition(TaskContext taskContext, GlobalTestContext globalTestContext) {
        this.taskContext = taskContext;
        this.globalTestContext = globalTestContext;
    }

    @Given("a new task is created")
    public void aNewTaskIsCreated() throws Exception {
        createTask(1);
    }

    @Given("{int} new tasks are created")
    public void newTasksAreCreated(int numberOfTasks) throws Exception {
        for (int i = 1; i <= numberOfTasks; i++) {
            createTask(i);
        }
    }

    @When("the task is retrieved by ID")
    public void theTaskIsRetrievedById() throws Exception {
        taskContext.setLastResponse(
                taskApiClient.getTaskById(taskContext.getCreatedTaskId())
        );
    }

    @When("all tasks are retrieved")
    public void allTasksAreRetrieved() throws Exception {
        taskContext.setLastResponse(taskApiClient.getAllTasks());
    }

    @When("all tasks are retrieved by customer ID")
    public void allTasksAreRetrievedByTheCustomerId() throws Exception {
        taskContext.setLastResponse(
                taskApiClient.getAllTasksByCustomerId(taskContext.getCustomerId())
        );
    }

    @When("the created task is deleted")
    public void theCreatedTaskIsDeleted() throws Exception {
        HttpResponse<String> response = taskApiClient.deleteTask(taskContext.getCreatedTaskId());
        taskContext.setLastResponse(response);
        taskContext.getCreatedTaskIds().remove(taskContext.getCreatedTaskId());
    }

    @Then("the task database is empty")
    public void theTaskDatabaseIsEmpty() {
        Assertions.assertThat(taskContext.getCreatedTaskIds()).isEmpty();
    }

    @When("the created task's details are updated")
    public void theCreatedTaskDetailsAreUpdated() throws Exception {
        TaskRequest updatedRequest = new TaskRequest(
                null,
                "task_updatedTitle",
                "task_updatedDescription",
                ZonedDateTime.now().plusDays(5),
                ZonedDateTime.now().plusDays(10),
                "COMPLETED"
        );
        HttpResponse<String> response = taskApiClient.updateTask(
                taskContext.getCreatedTaskId(),
                updatedRequest
        );
        taskContext.setLastResponse(response);

        if (response.statusCode() == 200) {
            PlatformResponse<TaskResponse> resultResponse =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {
                    });
            taskContext.setLastTaskResponse(resultResponse.data());
        }
    }

    @Then("the response should contain the updated task's details")
    public void theResponseShouldContainTheUpdatedTaskDetails() throws Exception {
        theResponseShouldContainTheTaskDetails();
    }

    @When("the task with ID {string} is requested")
    public void theTaskWithNonExistentIdIsRequested(String taskId) throws Exception {
        taskContext.setLastResponse(taskApiClient.getTaskById(taskId));
    }

    @Then("the response should contain the task's details")
    public void theResponseShouldContainTheTaskDetails() throws Exception {
        PlatformResponse<TaskResponse> resultResponse =
                ApiResponseParser.parseResponse(taskContext.getLastResponse(), new TypeReference<>() {
                });
        TaskResponse expectedResponse = taskContext.getLastTaskResponse();

        Assertions.assertThat(resultResponse.data())
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "completedAt", "reminder", "dueDate")
                .isEqualTo(expectedResponse);
    }

    @Then("the response should contain all tasks' details")
    public void theResponseShouldContainAllTasksDetails() throws Exception {
        PlatformResponse<List<TaskResponse>> resultResponse =
                ApiResponseParser.parseResponse(taskContext.getLastResponse(), new TypeReference<>() {
                });
        List<String> expectedIds = taskContext.getCreatedTaskIds();

        for (String id : expectedIds) {
            boolean found = resultResponse.data().stream()
                    .anyMatch(t -> t.id().equals(id));
            Assertions.assertThat(found)
                    .as("Task with ID %s should be in the response", id)
                    .isTrue();
        }
    }

    @And("the created task should no longer exist in the database")
    public void theCreatedTaskShouldNoLongerExistInTheDatabase() throws Exception {
        HttpResponse<String> response = taskApiClient.getTaskById(taskContext.getCreatedTaskId());
        HttpAssertions.assertStatusCode(response, 404);
    }

    @When("a new task is created with no title")
    public void aNewTaskIsCreatedWithNoTitle() throws Exception {
        TaskRequest taskRequest = new TaskRequest(
                "",
                "",
                "",
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "OPEN"
        );
        taskContext.setLastResponse(taskApiClient.createTask(taskRequest));
    }

    private void createTask(int taskNumber) throws Exception {
        ensureRelatedCustomerExists();

        TaskRequest request = buildTaskRequest(taskNumber);
        HttpResponse<String> response = taskApiClient.createTask(request);
        taskContext.setLastResponse(response);

        if (response.statusCode() == 201) {
            PlatformResponse<TaskResponse> parsed =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {
                    });
            String taskId = parsed.data().id();

            taskContext.setCreatedTaskId(taskId);
            taskContext.addCreatedTaskId(taskId);
            taskContext.setLastTaskResponse(parsed.data());
        }
    }

    private TaskRequest buildTaskRequest(int taskNumber) {
        return new TaskRequest(
                taskContext.getCustomerId(),
                "task_title_" + taskNumber,
                "task_description_" + taskNumber,
                ZonedDateTime.now().plusDays(1),
                ZonedDateTime.now().plusDays(2),
                "OPEN"
        );
    }

    private void ensureRelatedCustomerExists() throws Exception {
        if (taskContext.getCustomerId() == null) {
            CustomerRequest request = new CustomerRequest(
                    "firstName",
                    "lastName",
                    "nickname",
                    "email@example.com",
                    "phoneNumber",
                    "relationship",
                    null
            );
            HttpResponse<String> response = customerApiClient.createCustomer(request);
            PlatformResponse<CustomerResponse> parsed =
                    ApiResponseParser.parseResponse(response, new TypeReference<>() {
                    });
            String customerId = parsed.data().id();

            taskContext.setCustomerId(customerId);
            globalTestContext.addCreatedCustomerId(customerId);
        }
    }

    @After
    public void cleanUpAfterScenario() throws Exception {
        if (!taskContext.getCreatedTaskIds().isEmpty()) {
            for (String id : taskContext.getCreatedTaskIds()) {
                taskApiClient.deleteTask(id);
            }
            globalTestContext.clear();
        }
    }
}