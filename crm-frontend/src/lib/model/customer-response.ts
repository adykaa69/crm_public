export type CustomerResponse = {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    relationship: string;
    createdAt: string;
    updatedAt: string;
}

export function isCustomerResponse(object: any): object is CustomerResponse {
    return (
        typeof object.id === 'string' &&
        typeof object.firstName === 'string' &&
        typeof object.lastName === 'string' &&
        typeof object.email === 'string' &&
        typeof object.phoneNumber === 'string' &&
        typeof object.relationship === 'string' &&
        typeof object.createdAt === 'string' &&
        typeof object.updatedAt === 'string'
    );
}