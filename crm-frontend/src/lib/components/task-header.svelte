<script lang="ts">
  import { TaskStatus } from "$lib/models/task";
  import { Plus } from "@lucide/svelte";
  import Filter from "./elements/filter.svelte";

  interface Props {
    totalTasks: number;
    taskAddingEnable: boolean;
    selectedStatuses: TaskStatus[];
  }

  let { totalTasks, taskAddingEnable = $bindable(), selectedStatuses = $bindable() }: Props = $props();
  let taskStatuses: string[] = $state(Object.keys(TaskStatus));
  let selectedValues: string[] = $state([...taskStatuses]);

  $effect(() => {
    selectedStatuses = toTaskStatusArray();
  });

  function toTaskStatusArray(): TaskStatus[] {
    return selectedValues.filter((value): value is TaskStatus =>
      Object.values(TaskStatus).includes(value as TaskStatus)
    );
  }
</script>

<div
  class="flex flex-col items-start justify-between gap-4 border-b border-gray-200 p-4 sm:flex-row sm:items-center sm:p-6"
>
  <div class="flex items-center gap-4">
    <h1 class="text-lg font-semibold text-gray-900 sm:text-xl">
      Total: {totalTasks} tasks
    </h1>
  </div>

  <div class="flex items-center gap-3 sm:w-auto">
    <div class="z-10">
      <Filter filterParams={taskStatuses} bind:selectedValues />
    </div>

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
