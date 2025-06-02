<script lang="ts">
  import type { SvelteComponent } from "svelte";
  import { page } from "$app/state";

  export let title: string;
  export let items: {
    href: string;
    label: string;
    ariaCurrent: boolean;
    component: typeof SvelteComponent;
  }[] = [];
</script>

<nav class="container">
  <ul>
    {#each items as item}
      <li>
        <a
          href={item.href}
          aria-label={item.label}
          aria-current={item.ariaCurrent ? page.url.pathname === item.href : false}
        >
          <svelte:component this={item.component} />
        </a>
      </li>
    {/each}
  </ul>
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

  nav a[aria-current="true"] {
    border-bottom: 2px solid;
  }

  ul {
    display: flex;
    flex-direction: row;
  }

  li {
    align-content: center;
    max-height: 45px;
  }

  .container {
    display: flex;
    flex-direction: row;
    max-width: unset;
    align-items: center;
  }
</style>
