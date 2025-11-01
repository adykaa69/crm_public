<script lang="ts">
  import { TaskStatus, type TaskDto } from "$lib/models/task";
  import { Check, PenLine, Trash, X } from "@lucide/svelte";
  import TableCell from "../elements/table-cell.svelte";

  interface Props {
    task: TaskDto;
    isSelected?: boolean;
    onSave?: () => void;
    onDelete?: () => void;
    onCancel?: () => void;
  }
  const taskStatuses: string[] = Object.values(TaskStatus);
  let { task, onSave, onDelete, onCancel }: Props = $props();
  let isEditing = $state(false);
  let taskCache: TaskDto = $derived(task);
  let isCompleted: boolean = $derived(task.status === TaskStatus.COMPLETED);

  async function saveEdit(task: TaskDto) {
    const res = await fetch(`/task?/taskupdate`, {
      method: "POST",
      body: JSON.stringify(task)
    });
    const json = await res.json();
    if (json.error) {
      console.log("error");
      return "hiba";
    }
    isEditing = false;
    onSave?.();
    return json.task;
  }

  function cacheTask() {
    taskCache = task;
  }
  function cancelEdit() {
    taskCache = task;
    isEditing = false;
    onCancel?.();
  }

  async function removeTask() {
    if (confirm(`Biztosan törölni szeretnéd ${task.title} feladatot?`)) {
      const res = await fetch(`/task?/taskdelete`, {
        method: "POST",
        body: JSON.stringify(task)
      });
      if (res.ok) alert(`${task.title} was deleted`);
      else {
        let json = await res.json();
        alert(`Error occured during deleting the task: ${json.errorMessage}`);
      }
      onDelete?.();
    }
  }

  function handleStatusChange(e: Event) {
    const selectedStatus: string = (e.target as HTMLSelectElement).value;
    taskCache.status = TaskStatus[selectedStatus as keyof typeof TaskStatus];
  }

  async function onkeydown(e: KeyboardEvent) {
    if (e.key === "Enter") {
      task = taskCache;
      await saveEdit(task);
    }
    if (e.key === "Escape") cancelEdit();
  }
</script>

<div role="button" class="hidden items-center gap-4 sm:grid sm:grid-cols-30" {onkeydown} tabindex="0">
  <div class="col-span-4">
    {#if isEditing}
      <select value={taskCache.status} onchange={handleStatusChange}>
        {#each taskStatuses as taskStatus}
          <option value={taskStatus}>{taskStatus}</option>
        {/each}
      </select>
    {:else}
      <span class="text-sm text-gray-900 {isCompleted ? 'text-gray-500 line-through' : ''}">
        {taskCache.status}
      </span>
    {/if}
  </div>

  <div class="col-span-6">
    <TableCell cellType="text" bind:text={taskCache.title} {isEditing} {isCompleted} />
  </div>
  <div class="col-span-7">
    <TableCell cellType="text" bind:text={taskCache.description} {isEditing} {isCompleted} />
  </div>
  <div class="col-span-4 flex items-center gap-3">
    <TableCell
      cellType="date"
      bind:date={taskCache.reminder}
      {isEditing}
      isCompleted={task.status === TaskStatus.COMPLETED}
    />
  </div>
  <div class="col-span-4 flex items-center gap-3">
    <TableCell
      cellType="date"
      bind:date={taskCache.dueDate}
      {isEditing}
      isCompleted={task.status === TaskStatus.COMPLETED}
    />
  </div>
  <div class="col-span-4 flex items-center gap-3">
    <TableCell
      cellType="date"
      bind:date={taskCache.completedAt}
      {isEditing}
      isCompleted={task.status === TaskStatus.COMPLETED}
    />
  </div>
  {#if isEditing}
    <div class="col-span-1 flex justify-end gap-3">
      <button
        onclick={(e) => {
          e.preventDefault();
          saveEdit(taskCache);
          isEditing = false;
        }}
        class="p-1 text-gray-400 transition-colors hover:text-gray-600"
      >
        <Check class="h-4 w-4" />
      </button>
      <button onclick={cancelEdit} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
        <X class="h-4 w-4" />
      </button>
    </div>
  {:else}
    <div class="col-span-1 flex justify-end gap-3">
      <button
        onclick={() => {
          cacheTask();
          isEditing = true;
        }}
        class="p-1 text-gray-400 transition-colors hover:text-gray-600"
      >
        <PenLine class="h-4 w-4" />
      </button>
      <button onclick={() => removeTask()} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
        <Trash class="h-4 w-4" />
      </button>
    </div>
  {/if}
</div>
