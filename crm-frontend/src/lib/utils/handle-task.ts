import { httpDelete, httpGet, httpPost, httpPut } from "$lib/common/api";
import type { TaskRequest, TaskUpdateRequest } from "$lib/models/task";

export async function getTask(taskId: string) {
  return httpGet(`/api/v1/tasks/${taskId}`);
}

export async function getTasks() {
  return httpGet(`/api/v1/tasks`);
}

export async function registerTask(task: TaskRequest) {
  return httpPost("/api/v1/tasks", task);
}

export async function updateTask(task: TaskUpdateRequest) {
  return httpPut(`/api/v1/tasks/${task.id}`, task);
}

export async function deleteTask(taskId: string) {
  return httpDelete(`/api/v1/tasks/${taskId}`);
}
