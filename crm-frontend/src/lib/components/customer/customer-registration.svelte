<script lang="ts">
  import { isKeyOfTask, type CustomerDto, type ResidenceDto } from "$lib/models/customer";
  import { X } from "@lucide/svelte";

  interface Props {
    enable: boolean;
  }

  let { enable = $bindable() }: Props = $props();

  function disableAddingCustomer(): void {
    enable = false;
  }

  function extendNameWithResidence(name: keyof CustomerDto | keyof ResidenceDto): string {
    if (isKeyOfTask(name)) return `residence.${name}`;
    return name;
  }

  interface FieldGroup {
    title: string;
    id: keyof CustomerDto | keyof ResidenceDto;
  }
</script>

{#snippet generateFields(fields: FieldGroup | Array<FieldGroup>)}
  {#if Array.isArray(fields)}
    <div class="grid grid-cols-{fields.length} gap-4">
      {#each fields as field}
        <div class="space-y-2">
          <label for={field.id} class="text-lg font-bold">{field.title}</label>
          <br />
          <input type="text" id={field.id} name={extendNameWithResidence(field.id)} class="w-full rounded-sm border" />
        </div>
      {/each}
    </div>
  {:else}
    <div class="space-y-2">
      <label for={fields.id} class="text-lg font-bold">{fields.title}</label>
      <br />
      <input type="text" id={fields.id} name={extendNameWithResidence(fields.id)} class="w-full rounded-sm border" />
    </div>
  {/if}
{/snippet}

<form action="/customer?/customerregister" method="POST" class="space-y-6 p-6">
  <div class="sticky top-0 flex items-center justify-between border-b bg-white p-6">
    <h1 class="text-xl font-semibold">Ügyfél felvétele</h1>
    <button
      onclick={disableAddingCustomer}
      class="bg-secondary text-secondary-foreground hover:bg-muted rounded-full p-2 transition"
    >
      <X class="h-4 w-4" />
    </button>
  </div>
  {@render generateFields([
    { title: "Vezetéknév", id: "lastName" },
    { title: "Keresztnév", id: "firstName" }
  ])}
  {@render generateFields([
    { title: "Becenév", id: "nickname" },
    { title: "Ismeretség", id: "relationship" }
  ])}
  {@render generateFields([
    { title: "Email", id: "email" },
    { title: "Telefonszám", id: "phoneNumber" }
  ])}

  <div class="space-y-4">
    <h3 class="text-foreground text-xl font-semibold">Cím</h3>
    {@render generateFields([
      { title: "Ország", id: "country" },
      { title: "Város", id: "city" },
      { title: "Irányítószám", id: "zipCode" }
    ])}
    {@render generateFields({ title: "Lakcím", id: "streetAddress" })}
    {@render generateFields({ title: "Egyéb cím", id: "addressLine2" })}
  </div>
  <div class="bg-background flex justify-end gap-3 border-t p-6">
    <button
      class="bg-cancel-btn/80 hover:bg-cancel-btn border-input rounded-md border px-6 py-2 text-sm font-medium transition"
      onclick={disableAddingCustomer}>Mégse</button
    >
    <button
      class="bg-submit-btn/80 hover:bg-submit-btn rounded-md px-8 py-2 text-sm font-medium text-white transition"
      type="submit">Mentés</button
    >
  </div>
</form>
