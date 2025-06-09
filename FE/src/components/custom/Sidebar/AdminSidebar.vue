<script setup lang="ts">
import { useSidebarStore } from "@/stores/sidebar";
import { onClickOutside } from "@vueuse/core";
import { ref } from "vue";
import SidebarItem from "./SidebarItem.vue";
import { ROUTES_CONSTANTS } from "@/constants/path";

const target = ref(null);
const sidebarStore = useSidebarStore();

// onClickOutside(target, () => {
//   sidebarStore.isSidebarOpen = true
// })

const menuGroups = ref([
  {
    menuItems: [
    {
        label: "Quản lý bán hàng",
        icon: `<svg>...</svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.name,
      },
    {
        label: "Quản lý sản phẩm",
        icon: `<svg>...</svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM.name,
      },
      {
        label: "Thuộc tính",
        icon: `<svg>...</svg>`,
        children: [
          {
            label: "Quản lý màu sắc",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.MAUSAC.name,
          },
          {
            label: "Quản lý chất liệu",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.CHAT_LIEU.name,
          },
          {
            label: "Quản lý loại đế",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.LOAI_DE.name,
          },
          {
            label: "Quản lý loại giày",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.LOAI_GIAY.name,
          },
          {
            label: "Quản lý kích thước",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.SIZE.name,
          },
            {
            label: "Quản lý xuất xứ",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.XUAT_XU.name,
          },
          {
            label: "Quản lý thương hiệu",
            icon: `<svg>...</svg>`,
            routeName: ROUTES_CONSTANTS.ADMIN.children.THUONG_HIEU.name,
          },
        ],
      },
      {
        label: "Quản lý hoán đơn",
        icon: `<svg>...</svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.HOA_DON.name,
      },
      {
        label: "Quản lý khách hàng",
        icon: `<svg>...</svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.KHACH_HANG.name,
      },
      {
        label: "Quản lý nhân viên",
        icon: `<svg>...</svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.NHAN_VIEN.name,
      },
        {
        label: "Quản lý phiếu giảm giá",
        icon: `<svg>...</svg>`,
        routeName: ROUTES_CONSTANTS.ADMIN.children.VOUCHER.name,
      },
    ],
  },
]);
</script>

<template>
  <aside
    style="background-color: rgba(0, 21, 40, 255)"
    class="sidebar col-0 col-xl-4 bg-light text-dark vh-100 d-flex flex-column"
    :class="{
      'translate-x-0': !sidebarStore.isSidebarOpen,
      '-translate-x-full': sidebarStore.isSidebarOpen,
    }"
    ref="target"
  >
    <div
      style="background-color: white"
      class="d-flex justify-content-between align-items-center p-3"
    >
      <div class="logo-container text-center mb-4">
        <router-link to="/">
          <img src="/images/logo.jpg" alt="Logo" class="logo" />
        </router-link>
      </div>
    </div>

    <!-- SIDEBAR MENU -->
    <nav class="flex-grow-1 p-3">
      <template v-for="menuGroup in menuGroups" :key="menuGroup.name">
        <div>
          <h3 class="fs-6 text-dark">
            {{ menuGroup.name }}
          </h3>
          <ul class="list-unstyled">
            <SidebarItem
              v-for="(menuItem, index) in menuGroup.menuItems"
              :item="menuItem"
              :key="index"
              :index="index"
              class="nav-link text-light d-flex align-items-center p-2"
            />
          </ul>
        </div>
      </template>
    </nav>
  </aside>
</template>



<style scoped>
aside {
  background-color: white;
}

nav .list-unstyled {
  background-color: white;
}

nav {
  background-color: white;
}

nav .nav-link {
  background-color: white;
  transition: transform 0.3s ease-in-out;
}

.sidebar {
  background-color: white;
  width: 260px;
  transition: transform 0.3s ease-in-out;
}

.translate-x-0 {
  transform: translateX(0);
}

/* Đảm bảo sidebar thu lại */
.-translate-x-full {
  transform: translateX(-80%);
}

/* Logo */
.logo-container {
  margin-right: 20px;
}

.logo {
  width: 200px;
  max-width: 80%;
  display: block;
  margin: 0 auto;
  filter: brightness(1.2);
}

.sidebar-title {
  color: #333 !important; /* màu xám đậm, nhìn rõ ràng */
  font-weight: bold; /* cho nó đậm luôn nếu cần */
  padding-left: 10px; /* căn lề đẹp hơn */
  margin-top: 10px;
}

/* Điều chỉnh button để di chuyển lên trên */
.sidebar-toggle-btn {
  background-color: transparent;
  border: none;
  padding: 0;
  cursor: pointer;
  display: flex;
  justify-content: center;
  /* Căn giữa theo chiều ngang */
  align-items: center;
  /* Căn giữa theo chiều dọc */
  width: 50px;
  height: 50px;
  position: absolute;
  /* Đảm bảo nút nằm trên cùng */
  top: 20px;
  /* Di chuyển lên trên */
  right: 10px;
  /* Đảm bảo nút nằm ở góc phải */
}

/* Điều chỉnh icon trong button */
.sidebar-toggle-btn svg {
  fill: currentColor;
  width: 36px;
  /* Tăng kích thước rộng của icon */
  height: 36px;
  /* Tăng kích thước cao của icon */
  margin: 0 auto;
  /* Căn giữa icon trong button */
  display: block;
  /* Đảm bảo icon là block để căn giữa dễ dàng */
}
</style>
