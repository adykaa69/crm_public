<script lang="ts">
  import { Plus } from "@lucide/svelte";

  interface Props {
    onclick?: () => void;
    btnName: string;
    plusSign?: boolean;
    type?: "button" | "submit" | "reset" | null | undefined;
  }
  let { onclick, btnName, plusSign = false, type = "button" }: Props = $props();
</script>

<button
  class=" bg-submit-btn/80 hover:bg-submit-btn flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-white transition-colors"
  {onclick}
  {type}
>
  {#if plusSign}
    <Plus class="h-4 w-4" />
  {/if}
  <span class="hidden sm:inline">{btnName}</span>
</button>
