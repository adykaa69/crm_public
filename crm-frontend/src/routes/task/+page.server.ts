import type { ErrorResponse } from "$lib/models/error";
import { parseTaskResponse } from "$lib/models/task";
import { getTasks } from "$lib/utils/handle-task";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async () => {
  const response: Response = await getTasks();

  const responseJson = await response.json();
  if (response.status !== 200) {
    return { errors: responseJson.data as ErrorResponse[] };
  }
  return { tasks: responseJson.data.map(parseTaskResponse) };
};
