<script lang="ts" module>
  export type CellType = "text" | "date" | "combo";
</script>

<script lang="ts">
  import TaskCalendarCell from "./task-calendar-cell.svelte";

  import TaskTextCell from "./task-text-cell.svelte";

  interface Props {
    cellType?: CellType;
    text?: string;
    isEditing?: boolean;
    onkeydown?: (event: KeyboardEvent) => void;
    isCompleted: boolean;
    date?: Date;
    onSave?: () => void;
    onCancel?: () => void;
  }

  let {
    cellType = "text",
    text = $bindable(""),
    isEditing = false,
    isCompleted,
    date = $bindable(),
    onSave,
    onCancel
  }: Props = $props();

  function onkeydown(e: KeyboardEvent) {
    const target = e.target as HTMLInputElement;
    if (!target) return;
    if (e.key === "Enter") {
      onSave?.();
    }
    if (e.key === "Escape") onCancel?.();
  }
</script>

{#if cellType === "text"}
  <TaskTextCell bind:text {isEditing} {onkeydown} {isCompleted} />
{:else if cellType === "date"}
  <TaskCalendarCell {isCompleted} {isEditing} bind:date />
{/if}
