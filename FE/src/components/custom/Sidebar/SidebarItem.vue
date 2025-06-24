<script setup>
import { ref, computed, watch } from 'vue';
import { useRoute } from 'vue-router';
import SidebarDropdown from './SidebarDropdown.vue';

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const isOpen = ref(false);
const route = useRoute();

const isActive = computed(() => {
  if (props.item.routeName) {
    return route.name === props.item.routeName;
  }
  if (props.item.children && props.item.children.length) {
    return props.item.children.some(child => child.routeName === route.name);
  }
  return false;
});

watch(isActive, (active) => {
  if (active && props.item.children && props.item.children.length) {
    isOpen.value = true;
  }
});

function toggleDropdown() {
  isOpen.value = !isOpen.value;
}
</script>

<template>
  <div>
    <router-link
      v-if="!item.children"
      :to="{ name: item.routeName }"
      class="nav-link"
      :aria-current="isActive ? 'page' : null"
      tabindex="0"
    >
      <span class="icon" v-html="item.icon"></span>
      <span class="label">{{ item.label }}</span>
    </router-link>

    <div v-else>
      <div
        @click="toggleDropdown"
        role="button"
        tabindex="0"
        @keydown.enter.prevent="toggleDropdown"
        @keydown.space.prevent="toggleDropdown"
        :aria-expanded="isOpen.toString()"
        class="nav-link nav-dropdown"
        :class="{ active: isActive || isOpen }"
      >
        <span class="icon" v-html="item.icon"></span>
        <span class="label">{{ item.label }}</span>
        <span class="dropdown-arrow" :class="{ open: isOpen }" aria-hidden="true">
          <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 24 24">
            <polyline points="6 9 12 15 18 9"></polyline>
          </svg>
        </span>
      </div>
      <SidebarDropdown :items="item.children" :isOpen="isOpen" @item-click="toggleDropdown" />
    </div>
  </div>
</template>

<style scoped>
.nav-link {
  display: flex;
  align-items: center;
  padding: 0.625rem 1rem;
  margin-bottom: 0.25rem;
  color: black;
  text-decoration: none;
  font-weight: 600;
  border-radius: 10px;
  transition: background-color 0.3s ease, color 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  user-select: none;
  outline-offset: 2px;
}

.nav-link:hover,
.nav-link:focus {
  background-color: #f3f4f6;
  color: #111827;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  outline: none;
}

.nav-link[aria-current="page"] {
  background-color: #2563eb;
  color: white;
  font-weight: 700;
  box-shadow: 0 2px 12px rgba(37, 99, 235, 0.5);
}

.icon {
  width: 20px;
  height: 20px;
  margin-right: 12px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: inherit;
}

.label {
  flex-grow: 1;
  white-space: nowrap;
}

.nav-dropdown {
  user-select: none;
  justify-content: space-between;
}

.nav-dropdown.active {
  background-color: #e0e7ff;
  color: #3730a3;
  box-shadow: 0 2px 8px rgba(55, 48, 163, 0.3);
  font-weight: 700;
}

.dropdown-arrow {
  transition: transform 0.3s ease;
  display: flex;
  align-items: center;
  color: inherit;
}

.dropdown-arrow.open {
  transform: rotate(180deg);
}
</style>