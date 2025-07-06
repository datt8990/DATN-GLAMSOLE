<template>
    <div class="container ">
        <div class="row align-items-center">
            <BreadCrumbUser :routes="breadcrumbRoutes" title="Đăng nhập tại đây" />
        </div>
    </div>
    <div class="container">

        <!-- Login Box -->
        <div class="d-flex justify-content-center">
            <div class="card shadow p-4" style="max-width: 400px; width: 100%;">
                <h4 class="text-center mb-2 fw-semibold text-dark">ĐĂNG NHẬP</h4>
                <p class="text-center text-muted small">
                    Hãy <span class="fw-semibold text-dark">hoàn tất đăng nhập</span> để tiếp tục trải nghiệm cùng
                    <span class="fw-bold" style="color: #4fc3f7;">GLAMSOLE</span>.
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


import { GoogleOutlined, GithubOutlined } from '@ant-design/icons-vue'
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'
import { URL_OAUTH2_GITHUB_USERS, URL_OAUTH2_GOOGLE_USERS } from '@/constants/url'
import { cookieStorageAction } from '@/utils/storage'
import { ACCOUNT_EXIST, ACCOUNT_EXIST_MESSAGE, ACCOUNT_NOT_EXIST, ACCOUNT_NOT_EXIST_MESSAGE, Registered_Awaiting_Confirmation, Registered_Awaiting_Confirmation_MESSAGE, Unverified_Account, Unverified_Account_MESSAGE } from '@/constants/cookie.constant'
import { toast } from 'vue3-toastify'
import { loginAdmin } from '@/services/api/auth/authentication.api'
import { getUserInformation } from '@/utils/token.helper'
import { useAuthStore } from '@/stores/auth'
import { router } from '@/routes/router'
import { ROUTES_CONSTANTS } from '@/constants/path'

const breadcrumbRoutes = [
    { name: 'Trang chủ', path: '/admin' },
    { name: 'Đăng nhập' }
]
const loading = ref(false)
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

            const res = await loginAdmin(payload)
            // console.log(res.data)
            const accessToken = res.data.accessToken;
            const refreshToken = res.data.refreshToken;
            const userInfo = getUserInformation(accessToken)

            authStore.login({
                user: userInfo,
                accessToken,
                refreshToken
            })

            router.push({ name: ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.name })

        } catch (err: any) {
            // Nếu lỗi từ API có message
            const errorMessage = err?.response?.data?.message ?? 'Đã xảy ra lỗi. Vui lòng thử lại.'
            errors.password = errorMessage
             loading.value = false
        }


    }
}

</script>

<style scoped>
.card {
    border-radius: 0.75rem;
}
</style>
