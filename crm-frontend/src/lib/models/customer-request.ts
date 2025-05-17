export interface CustomerRegistrationRequest {
  firstName?: string;
  lastName?: string;
  nickname?: string;
  email?: string;
  phoneNumber?: string;
  relationship?: string;
  residence?: ResidenceRegistrationRequest;
}

export interface CustomerUpdateRequest {
  customerId?: string;
  firstName?: string;
  lastName?: string;
  nickname?: string;
  email?: string;
  phoneNumber?: string;
  relationship?: string;
}

export interface ResidenceRegistrationRequest {
  zipCode?: string;
  streetAddress?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
}

export interface ResidenceUpdateRequest {
  residenceId?: string;
  customerId?: string;
  zipCode?: string;
  streetAddress?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
}
