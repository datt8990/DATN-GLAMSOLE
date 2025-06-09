<script setup lang="ts">
const props = defineProps(['pageTitle']);
</script>

<template>
  <div class="mb-6 d-flex flex-col justify-content-between">
    <h2 class="text-title-sm font-semibold text-black dark:text-white ms-3">
      {{ props.pageTitle }}
    </h2>
  </div>
</template>

<style scoped>
.text-title-sm {
  font-size: 1.5rem;
}
</style>
