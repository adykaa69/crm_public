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
  residence?: ResidenceUpdateRequest;
}

export interface ResidenceRegistrationRequest {
  zipCode?: string;
  streetAddress?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
}

export interface ResidenceUpdateRequest {
  id?: string;
  zipCode?: string;
  streetAddress?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
}

export interface CustomerDetailsRequest {
  note: string;
}

export interface CustomerResponse {
  id: string;
  firstName: string;
  lastName: string;
  nickname: string;
  email: string;
  phoneNumber: string;
  relationship: string;
  residence: ResidenceResponse;
  createdAt: Date;
  updatedAt: Date;
}

export interface ResidenceResponse {
  residenceId: string;
  zipCode?: string;
  streetAddress?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface CustomerDetailsResponse {
  id: string;
  customerId: string;
  note: string;
  createdAt: string;
  updatedAt: string;
}

export interface CustomerDto {
  id: string;
  firstName?: string;
  lastName?: string;
  nickname?: string;
  email?: string;
  phoneNumber?: string;
  relationship?: string;
  residence?: ResidenceDto;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface ResidenceDto {
  residenceId?: string;
  zipCode?: string;
  streetAddress?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface CustomerDetailsDto {
  id: string;
  customerId: string;
  note: string;
  createdAt?: Date;
  updatedAt?: Date;
}

const taskDto: ResidenceDto = {
  residenceId: undefined,
  zipCode: undefined,
  streetAddress: undefined,
  addressLine2: undefined,
  city: undefined,
  country: undefined,
  createdAt: undefined,
  updatedAt: undefined
};

const taskDtoKeys = Object.keys(taskDto) as Array<keyof ResidenceDto>;

export function isKeyOfTask(key: string): key is keyof CustomerDto {
  return taskDtoKeys.includes(key as keyof ResidenceDto);
}

export function parseCustomerResponseToCustomerDto(raw: any): CustomerDto {
  return {
    id: raw.id,
    firstName: raw.firstName,
    lastName: raw.lastName,
    nickname: raw.nickname,
    email: raw.email,
    phoneNumber: raw.phoneNumber,
    relationship: raw.relationship,
    residence: parseResidenceResponseToResidenceDto(raw.residence),
    createdAt: new Date(raw.createdAt),
    updatedAt: new Date(raw.updatedAt)
  };
}

export function parseCustomerDetailsResponseToCustomerDetailsDto(raw: any): CustomerDetailsDto {
  return {
    id: raw.id,
    customerId: raw.customerId,
    note: raw.note,
    createdAt: new Date(raw.createdAt),
    updatedAt: new Date(raw.updatedAt)
  };
}

export function toCustomerDetailsRequest(customerDetailsDto: CustomerDetailsDto): CustomerDetailsRequest {
  return {
    note: customerDetailsDto.note
  }
}

export function toCustomerRegistrationRequest(customerDto: CustomerDto): CustomerRegistrationRequest {
  return {
    firstName: customerDto.firstName,
    lastName: customerDto.lastName,
    nickname: customerDto.nickname,
    email: customerDto.email,
    phoneNumber: customerDto.phoneNumber,
    relationship: customerDto.relationship,
    residence: toResidenceRegistraionRequest(customerDto.residence)
  };
}

export function toCustomerUpdateRequest(customerDto: CustomerDto): CustomerUpdateRequest {
  return {
    customerId: customerDto.id,
    firstName: customerDto.firstName,
    lastName: customerDto.lastName,
    nickname: customerDto.nickname,
    email: customerDto.email,
    phoneNumber: customerDto.phoneNumber,
    relationship: customerDto.relationship,
    residence: toResidenceUpdateRequest(customerDto.residence)
  };
}

export function parseResidenceResponseToResidenceDto(raw: any): ResidenceDto | undefined {
  if (raw) {
    return {
      residenceId: raw.id,
      zipCode: raw.zipCode,
      streetAddress: raw.streetAddress,
      addressLine2: raw.addressLine2,
      city: raw.city,
      country: raw.country,
      createdAt: new Date(raw.createdAt),
      updatedAt: new Date(raw.updatedAt)
    };
  }
}

export function toResidenceRegistraionRequest(residenceDto?: ResidenceDto): ResidenceRegistrationRequest {
  return {
    zipCode: residenceDto?.zipCode,
    streetAddress: residenceDto?.streetAddress,
    addressLine2: residenceDto?.streetAddress,
    city: residenceDto?.city,
    country: residenceDto?.country
  };
}

export function toResidenceUpdateRequest(residenceDto?: ResidenceDto): ResidenceUpdateRequest {
  return {
    id: residenceDto?.residenceId,
    zipCode: residenceDto?.zipCode,
    streetAddress: residenceDto?.streetAddress,
    addressLine2: residenceDto?.addressLine2,
    city: residenceDto?.city,
    country: residenceDto?.country
  };
}
