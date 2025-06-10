<script lang="ts">
  let counter: number = 0;

  export let data;
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
      <th>Becenév</th>
      <th>Email</th>
      <th>Telefonszám</th>
      <th>Ismeretség</th>
      <th>Település</th>
      <th>Műveletek</th>
    </tr>
  </thead>
  <tbody>
    {#if !data.costumers || data.costumers.length === 0}
      <tr>
        <td colspan="7">Nincs megjeleníthető adat</td>
      </tr>
    {:else}
      {#each data.costumers as customer}
        <tr>
          <td>{incrementCounter()}</td>
          <td>{customer.lastName}</td>
          <td>{customer.firstName}</td>
          <td>{customer.nickname}</td>
          <td>{customer.email}</td>
          <td>{customer.phoneNumber}</td>
          <td>{customer.relationship}</td>
          <td>{customer.residence?.city}</td>
          <td>
            <a class="update-button" href="/customer/{customer.id}">Részletek</a>
          </td>
        </tr>
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
