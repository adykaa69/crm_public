<script lang="ts">
  import CustomerPage from "$lib/components/customer-page.svelte";
  let { data } = $props();
  let counter: number = 0;

  function incrementCounter() {
    return ++counter;
  }
</script>

<CustomerPage {data} />
