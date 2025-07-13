<template>

  <div class="container py-3">
    <div class="row align-items-center">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Thông tin chi tiết" />
    </div>
  </div>

  <div class="container ">
    <div class="row gx-5 align-items-start">
      <!-- Cột trái: ảnh + mô tả -->
      <div class="col-md-6 d-flex flex-column gap-4">
        <!-- Ảnh -->
        <div class="bg-white border rounded shadow-sm p-3">
          <img :src="typeSelected?.hinh_anh || 'https://via.placeholder.com/400'" class="img-fluid rounded w-100"
            alt="Ảnh sản phẩm" />
        </div>

        <!-- Mô tả -->
        <div class="border-top pt-4">
          <h5 class="fw-bold mb-3">Mô tả sản phẩm</h5>
          <p class="text-muted" style="white-space: pre-line;">
            {{ displayedDescription }}
          </p>
          <button v-if="hasMoreDescription" class="btn btn-sm btn-outline-secondary"
            @click="showFullDescription = !showFullDescription">
            {{ showFullDescription ? 'Thu gọn' : 'Xem thêm' }}
            <i :class="'ri-arrow-' + (showFullDescription ? 'up' : 'down') + '-s-line ms-1'"></i>
          </button>
        </div>
      </div>

      <!-- Cột phải: thông tin + hành động -->
      <div class="col-md-6">
        <div class="bg-white border rounded shadow-sm p-3 d-flex flex-column gap-2">
          <!-- Tên sản phẩm -->
          <h5 class="fw-bold mb-1">{{ product.ten_san_pham }}</h5>
          <div class="text-muted small">
            Thương hiệu: <strong>{{ product.thuong_hieu?.ten_thuong_hieu }}</strong> |
            Xuất xứ: <strong>{{ product.xuat_xu?.ten_xuat_xu }}</strong>
          </div>

          <!-- Giá -->
          <div class="text-danger fw-bold fs-6 mb-2">
            {{ typeSelected?.gia_ban?.toLocaleString('vi-VN') }} ₫
          </div>

          <!-- Màu sắc -->
          <div>
            <label class="form-label small fw-semibold mb-1">Màu sắc</label>
            <div class="d-flex flex-wrap gap-2">
              <div v-for="color in uniqueColors" :key="color.id" class="d-flex flex-column align-items-center"
                style="cursor: pointer; width: 50px;" @click="chooseColor(color)">
                <div class="shadow-sm" :style="{
                  backgroundColor: color.ma_mau,
                  width: '30px',
                  height: '30px',
                  borderRadius: '50%',
                  border: colorSelected?.id === color.id ? '2px solid #0d6efd' : '1px solid #ccc',
                  transition: 'border 0.2s ease'
                }"></div>
                <small class="mt-1 text-muted text-center" style="font-size: 0.7rem;">
                  {{ color.ten_mau_sac }}
                </small>
              </div>
            </div>
          </div>

          <!-- Kích thước -->
          <div>
            <label class="form-label small fw-semibold mb-1">Kích thước</label>
            <div class="d-flex flex-wrap gap-2">
              <span v-for="size in filteredSizes" :key="size.id" class="px-2 py-1 border rounded text-center"
                :class="sizeSelected?.id === size.id ? 'bg-dark text-white' : 'bg-light text-dark'"
                style="min-width: 40px; font-size: 0.85rem; cursor: pointer;" @click="chooseSize(size)">
                {{ size.ten_kich_co }}
              </span>
            </div>
          </div>

          <!-- Số lượng -->
          <div class="d-flex justify-content-between align-items-center">
            <label class="form-label mb-0 small fw-semibold">Số lượng</label>
            <input type="number" class="form-control form-control-sm w-25 text-end" v-model="cart.quantity"
              :max="typeSelected?.so_luong" min="1" />
          </div>

          <!-- Tạm tính -->
          <div class="d-flex justify-content-between align-items-center border-top pt-2 mt-2">
            <span class="fw-semibold small">Tạm tính:</span>
            <span class="fw-bold text-primary small">
              {{ (typeSelected?.gia_ban * cart.quantity).toLocaleString('vi-VN') }} ₫
            </span>
          </div>

          <!-- Cảnh báo -->
          <div v-if="errValidate.cart" class="text-danger small">
            {{ errValidate.cart }}
          </div>

          <!-- Nút hành động -->
          <div class="d-grid gap-1 mt-2">
            <button class="btn btn-outline-primary btn-sm fw-semibold" @click="addToCart">
              <i class="ri-shopping-cart-line me-1"></i> Giỏ hàng
            </button>
            <button class="btn btn-primary btn-sm fw-semibold" @click="buyNow">
              Mua ngay
            </button>
          </div>
        </div>
      </div>



    </div>
  </div>
</template>

<script setup lang="ts">
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue';
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const breadcrumbRoutes = [
  { name: 'Trang chủ', path: '/' },
  { name: 'Sản phẩm', path: '/san-pham' },
  { name: 'Chi tiết sản phẩm', path: '/chi-tiet-san-pham' } 
]


const product = ref({
  ten_san_pham: 'Áo Thun Nam',
  mo_ta: 'Chất liệu cotton cao cấp\nCo giãn, thấm hút tốt\nThiết kế unisex phù hợp mọi đối tượng',
  thuong_hieu: { id: 1, ten_thuong_hieu: 'Coolmate' },
  xuat_xu: { id: 1, ten_xuat_xu: 'Việt Nam' },
  chi_tiet: [
    {
      id: 1,
      mau_sac: {
        id: 1,
        ten_mau_sac: 'Đỏ',
        ma_mau: '#dc3545' 
      },
      kich_co: { id: 1, ten_kich_co: 'M' },
      gia_ban: 199000,
      so_luong: 10,
      hinh_anh: 'https://hthaostudio.com/wp-content/uploads/2019/08/Giay-nam-2.jpg'
    },
    {
      id: 2,
      mau_sac: {
        id: 1,
        ten_mau_sac: 'Đỏ',
        ma_mau: '#dc3545' 
      },
      kich_co: { id: 2, ten_kich_co: 'L' },
      gia_ban: 199000,
      so_luong: 5,
      hinh_anh: 'https://via.placeholder.com/400x400.png?text=Đỏ+L'
    },
    {
      id: 3,
      mau_sac: {
        id: 2,
        ten_mau_sac: 'Xanh',
        ma_mau: '#0d6efd' 
      },
      kich_co: { id: 1, ten_kich_co: 'M' },
      gia_ban: 199000,
      so_luong: 8,
      hinh_anh: 'https://via.placeholder.com/400x400.png?text=Xanh+M'
    }
  ]
})


const cart = ref({ quantity: 1 })
const showFullDescription = ref(false)

const uniqueColors = computed(() => {
  const seen = new Set()
  return product.value.chi_tiet
    .map(ct => ct.mau_sac)
    .filter(color => {
      if (seen.has(color.id)) return false
      seen.add(color.id)
      return true
    })
})

const filteredSizes = computed(() => {
  return product.value.chi_tiet
    .filter(ct => ct.mau_sac.id === colorSelected.value?.id)
    .map(ct => ct.kich_co)
})

const colorSelected = ref(product.value.chi_tiet[0].mau_sac)
const sizeSelected = ref(product.value.chi_tiet[0].kich_co)

const typeSelected = computed(() => {
  return product.value.chi_tiet.find(
    ct =>
      ct.mau_sac.id === colorSelected.value?.id &&
      ct.kich_co.id === sizeSelected.value?.id
  )
})

const displayedDescription = computed(() => {
  return showFullDescription.value
    ? product.value.mo_ta
    : product.value.mo_ta.split('\n').slice(0, 2).join('\n')
})

const hasMoreDescription = computed(() => {
  return product.value.mo_ta.split('\n').length > 2
})

const errValidate = computed(() => {
  if (!typeSelected.value) return { cart: 'Vui lòng chọn đủ màu và kích thước' }
  if (cart.value.quantity > typeSelected.value.so_luong) return { cart: 'Vượt quá tồn kho' }
  return { cart: '' }
})

const chooseColor = color => {
  colorSelected.value = color
}

const chooseSize = size => {
  sizeSelected.value = size
}

const addToCart = () => {
  alert('Đã thêm vào giỏ hàng')
}

const buyNow = () => {
  router.push(`/checkout?ctspId=${typeSelected.value.id}&quantity=${cart.value.quantity}`)
}
</script>
