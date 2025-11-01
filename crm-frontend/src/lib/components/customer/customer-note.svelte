<script lang="ts">
  import type { CustomerDetailsResponse } from "$lib/models/customer-details";

  export let customerDetails: CustomerDetailsResponse;
</script>

<div class="rounded-lg border bg-gray-50 p-4">
  <div class="mb-2 flex items-start justify-between">
    <span class="inline-block rounded-full bg-blue-100 px-2 py-1 text-xs text-blue-800">
      {customerDetails.createdAt}
    </span>
    <!-- TODO author need to add at a later stage -->
    <!-- <span class="text-xs text-gray-500">{note.author}</span> -->
  </div>
  <p class="text-sm leading-relaxed text-gray-700">{customerDetails.note}</p>
</div>
