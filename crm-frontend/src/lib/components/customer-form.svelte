<script lang="ts">
  import type { CustomerRegistrationRequest } from "$lib/models/customer-request";
  import { type CustomerResponse } from "$lib/models/customer-response";

  export let title: string;
  export let action: string;
  export let customer: CustomerResponse | CustomerRegistrationRequest;
  export let form;
  export let isDisabled: boolean;
</script>

<h1>{title}</h1>
<form method="POST" {action}>
  <div>
    <label for="firstName">Keresztnév:</label>
    <input id="firstName" name="firstName" type="text" value={customer.firstName} disabled={isDisabled} />
  </div>
  <div>
    <label for="lastName">Vezetéknév:</label>
    <input id="lastName" name="lastName" type="text" value={customer.lastName} disabled={isDisabled} />
  </div>
  <div>
    <label for="nickname">Becenév:</label>
    <input id="nickname" name="nickname" type="text" value={customer.nickname} disabled={isDisabled} />
  </div>
  <div>
    <label for="email">Email:</label>
    <input id="email" name="email" type="email" value={customer.email} disabled={isDisabled} />
  </div>
  <div>
    <label for="phoneNumber">Telefonszám:</label>
    <input id="phoneNumber" name="phoneNumber" type="tel" value={customer.phoneNumber} disabled={isDisabled} />
  </div>
  <div>
    <label for="relationship">Ismeretség:</label>
    <input id="relationship" name="relationship" type="text" value={customer.relationship} disabled={isDisabled} />
  </div>
  <div>
    <label for="country">Ország:</label>
    <input
      id="country"
      name="residence.country"
      type="text"
      value={customer.residence?.country}
      disabled={isDisabled}
    />
  </div>
  <div>
    <label for="zipCode">Irányítószám:</label>
    <input
      id="zipCode"
      name="residence.zipCode"
      type="text"
      value={customer.residence?.zipCode}
      disabled={isDisabled}
    />
  </div>
  <div>
    <label for="city">Település:</label>
    <input id="city" name="residence.city" type="text" value={customer.residence?.city} disabled={isDisabled} />
  </div>
  <div>
    <label for="streetAddress">Lakcím:</label>
    <input
      id="streetAddress"
      name="residence.streetAddress"
      type="text"
      value={customer.residence?.streetAddress}
      disabled={isDisabled}
    />
  </div>
  <div>
    <label for="addressLine2">Egyéb cím:</label>
    <input
      id="addressLine2"
      name="residence.addressLine2"
      type="text"
      value={customer.residence?.addressLine2}
      disabled={isDisabled}
    />
  </div>
  <button class="update {isDisabled ? '' : 'enabled'}" type="submit" disabled={isDisabled}>Submit</button>
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

  button.update {
    padding: 5px 10px;
    background-color: #82a3ff;
    color: white;
    border: none;
    border-radius: 3px;
  }

  button.enabled {
    cursor: pointer;
    background-color: #2a2eff;
  }

  button.enabled:hover {
    background-color: #070aa1;
  }
</style>
