<script lang="ts">
  import type { CustomerRegistrationRequest } from "$lib/models/customer.js";
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

<h1>Need to delete this page</h1>
<h1>Consider deleting +page.server.ts</h1>

