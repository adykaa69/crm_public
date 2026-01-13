<script lang="ts">
  import CustomerInfo from "$lib/components/customer/customer-info.svelte";

  let { data, form } = $props();
</script>

{#if data.customer}
  <CustomerInfo customer={data.customer} customerDetails={data.customerDetails} />
{:else}
  {data.errors}
{/if}
