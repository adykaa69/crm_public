<script lang="ts">
  interface Props {
    text: string;
    isEditing?: boolean;
    onkeydown?: (event: KeyboardEvent) => void;
    isCompleted?: boolean;
  }

  let { text = $bindable(), isEditing = false, onkeydown, isCompleted = false }: Props = $props();
</script>

{#if isEditing}
  <input
    bind:value={text}
    class="w-full rounded border border-gray-300 px-2 py-1 text-sm focus:ring-2 focus:ring-blue-500 focus:outline-none"
    {onkeydown}
  />
{:else}
  <span class="text-sm text-gray-900 {isCompleted ? 'text-gray-500 line-through' : ''}">{text}</span>
{/if}
