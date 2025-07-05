export interface CustomerDetailsRequest {
  note: string;
}

export interface CustomerDetailsResponse {
  id: string;
  customerId: string;
  note: string;
  createdAt: string;
  updatedAt: string;
}
