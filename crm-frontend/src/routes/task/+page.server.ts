import type { ErrorResponse } from "$lib/models/error";
import type { PlatformApiResponse } from "$lib/models/platform-api-response";
import {
  parseTaskDtoToTaskRequest,
  parseTaskResponseToTaskDto,
  type TaskDto,
  type TaskRequest
} from "$lib/models/task";
import { getTasks, registerTask } from "$lib/utils/handle-task";
import type { PageServerLoad } from "./$types";
import type { Actions } from "./$types";
import { fail, redirect } from "@sveltejs/kit";

export const load: PageServerLoad = async () => {
  const response: Response = await getTasks();

  const responseJson = await response.json();
  if (response.status !== 200) {
    return { errors: responseJson.data as ErrorResponse[] };
  }
  return { tasks: responseJson.data.map(parseTaskResponseToTaskDto) };
};

export const actions = {
  taskregister: async ({ cookies, request }) => {
    const taskDto: TaskDto = parseToTaskDto(await request.formData());
    const taskRequest: TaskRequest = parseTaskDtoToTaskRequest(taskDto);
    const response = await registerTask(taskRequest);
    if (response.ok) {
      throw redirect(302, "/task");
    } else {
      const errorResponse: PlatformApiResponse<ErrorResponse> = await response.json();
      console.log("Error response:", errorResponse.data?.errorMessage);
      return fail(404, {
        errorMessage: errorResponse.data?.errorMessage,
        taskRequest
      });
    }
  },
  update: async (event) => {
    // TODO register the user
  }
} satisfies Actions;

function parseToTaskDto(rawFormData: any): TaskDto {
  return {
    id: "dummy",
    title: rawFormData.get("title"),
    description: rawFormData.get("description"),
    status: rawFormData.get("status"),
    reminder: rawFormData.get("reminder"),
    dueDate: rawFormData.get("dueDate")
  };
}
