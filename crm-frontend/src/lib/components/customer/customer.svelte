<script lang="ts">
  import CustomerHeader from "./customer-header.svelte";
  import type { CustomerDto } from "$lib/models/customer";
  import CustomerTable from "./customer-table.svelte";
  import CustomerRegistration from "./customer-registration.svelte";

  interface Props {
    customers: CustomerDto[];
    onSave?: () => void;
    onDelete?: () => void;
    onCancel?: () => void;
  }

  let { customers, onSave, onDelete, onCancel }: Props = $props();

  let customerAddingEnable = $state(false);
</script>

<div class="min-h-screen bg-gray-50 p-4 sm:p-6 lg:p-8">
  <div class="rounded-lg bg-white shadow-sm lg:rounded-xl">
    <div class="fix z-10">
      <CustomerHeader totalCustomers={customers.length} bind:customerAddingEnable />
    </div>
    <div class="fix z-11">
      <CustomerTable {customers} {onSave} {onDelete} {onCancel} />
    </div>
  </div>
</div>

{#if customerAddingEnable}
  <div class="bg-background flex min-h-screen items-center justify-center p-4">
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
      <div class="max-h-[90vh] w-full max-w-2xl overflow-y-auto rounded-lg bg-white shadow-lg">
        <CustomerRegistration bind:enable={customerAddingEnable} />
      </div>
    </div>
  </div>
{/if}
