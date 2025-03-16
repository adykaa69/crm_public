import type { PageServerLoad } from "./$types";
import type { PlatformApiResponse } from "$lib/model/platform-api-response";
import type { CustomerResponse } from "$lib/model/customer-response";

export const load: PageServerLoad = () => {
    // const response = await fetch("http://localhost:8080/api/v1/customer");
    // const data = await response.json();

    const response: PlatformApiResponse<CustomerResponse[]> = {
        "status": "success",
        "message": "All Customers retrieved successfully",
        "data": [
            {
                "id": "1",
                "firstName": "Adam",
                "lastName": "Kósa",
                "email": "foo@gmail.com",
                "phoneNumber": "+36701236969",
                "relationship": "cousin",
                "createdAt": "2022-01-01T00:00:00.000Z",
                "updatedAt": "2022-01-01T00:00:00.000Z"
            },
            {
                "id": "2",
                "firstName": "Tamás",
                "lastName": "Bihari",
                "email": "bar@gmail.com",
                "phoneNumber": "+36201234567",
                "relationship": "cousin",
                "createdAt": "2025-03-13T21:22:00.000Z",
                "updatedAt": "2025-03-13T21:23:00.000Z"
            }
        ]
    };
    return response;
}