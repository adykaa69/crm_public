import type { ErrorResponse } from "$lib/models/error";
import type { TaskDto, TaskUpdateRequest } from "$lib/models/task";
import { updateTask } from "$lib/utils/handle-task";
import { json, type RequestHandler } from "@sveltejs/kit";

export const PUT: RequestHandler = async ({ params, request }) => {
  const { id } = params;
  const body = (await request.json()) as TaskUpdateRequest;

  const response = await updateTask(body);
  const data = await response.json();

  if (response.status !== 200) {
    return json({ errors: data.data as ErrorResponse });
  }
  return json({ task: data.data as TaskDto });
};
