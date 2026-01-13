<script lang="ts">
  import type { CustomerDetailsDto } from "$lib/models/customer";
  import { dateCustomFormatting } from "$lib/utils/formatter";
  import { Check, Pen, Trash, X } from "@lucide/svelte";
  import TableCell from "../elements/table-cell.svelte";
  import TaskTextCell from "../elements/task-text-cell.svelte";
  import CustomerNote from "./customer-note.svelte";

  interface Props {
    customerDetails: CustomerDetailsDto[];
  }

  let { customerDetails }: Props = $props();
</script>

<div class="m-2 mx-2 mb-16 flex-1 rounded-sm bg-white shadow-md md:m-4 md:mx-4 md:mb-0">
  <form action="?/addnote" method="POST" class="m-3 md:m-4">
    <div class="grid grid-cols-1 gap-3 md:gap-4">
      <h1 class="m-2 justify-self-center text-2xl font-semibold md:m-4 md:text-3xl">Megjegyzések</h1>
      <div>
        <textarea
          id="note"
          name="note"
          rows="3"
          class="w-full rounded-sm border bg-[#F6FAFD] p-2 text-sm shadow-md md:text-base"
        ></textarea>
      </div>
    </div>
    <button class="crm-btn m-3">Megjegyzés hozzáadása</button>
  </form>
  <div class="m-3 md:m-4">
    <div class="mt-6 mb-2 md:mt-8">
      {#each customerDetails as detail}
        <CustomerNote {detail} />
      {/each}
    </div>
  </div>
</div>

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
