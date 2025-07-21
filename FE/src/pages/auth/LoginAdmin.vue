<template>
  <div class="container">
    <div class="row align-items-center">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Đăng nhập tại đây" />
    </div>
  </div>
  <div class="container">
    <div class="d-flex justify-content-center">
      <div class="card shadow p-4" style="max-width: 400px; width: 100%;">
        <h4 class="text-center mb-2 fw-semibold text-dark">ĐĂNG NHẬP</h4>
        <p class="text-center text-muted small">
          Hãy <span class="fw-semibold text-dark">hoàn tất đăng nhập</span> để tiếp tục trải nghiệm cùng
          <span class="fw-bold" style="color: #4fc3f7;">GLAMSOLE</span>.
        </p>
        <form @submit.prevent="onLogin">
          <!-- Email -->
          <div class="mb-3">
            <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
            <small class="text-danger" v-if="errors.email">{{ errors.email }}</small>
          </div>

          <!-- Mật khẩu + icon -->
          <div class="mb-3">
            <div class="position-relative">
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                class="form-control"
                placeholder="Mật khẩu"
                autocomplete="current-password"
              />
              <span
                class="toggle-eye"
                @click="togglePassword"
                :title="showPassword ? 'Ẩn mật khẩu' : 'Hiện mật khẩu'"
              >
                <svg v-if="showPassword" xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" viewBox="0 0 16 16">
                  <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288a13.133 13.133 0 0 1-1.66 2.043C11.88 11.332 10.12 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8z"/>
                  <path d="M8 5.5a2.5 2.5 0 1 1 0 5 2.5 2.5 0 0 1 0-5zm0 1a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3z"/>
                </svg>
                <svg v-else xmlns="http://www.w3.org/2000/svg" width="22" height="22" fill="currentColor" viewBox="0 0 16 16">
                  <path d="M13.359 11.238l2.122 2.122-.708.708-2.122-2.122C11.12 12.331 9.44 13.5 8 13.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8a13.133 13.133 0 0 1 1.66-2.043c.465-.488 1.008-.976 1.619-1.432L2.146 3.854l.708-.708 12 12-.708.708-2.122-2.122zm-1.033-1.033l-2.07-2.07a2 2 0 0 0-2.364-2.364l-2.07-2.07C4.12 4.669 5.88 3.5 8 3.5c2.12 0 3.88 1.169 5.168 2.457A13.133 13.133 0 0 1 14.828 8a13.133 13.133 0 0 1-1.66 2.043c-.368.386-.783.772-1.221 1.162z"/>
                </svg>
              </span>
            </div>
            <!-- Dòng báo lỗi ra ngoài position-relative để icon không bị đẩy xuống -->
            <small class="text-danger" v-if="errors.password">{{ errors.password }}</small>
          </div>

          <!-- Nút Đăng nhập -->
          <div class="d-grid mb-2">
            <button type="submit" class="btn text-white d-flex justify-content-center align-items-center"
              :disabled="loading" style="background-color: #4fc3f7; height: 38px;">
              <span v-if="!loading">Đăng nhập</span>
              <div v-else class="spinner-border spinner-border-sm text-light" role="status"></div>
            </button>
          </div>
          <div class="text-center">
            <a href="#" class="small text-muted text-decoration-none">Quên mật khẩu?</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'
import { loginAdmin } from '@/services/api/auth/authentication.api'
import { getUserInformation } from '@/utils/token.helper'
import { useAuthStore } from '@/stores/auth'
import { router } from '@/routes/router'
import { ROUTES_CONSTANTS } from '@/constants/path'

const form = reactive({
  email: '',
  password: ''
})

const errors = reactive({
  email: '',
  password: ''
})

const showPassword = ref(false)
const loading = ref(false)
const breadcrumbRoutes = [
  { name: 'Trang chủ', path: '/admin' },
  { name: 'Đăng nhập' }
]

const authStore = useAuthStore()

const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const onLogin = async () => {
  loading.value = true
  errors.email = ''
  errors.password = ''

  // Validate
  if (!form.email.trim()) {
    errors.email = 'Vui lòng nhập email'
  }
  if (!form.password.trim()) {
    errors.password = 'Vui lòng nhập mật khẩu'
  }

  // Nếu có lỗi validate thì không gọi API và ngừng loading
  if (errors.email || errors.password) {
    loading.value = false
    return
  }

  try {
    const payload = {
      email: form.email,
      password: form.password
    }
    const res = await loginAdmin(payload)

    // Chỉ redirect khi đăng nhập thành công (có accessToken)
    if (res && res.data && res.data.accessToken) {
      const accessToken = res.data.accessToken;
      const refreshToken = res.data.refreshToken;
      const userInfo = getUserInformation(accessToken)

      authStore.login({
        user: userInfo,
        accessToken,
        refreshToken
      })

      router.push({ name: ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.name })
    } 
  } catch (err: any) {
    const errorMessage = err?.response?.data?.message ?? 'Đã xảy ra lỗi. Vui lòng thử lại.'
    errors.password = errorMessage 
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.card {
  border-radius: 0.75rem;
}
.toggle-eye {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 12px;
  margin: auto 0;
  height: 22px;
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #757575;
  z-index: 2;
  user-select: none;
}
.toggle-eye:hover {
  color: #03a9f4;
}
.position-relative {
  position: relative;
}
</style>
