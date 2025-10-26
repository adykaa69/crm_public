<script lang="ts">
  import { type TaskDto } from "$lib/models/task";
  import TaskHeader from "./task-header.svelte";
  import TaskTable from "./task-table.svelte";
  import AddTask from "./elements/add-task.svelte";

  interface Props {
    tasks: TaskDto[];
    sortBy: string;
    showFilter: boolean;
    onSave?: () => void;
    onDelete?: () => void;
  }

  let { tasks, sortBy, showFilter, selectedTasks = false, onSave = () => {}, onDelete = () => {} } = $props();

  let taskAddingEnable = $state(false);

  // Sort options
  const sortOptions = ["Due Date", "Task Name", "Status", "Created Date"];
</script>

<div class="min-h-screen bg-gray-50 p-4 sm:p-6 lg:p-8">
  <div class="rounded-lg bg-white shadow-sm lg:rounded-xl">
    <!-- Header -->
    <TaskHeader {sortBy} {selectedTasks} totalTasks={tasks.length} bind:taskAddingEnable />

    <TaskTable {tasks} {onSave} {onDelete} />
  </div>
</div>

{#if taskAddingEnable}
  <div class="fixed inset-0 z-10 flex items-center justify-center bg-gray-300/40">
    <div class="min-w-[350px] rounded-lg bg-white p-3 shadow-lg">
      <AddTask bind:enable={taskAddingEnable} />
    </div>
  </div>
{/if}

<!-- Click outside to close dropdown -->
{#if showFilter}
  <div
    class="fixed inset-0 z-0"
    role="button"
    tabindex="-1"
    onkeydown={(e) => e.key === "Escape" && (showFilter = false)}
    onclick={() => (showFilter = false)}
  ></div>
{/if}
