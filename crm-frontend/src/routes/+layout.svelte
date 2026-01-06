<script lang="ts">
  import NavigationBar from "$lib/components/navigation-bar.svelte";
  import Sidebar from "$lib/components/sidebar.svelte";
  import "../app.css";

  let { children } = $props();
</script>

<NavigationBar />
<div class="flex min-h-[calc(100vh - 45px)]">
  <Sidebar />
  <main>
    {@render children()}
  </main>
</div>

<style>
  main {
    flex: 1;
  }
</style>
