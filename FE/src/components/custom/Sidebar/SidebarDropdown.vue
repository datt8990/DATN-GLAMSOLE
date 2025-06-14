<template>
  <transition name="slide-fade">
    <ul class="dropdown-menu" role="menu" aria-label="Sub menu">
      <template v-for="(childItem, index) in items" :key="index">
        <li class="dropdown-item" role="none">
          <router-link
            :to="{ name: childItem.routeName }"
            @click="handleItemClick(index)"
            class="dropdown-link"
            role="menuitem"
            tabindex="0"
            :aria-current="isActive(childItem) ? 'page' : null"
          >
            <span class="icon" v-html="childItem.icon"></span>
            <span class="label">{{ childItem.label }}</span>
          </router-link>
        </li>
      </template>
    </ul>
  </transition>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";
import { useRoute } from "vue-router";

const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
  isOpen: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(["item-click"]);
const route = useRoute();

function handleItemClick(index) {
  emit("item-click", index);
}

function isActive(item) {
  return route.name === item.routeName;
}
</script>

<style scoped>
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(-8px);
  opacity: 0;
}

.dropdown-menu {
  margin: 0.5rem 0;
  list-style: none;
  padding: 0;
  display: v-bind('isOpen ? "block" : "none"');
  background-color: #f8fafc;
  border-radius: 12px;
  padding: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  max-height: 300px; /* Chiều cao tối đa cho dropdown */
  overflow-y: auto; /* Thêm cuộn dọc nếu nội dung vượt quá chiều cao tối đa */
}

.dropdown-item {
  margin-bottom: 4px;
}

.dropdown-item:last-child {
  margin-bottom: 0;
}

.dropdown-link {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  color: #374151;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  border-radius: 8px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  user-select: none;
  outline-offset: 2px;
  background-color: transparent;
  position: relative;
}

.dropdown-link:hover,
.dropdown-link:focus {
  background-color: #e5e7eb;
  color: #1f2937;
  transform: translateX(2px);
  outline: none;
}

.dropdown-link[aria-current="page"] {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  color: white;
  font-weight: 600;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.dropdown-link[aria-current="page"]:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
  transform: translateX(2px);
}

.icon {
  width: 18px;
  height: 18px;
  margin-right: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: inherit;
}

.icon :deep(svg) {
  width: 100%;
  height: 100%;
}

.label {
  white-space: nowrap;
  font-size: 14px;
  line-height: 1.4;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .dropdown-menu {
    margin-left: 16px;
    padding: 6px;
  }
  
  .dropdown-link {
    padding: 10px 12px;
    font-size: 13px;
  }
  
  .icon {
    width: 16px;
    height: 16px;
    margin-right: 10px;
  }
  
  .label {
    font-size: 13px;
  }
}
</style>