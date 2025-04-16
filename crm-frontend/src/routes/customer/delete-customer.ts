import { httpDelete } from "$lib/common/api";
import type { CustomerRequest } from "$lib/model/customer-request";

export async function deleteCustomer(customerId: string) {
    return httpDelete(`/api/v1/customers/${customerId}`);
}