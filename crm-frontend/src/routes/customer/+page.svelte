<script lang="ts">
  import Customer from "$lib/components/customer/customer.svelte";
  import { invalidateAll } from "$app/navigation";

  let { data } = $props();

  async function handleUpdate() {
    await invalidateAll();
  }
</script>

<Customer customers={data.customers} onDelete={handleUpdate} />
