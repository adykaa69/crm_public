<script lang="ts">
	let { data } = $props();
	let counter: number = 0;
	function incrementCounter() {
		return ++counter;
	}
</script>

<h1>Ez az ügyfél oldal</h1>
<a href="/customer/registration">Új ügyfél</a>
<!-- <input type="button" onclick={a} value="Új ügyfél"> -->
<table>
	<thead>
		<tr>
			<th>No.</th>
			<th>Vezetéknév</th>
			<th>Keresztnév</th>
			<th>Email</th>
			<th>Telefonszám</th>
			<th>Ismeretség</th>
		</tr>
	</thead>
	<tbody>
		{#if !data.data}
			<tr>
				<td colspan="5">Nincs megjeleníthető adat</td>
			</tr>
		{:else}
			{#each data.data as customer}
				<tr>
					<td>{incrementCounter()}</td>
					<td>{customer.lastName}</td>
					<td>{customer.firstName}</td>
					<td>{customer.email}</td>
					<td>{customer.phoneNumber}</td>
					<td>{customer.relationship}</td>
				</tr>
			{/each}
		{/if}
	</tbody>
</table>

<style>
	table {
		width: 50%;
		border-collapse: collapse;
		border: 1px solid black;
	}
	th,
	td {
		border: 1px solid black;
		text-align: center;
	}
	h1 {
		font-size: 20px;
		font-weight: bold;
		text-align: center;
	}
	button {
		cursor: pointer;
	}
</style>
