<script lang="ts">
  import { TaskStatus, type TaskResponse, type TaskUpdateRequest } from "$lib/models/task";
  import { Check, ChevronDown, Funnel, PenLine, Plus, X } from "@lucide/svelte";
  import TaskHeader from "./task-header.svelte";
  import TaskTable from "./task-table.svelte";

  interface Props {
    tasks: TaskResponse[];
    sortBy: string;
    showFilter: boolean;
    newTaskText: string;
    showAddTask: boolean;
    selectedTasks;
    onSave?: () => void;
    onDelete?: () => void;
  }

  let {
    tasks,
    sortBy,
    showFilter,
    newTaskText,
    showAddTask,
    selectedTasks,
    onSave = () => {},
    onDelete = () => {}
  } = $props();

  // Sort options
  const sortOptions = ["Due Date", "Task Name", "Status", "Created Date"];

  async function addNewTask() {}
</script>

<div class="min-h-screen bg-gray-50 p-4 sm:p-6 lg:p-8">
  <div class="rounded-lg bg-white shadow-sm lg:rounded-xl">
    <!-- Header -->
    <TaskHeader {sortBy} {newTaskText} {showAddTask} {selectedTasks} totalTasks={tasks.length} />

    <!-- Add New Task Form -->
    {#if showAddTask}
      <div class="border-b border-gray-200 bg-blue-50 p-4 sm:p-6">
        <div class="flex flex-col gap-3 sm:flex-row">
          <input
            bind:value={newTaskText}
            placeholder="Enter new task..."
            class="flex-1 rounded-lg border border-gray-300 px-3 py-2 focus:border-transparent focus:ring-2 focus:ring-blue-500 focus:outline-none"
            onkeydown={(e) => e.key === "Enter" && addNewTask()}
          />
          <div class="flex gap-2">
            <button
              onclick={addNewTask}
              class="rounded-lg bg-blue-600 px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-blue-700"
            >
              Add Task
            </button>
            <button
              onclick={() => {
                showAddTask = false;
                newTaskText = "";
              }}
              class="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition-colors hover:bg-gray-50"
            >
              Cancel
            </button>
          </div>
        </div>
      </div>
    {/if}

    <TaskTable {tasks} {onSave} {onDelete} />
  </div>
</div>

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
