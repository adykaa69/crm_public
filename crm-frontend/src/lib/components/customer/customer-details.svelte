<script lang="ts">
  import type { CustomerDetailsResponse } from "$lib/models/customer-details";
  import type { CustomerResponse } from "$lib/models/customer-response";
  import CustomerForm from "./customer-form.svelte";
  import CustomerNote from "./customer-note.svelte";

  export let customer: CustomerResponse;
  export let customerDetails: CustomerDetailsResponse[];
  export let form;
  export let isDisabled: boolean;
</script>

<div>
  {#each customerDetails as details}
    <CustomerNote customerDetails={details} />
  {/each}
</div>
<div>
  <CustomerForm action="?/update" {isDisabled} title="Ügyfél adat szerkesztése" {customer} {form} />
</div>
<!-- <CustomerDetailsAi /> -->
