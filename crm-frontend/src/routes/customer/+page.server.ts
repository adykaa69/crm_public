import type { PageServerLoad } from "./$types";
import type { CustomerResponse } from "$lib/models/customer-response";
import type { ErrorResponse } from "$lib/models/error-response";
import { getCustomers } from "$lib/utils/handle-customer";

export const load: PageServerLoad = async () => {
  const response = await getCustomers();

  const data = await response.json();
  if (response.status !== 200) {
    return { errors: data.data as ErrorResponse[] };
  }
  return { customers: data.data as CustomerResponse[] };
};
