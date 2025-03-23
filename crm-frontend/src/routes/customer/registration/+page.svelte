<script lang="ts">
	import { goto } from '$app/navigation';

	let formData = {
		firstName: '',
		lastName: '',
		nickname: '',
		email: '',
		phoneNumber: '',
		relationship: ''
	};

	function handleSubmit(event: Event) {
		event.preventDefault();
		goto('/customer');
		alert('A beküldött adatok: ' + JSON.stringify(formData));
		console.log('Form submitted:', formData);
		// You can send `formData` to your backend API here
	}
</script>

<h1>Új ügyfél regisztráció</h1>

<form on:submit={handleSubmit}>
	<div>
		<label for="firstName">Keresztnév:</label>
		<input id="firstName" type="text" bind:value={formData.firstName} required />
	</div>
	<div>
		<label for="lastName">Vezetéknév:</label>
		<input id="lastName" type="text" bind:value={formData.lastName} required />
	</div>
	<div>
		<label for="nickname">Becenév:</label>
		<input id="nickname" type="text" bind:value={formData.nickname} />
	</div>
	<div>
		<label for="email">Email:</label>
		<input id="email" type="email" bind:value={formData.email} required />
	</div>
	<div>
		<label for="phoneNumber">Telefonszám:</label>
		<input id="phoneNumber" type="tel" bind:value={formData.phoneNumber} required />
	</div>
	<div>
		<label for="relationship">Ismeretség:</label>
		<input id="relationship" type="text" bind:value={formData.relationship} />
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
</style>
