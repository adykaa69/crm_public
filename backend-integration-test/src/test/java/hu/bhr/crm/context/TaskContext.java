package hu.bhr.crm.context;

import hu.bhr.crm.step_definition.dto.TaskResponse;

import java.util.ArrayList;
import java.util.List;

public class TaskContext extends BaseContext {

    private String createdTaskId;
    private final List<String> createdTaskIds = new ArrayList<>();
    private TaskResponse lastTaskResponse;
    private String customerId;

    @Override
    protected void clear() {
        createdTaskId = null;
        createdTaskIds.clear();
        lastTaskResponse = null;
        lastResponse = null;
        customerId = null;
    }

    public String getCreatedTaskId() {
        return createdTaskId;
    }

    public void setCreatedTaskId(String createdTaskId) {
        this.createdTaskId = createdTaskId;
    }

    public List<String> getCreatedTaskIds() {
        return createdTaskIds;
    }

    public void addCreatedTaskId(String createdTaskId) {
        this.createdTaskIds.add(createdTaskId);
    }

    public TaskResponse getLastTaskResponse() {
        return lastTaskResponse;
    }

    public void setLastTaskResponse(TaskResponse lastTaskResponse) {
        this.lastTaskResponse = lastTaskResponse;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}