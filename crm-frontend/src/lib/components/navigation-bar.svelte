<script lang="ts">
  import { page } from "$app/state";
  import { getTitleByHref } from "$lib/utils/handle-constants";
  import Logo from "./logo.svelte";

  let title: string = $derived(getTitleByHref(page.url.pathname));
</script>

<nav class="container">
  <a href="/">
    <Logo />
  </a>
  <h1>{title}</h1>
</nav>

<style>
  a {
    margin-left: 0.5rem;
    margin-right: 0.5rem;
  }

  nav {
    background: rgba(246, 250, 253, 0.9);
  }

  .container {
    display: flex;
    flex-direction: row;
    max-width: unset;
    align-items: center;
  }
</style>
