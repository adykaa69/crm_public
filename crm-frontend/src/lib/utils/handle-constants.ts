import { items } from "$lib/constants";

export function getTitleByHref(href: string): string {
  const item = items.find((i) => i.href === href);
  return item ? item.title : "";
}

export function getBtnNameByHref(href: string): string | undefined {
  const item = items.find((i) => i.href === href);
  return item ? item.btnName : "";
}

export function getBtnHrefByHref(href: string): string | undefined {
  const item = items.find((i) => i.href === href);
  return item ? item.btnHref : "";
}
