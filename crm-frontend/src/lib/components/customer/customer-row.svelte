<script lang="ts">
  import { Trash } from "@lucide/svelte";
  import TableCell from "../elements/table-cell.svelte";
  import type { CustomerDto } from "$lib/models/customer";
  import { goto } from "$app/navigation";
  import Button from "../elements/button.svelte";
  import { deleteCustomer } from "$lib/utils/handle-customer";

  interface Props {
    customer: CustomerDto;
    onDelete?: () => void;
  }
  let { customer, onDelete }: Props = $props();
  let isEditing = $state(false);
  let customerCache: CustomerDto = $derived(customer);
  let city: string = $derived(customerCache.residence ? (customerCache.residence.city ?? "") : "");

  async function remove() {
    if (
      confirm(`Biztosan törölni szeretnéd ${customer.lastName} ${customer.firstName} (${customer.nickname}) ügyfelet?`)
    ) {
      const res = await deleteCustomer(customer.id);
      if (res.ok) alert(`${customer.lastName} ${customer.firstName} (${customer.nickname}) törölve`);
      else {
        let json = await res.json();
        alert(`Error occured during deleting the task: ${json.errorMessage}`);
      }
      onDelete?.();
    }
  }
</script>

<div role="button" class="hidden items-center gap-4 sm:grid sm:grid-cols-30" tabindex="0">
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
  <div class="col-span-1 flex justify-end gap-3">
    <Button btnName="Részletek" onclick={() => goto(`/customer/${customer.id}`)} />
    <button onclick={() => remove()} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
      <Trash class="h-4 w-4" />
    </button>
  </div>
</div>
