export type CustomerRegistrationRequest = {
    firstName: string;
    lastName: string;
    nickname: string;
    email: string;
    phoneNumber: string;
    relationship: string;
}

export type CustomerUpdateRequest = {
    customerId: string;
    firstName: string;
    lastName: string;
    nickname: string;
    email: string;
    phoneNumber: string;
    relationship: string;
}