<script lang="ts">
  import CustomerForm from "$lib/components/customer-form.svelte";
  import { type CustomerResponse } from "$lib/models/customer-response";
  import { deleteCustomer } from "$lib/utils/handle-customer";
  import { goto } from "$app/navigation";
  import CustomerDetails from "$lib/components/customer-details.svelte";
  import type { CustomerDetailsResponse } from "$lib/models/customer-details.js";

  let { data, form } = $props();
  // let customer: CustomerResponse = data.data as CustomerResponse;
  let customerDetails: CustomerDetailsResponse[] = [];
  let isDisabled = $state(true);

  function toggleDisabled() {
    isDisabled = !isDisabled;
  }

  async function confirmCustomerDeletion(customer: CustomerResponse) {
    if (confirm(`Biztosan törölni szeretné ezt az ügyfelet? ${customer.lastName} ${customer.firstName}`)) {
      await deleteCustomer(customer.id);
      await goto("/customer");
    }
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

<style>
  button.delete {
    padding: 5px 10px;
    background-color: #ff9898;
    color: white;
    border: none;
    border-radius: 3px;
  }

  button.enabled {
    cursor: pointer;
    background-color: #fb3b3b;
  }

  button.enabled:hover {
    background-color: #8e0e0e;
  }
</style>
