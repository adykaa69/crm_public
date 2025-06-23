<script lang="ts">
  export let columns: { key: string; label: string }[] = [];
  export let rows: any[] = [];
  export let emptyMessage: string = "Nincs megjeleníthető adat";
  export let buttonTitle: string;
  export let buttonHref: string;
  let counter: number = 0;
  function incrementCounter() {
    return ++counter;
  }
  function getProperty(obj: any, key?: string) {
    if (!key) return obj;
    return key.split(".").reduce((acc, part) => acc?.[part], obj);
  }
</script>

<table>
  <thead>
    <tr>
      <th><img src="/user_octagon.svg" alt="No." /></th>
      {#each columns as col}
        <th><p>{col.label}</p></th>
      {/each}
      <th><p>Edit</p></th>
    </tr>
  </thead>
  <tbody>
    {#if !rows || rows.length === 0}
      <tr>
        <td class="empty_row" colspan={columns.length + 2}>{emptyMessage}</td>
      </tr>
    {:else}
      {#each rows as row}
        <tr>
          <td class="first_column">{incrementCounter()}</td>
          {#each columns as col}
            <td>{getProperty(row, col.key)}</td>
          {/each}
          <td class="last_column">
            <a class="update_button" href={buttonHref.concat("/", row.id)}>{buttonTitle}</a>
          </td>
        </tr>
      {/each}
    {/if}
  </tbody>
</table>

<style>
  table {
    grid-template-columns: 1fr repeat(7, 5fr) 1fr;
    width: 100%;
  }

  td {
    column-span: all;
  }

  th {
    border: 1px solid black;
    color: #7e92a2;
    font-family: "Inter";
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    text-align: start;
  }

  th p {
    padding-left: 20px;
    padding-right: 20px;
  }

  th img {
    margin: 0 auto;
  }

  td {
    border: 1px solid black;
    font-family: "Inter";
    font-style: normal;
    font-weight: 400;
    font-size: 16px;
    line-height: 30px;
  }

  .first_column {
    text-align: center;
  }

  .empty_row {
    text-align: center;
  }

  .last_column {
    text-align: center;
  }

  a.update_button {
    cursor: pointer;
    padding: 5px 10px;
    background-color: #4d77ff;
    color: white;
    border: none;
    border-radius: 3px;
  }

  a.update_button:hover {
    background-color: #1a1eff;
  }
</style>
