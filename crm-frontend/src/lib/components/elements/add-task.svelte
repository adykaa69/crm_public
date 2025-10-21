<script lang="ts">
  import { TaskStatus } from "$lib/models/task";
  import TaskCalendarCell from "./task-calendar-cell.svelte";

  interface Props {
    taskEnable: boolean;
  }

  let { taskEnable = $bindable() }: Props = $props();
  const taskStatuses: string[] = Object.values(TaskStatus);
  let reminder: Date | undefined = $state();
  let dueDate: Date | undefined = $state();
  let completedAt: Date | undefined = $state();
</script>

<div class="container mx-auto w-100 justify-items-center">
  <h2 class="mb-3 text-xl font-bold">Új feladat létrehozása</h2>
  <div>
    <label for="status" class="text-lg font-bold">Státusz</label>
    <div>
      <select name="status" id="status">
        {#each taskStatuses as taskStatus}
          <option value={taskStatus}>{taskStatus}</option>
        {/each}
      </select>
    </div>
  </div>
  <div>
    <label for="title" class="text-lg font-bold">Cím</label>
    <br />
    <input type="text" id="title" name="title" class="rounded-sm border-1" />
  </div>
  <div>
    <label for="description" class="w-55 text-lg font-bold">Leírás</label>
    <br />
    <textarea id="description" name="description" class="h-32 w-55 resize-y overflow-auto rounded-sm border-1 p-2"
    ></textarea>
    <!-- <input type="text" id="description" name="description" class="rounded-sm border-1" /> -->
  </div>
  <div>
    <label for="reminder" class="text-lg font-bold">Emlékeztető</label>
    <br />
    <TaskCalendarCell isEditing={true} bind:date={reminder}></TaskCalendarCell>
  </div>
  <div>
    <label for="dueDate" class="text-lg font-bold">Határidő</label>
    <br />
    <TaskCalendarCell isEditing={true} bind:date={dueDate}></TaskCalendarCell>
  </div>
  <div>
    <label for="completedAt" class="text-lg font-bold">Elvégezve</label>
    <br />
    <TaskCalendarCell isEditing={true} bind:date={completedAt}></TaskCalendarCell>
  </div>
  <div class="row">
    <button class="rounded bg-gray-200 px-4 py-2 hover:bg-gray-300">Mégse</button>
    <button
      class="bg-submit-btn/80 hover:bg-submit-btn m-2 rounded p-3 px-4 py-2 text-white"
      onclick={() => (taskEnable = !taskEnable)}>Mentés</button
    >
  </div>
</div>

<style>
  @import "tailwindcss";

  @theme {
    --color-submit-btn: #514ef3;
  }
</style>
