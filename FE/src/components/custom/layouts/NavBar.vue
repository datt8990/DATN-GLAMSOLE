<template>
  <header class="fixed-top bg-white border-bottom shadow-sm">
    <!-- Top line -->
    <div class="bg-light d-flex justify-content-between align-items-center px-3" style="height: 35px">
      <small class="d-none d-md-block text-muted">📞 Gọi ngay: 0366994505</small>

      <div class="text-center flex-grow-1">
        <small class="text-dark">
        <small class="text-dark">Bước chân <strong>phong cách</strong>, khẳng định <strong>chính mình</strong></small>

        </small>

      </div>
      <small class="d-none d-md-block text-muted">Về chúng tôi | Trợ giúp</small>

    </div>

    <!-- Main Navbar -->
    <nav class="container-fluid py-2 px-3 px-md-4 d-flex flex-wrap justify-content-between align-items-center">
      <!-- Logo -->
      <a href="/" class="d-flex align-items-center text-decoration-none me-3">
        <img src="/images/logo.jpg" alt="Logo" style="height: 40px; object-fit: contain;" class="me-2" />
        <span class="fw-bold fs-5 text-dark">GLAMSOLE</span>
      </a>

      <!-- Search Form -->
      <form class="d-none d-md-block mx-3" style="max-width: 500px; width: 100%;" @submit.prevent="onSearch">
        <div class="input-group input-group-sm">
          <input type="text" class="form-control" placeholder="Search..." v-model="keyword" />
          <button class="btn" type="submit" style="background-color: #4fc3f7; border: none; color: white;">
            <SearchOutlined style="font-size: 1rem" />
          </button>

        </div>
      </form>

      <!-- Action buttons -->
      <div class="d-flex align-items-center gap-2 gap-md-3 mt-2 mt-md-0">
        <a href="/login" class="btn btn-outline-cyan btn-sm px-3">Sign in</a>
        <a href="/register" class="btn btn-cyan btn-sm px-3 text-white">Sign up</a>

        <button class="btn btn-light position-relative">
          <ShoppingCartOutlined style="font-size: 1.25rem" />
          <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
            style="font-size: 0.7rem">
            3
          </span>
        </button>
      </div>

    </nav>
  </header>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { SearchOutlined, ShoppingCartOutlined } from '@ant-design/icons-vue'

const keyword = ref('')
const router = useRouter()

const onSearch = () => {
  const trimmed = keyword.value.trim()

  router.push({ path: '/san-pham', query: { keyword: trimmed } })

}
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
</style>