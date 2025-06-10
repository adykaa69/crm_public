<script lang="ts">
  import "../../app.css";

  let { children, data } = $props();
</script>

<!-- <nav>
  <a href="/customer">{data.title}</a>
</nav> -->
{@render children()}
