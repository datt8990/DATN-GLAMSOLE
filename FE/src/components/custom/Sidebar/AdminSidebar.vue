<script setup lang="ts">
import { useSidebarStore } from "@/stores/sidebar";
import { ref } from "vue";
import SidebarItem from "./SidebarItem.vue";
import { ROUTES_CONSTANTS } from "@/constants/path";

const sidebarStore = useSidebarStore();

const menuGroups = ref([
  {
    menuItems: [
      {
        label: "Quản lý bán hàng",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M3 3h18v18H3V3z" />
        </svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.name,
      },
      {
        label: "Quản lý sản phẩm",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6l4 2" />
        </svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM.name,
      },
      {
        label: "Thuộc tính",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>`,
        children: [
          {
            label: "Quản lý màu sắc",
            icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
              <circle cx="12" cy="12" r="3" />
            </svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.MAUSAC.name,
          },
          {
            label: "Quản lý chất liệu",
            icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
              <rect x="4" y="4" width="16" height="16" rx="2" ry="2" />
            </svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.CHAT_LIEU.name,
          },
          {
            label: "Quản lý loại đế",
            icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
              <path d="M3 7h18M3 12h18M3 17h18" />
            </svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.LOAI_DE.name,
          },
          {
            label: "Quản lý loại giày",
            icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
              <path d="M6 18L18 6M6 6l12 12" />
            </svg>`,
            label: "Quản lý danh mục", 
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.LOAI_GIAY.name,
          },
          {
            label: "Quản lý kích thước",
            icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
              <circle cx="12" cy="12" r="10" />
            </svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.SIZE.name,
          },
          {
            label: "Quản lý thương hiệu",
            icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-5 h-5">
              <path d="M4 6h16M4 12h16M4 18h16" />
            </svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.THUONG_HIEU.name,
          },
        ],
      },
      {
        label: "Quản lý hoá đơn",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <path d="M4 6h16M4 12h8m-8 6h16" />
        </svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.HOA_DON.name,
      },
      {
        label: "Quản lý khách hàng",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <circle cx="12" cy="7" r="4" />
          <path d="M5 21v-2a4 4 0 018 0v2" />
        </svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.KHACH_HANG.name,
      },
      {
        label: "Quản lý nhân viên",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <circle cx="12" cy="7" r="4" />
          <path d="M16 21v-2a4 4 0 00-8 0v2" />
        </svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.NHAN_VIEN.name,
      },
      {
        label: "Quản lý phiếu giảm giá",
        icon: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-6 h-6">
          <path d="M12 8v8m4.5-4h-9" />
        </svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.VOUCHER.name,
      },
    ],
  },
]);
</script>

<template>
  <aside
    class="sidebar"
    :class="{
      'translate-x-0': !sidebarStore.isSidebarOpen,
      '-translate-x-full': sidebarStore.isSidebarOpen,
    }"
  >
    <div class="header">
      <div class="logo-container">
        <router-link to="/">
          <img src="/images/logo.jpg" alt="Tên thương hiệu logo, biểu tượng màu sắc chủ đạo trắng đen tinh tế" class="logo" />
        </router-link>
      </div>
    </div>

    <nav class="menu" aria-label="Menu điều hướng chính">
      <template v-for="(menuGroup, groupIndex) in menuGroups" :key="groupIndex">
        <div>
          <h3 class="menu-title">{{ menuGroup.name }}</h3>
          <ul class="list-unstyled" role="menu">
            <SidebarItem
              v-for="(menuItem, index) in menuGroup.menuItems"
              :item="menuItem"
              :key="index"
              :index="index"
              class="nav-link"
              role="menuitem"
            />
          </ul>
        </div>
      </template>
    </nav>
  </aside>
</template>

<style scoped>
.sidebar {
  background-color: white;
  width: 260px;
  height: 100vh;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s ease-in-out;
  color: black;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  user-select: none;
  font-family: 'Inter', sans-serif;
  position: relative;
  z-index: 100;
}

.header {
  background-color: white;
  padding: 1rem;
  display: flex;
  justify-content: center;
  border-bottom: 1px solid #ddd;
}

.logo {
  width: 200px;
  max-width: 80%;
  object-fit: contain;
  filter: none;
}

.menu {
  flex-grow: 1;
  padding: 1rem;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.menu-title {
  color: #222222;
  font-weight: 700;
  font-size: 0.875rem;
  margin-bottom: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.list-unstyled {
  margin: 0;
  padding: 0;
  list-style: none;
}

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
}

.nav-link:hover,
.nav-link:focus {
  background-color: #f3f4f6;
  outline: none;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  color: #111827;
  text-decoration: none;
}

.nav-link[aria-current="page"] {
  background-color: #2563eb;
  color: #ffffff;
  font-weight: 700;
  box-shadow: 0 2px 12px rgba(37, 99, 235, 0.4);
}

.translate-x-0 {
  transform: translateX(0);
}

.-translate-x-full {
  transform: translateX(-100%);
}
</style>
