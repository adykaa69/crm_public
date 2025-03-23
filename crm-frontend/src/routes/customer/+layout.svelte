<script lang="ts">
	import '../../app.css';
	let { children } = $props();
</script>

<nav>
	<a href="/customer">Ügyfelek</a>
</nav>
{@render children()}
