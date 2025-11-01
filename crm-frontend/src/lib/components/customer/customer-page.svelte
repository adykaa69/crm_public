<script lang="ts">
  import Table from "./table.svelte";

  export let data;
  const columns = [
    { key: "lastName", label: "Vezetéknév" },
    { key: "firstName", label: "Keresztnév" },
    { key: "nickname", label: "Becenév" },
    { key: "email", label: "Email" },
    { key: "phoneNumber", label: "Telefonszám" },
    { key: "relationship", label: "Ismeretség" },
    { key: "residence.city", label: "Település" }
  ];
</script>

<Table {columns} rows={data.customers} buttonTitle="Részletek" buttonHref="/customer" />
