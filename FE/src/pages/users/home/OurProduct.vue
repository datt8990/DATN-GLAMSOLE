<template>
  <div class="container py-5  rounded">
    <!-- Tiêu đề -->
    <div class="d-flex justify-content-between align-items-center mb-4">
      <div class="d-flex align-items-center">
        <StarFilled class="me-2" style="color: #fadb14; font-size: 1.5rem;" />
        <h2 class="fw-bold mb-0">Sản phẩm nổi bật</h2>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <div class="row g-4">
      <div v-for="item in visibleProducts" :key="item.id" class="col-6 col-md-4 col-lg-3">
        <div class="card h-100 shadow-sm border-0 product-card" @click="handleClick(item)">
          <div class="product-img-wrapper">
            <img :src="item.imageUrl" class="card-img-top" alt="Ảnh sản phẩm" />
          </div>
          <div class="card-body">
            <h6 class="card-title fw-semibold text-truncate" :title="item.name">{{ item.name }}</h6>
            <p class="mb-1 text-muted">Brand: {{ item.brand }}</p>
            <p class="mb-1 text-dark fw-bold">{{ item.price.toLocaleString() }}₫</p>
            <div class="text-muted" style="font-size: 14px">
              <span>Màu:</span>
              <span v-for="(color, i) in item.colors" :key="i" class="color-dot ms-1"
                :style="{ backgroundColor: color.maMau }" :title="color.tenMau"></span>
            </div>
            <div class="mt-1" style="font-size: 14px">
              <span>Kích cỡ:</span>
              <span v-for="(size, i) in item.sizes" :key="i" class="size-box ms-1">{{ size }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Nút điều hướng -->
    <div class="d-flex justify-content-center gap-3 mt-4">
      <button class="btn rounded-pill px-3 py-1 fw-medium text-secondary border"
        style="background-color: #f8f9fa; border-color: #ced4da; font-size: 14px;" @click="showLess"
        v-if="currentPage > 1">
        Xem ít hơn
      </button>
      <button class="btn rounded-pill px-3 py-1 fw-medium text-white"
        style="background-color: #5c6bc0; font-size: 14px;" @click="showMore" v-if="hasMore && !isLoading">
        <PlusOutlined class="me-1" /> Xem thêm
      </button>
    </div>

    <div v-if="isLoading" class="text-center mt-4">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { StarFilled, PlusOutlined } from '@ant-design/icons-vue'

const currentPage = ref(1)
const pageSize = 4
const isLoading = ref(false)
const hasMore = ref(true)

const allProducts = ref([
  {
    id: 1,
    name: 'Sneaker Trắng Basic Nam',
    imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
    brand: 'Nike',
    price: 1250000,
    colors: [
      { tenMau: 'Trắng', maMau: '#ffffff' },
      { tenMau: 'Đen', maMau: '#000000' }
    ],
    sizes: ['40', '41', '42']
  },
  {
    id: 2,
    name: 'Adidas Nữ Hồng Pastel',
    imageUrl: 'https://tse1.mm.bing.net/th/id/OIP.kHHSVIvcAzElFc54Nb0MxQHaFc?pid=Api&P=0&h=180',
    brand: 'Adidas',
    price: 1320000,
    colors: [
      { tenMau: 'Hồng', maMau: '#ffc0cb' },
      { tenMau: 'Trắng', maMau: '#ffffff' }
    ],
    sizes: ['36', '37', '38']
  },
  {
    id: 3,
    name: 'Boot Nam Da Nâu Cao Cổ',
    imageUrl: 'https://vn-test-11.slatic.net/p/23248c8fb9647b30f8013baff44c8029.jpg',
    brand: 'Dr.Martens',
    price: 1650000,
    colors: [
      { tenMau: 'Nâu', maMau: '#6e4b2a' }
    ],
    sizes: ['42', '43', '44']
  },
  {
    id: 4,
    name: 'Giày Lười Nam Sang Trọng',
    imageUrl: 'https://tse1.mm.bing.net/th/id/OIP.yN7D3ahIsBr6iN8TYr-F0AHaFj?pid=Api&P=0&h=180',
    brand: 'Savio',
    price: 980000,
    colors: [
      { tenMau: 'Nâu Đậm', maMau: '#4b3621' }
    ],
    sizes: ['40', '41', '42']
  },
  {
    id: 5,
    name: 'Sneaker Trắng Đế Dày',
    imageUrl: 'https://tse1.mm.bing.net/th/id/OIP.XvCDn8VKj9U2MNvGMhTC9wHaHa?pid=Api&P=0&h=180',
    brand: 'MLB',
    price: 1190000,
    colors: [
      { tenMau: 'Trắng', maMau: '#ffffff' }
    ],
    sizes: ['38', '39', '40', '41']
  }
])

const visibleProducts = computed(() =>
  allProducts.value.slice(0, currentPage.value * pageSize)
)

const showMore = () => {
  isLoading.value = true
  setTimeout(() => {
    currentPage.value++
    isLoading.value = false
    hasMore.value = visibleProducts.value.length < allProducts.value.length
  }, 600)
}

const showLess = () => {
  currentPage.value = 1
  hasMore.value = true
}

import { useRouter } from 'vue-router'

const router = useRouter()

const handleClick = (product: any) => {
    console.log('Bạn đã click vào sản phẩm:', product)
  router.push({
    name: 'san-pham-chi-tiet',
    params: { id: product.id }
  })
}

</script>

<style scoped>
.product-img-wrapper {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background-color: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
}
.product-img-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}
.product-card {
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}
.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}
.color-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 1px solid #ccc;
  display: inline-block;
}
.size-box {
  display: inline-block;
  padding: 2px 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 12px;
  background-color: #f9f9f9;
  margin-right: 3px;
}
</style>
