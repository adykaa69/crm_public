export enum ActionParameters {
  REGISTER = "register",
  UPDATE = "update"
}

export const items = [
  {
    href: "/",
    activeIcon: "/dashboard_active_logo.svg",
    inactiveIcon: "/dashboard_inactive_logo.svg",
    label: "dashboard",
    title: "Kezdőoldal",
    btnName: "Hozzáadás",
    btnHref: "/"
  },
  {
    href: "/customer",
    activeIcon: "/customer_active_logo.svg",
    inactiveIcon: "/customer_inactive_logo.svg",
    label: "customer",
    title: "Ügyfelek",
    btnName: "Ügyfél hozzáadása",
    btnHref: "/customer/registration"
  },
  {
    href: "/about",
    activeIcon: "/about_active_logo.svg",
    inactiveIcon: "/about_inactive_logo.svg",
    label: "about",
    title: "Rólunk"
  }
];
