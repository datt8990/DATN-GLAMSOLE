<template>
  <header class="fixed-top bg-white border-bottom shadow-sm">
    <!-- Top line -->
    <div class="bg-light d-flex justify-content-between align-items-center px-3" style="height: 35px">
      <small class="d-none d-md-block text-muted">📞 Gọi ngay: 0366994505</small>
      <div class="text-center flex-grow-1">
        <small class="text-dark">
          Bước chân <strong>phong cách</strong>, khẳng định <strong>chính mình</strong>
        </small>
      </div>
      <small class="d-none d-md-block text-muted">Về chúng tôi | Trợ giúp</small>
    </div>

    <!-- Main Navbar -->
    <nav class="container-fluid py-2 px-3 px-md-4 d-flex flex-wrap justify-content-between align-items-center">
      <!-- Logo -->
      <a href="/" class="d-flex align-items-center text-decoration-none me-3">
        <img src="/images/logo.jpg" alt="Logo" style="height: 40px; object-fit: contain;" class="me-2" />
        <span class="fw-bold fs-5 text-dark d-none d-md-inline">GLAMSOLE</span>
      </a>

      <!-- Search Form -->
      <form class="d-none d-md-block mx-3" style="max-width: 500px; width: 100%;" @submit.prevent="onSearch">
        <div class="input-group input-group-sm">
          <input type="text" class="form-control" placeholder="Search..." v-model="keyword"
            style="box-shadow: none; outline: none;" />
          <button class="btn" type="submit"
            style="background-color: #4fc3f7; border: none; color: white; box-shadow: none; outline: none;">
            <SearchOutlined style="font-size: 1rem" />
          </button>
        </div>
      </form>

      <!-- Action buttons -->
      <div class="d-flex align-items-center gap-3 position-relative" style="margin-right: 1.5rem;">
        <!-- Dropdown tài khoản -->
        <div class="account-container" ref="accountRef">
          <a href="#" class="d-flex align-items-center text-black text-decoration-none" @click.prevent="toggleDropdown">
            <div style="font-size: 1.5rem; display: flex; align-items: center;">
              <LoginOutlined />
            </div>
            <div class="ms-2 lh-sm">
              <div style="font-size: 0.75rem; font-weight: 500;">Tài khoản</div>
              <div style="font-size: 0.68rem;">Đăng nhập / Đăng ký</div>
            </div>
          </a>

          <!-- Tam giác đỏ -->
          <div v-if="showDropdown" class="triangle-up"></div>

          <!-- Menu xổ xuống -->
          <div v-if="showDropdown" class="account-dropdown-menu shadow-sm">
      <RouterLink
  :to="{ name: ROUTES_CONSTANTS.USERS.children.LOGIN.name }"
  class="dropdown-entry"
  @click="closeDropdown"
>
  <UserOutlined class="me-2 icon-thin" />
  <span class="text">Đăng nhập</span>
</RouterLink>

<RouterLink
  :to="{ name: ROUTES_CONSTANTS.USERS.children.REGISTER.name }"
  class="dropdown-entry border-top"
  @click="closeDropdown"
>
  <UserAddOutlined class="me-2 icon-thin" />
  <span class="text">Đăng ký</span>
</RouterLink>

          </div>


        </div>

        <!-- Nút giỏ hàng -->
        <!-- Nút giỏ hàng gọn đẹp như mẫu -->
        <div class="cart-icon-wrapper position-relative" @click="goToCart" style="cursor: pointer;">
          <ShoppingCartOutlined class="text-black" style="font-size: 1.6rem;" />
          <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
            style="font-size: 0.65rem;">
            {{ cartItemCount }}
          </span>
        </div>

      </div>
    </nav>
  </header>
</template>
<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import {
  LoginOutlined,
  SearchOutlined,
  ShoppingCartOutlined,
  UserAddOutlined,
  UserOutlined
} from '@ant-design/icons-vue'
import { ROUTES_CONSTANTS } from '@/constants/path'

const keyword = ref('')
const showDropdown = ref(false)
const cartItemCount = ref(3)
const accountRef = ref<HTMLElement | null>(null)
const closeDropdown = () => {
  showDropdown.value = false
}

const router = useRouter()

const onSearch = () => {
  const trimmed = keyword.value.trim()
  router.push({ path: '/san-pham', query: { keyword: trimmed } })
}

const goToCart = () => {
  router.push({
    name: ROUTES_CONSTANTS.USERS.children.GIOHANG.name
  })
}

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value
}

// Tự đóng khi click ra ngoài
const handleClickOutside = (event: MouseEvent) => {
  if (accountRef.value && !accountRef.value.contains(event.target as Node)) {
    showDropdown.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>
<style scoped>
.btn-cyan {
  background-color: #4fc3f7;
  border: none;
  color: white;
  transition: background-color 0.3s ease;
}

.btn-cyan:hover {
  background-color: #29b6f6;
  color: white;
}

.btn-outline-cyan {
  background-color: transparent;
  color: #4fc3f7;
  border: 1px solid #4fc3f7;
  transition: all 0.3s ease;
}

.btn-outline-cyan:hover {
  background-color: #4fc3f7;
  color: white;
}

.account-container {
  position: relative;
  display: inline-block;
}

.account-dropdown-menu {
  position: absolute;
  top: calc(100% + 12px);
  left: 50%;
  transform: translateX(-50%);
  background-color: white;
  border-radius: 0.5rem;
  min-width: 180px;
  z-index: 1000;
  padding: 0.5rem 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.dropdown-entry {
  display: flex;
  align-items: center;
  padding: 0.5rem 1rem;
  color: #333;
  font-size: 0.875rem;
  text-decoration: none;
  transition: background-color 0.2s ease;
}

.dropdown-entry:hover {
  background-color: #f8f9fa;
}

.triangle-up {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-bottom: 8px solid red;
}

.account-dropdown-menu {
  position: absolute;
  top: calc(100% + 10px);
  left: 50%;
  transform: translateX(-50%);
  background-color: #fff;
  border-radius: 8px;
  min-width: 180px;
  z-index: 1000;
  padding: 0.25rem 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.dropdown-entry {
  display: flex;
  align-items: center;
  padding: 8px 14px;
  color: #333;
  font-size: 0.85rem;
  text-decoration: none;
  transition: background-color 0.2s ease;
}

.dropdown-entry:hover {
  background-color: #f5f5f5;
}

.icon-thin {
  font-size: 1.1rem;
  color: #4a4a4a;
}

.text {
  font-weight: 400;
  font-size: 0.85rem;
}

.cart-icon-wrapper:hover {
  opacity: 0.85;
}
</style>
