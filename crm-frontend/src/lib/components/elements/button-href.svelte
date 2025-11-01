<script lang="ts">
  import { Plus } from "@lucide/svelte";

  interface Props {
    btnName: string;
    plusSign?: boolean;
    href?: string;
  }
  let { btnName, plusSign = false, href = "#" }: Props = $props();
</script>

<a
  class="bg-submit-btn/80 hover:bg-submit-btn flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-white transition-colors"
  {href}
  aria-label="Add new customer"
>
  {#if plusSign}
    <Plus class="h-4 w-4" />
  {/if}
  {btnName}
</a>
