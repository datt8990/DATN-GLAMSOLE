<template>
  <DivCustom>
    <BreadcrumbDefault pageTitle="Sản phẩm chi tiết" />
    +
    <ProductFilter :searchQuery="state.searchQuery" :searchStatus="state.searchStatus" :priceRange="state.priceRange"
      :searchColor="state.searchColor" :searchSize="state.searchSize" @update:searchQuery="updateSearchQuery"
      @update:searchStatus="updateSearchStatus" @update:priceRange="updatePriceRange"
      @update:searchColor="updateSearchColor" @update:searchSize="updateSearchSize" />

    <ProductTable :products="state.products" :paginationParams="state.paginationParams" :totalItems="state.totalItems"
      :idSP="idSanPham" @add="openAddModal" @view="openViewModal" @page-change="handlePageChange"
      @change-status="handleChangeStatus" />

    <ProductModal :open="state.isModalOpen" :openChangeStatus="state.isModalChangeStatus"
      :productId="state.selectedProductId" :title="modalTitle" @closeChangeStatus="closeModalChangeStatus"
      @close="closeModal" @success="fetchProducts" />
  </DivCustom>
</template>

<script setup lang="ts">
import BreadcrumbDefault from '@/components/ui/Breadcrumbs/BreadcrumbDefault.vue';
import ProductFilter from './SanPhamChiTietFilter.vue';
import ProductTable from './SanPhamChiTietTable.vue';
import ProductModal from './SanPhamChiTietModal.vue';
import { computed, onMounted, reactive, watch } from 'vue';
import { GetSanPhams, type SanPhamResponse, type ParamsGetSanPham } from '@/services/api/admin/sanphamchitiet.api';
import { debounce } from 'lodash';
import DivCustom from '@/components/custom/Div/DivCustomAll.vue'
import { useRoute } from 'vue-router';
const route = useRoute();
const state = reactive({
  searchQuery: '',
  idSP: '',
  searchStatus: null as number | null,
  isModalOpen: false,
  priceRange: [0, 10000000] as [number, number],
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  products: [] as SanPhamResponse[],
  paginationParams: { page: 1, size: 10 },
  totalItems: 0,
  searchColor: null as string | null,    // ← thêm đây
  searchSize: null as string | null,
})

const idSanPham = route.query.id || 'default-id';
const modalTitle = computed(() => {
  return state.selectedProductId ? 'Cập nhật sản phẩm chi tiết  ' : 'Thêm khách hàng'
})

const updateSearchQuery = (newQuery: string) => {
  state.searchQuery = newQuery
}

const updateSearchStatus = (newStatus: number | null) => {
  state.searchStatus = newStatus
}

const openAddModal = () => {
  state.selectedProductId = null
  state.isModalOpen = true
}

const updatePriceRange = (newRange: [number, number]) => {
  state.priceRange = newRange
}

const updateSearchColor = (newColor: string | null) => {
  state.searchColor = newColor
}
const updateSearchSize = (newSize: string | null) => {
  state.searchSize = newSize
}

const openViewModal = (id: string) => {
  state.selectedProductId = id
  state.isModalOpen = true
}

const openChangeStatusModal = (id: string) => {
  state.selectedProductId = id
  state.isModalChangeStatus = true
}

const closeModal = () => {
  state.isModalOpen = false
}

const closeModalChangeStatus = () => {
  fetchProducts();
  state.isModalChangeStatus = false
}

const fetchProducts = async () => {

  console.log(state.searchStatus)
  try {

    const params: ParamsGetSanPham = {
      page: state.paginationParams.page,
      size: state.paginationParams.size,
      q: state.searchQuery,
      idSP: idSanPham,
      idMS: state.searchColor,    
      idKT: state.searchSize,
      status: state.searchStatus,
      priceMin: state.priceRange[0],
      priceMax: state.priceRange[1]
    }


    const response = await GetSanPhams(params)


    state.products = response.data?.data
    state.totalItems = response.data?.totalElements
  } catch (error) {
    console.error('Failed to fetch products:', error)
  }
}

console.log(state.products)

const debouncedFetchProducts = debounce(fetchProducts, 300)

onMounted(() => {
  fetchProducts()
})

watch(
  () => [state.searchQuery, state.searchStatus, state.priceRange, state.searchColor, state.searchSize],
  () => {
    state.paginationParams.page = 1
    debouncedFetchProducts()
  }
)


const handlePageChange = ({ page, pageSize }: { page: number; pageSize?: number }) => {
  state.paginationParams.page = page
  if (pageSize) {
    state.paginationParams.size = pageSize
  }
  fetchProducts()
}


const handleChangeStatus = async () => {
  fetchProducts();
}
</script>
