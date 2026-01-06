<script lang="ts">
  import CustomerDetails from "$lib/components/customer/customer-details.svelte";

  let { data, form } = $props();
</script>

{#if data.customer}
  <CustomerDetails customer={data.customer} customerDetails={data.customerDetails} />
{:else}
  {data.errors}
{/if}
