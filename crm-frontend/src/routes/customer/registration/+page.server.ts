import type { CustomerRegistrationRequest } from "$lib/models/customer-request";
import { fail, redirect } from "@sveltejs/kit";
import type { Actions } from "./$types";
import { register } from "$lib/utils/handle-customer";
import type { PlatformApiResponse } from "$lib/models/platform-api-response";
import type { ErrorResponse } from "$lib/models/error-response";

export const actions = {
  register: async (event) => {
    const formData = Object.fromEntries(await event.request.formData());
    const registrationRequest: CustomerRegistrationRequest = formData as CustomerRegistrationRequest;

    if (!registrationRequest.firstName && !registrationRequest.nickname) {
      return fail(422, {
        errorMessage: "A keresztnevet vagy a becenevet meg kell adni!",
        registrationRequest
      });
    }

    const res = await register(registrationRequest);
    if (res.ok) {
      throw redirect(302, "/customer");
    } else {
      const errorResponse: PlatformApiResponse<ErrorResponse> = await res.json();
      console.log("Error response:", errorResponse.data?.errorMessage);
      return fail(404, {
        errorMessage: errorResponse.data?.errorMessage,
        registrationRequest
      });
    }
  }
} satisfies Actions;
