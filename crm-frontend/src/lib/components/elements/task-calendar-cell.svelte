<script lang="ts">
  import SveltyPicker, { formatDate, parseDate } from "svelty-picker";

  interface Props {
    isEditing?: boolean;
    isCompleted?: boolean;
    date?: Date;
  }

  let { isEditing = false, isCompleted = false, date = $bindable() }: Props = $props();

  let dateTimeToString: string | null = $derived(toString(date));

  function toString(date: Date | undefined) {
    if (!date) {
      return null;
    }
    return dateCustomFormatting(date);
  }

  function updateDateTime() {
    if (dateTimeToString) date = new Date(dateTimeToString);
  }

  function dateCustomFormatting(date: Date): string {
    const padStart = (value: number): string => value.toString().padStart(2, "0");
    return `${date.getFullYear()}-${padStart(date.getMonth() + 1)}-${padStart(date.getDate())} ${padStart(date.getHours())}:${padStart(date.getMinutes())}`;
  }
</script>

{#if isEditing}
  <SveltyPicker
    format="yyyy-mm-dd hh:ii"
    bind:value={dateTimeToString}
    manualInput={true}
    mode="datetime"
    autocommit={true}
    onChange={updateDateTime}
  ></SveltyPicker>
{:else}
  <span class="text-sm text-gray-900 {isCompleted ? 'text-gray-500 line-through' : ''}">{dateTimeToString}</span>
{/if}

<style>
  :global(.dark) {
    --sdt-bg-main: #585858;
    --sdt-shadow-color: #777;
    --sdt-color: #eee;
    --sdt-clock-color: var(--sdt-color);
    --sdt-clock-color-hover: var(--sdt-color);
    --sdt-clock-time-bg: transparent;
    --sdt-clock-time-bg-hover: transparent;
    --sdt-clock-disabled: #b22222;
    --sdt-clock-disabled-bg: var(--sdt-bg-main);
    --sdt-clock-selected-bg: var(--sdt-bg-selected);
    --sdt-header-color: #eee;
    --sdt-bg-selected: #e1ac4a;
    --sdt-table-disabled-date: #b22222;
    --sdt-table-disabled-date-bg: var(--sdt-bg-main);
    --sdt-table-data-bg-hover: #777;
    --sdt-table-selected-bg: var(--sdt-bg-selected);
    --sdt-header-btn-bg-hover: #777;
    --sdt-color-selected: #fff;
    --sdt-table-today-indicator: #ccc;
    --sdt-clock-bg: #999;
    /* custom buttons */
    --sdt-today-bg: #e4a124;
    --sdt-today-color: #fff;
    --sdt-clear-color: #666;
    --sdt-clear-bg: #ddd;
    --sdt-clear-hover-color: #fff;
    --sdt-clear-hover-bg: #dc3545;
  }
  :global(.light) {
    --sdt-bg-main: #fff;
    --sdt-shadow-color: #ccc;
    --sdt-color: inherit;
    --sdt-clock-color: var(--sdt-color);
    --sdt-clock-color-hover: var(--sdt-color);
    --sdt-clock-time-bg: transparent;
    --sdt-clock-time-bg-hover: transparent;
    --sdt-clock-disabled: #b22222;
    --sdt-clock-disabled-bg: var(--sdt-bg-main);
    --sdt-clock-selected-bg: var(--sdt-bg-selected);
    --sdt-bg-selected: #286090;
    --sdt-table-disabled-date: #b22222;
    --sdt-table-disabled-date-bg: var(--sdt-bg-main);
    --sdt-table-data-bg-hover: #eee;
    --sdt-table-selected-bg: var(--sdt-bg-selected);
    --sdt-header-btn-bg-hover: #dfdfdf;
    --sdt-color-selected: #fff;
    --sdt-table-today-indicator: #ccc;
    --sdt-clock-bg: #eeeded;
    /* custom buttons */
    --sdt-today-bg: #1e486d;
    --sdt-today-color: #fff;
    --sdt-clear-color: #dc3545;
    --sdt-clear-bg: #fff;
    --sdt-clear-hover-color: #fff;
    --sdt-clear-hover-bg: #dc3545;
  }
</style>
