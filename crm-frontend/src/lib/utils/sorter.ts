import type { TaskDto } from "$lib/models/task";

export function sortObjects(array: TaskDto[], field: keyof TaskDto, asc: boolean = true): TaskDto[] {
  return [...array].sort((a, b) => {
    const aVal = a[field];
    const bVal = b[field];

    if (aVal === bVal) return 0;

    // Handle null/undefined
    if (aVal == null) return asc ? -1 : 1;
    if (bVal == null) return asc ? 1 : -1;

    // Date handling
    if (aVal instanceof Date && bVal instanceof Date) {
      return asc ? aVal.getTime() - bVal.getTime() : bVal.getTime() - aVal.getTime();
    }

    // String handling
    if (typeof aVal === "string" && typeof bVal === "string") {
      return asc ? aVal.localeCompare(bVal) : bVal.localeCompare(aVal);
    }

    // Default comparison
    return asc ? (aVal < bVal ? -1 : 1) : aVal > bVal ? -1 : 1;
  });
}
