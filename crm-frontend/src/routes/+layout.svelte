<script lang="ts">
  import "../app.css";

  let { children } = $props();
</script>

<nav class="container">
  <a class="logo" href="/" aria-label="Kezdőlap"></a>
  <a href="/about">Rólunk</a>
</nav>
{@render children()}

<style>
  a {
    margin: 1vw 1vw;
  }

  .logo {
    background-image: url("/bemer_logo.svg");
    background-repeat: no-repeat;
    background-size: contain;
    min-width: 20px;
    min-height: 20px;
    width: 45px;
    height: 45px;
  }

  .container {
    border-style: solid;
    border-color: black;
    border-width: 1px;
    display: flex;
    flex-direction: row;
    max-width: fit-content;
    align-items: center;
    justify-content: center;
    overflow: visible;
    min-width: fit-content;
  }
</style>
