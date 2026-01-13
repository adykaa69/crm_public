<script lang="ts">
  import { toCustomerDetailsRequest, type CustomerDetailsDto } from "$lib/models/customer";
  import { dateCustomFormatting } from "$lib/utils/formatter";
  import { Check, Pen, Trash, X } from "@lucide/svelte";
  import TableCell from "../elements/table-cell.svelte";
  import TaskTextCell from "../elements/task-text-cell.svelte";
  import { goto, invalidate, invalidateAll } from "$app/navigation";
  import { deleteCustomerDetails, updateCustomerDetails } from "$lib/utils/handle-customer-details";

  interface Props {
    detail: CustomerDetailsDto;
  }

  let { detail }: Props = $props();
  let detailCache: CustomerDetailsDto = $state(detail);
  let disabled = $state(true);

  function allowEditing() {
    disabled = false;
  }

  function cancelEditing() {
    detailCache = detail;
    disabled = true;
  }


  async function deleteNote() {
    if (confirm("Biztosan törölni szeretnéd a megjegyzéset?")) {
      await deleteCustomerDetails(detailCache.id);
      location.reload();
    }
    disabled = true;
  }

  async function modifyNote() {
    await updateCustomerDetails(detailCache.id, toCustomerDetailsRequest(detailCache))
    disabled = true;
  }
</script>

<div class="mt-3 flex items-center">
  <label for="description" class="date text-xs md:text-sm">{dateCustomFormatting(detail.updatedAt)}</label>
  <div class="ml-auto">
    {#if disabled}
      <button class="cursor-pointer" onclick={() => allowEditing()}>
        <Pen />
      </button>
    {:else}
      <button class="cursor-pointer" onclick={() => modifyNote()}>
        <Check />
      </button>
      <button class="ml-2 cursor-pointer" onclick={(e) => cancelEditing()}>
        <X />
      </button>
      <button class="ml-5 cursor-pointer text-red-500 hover:text-red-700" onclick={() => deleteNote()}>
        <Trash />
      </button>
    {/if}
  </div>
</div>
<textarea
  name={detail.id}
  id={detail.id}
  rows="3"
  class="w-full rounded-sm border bg-[#F6FAFD] p-2 text-sm shadow-md md:text-base"
  bind:value={detailCache.note}
  {disabled}
>
</textarea>

<style>
  .crm-btn {
    background-color: rgba(81, 78, 243, 0.8); /* --color-submit-btn at 80% */
    color: #fff;
    border-radius: 0.5rem; /* rounded-lg */
    padding: 0.5rem 0.5rem; /* py-2 px-2 (default) */
    font-size: 0.75rem; /* text-xs */
    cursor: pointer;
    box-shadow: 0 4px 6px rgba(16, 24, 40, 0.08);
    width: 100%;
    text-align: center;
    border: none;
    white-space: nowrap; /* whitespace-nowrap */
  }

  .crm-btn-delete {
    background-color: red;
    color: #fff;
    border-radius: 0.5rem; /* rounded-lg */
    padding: 0.5rem 0.5rem; /* py-2 px-2 (default) */
    font-size: 0.75rem; /* text-xs */
    cursor: pointer;
    box-shadow: 0 4px 6px rgba(16, 24, 40, 0.08);
    text-align: center;
    border: none;
    white-space: nowrap; /* whitespace-nowrap */
  }

  @media (min-width: 640px) {
    .crm-btn {
      width: auto;
      align-items: center; /* items-center */
    }
  }
  @media (min-width: 768px) {
    .crm-btn {
      font-size: 0.875rem; /* md:text-sm */
      padding: 0.5rem 0.75rem; /* px-3 on md */
    }
  }
  .crm-btn:hover {
    background-color: var(--color-submit-btn);
  }
</style>
