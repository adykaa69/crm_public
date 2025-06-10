import { items } from "$lib/constants";

export function getTitleByHref(href: string): string {
  const item = items.find((i) => i.href === href);
  return item ? item.title : "";
}
