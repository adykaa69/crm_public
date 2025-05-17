<script lang="ts">
  import CustomerForm from "$lib/components/customer-form.svelte";
  import type { CustomerRegistrationRequest } from "$lib/models/customer-request.js";
  import { ActionParameters } from "$lib/constants";

  let { form } = $props();
  let customer: CustomerRegistrationRequest = {
    firstName: form?.registrationRequest.firstName || "",
    lastName: form?.registrationRequest.lastName || "",
    nickname: form?.registrationRequest.nickname || "",
    email: form?.registrationRequest.email || "",
    phoneNumber: form?.registrationRequest.phoneNumber || "",
    relationship: form?.registrationRequest.relationship || ""
  };
</script>

<CustomerForm
  action="?/{ActionParameters.REGISTER}"
  title="Új ügyfél regisztráció"
  isDisabled={false}
  {customer}
  {form}
/>
