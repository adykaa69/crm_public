import { httpPost } from "$lib/common/api";
import type { CustomerRequest } from "$lib/model/customer-request";

export async function register(user: CustomerRequest) {
    return httpPost('/api/v1/customers', user);
}