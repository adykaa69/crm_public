<script lang="ts">
  import { page } from "$app/state";
</script>

<h1>{page.status} - Hiba</h1>
<p>{page?.error?.message ?? "A kért oldal nem található"}</p>

<a href="/">Vissza a kezdőlapra</a>

<style>
  h1 {
    color: #d32f2f;
    text-align: center;
  }

  p {
    text-align: center;
    margin: 20px 0;
  }

  a {
    display: block;
    text-align: center;
    color: #4d77ff;
    text-decoration: underline;
    margin-top: 20px;
  }
</style>
