package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.TaskResponse;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.repository.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface TaskMapper {

    Task taskEntityToTask(TaskEntity taskEntity);

    TaskEntity taskToTaskEntity(Task task);

    @Mapping(source = "customer.id", target = "customerId")
    TaskResponse taskToTaskResponse(Task task);
}
