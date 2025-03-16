<script lang="ts">
	import '../app.css';
	let { children } = $props();
</script>

<nav>
	<a href="/">Kezdőlap</a>
	<a href="/about">Rólunk</a>
</nav>
{@render children()}
