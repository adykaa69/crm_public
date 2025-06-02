<script lang="ts">
  import "../app.css";
  import Logo from "$lib/components/logo.svelte";
  import NavigationBar from "$lib/components/navigation-bar.svelte";
  import About from "$lib/components/about.svelte";

  let { children, data } = $props();
  let items = [
    { href: "/", label: "Kezdőlap", ariaCurrent: false, component: Logo },
    { href: "/about", label: "Rólunk", ariaCurrent: true, component: About }
  ];
</script>

<NavigationBar title={data.title} {items} />
<main>
  {@render children()}
</main>
