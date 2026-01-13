<script lang="ts">
  import { toCustomerDetailsRequest, type CustomerDetailsDto } from "$lib/models/customer";
  import { dateCustomFormatting } from "$lib/utils/formatter";
  import { Check, Pen, Trash, X } from "@lucide/svelte";
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
    await updateCustomerDetails(detailCache.id, toCustomerDetailsRequest(detailCache));
    disabled = true;
  }
</script>

{#if detailCache.updatedAt}
  <div class="mt-3 flex items-center">
    <label for="description" class="date text-xs md:text-sm">{dateCustomFormatting(detailCache.updatedAt)}</label>
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
    name={detailCache.id}
    id={detailCache.id}
    rows="3"
    class="w-full rounded-sm border bg-[#F6FAFD] p-2 text-sm shadow-md md:text-base"
    bind:value={detailCache.note}
    {disabled}
  >
  </textarea>
{/if}