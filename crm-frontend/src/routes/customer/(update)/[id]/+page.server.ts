import type { CustomerUpdateRequest } from "$lib/models/customer-request";
import type { CustomerResponse } from "$lib/models/customer-response";
import { type ErrorResponse } from "$lib/models/error-response";
import { type PlatformApiResponse } from "$lib/models/platform-api-response";
import { getCustomer, updateCustomer } from "$lib/utils/handle-customer";
import { type Actions, fail, redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ params }) => {
  const response = await getCustomer(params.id);

  const data = await response.json();
  if (response.status !== 200) {
    return data as PlatformApiResponse<ErrorResponse[]>;
  }
  return data as PlatformApiResponse<CustomerResponse>;
};

export const actions = {
  update: async ({ request, params }) => {
    const formData = Object.fromEntries(await request.formData());
    const updateRequest: CustomerUpdateRequest = formData as CustomerUpdateRequest;
    updateRequest.customerId = typeof params.id === "string" ? params.id : "";

    if (!updateRequest.firstName && !updateRequest.nickname) {
      return fail(422, {
        errorMessage: "A keresztnevet vagy a becenevet meg kell adni!",
        updateRequest
      });
    }

    const response = await updateCustomer(updateRequest);
    if (response.ok) {
      throw redirect(302, "/customer");
    } else {
      const errorResponse: PlatformApiResponse<ErrorResponse> = await response.json();
      console.log("Error response:", errorResponse.data?.errorMessage);
      return fail(404, {
        errorMessage: errorResponse.data?.errorMessage,
        updateRequest
      });
    }
  }
} satisfies Actions;
