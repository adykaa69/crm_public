<script lang="ts">
  import { type TaskDto } from "$lib/models/task";
  import TaskRow from "./task-row.svelte";
  import { sortObjects } from "$lib/utils/sorter";

  interface Props {
    tasks: TaskDto[];
    onSave: () => void;
    onDelete: () => void;
  }

  let { tasks, onSave, onDelete }: Props = $props();

  let sortField: keyof TaskDto | null = $state(null);
  let sortAsc: boolean = $state(true);

  let sortedTasks: TaskDto[] = $derived(sortField ? sortObjects(tasks, sortField, sortAsc) : tasks);

  function toggleSort(field: keyof TaskDto) {
    if (sortField === field) {
      sortAsc = !sortAsc;
    } else {
      sortField = field;
      sortAsc = true;
    }
  }

  function loadMore() {}
</script>

<div class="hidden gap-4 border-b border-gray-200 bg-gray-50 p-4 sm:grid sm:grid-cols-30 sm:p-6">
  <div class="col-span-1 flex items-center">
    <input type="checkbox" class="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
  </div>
  <button
    class="col-span-4 flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort("status")}
  >
    Státusz
    {#if sortField === "status"}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>

  <button
    class="col-span-7 flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort("title")}
  >
    Cím
    {#if sortField === "title"}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>

  <button
    class="col-span-7 flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort("description")}
  >
    Leírás
    {#if sortField === "description"}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>

  <button
    class="col-span-3 flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort("reminder")}
  >
    Emlékeztető
    {#if sortField === "reminder"}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>

  <button
    class="col-span-3 flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort("dueDate")}
  >
    Határidő
    {#if sortField === "dueDate"}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>

  <button
    class="col-span-3 flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort("completedAt")}
  >
    Elvégezve
    {#if sortField === "completedAt"}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>
  <div class="col-span-1 text-right text-sm font-medium text-gray-700"></div>
  <div class="col-span-1 text-right text-sm font-medium text-gray-700"></div>
</div>

<div class="divide-y divide-gray-200">
  {#each sortedTasks as task}
    <div class="p-4 transition-colors hover:bg-gray-50 sm:p-6">
      <!-- Desktop Layout -->
      <TaskRow {task} {onSave} {onDelete}></TaskRow>
    </div>
  {/each}
</div>

<!-- Load More Button -->
<div class="border-t border-gray-200 p-4 text-center sm:p-6">
  <button
    onclick={loadMore}
    class="rounded-lg px-6 py-2 text-sm font-medium text-blue-600 transition-colors hover:bg-blue-50 hover:text-blue-700"
  >
    Load More
  </button>
</div>
