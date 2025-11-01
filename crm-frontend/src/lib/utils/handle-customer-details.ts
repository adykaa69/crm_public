import { httpDelete, httpGet, httpPost, httpPut } from "$lib/common/api";
import type { CustomerDetailsRequest } from "$lib/models/customer-details";

const basePath: string = "/api/v1/customers";
const detailsPath: string = "/details";

export async function getCustomerDetails(customerDetailsId: string) {
  return httpGet(`${basePath}${detailsPath}${customerDetailsId}`);
}

export async function getAllCustomerDetails(customerId: string) {
  return httpGet(`${basePath}/${customerId}${detailsPath}`);
}

export async function saveCustomerDetails(customerId: string, customerDetails: CustomerDetailsRequest) {
  return httpPost(`${basePath}/${customerId}${detailsPath}`, customerDetails);
}

export async function updateCustomerDetails(customerDetailsId: string, customerDetails: CustomerDetailsRequest) {
  return httpPut(`${basePath}${detailsPath}${customerDetailsId}`, customerDetails);
}

export async function deleteCustomerDetails(customerDetailsId: string) {
  return httpDelete(`${basePath}${detailsPath}${customerDetailsId}`);
}
