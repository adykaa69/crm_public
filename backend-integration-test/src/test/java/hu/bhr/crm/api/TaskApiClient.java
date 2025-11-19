package hu.bhr.crm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hu.bhr.crm.Constants;
import hu.bhr.crm.HttpRequestFactory;
import hu.bhr.crm.step_definition.dto.TaskRequest;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TaskApiClient {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String TASK_PATH = "/api/v1/tasks";
    private static final String TASK_BY_ID_PATH = TASK_PATH + "/%s";
    private static final String TASK_BY_CUSTOMER_ID_PATH = TASK_PATH + "/%s/tasks";

    public HttpResponse<String> createTask(TaskRequest request) throws Exception {
        String body = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequestFactory.createPost(Constants.SERVICE_URL + TASK_PATH, body);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getTaskById(String id) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(
                Constants.SERVICE_URL + String.format(TASK_BY_ID_PATH, id));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getAllTasks() throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(Constants.SERVICE_URL + TASK_PATH);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getAllTasksByCustomerId(String customerId) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createGet(
                Constants.SERVICE_URL + String.format(TASK_BY_CUSTOMER_ID_PATH, customerId));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteTask(String id) throws Exception {
        HttpRequest httpRequest = HttpRequestFactory.createDelete(
                Constants.SERVICE_URL + String.format(TASK_BY_ID_PATH, id));
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> updateTask(String id, TaskRequest request) throws Exception {
        String body = objectMapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequestFactory.createPut(
                Constants.SERVICE_URL + String.format(TASK_BY_ID_PATH, id), body);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}