<script lang="ts">
  import CustomerPage from "$lib/components/customer-page.svelte";
  let { data } = $props();
</script>

<CustomerPage {data} />
