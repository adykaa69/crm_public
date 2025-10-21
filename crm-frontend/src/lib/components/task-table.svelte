<script lang="ts">
  import { TaskStatus, type TaskDto } from "$lib/models/task";
  import { Check, PenLine, X } from "@lucide/svelte";
  import TaskRow from "./task-row.svelte";
  import AddTask from "./elements/add-task.svelte";

  interface Props {
    tasks: TaskDto[];
    onSave: () => void;
    onDelete: () => void;
  }

  let { tasks, onSave, onDelete } = $props();
  let selectAll: boolean = false;
  let editingTask: string | undefined | null;
  let taskEnable = $state(false);

  function toggleSelectAll() {}
  function toggleTaskComplete(taskId: string) {}

  function toggleTaskSelect(taskId: string) {}

  function startEditing(taskId: string) {
    editingTask = taskId;
  }

  // FIXME move to server side
  async function saveEdit(updatedRow: TaskDto) {
    const res = await fetch(`/task/rows`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updatedRow)
    });
    const json = await res.json();
    if (json.error) {
      console.log("error");
      return "hiba";
    }
    editingTask = null;
    return json.task;
  }

  function cancelEdit() {
    editingTask = null;
  }

  function getStatusIcon(task: TaskDto) {
    if (task.status === TaskStatus.COMPLETED) {
      return { component: Check, class: "w-4 h-4 text-white", bgClass: "bg-green-500" };
    } else if (task.status === TaskStatus.OPEN) {
      return { component: X, class: "w-4 h-4 text-white", bgClass: "bg-red-500" };
    } else {
      return { component: null, class: "", bgClass: "bg-gray-300" };
    }
  }

  function loadMore() {}
</script>

<!-- Table Header -->
<div class="hidden gap-4 border-b border-gray-200 bg-gray-50 p-4 sm:grid sm:grid-cols-30 sm:p-6">
  <div class="col-span-1 flex items-center">
    <input
      type="checkbox"
      checked={selectAll}
      onchange={toggleSelectAll}
      class="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
    />
  </div>
  <div class="col-span-4 text-sm font-medium text-gray-700">Státusz</div>
  <div class="col-span-7 text-sm font-medium text-gray-700">Cím</div>
  <div class="col-span-7 text-sm font-medium text-gray-700">Leírás</div>
  <div class="col-span-3 text-sm font-medium text-gray-700">Emlékeztető</div>
  <div class="col-span-3 text-sm font-medium text-gray-700">Határidő</div>
  <div class="col-span-3 text-sm font-medium text-gray-700">Elvégezve</div>
  <div class="col-span-1 text-right text-sm font-medium text-gray-700"></div>
  <div class="col-span-1 text-right text-sm font-medium text-gray-700"></div>
</div>

<!-- Task List -->
<div class="divide-y divide-gray-200">
  {#each tasks as task (task.id)}
    <div class="p-4 transition-colors hover:bg-gray-50 sm:p-6">
      <!-- Mobile Layout -->
      <div class="space-y-3 sm:hidden">
        <div class="flex items-start gap-3">
          <div class="mt-1 flex flex-shrink-0 items-center gap-3">
            <input
              type="checkbox"
              checked={task.selected || false}
              onchange={() => toggleTaskSelect(task.id)}
              class="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
            />
            <button
              onclick={() => toggleTaskComplete(task.id)}
              class="flex h-6 w-6 items-center justify-center rounded-full transition-colors {getStatusIcon(task)
                .bgClass}"
            >
              {#if getStatusIcon(task).component}
                {@const SvelteComponent = getStatusIcon(task).component}
                <SvelteComponent class={getStatusIcon(task).class} />
              {/if}
            </button>
          </div>

          <div class="min-w-0 flex-1">
            <div class="mb-1 text-sm text-gray-500">
              {#if task.dueDate}
                {task.dueDate.toLocaleDateString()}
              {/if}
            </div>
            {#if editingTask === task.id}
              <div class="flex gap-2">
                <input
                  value={task.title}
                  class="flex-1 rounded border border-gray-300 px-2 py-1 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
                  onkeydown={(e) => {
                    if (e.key === "Enter") saveEdit(task.id, e.target.value);
                    if (e.key === "Escape") cancelEdit();
                  }}
                  onblur={(e) => saveEdit(task.id, e.target.value)}
                />
              </div>
            {:else}
              <div class="text-sm text-gray-900 {task.completedAt ? 'text-gray-500 line-through' : ''}">
                {task.title}
              </div>
            {/if}
          </div>

          <button
            onclick={() => startEditing(task.id)}
            class="flex-shrink-0 p-1 text-gray-400 transition-colors hover:text-gray-600"
          >
            <PenLine class="h-4 w-4" />
          </button>
        </div>
      </div>

      <!-- Desktop Layout -->
      <!-- <div class="hidden items-center gap-4 sm:grid sm:grid-cols-30"> -->
      <TaskRow {task} {onSave} {onDelete}></TaskRow>
      <!-- </div> -->
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

// FIXME need to create addTask button function
{#if taskEnable}
  <div class="fixed inset-0 z-10 flex items-center justify-center bg-gray-300/40">
    <div class="min-w-[350px] rounded-lg bg-white p-3 shadow-lg">
      <AddTask bind:taskEnable />
    </div>
  </div>
{/if}
