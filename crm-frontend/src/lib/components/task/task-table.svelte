<script lang="ts">
  import { type TaskDto } from "$lib/models/task";
  import TaskRow from "./task-row.svelte";
  import { sortObjects } from "$lib/utils/sorter";
  import { render } from "svelte/server";

  interface Props {
    tasks: TaskDto[];
    onSave?: () => void;
    onDelete?: () => void;
    onCancel?: () => void;
  }

  let { tasks, onSave, onDelete, onCancel }: Props = $props();

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

{#snippet generateHeader(btnName: string, toggleName: keyof TaskDto, colSpan: number)}
  <button
    class="col-span-{colSpan} flex cursor-pointer items-center gap-1 text-sm font-medium text-gray-700 hover:text-blue-600"
    onclick={() => toggleSort(toggleName)}
  >
    {btnName}
    {#if sortField === toggleName}
      <span class="text-xs">{sortAsc ? "↑" : "↓"}</span>
    {/if}
  </button>
{/snippet}

<div class="hidden gap-4 border-b border-gray-200 bg-gray-50 p-4 sm:grid sm:grid-cols-30 sm:p-6">
  {@render generateHeader("Státusz", "status", 4)}
  {@render generateHeader("Cím", "title", 6)}
  {@render generateHeader("Leírás", "description", 7)}
  {@render generateHeader("Emlékeztető", "reminder", 4)}
  {@render generateHeader("Határidő", "dueDate", 4)}
  {@render generateHeader("Elvégezve", "completedAt", 4)}
  <div class="col-span-1 text-right text-sm font-medium text-gray-700"></div>
</div>

<div class="divide-y divide-gray-200">
  {#each sortedTasks as task}
    <div class="p-4 transition-colors hover:bg-gray-50 sm:p-6">
      <!-- Desktop Layout -->
      <TaskRow {task} {onSave} {onDelete} {onCancel}></TaskRow>
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
