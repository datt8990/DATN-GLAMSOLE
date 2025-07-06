<template>
  <div class="container py-3">
    <div class="row align-items-center">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Đăng nhập tại đây" />
    </div>
  </div>
  <div class="container ">

    <!-- Login Box -->
    <div class="d-flex justify-content-center">
      <div class="card shadow p-4" style="max-width: 400px; width: 100%;">
        <h4 class="text-center mb-2 fw-semibold text-dark">ĐĂNG NHẬP</h4>
        <p class="text-center text-muted small">
          Nếu bạn chưa có tài khoản,
          <a href="/register" class="fw-medium text-decoration-none" style="color: #4fc3f7;">đăng ký tại đây</a>
        </p>

        <!-- Form -->
        <form @submit.prevent="onLogin">
          <!-- Email -->
          <div class="mb-3">
            <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
            <small class="text-danger" v-if="errors.email">{{ errors.email }}</small>
          </div>

          <!-- Mật khẩu + nút hiện ẩn -->
          <div class="mb-3 position-relative">
            <input v-model="form.password" :type="showPassword ? 'text' : 'password'" class="form-control"
              placeholder="Mật khẩu" />
            <small class="text-danger" v-if="errors.password">{{ errors.password }}</small>
            <button type="button"
              class="btn btn-sm position-absolute top-50 end-0 translate-middle-y me-2 border-0 bg-transparent"
              @click="togglePassword">
              {{ showPassword ? '🙈' : '👁️' }}
            </button>
          </div>

          <!-- Nút Đăng nhập -->
          <div class="d-grid mb-2">
            <button type="submit" class="btn text-white d-flex justify-content-center align-items-center"
              :disabled="loading" style="background-color: #4fc3f7; height: 38px;">
              <span v-if="!loading">Đăng nhập</span>
              <div v-else class="spinner-border spinner-border-sm text-light" role="status"></div>
            </button>
          </div>


          <!-- Đăng nhập mạng xã hội -->
          <div class="text-center text-muted small mb-2">Hoặc đăng nhập bằng</div>


          <div class="d-grid gap-2 mb-3">
            <!-- Nút đăng nhập Google -->
            <button type="button" class="btn d-flex align-items-center justify-content-center gap-2 border rounded py-2"
              style="background-color: #fff; color: #444;" @click="loginWithGoogle">
              <GoogleOutlined style="font-size: 18px; color: #DB4437;" />
              <span style="font-size: 0.9rem;">Đăng nhập với Google</span>
            </button>

            <!-- Nút đăng nhập GitHub -->
            <button type="button" class="btn d-flex align-items-center justify-content-center gap-2 border rounded py-2"
              style="background-color: #fff; color: #000;" @click="loginWithGithub">
              <GithubOutlined style="font-size: 18px; color: #000;" />
              <span style="font-size: 0.9rem;">Đăng nhập với GitHub</span>
            </button>
          </div>



          <!-- Quên mật khẩu -->
          <div class="text-center">
            <a href="#" class="small text-muted text-decoration-none">Quên mật khẩu?</a>
          </div>

        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { GoogleOutlined, GithubOutlined } from '@ant-design/icons-vue'
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'
import { URL_OAUTH2_GITHUB_USERS, URL_OAUTH2_GOOGLE_USERS } from '@/constants/url'
import { cookieStorageAction } from '@/utils/storage'
import { ACCOUNT_EXIST, ACCOUNT_EXIST_MESSAGE, ACCOUNT_NOT_EXIST, ACCOUNT_NOT_EXIST_MESSAGE, Registered_Awaiting_Confirmation, Registered_Awaiting_Confirmation_MESSAGE, Unverified_Account, Unverified_Account_MESSAGE } from '@/constants/cookie.constant'
import { toast } from 'vue3-toastify'
import { loginUser } from '@/services/api/auth/authentication.api'
import { getUserInformation } from '@/utils/token.helper'
import { router } from '@/routes/router'
import { ROUTES_CONSTANTS } from '@/constants/path'
import { useAuthStore } from '@/stores/auth'

const loading = ref(false)


const form = reactive({
  email: '',
  password: ''
})

const errors = reactive({
  email: '',
  password: ''
})

const showPassword = ref(false)

const togglePassword = () => {
  showPassword.value = !showPassword.value
}


const authStore = useAuthStore()

const onLogin = async () => {
  loading.value = true
  // Reset errors
  errors.email = ''
  errors.password = ''

  // Validate
  if (!form.email.trim()) {
    errors.email = 'Vui lòng nhập email'
  }

  if (!form.password.trim()) {
    errors.password = 'Vui lòng nhập mật khẩu'
  }

  // Nếu không có lỗi thì in ra dữ liệu
  // Nếu không có lỗi thì in ra dữ liệu
  if (!errors.email && !errors.password) {
    try {
      const payload = {
        email: form.email,
        password: form.password
      }

      const res = await loginUser(payload)
      // console.log(res.data)
      const accessToken = res.data.accessToken;
      const refreshToken = res.data.refreshToken;
      const userInfo = getUserInformation(accessToken)

      authStore.login({
        user: userInfo,
        accessToken,
        refreshToken
      })

      router.push({ name: ROUTES_CONSTANTS.USERS.children.TRANGCHU.name })

    } catch (err: any) {
      // Nếu lỗi từ API có message
      const errorMessage = err?.response?.data?.message ?? 'Đã xảy ra lỗi. Vui lòng thử lại.'
      errors.password = errorMessage
    }


  }
}


const breadcrumbRoutes = [
  { name: 'Trang chủ', path: '/' },
  { name: 'Đăng nhập' }
]


const loginWithGoogle = () => {
  window.location.href = URL_OAUTH2_GOOGLE_USERS();
  console.log('Login với Google')
}

const loginWithGithub = () => {
  window.location.href = URL_OAUTH2_GITHUB_USERS();
}

onMounted(() => {

  const accountNotExistError = cookieStorageAction.get(ACCOUNT_NOT_EXIST)

  const accountExistError = cookieStorageAction.get(ACCOUNT_EXIST)

  const unverified_Account = cookieStorageAction.get(Unverified_Account)

  const registered_Awaiting_Confirmation = cookieStorageAction.get(Registered_Awaiting_Confirmation)

  if (accountNotExistError) {
    toast.error(ACCOUNT_NOT_EXIST_MESSAGE)
    cookieStorageAction.remove(ACCOUNT_NOT_EXIST)
  }
  if (accountExistError) {
    toast.error(ACCOUNT_EXIST_MESSAGE)
    cookieStorageAction.remove(ACCOUNT_EXIST)
  }
  if (unverified_Account) {
    toast.warn(Unverified_Account_MESSAGE)
    cookieStorageAction.remove(Unverified_Account)
  }
  if (registered_Awaiting_Confirmation) {
    toast.info(Registered_Awaiting_Confirmation_MESSAGE)
    cookieStorageAction.remove(Registered_Awaiting_Confirmation)
  }
})

</script>

<style scoped>
.card {
  border-radius: 0.75rem;
}
</style>
