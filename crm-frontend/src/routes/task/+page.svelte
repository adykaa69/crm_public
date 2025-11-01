<script lang="ts">
  import { invalidateAll } from "$app/navigation";
  import Task from "$lib/components/task/task.svelte";
  let { data } = $props();

  async function handleUpdate() {
    await invalidateAll();
  }
</script>

<Task tasks={data.tasks} onSave={handleUpdate} onDelete={handleUpdate} onCancel={handleUpdate} />
