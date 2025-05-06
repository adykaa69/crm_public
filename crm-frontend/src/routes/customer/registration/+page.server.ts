import type { CustomerRegistrationRequest } from '$lib/models/customer-request';
import { error, redirect, fail } from '@sveltejs/kit';
import type { Actions } from './$types';
import { register } from '$lib/utils/handle-customer';

export const actions = {
    register: async (event) => {
        const formData = Object.fromEntries(await event.request.formData());
        let registrationRequest: CustomerRegistrationRequest = formData as CustomerRegistrationRequest;

        if (!registrationRequest.firstName && !registrationRequest.nickname) {
            return fail(422, {
                errorMessage: "A keresztnevet vagy a becenevet meg kell adni!",
                registrationRequest
            });
        }

        const res = await register(registrationRequest);
        if (res.ok) {
            throw redirect(302, '/customer');
        } else {
            let json = await res.json();
            console.log(json);
            console.log(json.error);
            throw error(res.status, {
                message: json.error
            });
        }
    }
} satisfies Actions;