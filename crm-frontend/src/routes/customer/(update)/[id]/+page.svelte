<script lang="ts">
  import { type CustomerResponse } from "$lib/models/customer-response";
  import CustomerDetails from "$lib/components/customer/customer-details.svelte";
  import type { CustomerDetailsResponse } from "$lib/models/customer-details.js";

  let { data, form } = $props();
  // let customer: CustomerResponse = data.data as CustomerResponse;
  let isDisabled = $state(true);

  function toggleDisabled() {
    isDisabled = !isDisabled;
  }
</script>

<label class="switch">
  Szerkesztési mód
  <input type="checkbox" onclick={toggleDisabled} />
</label>
<CustomerDetails
  {isDisabled}
  customer={data.customer as CustomerResponse}
  customerDetails={data.customerDetails as CustomerDetailsResponse[]}
  {form}
/>
