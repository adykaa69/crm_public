import type { PageServerLoad } from "./$types";
import type { ErrorResponse } from "$lib/models/error";
import { getCustomers } from "$lib/utils/handle-customer";
import { parseCustomerResponseToCustomerDto, type CustomerRegistrationRequest } from "$lib/models/customer";
import { fail, redirect } from "@sveltejs/kit";
import type { Actions } from "./$types";
import { register } from "$lib/utils/handle-customer";
import type { PlatformApiResponse } from "$lib/models/platform-api-response";
import { nestData } from "$lib/utils/form-data-parser";

export const load: PageServerLoad = async () => {
  const response: Response = await getCustomers();

  const responseJson = await response.json();
  if (response.status !== 200) {
    return { errors: responseJson.data as ErrorResponse[] };
  }
  return { customers: responseJson.data.map(parseCustomerResponseToCustomerDto) };
};

export const actions = {
  customerregister: async ({ request }) => {
    const formData = Object.fromEntries(await request.formData());
    const registrationRequest = nestData<CustomerRegistrationRequest>(formData);

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
