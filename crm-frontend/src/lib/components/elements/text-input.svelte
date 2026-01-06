<script lang="ts">
  interface Props {
    id: string;
    title: string;
    value?: string;
    disabled?: boolean;
  }
  let { id, title, value = $bindable(), disabled = true }: Props = $props();
</script>

<label for={id} class="text-base font-bold md:text-lg">{title}</label>
<br />
<input
  type="text"
  {id}
  name={id}
  class="w-full rounded-sm border bg-[#F6FAFD] px-2 py-1 text-sm shadow-md md:text-base"
  bind:value
  {disabled}
/>
