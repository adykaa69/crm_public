<script lang="ts">
	let { form } = $props();
</script>

<h1>Új ügyfél regisztráció</h1>
{#if form?.errorMessage}
	<p class="error">{form.errorMessage}</p>
{/if}

<form method="POST" action="?/register">
	<div>
		<label for="firstName">Keresztnév:</label>
		<input id="firstName" name="firstName" type="text" value={form?.registrationRequest.firstName ?? ''} />
	</div>
	<div>
		<label for="lastName">Vezetéknév:</label>
		<input id="lastName" name="lastName" type="text" value={form?.registrationRequest.lastName ?? ''} />
	</div>
	<div>
		<label for="nickname">Becenév:</label>
		<input id="nickname" name="nickname" type="text" value={form?.registrationRequest.nickname ?? ''} />
	</div>
	<div>
		<label for="email">Email:</label>
		<input id="email" name="email" type="email" value={form?.registrationRequest.email ?? ''} />
	</div>
	<div>
		<label for="phoneNumber">Telefonszám:</label>
		<input
			id="phoneNumber"
			name="phoneNumber"
			type="tel"
			value={form?.registrationRequest.phoneNumber ?? ''}
		/>
	</div>
	<div>
		<label for="relationship">Ismeretség:</label>
		<input
			id="relationship"
			name="relationship"
			type="text"
			value={form?.registrationRequest.relationship ?? ''}
		/>
	</div>
	<button type="submit">Submit</button>
</form>

<style>
	form {
		display: flex;
		flex-direction: column;
		width: 300px;
		margin: 0 auto;
	}

	div {
		margin-bottom: 10px;
	}

	label {
		display: block;
		margin-bottom: 5px;
		font-weight: bold;
	}

	input {
		width: 100%;
		padding: 8px;
		box-sizing: border-box;
	}

	button {
		padding: 10px;
		background-color: #007bff;
		color: white;
		border: none;
		cursor: pointer;
	}

	button:hover {
		background-color: #0056b3;
	}
	.error {
		color: red;
		text-align: center;
		margin-bottom: 10px;
	}
</style>
