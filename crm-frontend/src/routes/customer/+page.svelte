<script lang="ts">
  import CustomerPage from "$lib/components/customer/customer-page.svelte";
  import Customer from "$lib/components/customer/customer.svelte";
  import { invalidateAll } from "$app/navigation";

  let { data } = $props();

  async function handleUpdate() {
    await invalidateAll();
  }
</script>

<CustomerPage {data} />
<Customer customers={data.customers} onSave={handleUpdate} onDelete={handleUpdate} onCancel={handleUpdate} />
