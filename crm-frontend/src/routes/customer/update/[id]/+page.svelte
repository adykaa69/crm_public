<script lang="ts">
  import CustomerForm from "$lib/components/customer-form.svelte";
  import { type CustomerResponse } from "$lib/models/customer-response";

  let { data, form } = $props();
  let customer: CustomerResponse = data.data as CustomerResponse;
</script>

<CustomerForm
  action="?/updateCustomer"
  title="Ügyfél adat szerkesztése"
  {customer}
  {form}
/>
