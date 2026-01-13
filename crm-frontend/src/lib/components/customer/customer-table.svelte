<script lang="ts">
  import { sortCustomers } from "$lib/utils/sorter";
  import type { CustomerDto } from "$lib/models/customer";
  import CustomerRow from "./customer-row.svelte";

  interface Props {
    customers: CustomerDto[];
    onDelete?: () => void;
  }

  let { customers, onDelete}: Props = $props();

  let sortField: keyof CustomerDto | null = $state(null);
  let sortAsc: boolean = $state(true);

  let sortedCustomer: CustomerDto[] = $derived(sortField ? sortCustomers(customers, sortField, sortAsc) : customers);

  function toggleSort(field: keyof CustomerDto) {
    if (sortField === field) {
      sortAsc = !sortAsc;
    } else {
      sortField = field;
      sortAsc = true;
    }
  }

  function loadMore() {}
</script>

{#snippet generateHeader(btnName: string, toggleName: keyof CustomerDto, colSpan: number)}
  <button
    class="col-span-{colSpan} flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort(toggleName)}
  >
    {btnName}
    {#if sortField === toggleName}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>
{/snippet}

<div class="hidden gap-4 border-b border-gray-200 bg-gray-50 p-4 sm:grid sm:grid-cols-30 sm:p-6">
  {@render generateHeader("Vezetéknév", "lastName", 4)}
  {@render generateHeader("Keresztnév", "firstName", 4)}
  {@render generateHeader("Becenév", "nickname", 3)}
  {@render generateHeader("Email", "email", 6)}
  {@render generateHeader("Telefonszám", "phoneNumber", 4)}
  {@render generateHeader("Ismeretség", "relationship", 4)}
  {@render generateHeader("Település", "residence", 4)}
  <div class="col-span-1 text-right text-sm font-medium text-gray-700"></div>
</div>

<div class="divide-y divide-gray-200">
  {#each sortedCustomer as customer}
    <div class="p-4 transition-colors hover:bg-gray-50 sm:p-6">
      <!-- Desktop Layout -->
      <CustomerRow {customer} {onDelete} />
    </div>
  {/each}
</div>

<!-- Load More Button -->
<div class="border-t border-gray-200 p-4 text-center sm:p-6">
  <button
    onclick={loadMore}
    class="rounded-lg px-6 py-2 text-sm font-medium text-blue-600 transition-colors hover:bg-blue-50 hover:text-blue-700"
  >
    Load More
  </button>
</div>
