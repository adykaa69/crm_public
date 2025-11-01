<script lang="ts">
  import { page } from "$app/state";
  import { getBtnHrefByHref, getBtnNameByHref, getTitleByHref } from "$lib/utils/handle-constants";
  import ButtonHref from "./elements/button-href.svelte";
  import Logo from "./elements/logo.svelte";

  let title: string = $derived(getTitleByHref(page.url.pathname));
  let btnName: string | undefined = $derived(getBtnNameByHref(page.url.pathname));
  let btnHref: string | undefined = $derived(getBtnHrefByHref(page.url.pathname));
</script>

<header class="header">
  <div class="header-left">
    <a href="/">
      <Logo />
    </a>
  </div>
  <div class="header-center">
    <h1>{title}</h1>
  </div>
  <div class="header-right">
    {#if btnName}
      <ButtonHref {btnName} href={btnHref} plusSign={true} />
      <button class="icon-btn" aria-label="Search">
        <img src="/magnifier_logo.svg" alt="Search" />
      </button>
    {/if}
    <img class="profile-photo" src="/profile.svg" alt="Profile" />
  </div>
</header>

<style>
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    min-height: 45px;
  }

  .header-left {
    display: flex;
    align-items: center;
    flex: 0 0 auto;
  }

  .header-center {
    flex: 1 1 auto;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    min-width: 0;
  }

  .header-center h1 {
    margin: 0;
    font-size: 1.5rem;
    font-weight: 600;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    flex: 0 0 auto;
    padding: 0 1rem;
  }

  /* .add-btn {
    color: #fff;
    border: none;
    border-radius: 50px;
    width: fit-content;
    height: 1.7rem;
  } */

  .add-logo {
    margin: 0.3rem;
    width: 0.5rem;
  }

  .icon-btn {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .profile-photo {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #eee;
  }

  a {
    margin-left: 0.5rem;
    margin-right: 0.5rem;
  }
</style>
