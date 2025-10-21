import type { ErrorResponse } from "$lib/models/error-response";
import { parseTaskResponse, type TaskResponse } from "$lib/models/task";
import { getTasks } from "$lib/utils/handle-task";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async () => {
  const response = await getTasks();

  const data = await response.json();
  if (response.status !== 200) {
    return { errors: data.data as ErrorResponse[] };
  }
  return { tasks: data.data.map(parseTaskResponse) as TaskResponse[] };
};
