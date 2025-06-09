<script setup lang="ts">
const props = defineProps({
  customClasses: String,
  customClassesLabel: String,
  label: String
});
</script>

<template>
  <div class="rounded border-0 bg-secondary-subtle p-3" :class="props.customClasses">
    <div class="bg-white rounded p-3 shadow-sm">
      <div class="mb-4 d-flex justify-content-between align-items-start">
        <h1 v-if="props.label" class="fs-6 fw-bold text-dark m-0" :class="props.customClassesLabel">
          {{ props.label }}
        </h1>
        <div class="d-inline px-3 mt-2">
          <slot name="extra"></slot>
        </div>
      </div>
      <div>
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<style scoped>
.bg-secondary-subtle {
  background-color: white !important;
}

.rounded {
  border-radius: 8px;
}

.bg-white {
  background-color: white;
  border: 1px solid #e0e0e0;
}
</style>
