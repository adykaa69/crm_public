export function nestData<T>(flatData: Record<string, FormDataEntryValue>): T {
  const nested = {} as Record<string, unknown>;
  for (const [key, value] of Object.entries(flatData)) {
    const keys = key.split(".");
    keys.reduce((acc, k, i) => {
      if (i === keys.length - 1) {
        acc[k] = value;
      } else {
        acc[k] = acc[k] ?? {};
      }
      return acc[k] as Record<string, unknown>;
    }, nested);
  }
  return nested as T;
}
