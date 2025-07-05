import type { CustomerUpdateRequest } from "$lib/models/customer-request";
import type { CustomerResponse } from "$lib/models/customer-response";
import { type ErrorResponse } from "$lib/models/error-response";
import { type PlatformApiResponse } from "$lib/models/platform-api-response";
import { getCustomer, updateCustomer } from "$lib/utils/handle-customer";
import { type Actions, error, fail, redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { nestData } from "$lib/utils/form-data-parser";
import { getAllCustomerDetails, getCustomerDetails } from "$lib/utils/handle-customer-details";
import type { CustomerDetailsResponse } from "$lib/models/customer-details";

export const load: PageServerLoad = async ({ params }) => {
  const customerResponse = await getCustomer(params.id);
  const customerDetailsResponse = await getAllCustomerDetails(params.id);

  const customerJson = await customerResponse.json();
  const customerDetailsJson = await customerDetailsResponse.json();
  if (customerResponse.status !== 200) {
    const errorResponse = customerJson as PlatformApiResponse<ErrorResponse>;
    return customerJson as PlatformApiResponse<ErrorResponse>;
  } else if (customerDetailsResponse.status !== 200) {
    return customerDetailsJson as PlatformApiResponse<ErrorResponse>;
  }
  const customerData = customerJson as PlatformApiResponse<CustomerResponse>;
  const customerDetailsData = customerDetailsJson as PlatformApiResponse<CustomerDetailsResponse[]>;
  return {
    customer: customerData.data,
    customerDetails: customerDetailsData.data
  };
};

export const actions = {
  update: async ({ request, params }) => {
    const formData = Object.fromEntries(await request.formData());
    const updateRequest = nestData<CustomerUpdateRequest>(formData);
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
    } else if (response.status === 500) {
      await getErrorResponse(response);
      throw error(500, "Az ügyfél frissítése sikertelen szerver hiba miatt.");
    } else {
      const errorResponse: PlatformApiResponse<ErrorResponse> = await getErrorResponse(response);
      return fail(404, {
        errorMessage: errorResponse.data?.errorMessage,
        updateRequest
      });
    }
  }
} satisfies Actions;

async function getErrorResponse(response: Response): Promise<PlatformApiResponse<ErrorResponse>> {
  const errorResponse: PlatformApiResponse<ErrorResponse> = await response.json();
  console.log("Error occurred while updating customer. Response:");
  console.log(errorResponse);
  return errorResponse;
}
