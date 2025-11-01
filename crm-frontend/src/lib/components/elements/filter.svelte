<script lang="ts">
  import { ChevronDown } from "@lucide/svelte";
  import { on } from "svelte/events";
  interface Props {
    filterParams: string[];
    selectedValues: string[];
  }

  let { filterParams, selectedValues = $bindable([...filterParams]) }: Props = $props();

  let enable: boolean = $state(false);
  let container: HTMLDivElement, button: HTMLButtonElement;

  $effect(() => {
    if (!enable) return;

    const removeClick = on(document, "click", (event) => {
      if (!container?.contains(event.target as Node) && !button?.contains(event.target as Node)) {
        enable = false;
      }
    });

    const removeEscape = on(document, "keydown", (event) => {
      if (event.key === "Escape") {
        enable = false;
      }
    });

    return () => {
      removeClick();
      removeEscape();
    };
  });

  function toggleDropDown() {
    enable = !enable;
    console.log(selectedValues);
  }
</script>

<div bind:this={container} class="relative inline-block">
  <button
    id="dropdownDefault"
    class="inline-flex items-center rounded-lg px-4 py-2.5 text-center text-sm font-medium focus:ring-4"
    type="button"
    onclick={toggleDropDown}
    bind:this={button}
  >
    Szűrő
    <ChevronDown></ChevronDown>
  </button>
  {#if enable}
    <!-- <div id="dropdown" class="fixed mt-13 inline-block rounded-lg bg-white p-3 shadow-lg"> -->
    <ul class="absolute top-full right-0 z-50 mt-2 w-fit min-w-max space-y-2 rounded-lg bg-white p-3 text-sm shadow">
      {#each filterParams as filterParam}
        <li class="flex items-center gap-2" onblur={toggleDropDown}>
          <input bind:group={selectedValues} type="checkbox" value={filterParam} class="h-4 w-4" />
          <span class="text-sm font-medium">{filterParam}</span>
        </li>
      {/each}
    </ul>
  {/if}
</div>
