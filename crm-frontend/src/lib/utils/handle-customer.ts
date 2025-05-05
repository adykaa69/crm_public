import { httpDelete, httpGet, httpPost, httpPut } from "$lib/common/api";
import type { CustomerRegistrationRequest, CustomerUpdateRequest } from "$lib/model/customer-request";

export async function getCustomer(customerId: string) {
    return httpGet(`/api/v1/customers/${customerId}`);
}

export async function getCustomers() {
    return httpGet(`/api/v1/customers`);
}

export async function register(customer: CustomerRegistrationRequest) {
    return httpPost('/api/v1/customers', customer);
}

export async function updateCustomer(customer: CustomerUpdateRequest) {
    return httpPut(`/api/v1/customers/${customer.customerId}`, customer);
}

export async function deleteCustomer(customerId: string) {
    return httpDelete(`/api/v1/customers/${customerId}`);
}
