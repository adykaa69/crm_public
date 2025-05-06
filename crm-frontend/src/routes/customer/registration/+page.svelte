<script lang="ts">
	import CustomerForm from '$lib/components/customer-form.svelte';

	let { form } = $props();
</script>

<h1>Új ügyfél regisztráció</h1>
{#if form?.errorMessage}
	<p class="error">{form.errorMessage}</p>
{/if}

<CustomerForm action="?/register" />

<style>
	.error {
		color: red;
		text-align: center;
		margin-bottom: 10px;
	}
</style>
