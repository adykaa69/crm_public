import type { PageServerLoad } from "./$types";
import type { PlatformApiResponse } from "$lib/models/platform-api-response";
import type { CustomerResponse } from "$lib/models/customer-response";
import type { ErrorResponse } from "$lib/models/error-response";
import { getCustomers } from "$lib/utils/handle-customer";

export const load: PageServerLoad = async () => {
  const response = await getCustomers();

  const data = await response.json();
  if (response.status !== 200) {
    return data as PlatformApiResponse<ErrorResponse[]>;
  }
  return data as PlatformApiResponse<CustomerResponse[]>;
};
