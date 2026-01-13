<script lang="ts">
  import { type CustomerDetailsDto, type CustomerDto } from "$lib/models/customer";
  import { dateCustomFormatting } from "$lib/utils/formatter";
  import { deleteCustomer } from "$lib/utils/handle-customer";
  import { redirect } from "@sveltejs/kit";
  import TextInput from "../elements/text-input.svelte";
  import { goto } from "$app/navigation";
  import CustomerNotes from "./customer-notes.svelte";

  interface Props {
    customer: CustomerDto;
    customerDetails: CustomerDetailsDto[];
  }

  let { customer, customerDetails }: Props = $props();

  let inputDisabled: boolean = $state(true);

  function toggleDisable() {
    inputDisabled = !inputDisabled;
  }

  async function removeCustomer() {
    if (confirm(`Biztos törölni szeretnéd ${customer.firstName ? customer.firstName : customer.nickname} ügyfelet?`)) {
      await deleteCustomer(customer.id);
      goto("/customer");
    }
  }
</script>

{#snippet createInput(id: string, title: string, value?: string)}
  <div>
    <TextInput {id} {title} {value} disabled={inputDisabled} />
  </div>
{/snippet}

<main class="flex w-full flex-1 flex-col gap-4 md:flex-row md:gap-0">
  <form action="?/customerupdate" method="POST" class="mx-2 flex-1 md:mx-4">
    <div class="m-2 rounded-sm bg-white p-3 shadow-md md:m-4 md:p-4">
      <div class="grid grid-cols-1 gap-3 sm:grid-cols-2 md:gap-4">
        <h1 class="col-span-1 m-2 justify-self-center text-2xl font-semibold sm:col-span-2 md:m-4 md:text-3xl">
          Ügyfél adatok
        </h1>
        {@render createInput("lastName", "Vezetéknév", customer.lastName)}
        {@render createInput("firstName", "Keresztnév", customer.firstName)}
        {@render createInput("nickname", "Megszólítás", customer.nickname)}
        {@render createInput("relationship", "Ismeretség", customer.relationship)}
        {@render createInput("email", "Email", customer.email)}
        {@render createInput("phoneNumber", "Telefonszám", customer.phoneNumber)}
      </div>
      <div class="mt-3 grid grid-cols-1 gap-3 sm:grid-cols-3 md:mt-4 md:gap-4">
        {@render createInput("residence.country", "Ország", customer.residence?.country)}
        {@render createInput("residence.city", "Település", customer.residence?.city)}
        {@render createInput("residence.zipCode", "Irányítószám", customer.residence?.zipCode)}
        <div class="col-span-1 sm:col-span-3">
          <TextInput
            id="residence.streetAddress"
            title="Lakcím"
            value={customer.residence?.streetAddress}
            disabled={inputDisabled}
          />
        </div>
        <div class="col-span-1 sm:col-span-3">
          <TextInput
            id="residence.addressLine2"
            title="Egyéb cím"
            value={customer.residence?.addressLine2}
            disabled={inputDisabled}
          />
        </div>
      </div>
      <div class="flex items-center justify-between">
        {#if inputDisabled}
          <div></div>
          <button type="button" class="crm-btn m-3 self-end" onclick={toggleDisable}>Szerekesztés engedélyezése</button>
        {:else}
          <button type="button" class="crm-btn-delete bg-red m-3 w-auto" onclick={removeCustomer}>Törlés</button>
          <div class="m-3 flex gap-3">
            <button type="submit" class="crm-btn m-3">Módosítás</button>
            <button type="button" class="bg-cancel-btn/80 hover:bg-cancel-btn rounded px-4 py-2" onclick={toggleDisable}
              >Mégse</button
            >
          </div>
        {/if}
      </div>
    </div>
  </form>

  <CustomerNotes {customerDetails}/>
</main>

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
