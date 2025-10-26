<script lang="ts">
  import { invalidateAll } from "$app/navigation";
  import Task from "$lib/components/task.svelte";
  let { data } = $props();
  let sortBy = $state("Due Date");
  let showFilter = $state(false);
  let newTaskText = $state("");
  let showAddTask = $state(false);

  async function handleUpdate() {
    await invalidateAll();
  }
</script>

<Task tasks={data.tasks} {sortBy} {showFilter} onSave={handleUpdate} onDelete={handleUpdate} />
