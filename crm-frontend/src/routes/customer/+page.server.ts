import type { PageServerLoad } from "./$types";
import type { ErrorResponse } from "$lib/models/error";
import { getCustomers } from "$lib/utils/handle-customer";
import { parseCustomerResponseToCustomerDto } from "$lib/models/customer";

export const load: PageServerLoad = async () => {
  const response: Response = await getCustomers();

  const responseJson = await response.json();
  if (response.status !== 200) {
    return { errors: responseJson.data as ErrorResponse[] };
  }
  return { customers: responseJson.data.map(parseCustomerResponseToCustomerDto) };
};
