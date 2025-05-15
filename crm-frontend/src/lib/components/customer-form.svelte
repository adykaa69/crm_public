<script lang="ts">
  import type { CustomerRegistrationRequest } from "$lib/models/customer-request";
  import { type CustomerResponse } from "$lib/models/customer-response";

  export let title: string;
  export let action: string;
  export let customer: CustomerResponse | CustomerRegistrationRequest = {
    id: "",
    firstName: "",
    lastName: "",
    nickname: "",
    email: "",
    phoneNumber: "",
    relationship: "",
    createdAt: "",
    updatedAt: ""
  };
  export let form;
</script>

<h1>{title}</h1>
<form method="POST" {action}>
  <div>
    <label for="firstName">Keresztnév:</label>
    <input
      id="firstName"
      name="firstName"
      type="text"
      value={customer.firstName}
    />
  </div>
  <div>
    <label for="lastName">Vezetéknév:</label>
    <input
      id="lastName"
      name="lastName"
      type="text"
      value={customer.lastName}
    />
  </div>
  <div>
    <label for="nickname">Becenév:</label>
    <input
      id="nickname"
      name="nickname"
      type="text"
      value={customer.nickname}
    />
  </div>
  <div>
    <label for="email">Email:</label>
    <input id="email" name="email" type="email" value={customer.email} />
  </div>
  <div>
    <label for="phoneNumber">Telefonszám:</label>
    <input
      id="phoneNumber"
      name="phoneNumber"
      type="tel"
      value={customer.phoneNumber}
    />
  </div>
  <div>
    <label for="relationship">Ismeretség:</label>
    <input
      id="relationship"
      name="relationship"
      type="text"
      value={customer.relationship}
    />
  </div>
  <button type="submit">Submit</button>
</form>

{#if form?.errorMessage}
  <p class="error">{form.errorMessage}</p>
{/if}

<style>
  h1 {
    font-size: 20px;
    font-weight: bold;
    text-align: center;
  }

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

  .error {
    color: red;
    text-align: center;
    margin-bottom: 10px;
  }
</style>
