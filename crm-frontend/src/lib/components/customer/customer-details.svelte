<script lang="ts">
  import { type CustomerDetailsDto, type CustomerDto } from "$lib/models/customer";
  import { dateCustomFormatting } from "$lib/utils/formatter";
  import TextInput from "../elements/text-input.svelte";

  interface Props {
    customer: CustomerDto;
    customerDetails: CustomerDetailsDto[];
  }

  let { customer, customerDetails }: Props = $props();

  let inputDisabled: boolean = $state(true);

  function toggleDisable() {
    inputDisabled = !inputDisabled;
  }
</script>

{#snippet createInput(id: string, title: string, value?: string)}
  <div>
    <TextInput {id} {title} {value} disabled={inputDisabled} />
  </div>
{/snippet}

<main class="flex w-full flex-1 flex-col gap-4 md:flex-row md:gap-0">
  <form action="" class="mx-2 flex-1 md:mx-4">
    <div class="m-2 rounded-sm bg-white p-3 shadow-md md:m-4 md:p-4">
      <div class="grid grid-cols-1 gap-3 sm:grid-cols-2 md:gap-4">
        <h1 class="col-span-1 m-2 justify-self-center text-2xl font-semibold sm:col-span-2 md:m-4 md:text-3xl">
          Ügyfél adatok
        </h1>
        {@render createInput("lastName", "Vezetéknév", customer.lastName)}
        {@render createInput("firstName", "Keresztnév", customer.firstName)}
        {@render createInput("nickName", "Megszólítás", customer.nickname)}
        {@render createInput("relationship", "Ismeretség", customer.relationship)}
        {@render createInput("email", "Email", customer.email)}
        {@render createInput("phoneNumber", "Telefonszám", customer.phoneNumber)}
      </div>
      <div class="mt-3 grid grid-cols-1 gap-3 sm:grid-cols-3 md:mt-4 md:gap-4">
        {@render createInput("country", "Ország", customer.residence?.country)}
        {@render createInput("city", "Település", customer.residence?.city)}
        {@render createInput("zipCode", "Irányítószám", customer.residence?.zipCode)}
        <div class="col-span-1 sm:col-span-3">
          <TextInput
            id="streetAddress"
            title="Lakcím"
            value={customer.residence?.streetAddress}
            disabled={inputDisabled}
          />
        </div>
        <div class="col-span-1 sm:col-span-3">
          <TextInput
            id="addressLine2"
            title="Egyéb cím"
            value={customer.residence?.addressLine2}
            disabled={inputDisabled}
          />
        </div>
      </div>
      {#if inputDisabled}
        <button type="button" class="crm-btn m-3" onclick={toggleDisable}>Szerekesztés engedélyezése</button>
      {:else}
        <button type="submit" class="crm-btn m-3">Módosítás</button>
        <button type="button" class="bg-cancel-btn/80 hover:bg-cancel-btn rounded px-4 py-2" onclick={toggleDisable}
          >Mégse</button
        >
      {/if}
    </div>
  </form>

  <div class="m-2 mx-2 mb-16 flex-1 rounded-sm bg-white shadow-md md:m-4 md:mx-4 md:mb-0">
    <form action="" class="m-3 md:m-4">
      <div class="grid grid-cols-1 gap-3 md:gap-4">
        <h1 class="m-2 justify-self-center text-2xl font-semibold md:m-4 md:text-3xl">Megjegyzések</h1>
        <div>
          <textarea
            id="notice"
            name="notice"
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
          {#if detail.updatedAt}
            <div class="mt-3">
              <label for="description" class="date text-xs md:text-sm">{dateCustomFormatting(detail.updatedAt)}</label>
              <p
                id="description"
                class="description note min-h-15 rounded-sm border bg-[#F6FAFD] p-2 text-sm shadow-md md:text-base"
              >
                {detail.note}
              </p>
            </div>
          {/if}
        {/each}
      </div>
    </div>
  </div>
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
