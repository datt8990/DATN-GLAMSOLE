<template>
    
      <div class="container py-3">
    <div class="row align-items-center">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Đăng ký tại đây" />
    </div>
  </div>

    <div class="container ">
        <div class="d-flex justify-content-center">
            <div class="card shadow p-4" style="max-width: 400px; width: 100%;">
                <h4 class="text-center mb-2 fw-semibold text-dark">ĐĂNG KÝ</h4>
                <p class="text-center text-muted small">
                    Đã có tài khoản?
                    <a href="/login" class="text-decoration-none fw-medium" style="color: #4fc3f7;">Đăng nhập tại
                        đây</a>
                </p>

                <form @submit.prevent="onRegister">
                    <!-- Họ tên -->
                    <div class="mb-3">
                        <input v-model="form.hoTen" type="text" class="form-control" placeholder="Họ tên" />
                        <small class="text-danger" v-if="errors.hoTen">{{ errors.hoTen }}</small>
                    </div>

                    <!-- Email -->
                    <div class="mb-3">
                        <input v-model="form.email" type="email" class="form-control" placeholder="Email" />
                        <small class="text-danger" v-if="errors.email">{{ errors.email }}</small>
                    </div>

                    <!-- Số điện thoại -->
                    <div class="mb-3">
                        <input v-model="form.soDienThoai" type="text" class="form-control"
                            placeholder="Số điện thoại" />
                        <small class="text-danger" v-if="errors.soDienThoai">{{ errors.soDienThoai }}</small>
                    </div>

                    <!-- Mật khẩu -->
                    <div class="mb-3">
                        <input v-model="form.matKhau" type="password" class="form-control" placeholder="Mật khẩu" />
                        <small class="text-danger" v-if="errors.matKhau">{{ errors.matKhau }}</small>
                    </div>

                    <!-- Xác nhận mật khẩu -->
                    <div class="mb-3">
                        <input v-model="form.xacNhanMatKhau" type="password" class="form-control"
                            placeholder="Xác nhận mật khẩu" />
                        <small class="text-danger" v-if="errors.xacNhanMatKhau">{{ errors.xacNhanMatKhau }}</small>
                    </div>

                    <!-- Nút đăng ký -->
                    <div class="d-grid mb-2">
                        <button type="submit" class="btn btn-custom-green text-white fw-medium">Đăng ký</button>
                    </div>
                </form>

            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'
import { reactive } from 'vue'

const breadcrumbRoutes = [
  { name: 'Trang chủ', path: '/' },
  { name: 'Đăng ký' } 
]

const form = reactive({
    hoTen: '',
    email: '',
    soDienThoai: '',
    matKhau: '',
    xacNhanMatKhau: ''
})

const errors = reactive({
    hoTen: '',
    email: '',
    soDienThoai: '',
    matKhau: '',
    xacNhanMatKhau: ''
})

const validate = () => {
    let valid = true

    errors.hoTen = form.hoTen ? '' : 'Vui lòng nhập họ tên'
    errors.email = /^\S+@\S+\.\S+$/.test(form.email) ? '' : 'Email không hợp lệ'
    errors.soDienThoai = /^[0-9]{9,11}$/.test(form.soDienThoai) ? '' : 'Số điện thoại không hợp lệ'
    errors.matKhau = form.matKhau ? '' : 'Vui lòng nhập mật khẩu'
    errors.xacNhanMatKhau =
        form.xacNhanMatKhau === form.matKhau && form.xacNhanMatKhau
            ? ''
            : 'Mật khẩu xác nhận không khớp'
    
    Object.values(errors).forEach((err) => {
        if (err) valid = false
    })

    return valid
}

const onRegister = () => {
    if (validate()) {
        console.log(' Dữ liệu đăng ký:', { ...form })
        alert('Đăng ký thành công!')
    } else {
        console.warn('Form không hợp lệ')
    }
}
</script>
<style scoped>
.card {
    border-radius: 0.75rem;
}

.btn-custom-green {
    background-color: #4fc3f7;
    border: none;
    transition: background-color 0.3s ease-in-out;
}

.btn-custom-green:hover {
    background-color: #03a9f4;
}
</style>
