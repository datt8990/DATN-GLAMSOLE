<template>
   <div class="container py-3">
    <div class="row align-items-center">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Giỏ hàng của bạn" />
    </div>
  </div>
  <div class="container ">

    
    <!-- Header -->
    <div class="d-none d-md-flex border-bottom pb-2 fw-semibold text-muted">
      <div class="form-check me-3">
        <input class="form-check-input" type="checkbox" :checked="isAllSelected" @change="toggleSelectAll" />
      </div>
      <div class="flex-grow-1">Sản Phẩm</div>
      <div class="text-center" style="width: 130px;">Đơn Giá</div>
      <div class="text-center" style="width: 100px;">Số Lượng</div>
      <div class="text-center" style="width: 130px;">Thành Tiền</div>
      <div class="text-center" style="width: 80px;">Thao Tác</div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div
      v-for="item in cartItems"
      :key="item.id"
      class="d-flex flex-column flex-md-row align-items-md-center border-bottom py-3 gap-3"
    >
      <div class="form-check me-md-3 align-self-start">
        <input
          class="form-check-input"
          type="checkbox"
          :value="item.id"
          v-model="selectedIds"
        />
      </div>
      <div class="d-flex flex-grow-1 gap-3">
        <img :src="item.imageUrl" alt="Ảnh giày" class="rounded" style="width: 80px; height: 80px; object-fit: cover;" />
        <div>
          <div class="fw-semibold">{{ item.name }}</div>
          <div class="text-muted small">Phân loại: Màu {{ item.color }} / Size {{ item.size }}</div>
        </div>
      </div>

      <!-- Giá -->
      <div class="text-md-center mt-2 mt-md-0" style="width: 130px;">
        <div v-if="item.discountPrice < item.originalPrice">
          <div class="text-danger fw-bold">{{ item.discountPrice.toLocaleString('vi-VN') }}₫</div>
          <div class="text-muted text-decoration-line-through small">{{ item.originalPrice.toLocaleString('vi-VN') }}₫</div>
        </div>
        <div v-else>
          <div class="fw-bold">{{ item.originalPrice.toLocaleString('vi-VN') }}₫</div>
        </div>
      </div>

      <!-- Số lượng -->
      <div class="text-center mt-2 mt-md-0" style="width: 100px;">
        <div class="d-flex justify-content-center align-items-center gap-2">
          <button class="btn btn-sm btn-outline-secondary px-2" @click="decreaseQuantity(item)">-</button>
          <span>{{ item.quantity }}</span>
          <button class="btn btn-sm btn-outline-secondary px-2" @click="increaseQuantity(item)">+</button>
        </div>
      </div>

      <!-- Thành tiền -->
      <div class="text-center fw-bold text-danger mt-2 mt-md-0" style="width: 130px;">
        {{ (getPrice(item) * item.quantity).toLocaleString('vi-VN') }}₫
      </div>

      <!-- Xóa -->
      <div class="text-center mt-2 mt-md-0" style="width: 80px;">
        <button class="btn btn-link text-danger p-0" @click="removeItem(item)">Xoá</button>
      </div>
    </div>

    <!-- Footer -->
    <div class="mt-4" v-if="cartItems.length > 0">
      <div class="d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center gap-3">
        <div class="form-check">
          <input class="form-check-input" type="checkbox" :checked="isAllSelected" @change="toggleSelectAll" />
          <label class="form-check-label">Chọn Tất Cả ({{ cartItems.length }})</label>
        </div>
        <div class="text-end w-100 w-md-auto">
          <div class="mb-2">
            Tổng tiền hàng ({{ totalSelectedQuantity }} sản phẩm):
            <strong class="text-danger fs-5">{{ totalSelectedPrice.toLocaleString('vi-VN') }}₫</strong>
          </div>
          <button
            class="btn btn-danger px-4 fw-semibold"
            @click="checkout"
            :disabled="selectedIds.length === 0"
            v-if="selectedIds.length > 0"
          >
            Mua Hàng
          </button>
          <div class="text-muted fst-italic" v-else>Chưa có sản phẩm nào được chọn</div>
        </div>
      </div>
    </div>

    <!-- Nếu giỏ hàng trống -->
    <div v-else class="text-center py-5 text-muted">
      <i class="bi bi-cart-x display-4 text-danger mb-3"></i>
      <div class="fw-semibold fs-5">Giỏ hàng của bạn đang trống.</div>
      <p class="mt-2 mb-4">Hãy quay lại cửa hàng để chọn những đôi giày yêu thích nhé!</p>
      <RouterLink to="/" class="btn btn-outline-primary px-4 fw-semibold">
        <i class="bi bi-arrow-left me-2"></i> Tiếp tục mua sắm
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const breadcrumbRoutes = [
  { name: 'Trang chủ', path: '/' },
  { name: 'Sản phẩm', path: '/san-pham' },
  { name: 'Giỏ hàng' }
]


interface CartItem {
  id: number
  name: string
  originalPrice: number
  discountPrice: number
  quantity: number
  imageUrl: string
  color: string
  size: string
}

const cartItems = ref<CartItem[]>([
  {
    id: 1,
    name: 'Giày Sneaker Nam',
    originalPrice: 699000,
    discountPrice: 599000,
    quantity: 1,
    imageUrl: 'https://via.placeholder.com/80x80.png?text=Giay1',
    color: 'Trắng',
    size: '42',
  },
  {
    id: 2,
    name: 'Giày Sneaker Nữ',
    originalPrice: 489000,
    discountPrice: 489000,
    quantity: 2,
    imageUrl: 'https://via.placeholder.com/80x80.png?text=Giay2',
    color: 'Đen',
    size: '38',
  },
])

const selectedIds = ref<number[]>([])

const isAllSelected = computed(() => selectedIds.value.length === cartItems.value.length)

const toggleSelectAll = () => {
  selectedIds.value = isAllSelected.value ? [] : cartItems.value.map((item) => item.id)
}

const getPrice = (item: CartItem) => {
  return item.discountPrice < item.originalPrice ? item.discountPrice : item.originalPrice
}

const increaseQuantity = (item: CartItem) => {
  item.quantity++
}

const decreaseQuantity = (item: CartItem) => {
  if (item.quantity > 1) item.quantity--
}

const removeItem = (item: CartItem) => {
  cartItems.value = cartItems.value.filter((i) => i.id !== item.id)
  selectedIds.value = selectedIds.value.filter((id) => id !== item.id)
}

const totalSelectedPrice = computed(() => {
  return cartItems.value
    .filter((item) => selectedIds.value.includes(item.id))
    .reduce((sum, item) => sum + getPrice(item) * item.quantity, 0)
})

const totalSelectedQuantity = computed(() => {
  return cartItems.value
    .filter((item) => selectedIds.value.includes(item.id))
    .reduce((sum, item) => sum + item.quantity, 0)
})

const router = useRouter()

const checkout = () => {
  if (totalSelectedQuantity.value === 0) {
    alert('Vui lòng chọn ít nhất một sản phẩm để thanh toán.')
    return
  }
  router.push('/thanh-toan')
}
</script>

<style scoped>
.text-decoration-line-through {
  text-decoration: line-through;
}
</style>
