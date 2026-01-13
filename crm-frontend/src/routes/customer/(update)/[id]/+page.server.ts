import type { CustomerDetailsRequest, CustomerUpdateRequest } from "$lib/models/customer";
import { type ErrorResponse } from "$lib/models/error";
import { type PlatformApiResponse } from "$lib/models/platform-api-response";
import { getCustomer, updateCustomer } from "$lib/utils/handle-customer";
import { type Actions, error, fail, redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { nestData } from "$lib/utils/form-data-parser";
import {
  parseCustomerDetailsResponseToCustomerDetailsDto,
  parseCustomerResponseToCustomerDto,
  type CustomerDetailsDto
} from "$lib/models/customer";
import { getAllCustomerDetails, saveCustomerDetails } from "$lib/utils/handle-customer-details";
import type { CustomerDetailsResponse } from "$lib/models/customer";

export const load: PageServerLoad = async ({ params }) => {
  const customerResponse: Response = await getCustomer(params.id);
  const customerDetailsResponse: Response = await getAllCustomerDetails(params.id);

  const customerJson = await customerResponse.json();
  const customerDetailsJson = await customerDetailsResponse.json();

  if (customerResponse.status !== 200) {
    return { errors: customerJson.data as ErrorResponse[] };
  }

  if (customerDetailsResponse.status !== 200) {
    return { errors: customerDetailsJson.data as ErrorResponse[] };
  }
  return {
    customer: parseCustomerResponseToCustomerDto(customerJson.content),
    customerDetails: customerDetailsJson.content.map(parseCustomerDetailsResponseToCustomerDetailsDto)
  };
};

export const actions = {
  customerupdate: async ({ request, params }) => {
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
        errorMessage: errorResponse.content?.errorMessage,
        updateRequest
      });
    }
  },
  addnote: async ({ request, params }) => {
    const formData = Object.fromEntries(await request.formData());
    const updateRequest = nestData<CustomerDetailsRequest>(formData);
    const customerId = typeof params.id === "string" ? params.id : "";
    const response = await saveCustomerDetails(customerId, updateRequest);

    if (response.ok) {
      throw redirect(302, `/customer/${customerId}`);
    } else if (response.status === 500) {
      await getErrorResponse(response);
      throw error(500, "Az ügyfél frissítése sikertelen szerver hiba miatt.");
    } else {
      const errorResponse: PlatformApiResponse<ErrorResponse> = await getErrorResponse(response);
      return fail(404, {
        errorMessage: errorResponse.content?.errorMessage,
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
