<script lang="ts">
  import Button from "../elements/button.svelte";

  interface Props {
    totalCustomers: number;
    customerAddingEnable: boolean;
  }

  let { totalCustomers, customerAddingEnable = $bindable() }: Props = $props();
</script>

<div
  class="flex flex-col items-start justify-between gap-4 border-b border-gray-200 p-4 sm:flex-row sm:items-center sm:p-6"
>
  <div class="flex items-center gap-4">
    <h1 class="text-lg font-semibold text-gray-900 sm:text-xl">
      Összes ügyfélszám: {totalCustomers}
    </h1>
  </div>

  <div class="flex items-center gap-3 sm:w-auto">
    <Button btnName="Ügyfél hozzáadása" onclick={() => (customerAddingEnable = true)} plusSign={true} />
  </div>
</div>
