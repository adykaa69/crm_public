package hu.bhr.crm.mapper;

import hu.bhr.crm.controller.dto.TaskResponse;
import hu.bhr.crm.model.Task;
import hu.bhr.crm.repository.entity.TaskEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface TaskMapper {

    Task taskEntityToTask(TaskEntity taskEntity);

    TaskEntity taskToTaskEntity(Task task);

    TaskResponse taskToTaskResponse(Task task);
}
