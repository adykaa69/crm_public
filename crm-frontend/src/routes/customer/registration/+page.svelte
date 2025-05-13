<script lang="ts">
  import CustomerForm from "$lib/components/customer-form.svelte";
  import type { CustomerRegistrationRequest } from "$lib/models/customer-request.js";

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

<h1>Új ügyfél regisztráció</h1>

<CustomerForm action="?/register" {customer} />
{#if form?.errorMessage}
  <p class="error">{form.errorMessage}</p>
{/if}

<style>
  .error {
    color: red;
    text-align: center;
    margin-bottom: 10px;
  }
</style>
