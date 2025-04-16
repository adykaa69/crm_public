import type { CustomerRequest } from '$lib/model/customer-request';
import { error, redirect } from '@sveltejs/kit';
import type { Actions } from './$types';
import { register } from './registration';

export const actions = {
    register: async (event) => {
        const formData = Object.fromEntries(await event.request.formData());
        let registrationRequest: CustomerRequest = formData as CustomerRequest;
        const res = await register(registrationRequest);
        if (res.ok) {
            throw redirect(302, '/customer');
        } else {
            let json = await res.json()
            console.log(json)
            console.log(json.error)
            throw error(res.status, {
                message: json.error
            });
        }
    }
} satisfies Actions;