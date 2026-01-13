package hu.bhr.crm.context;

import hu.bhr.crm.step_definition.dto.TaskResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    public void addCreatedTaskId(String createdTaskId) {
        this.createdTaskIds.add(createdTaskId);
    }
}