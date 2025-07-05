export interface CustomerResponse {
  id: string;
  firstName: string;
  lastName: string;
  nickname: string;
  email: string;
  phoneNumber: string;
  relationship: string;
  residence: ResidenceResponse;
  createdAt: string;
  updatedAt: string;
}

export interface ResidenceResponse {
  residenceId: string;
  customerId: string;
  zipCode: string;
  streetAddress: string;
  addressLine2: string;
  city: string;
  country: string;
  createdAt: string;
  updatedAt: string;
}
