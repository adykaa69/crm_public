<script lang="ts">
  import { ChevronDown, Funnel, Plus } from "@lucide/svelte";

  interface Props {
    sortBy: string;
    selectedTasks: boolean;
    showFilter?: boolean;
    totalTasks: number;
    taskAddingEnable: boolean;
  }

  let { sortBy, selectedTasks, showFilter = false, totalTasks, taskAddingEnable = $bindable() } = $props();
</script>

<div
  class="flex flex-col items-start justify-between gap-4 border-b border-gray-200 p-4 sm:flex-row sm:items-center sm:p-6"
>
  <div class="flex items-center gap-4">
    <h1 class="text-lg font-semibold text-gray-900 sm:text-xl">
      Total: {totalTasks} tasks
    </h1>
    {#if selectedTasks.length > 0}
      <span class="rounded bg-blue-50 px-2 py-1 text-sm text-blue-600">
        {selectedTasks.length} selected
      </span>
    {/if}
  </div>

  <div class="flex w-full items-center gap-3 sm:w-auto">
    <!-- Sort Dropdown -->
    <div class="relative flex-1 sm:flex-none">
      <button
        class="flex w-full min-w-[140px] items-center justify-between gap-2 rounded-lg border border-gray-300 px-3 py-2 text-sm transition-colors hover:bg-gray-50 sm:w-auto"
        onclick={() => (showFilter = !showFilter)}
      >
        <span class="text-gray-700">Sort by: {sortBy}</span>
        <ChevronDown class="h-4 w-4 text-gray-500" />
      </button>

      {#if showFilter}
        <div
          class="absolute top-full right-0 left-0 z-10 mt-1 rounded-lg border border-gray-200 bg-white shadow-lg sm:right-auto sm:w-48"
        >
          {#each sortOptions as option}
            <button
              class="w-full px-3 py-2 text-left text-sm transition-colors hover:bg-gray-50 {sortBy === option
                ? 'bg-blue-50 text-blue-600'
                : 'text-gray-700'}"
              onclick={() => {
                sortBy = option;
                showFilter = false;
              }}
            >
              {option}
            </button>
          {/each}
        </div>
      {/if}
    </div>

    <!-- Filter Button -->
    <button
      class="flex items-center gap-2 rounded-lg border border-gray-300 px-3 py-2 text-sm transition-colors hover:bg-gray-50"
    >
      <Funnel class="h-4 w-4" />
      <span class="hidden sm:inline">Filter</span>
    </button>

    <!-- Add Task Button -->
    <button
      class="add-task-btn flex items-center gap-2 rounded-lg px-3 py-2 text-sm text-white transition-colors hover:bg-blue-700"
      onclick={() => (taskAddingEnable = true)}
    >
      <Plus class="h-4 w-4" />
      <span class="hidden sm:inline">Add Task</span>
    </button>
  </div>
</div>

<style>
  .add-task-btn {
    background-color: #514ef390;
  }

  .add-task-btn:hover {
    background-color: #514ef3;
  }
</style>
