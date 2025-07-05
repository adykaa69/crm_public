<script lang="ts">
  import { page } from "$app/state";
  import { items } from "$lib/constants";

  let pathname = $derived(page.url.pathname);
  let pathNameChunk = $derived(pathname.match("^(\/[^/]*)")?.at(0));
</script>

<nav>
  <ul>
    {#each items as item}
      <li aria-current={pathNameChunk === item.href}>
        <a href={item.href} aria-label={item.label}>
          <img src={pathNameChunk === item.href ? item.activeIcon : item.inactiveIcon} alt={item.label} />
        </a>
      </li>
    {/each}
  </ul>
</nav>

<style>
  ul {
    display: flex;
    flex-direction: column;
    justify-content: left;
    align-items: center;
    list-style-type: none;
    padding: 0;
    margin: 0;
  }
  li {
    margin: 5px;
    min-width: 40px;
    background-color: #ffffff;
    border-radius: 50px;
    max-width: 45px;
  }

  li[aria-current="true"] {
    background-color: #514ef3;
    border-radius: 50px;
  }
  img {
    padding: 10px;
  }

  nav {
    display: flex;
    flex-direction: column;
    max-width: fit-content;
    align-items: center;
    background-size: 30px;
  }
</style>
