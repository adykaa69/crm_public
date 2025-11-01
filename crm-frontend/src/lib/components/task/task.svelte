<script lang="ts">
  import { TaskStatus, type TaskDto } from "$lib/models/task";
  import AddTask from "./add-task.svelte";
  import TaskHeader from "./task-header.svelte";
  import TaskTable from "./task-table.svelte";

  interface Props {
    tasks: TaskDto[];
    onSave?: () => void;
    onDelete?: () => void;
    onCancel?: () => void;
  }

  let { tasks, onSave, onDelete, onCancel }: Props = $props();
  let selectedStatuses: TaskStatus[] = $state([]);
  let filteredTasks: TaskDto[] = $derived(tasks.filter((t: TaskDto) => selectedStatuses.includes(t.status)));

  let taskAddingEnable = $state(false);
</script>

<div class="min-h-screen bg-gray-50 p-4 sm:p-6 lg:p-8">
  <div class="rounded-lg bg-white shadow-sm lg:rounded-xl">
    <div class="fix z-10">
      <TaskHeader totalTasks={tasks.length} bind:taskAddingEnable bind:selectedStatuses />
    </div>
    <div class="fix z-11">
      <TaskTable tasks={filteredTasks} {onSave} {onDelete} {onCancel} />
    </div>
  </div>
</div>

{#if taskAddingEnable}
  <div class="fixed inset-0 z-10 flex items-center justify-center bg-gray-300/40">
    <div class="min-w-[350px] rounded-lg bg-white p-3 shadow-lg">
      <AddTask bind:enable={taskAddingEnable} />
    </div>
  </div>
{/if}
