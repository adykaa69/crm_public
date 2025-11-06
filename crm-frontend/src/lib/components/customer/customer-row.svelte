<script lang="ts">
  import { Check, PenLine, Trash, X } from "@lucide/svelte";
  import TableCell from "../elements/table-cell.svelte";
  import type { CustomerDto } from "$lib/models/customer";

  interface Props {
    customer: CustomerDto;
    onSave?: () => void;
    onDelete?: () => void;
    onCancel?: () => void;
  }
  let { customer, onSave, onDelete, onCancel }: Props = $props();
  let isEditing = $state(false);
  let customerCache: CustomerDto = $derived(customer);
  let city: string = $derived(customerCache.residence ? (customerCache.residence.city ?? "") : "");

  async function saveEdit(customer: CustomerDto) {
    const res = await fetch(`/task?/taskupdate`, {
      method: "POST",
      body: JSON.stringify(customer)
    });
    const json = await res.json();
    if (json.error) {
      console.log("error");
      return "hiba";
    }
    isEditing = false;
    onSave?.();
    return json.customer;
  }

  function cacheCustomer() {
    customerCache = customer;
  }
  function cancelEdit() {
    customerCache = customer;
    isEditing = false;
    onCancel?.();
  }

  async function remove() {
    if (
      confirm(`Biztosan törölni szeretnéd ${customer.lastName} ${customer.firstName} (${customer.nickname}) ügyfelet?`)
    ) {
      const res = await fetch(`/task?/taskdelete`, {
        method: "POST",
        body: JSON.stringify(customer)
      });
      if (res.ok) alert(`${customer.lastName} ${customer.firstName} (${customer.nickname}) törölve`);
      else {
        let json = await res.json();
        alert(`Error occured during deleting the task: ${json.errorMessage}`);
      }
      onDelete?.();
    }
  }

  async function onkeydown(e: KeyboardEvent) {
    if (e.key === "Enter") {
      customer = customerCache;
      await saveEdit(customer);
    }
    if (e.key === "Escape") cancelEdit();
  }
</script>

<div role="button" class="hidden items-center gap-4 sm:grid sm:grid-cols-30" {onkeydown} tabindex="0">
  <div class="col-span-4">
    <TableCell cellType="text" bind:text={customerCache.lastName} {isEditing} />
  </div>
  <div class="col-span-4">
    <TableCell cellType="text" bind:text={customerCache.firstName} {isEditing} />
  </div>
  <div class="col-span-3">
    <TableCell cellType="text" bind:text={customerCache.nickname} {isEditing} />
  </div>
  <div class="col-span-6">
    <TableCell cellType="text" bind:text={customerCache.email} {isEditing} />
  </div>
  <div class="col-span-4">
    <TableCell cellType="text" bind:text={customerCache.phoneNumber} {isEditing} />
  </div>
  <div class="col-span-4">
    <TableCell cellType="text" bind:text={customerCache.relationship} {isEditing} />
  </div>
  <div class="col-span-4">
    <TableCell cellType="text" bind:text={city} isEditing={false} />
  </div>
  {#if isEditing}
    <div class="col-span-1 flex justify-end gap-3">
      <button
        onclick={(e) => {
          e.preventDefault();
          saveEdit(customerCache);
          isEditing = false;
        }}
        class="p-1 text-gray-400 transition-colors hover:text-gray-600"
      >
        <Check class="h-4 w-4" />
      </button>
      <button onclick={cancelEdit} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
        <X class="h-4 w-4" />
      </button>
    </div>
  {:else}
    <div class="col-span-1 flex justify-end gap-3">
      <button
        onclick={() => {
          cacheCustomer();
          isEditing = true;
        }}
        class="p-1 text-gray-400 transition-colors hover:text-gray-600"
      >
        <PenLine class="h-4 w-4" />
      </button>
      <button onclick={() => remove()} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
        <Trash class="h-4 w-4" />
      </button>
    </div>
  {/if}
</div>
