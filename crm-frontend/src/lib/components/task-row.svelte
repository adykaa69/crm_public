<script lang="ts">
  import { parseTaskResponse, TaskStatus, type TaskResponse, type TaskUpdateRequest } from "$lib/models/task";
  import { Check, PenLine, Trash, X } from "@lucide/svelte";
  import TableCell from "./elements/table-cell.svelte";
  import type { httpDelete } from "$lib/common/api";
  import { deleteTask } from "$lib/utils/handle-task";

  interface Props {
    task: TaskResponse;
    isSelected?: boolean;
    onSave?: () => void;
    onDelete?: () => void;
  }
  const taskPath: string = "/api/v1/customers/task";
  const taskStatuses: string[] = Object.values(TaskStatus);
  let { task, isSelected = false, onSave, onDelete }: Props = $props();
  let isEditing = $state(false);
  let taskCache: TaskResponse | TaskUpdateRequest = $state(task);
  let isCompleted: boolean = $state(task.status === TaskStatus.COMPLETED);

  function toggleTaskSelect(id: string) {}

  // FIXME
  function emptyTaskResponse() {
    return {
      id: "",
      customerId: "",
      title: "",
      description: "",
      status: TaskStatus.OPEN,
      reminder: new Date(),
      dueDate: new Date(),
      completedAt: new Date(),
      createdAt: new Date(),
      updatedAt: new Date()
    };
  }

  async function saveEdit(task: TaskResponse | TaskUpdateRequest) {
    const res = await fetch(`/task/rows`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(task)
    });
    const json = await res.json();
    if (json.error) {
      console.log("error");
      return "hiba";
    }
    isEditing = false;
    task = json.task;
    onSave?.();
    return task;
  }

  function logTask(task: TaskResponse | TaskUpdateRequest) {
    console.log(
      `Task: ${task.title} reminder: ${task.reminder} dueDate: ${task.dueDate} completedAt: ${task.completedAt}`
    );
  }
  function cacheTask() {
    taskCache = task;
  }
  function cancelEdit() {
    taskCache = task;
    isEditing = false;
  }

  async function removeTask() {
    if (confirm(`Biztosan törölni szeretnéd ${task.title} feladatot?`)) {
      await deleteTask(task.id);
      alert(`${task.title} was deleted`);
      onDelete?.();
    }
  }

  function handleStatusChange(e: Event) {
    const selectedStatus: string = (e.target as HTMLSelectElement).value;
    taskCache.status = TaskStatus[selectedStatus as keyof typeof TaskStatus];
  }

  function onkeydown(e: KeyboardEvent) {
    if (e.key === "Enter") {
      task = taskCache;
      saveEdit(task).then((jsonTask) => {
        jsonTask;
      });
    }
    if (e.key === "Escape") cancelEdit();
  }
</script>

<div role="button" class="hidden items-center gap-4 sm:grid sm:grid-cols-30" {onkeydown} tabindex="0">
  <div class="col-span-1 flex items-center">
    <input
      type="checkbox"
      checked={isSelected}
      onchange={() => toggleTaskSelect(task.id)}
      class="col-span-1 flex h-4 w-4 items-center rounded border-gray-300 text-blue-600 focus:ring-blue-500"
    />
  </div>
  <div class="col-span-4">
    {#if isEditing}
      <select value={taskCache.status} {onkeydown} onchange={handleStatusChange}>
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
    <div class="col-span-1 flex justify-end">
      <button
        onclick={() => {
          saveEdit(taskCache);
          isEditing = false;
        }}
        class="p-1 text-gray-400 transition-colors hover:text-gray-600"
      >
        <Check class="h-4 w-4" />
      </button>
    </div>
    <div class="col-span-1 flex justify-end">
      <button onclick={cancelEdit} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
        <X class="h-4 w-4" />
      </button>
    </div>
  {:else}
    <div class="col-span-1 flex justify-end">
      <button
        onclick={() => {
          cacheTask();
          isEditing = true;
        }}
        class="p-1 text-gray-400 transition-colors hover:text-gray-600"
      >
        <PenLine class="h-4 w-4" />
      </button>
    </div>
    <div class="col-span-1 flex justify-end">
      <button onclick={() => removeTask()} class="p-1 text-gray-400 transition-colors hover:text-gray-600">
        <Trash class="h-4 w-4" />
      </button>
    </div>
  {/if}
</div>
