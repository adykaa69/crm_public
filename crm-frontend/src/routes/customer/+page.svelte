<script lang="ts">
  import {
    isCustomerResponse,
    type CustomerResponse
  } from "$lib/models/customer-response.js";
  import { deleteCustomer } from "$lib/utils/handle-customer.js";

  let { data } = $props();
  let counter: number = 0;

  function incrementCounter() {
    return ++counter;
  }

  async function deleteCust(customer: CustomerResponse) {
    if (
      confirm(
        `Biztosan törölni szeretné ezt az ügyfelet? ${customer.lastName} ${customer.firstName}`
      )
    ) {
      await deleteCustomer(customer.id);
      location.reload(); // Refresh the page after successful deletion
    }
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
      <th>Becenév</th>
      <th>Email</th>
      <th>Telefonszám</th>
      <th>Ismeretség</th>
      <th>Műveletek</th>
    </tr>
  </thead>
  <tbody>
    {#if !data.data || data.data.length === 0}
      <tr>
        <td colspan="7">Nincs megjeleníthető adat</td>
      </tr>
    {:else}
      {#each data.data as customer}
        {#if isCustomerResponse(customer)}
          <tr>
            <td>{incrementCounter()}</td>
            <td>{customer.lastName}</td>
            <td>{customer.firstName}</td>
            <td>{customer.nickname}</td>
            <td>{customer.email}</td>
            <td>{customer.phoneNumber}</td>
            <td>{customer.relationship}</td>
            <td>
              <button class="delete-button" onclick={() => deleteCust(customer)}
                >Törlés
              </button>
              <a class="update-button" href="/customer/update/{customer.id}"
                >Szerkesztés</a
              >
            </td>
          </tr>
        {/if}
      {/each}
    {/if}
  </tbody>
</table>

<style>
  table {
    width: 90%;
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

  button.delete-button {
    cursor: pointer;
    padding: 5px 10px;
    background-color: #ff4d4d;
    color: white;
    border: none;
    border-radius: 3px;
  }

  button.delete-button:hover {
    background-color: #ff1a1a;
  }

  a.update-button {
    cursor: pointer;
    padding: 5px 10px;
    background-color: #4d77ff;
    color: white;
    border: none;
    border-radius: 3px;
  }

  a.update-button:hover {
    background-color: #1a1eff;
  }
</style>
