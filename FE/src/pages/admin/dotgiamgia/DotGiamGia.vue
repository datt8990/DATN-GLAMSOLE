<template>
  <DivCustom>
    <BreadcrumbDefault 
      pageTitle="Quản lý đợt giảm giá" 
    />
    
    <ProductFilter 
      :ma="state.filters.ma"
      :ten="state.filters.ten"
      :phanTramGiam="state.filters.phanTramGiam"
      :ngayBatDau="state.filters.ngayBatDau"
      :ngayKetThuc="state.filters.ngayKetThuc"
      :trangThai="state.filters.trangThai"
      @update:ma="updateFilter('ma', $event)"
      @update:ten="updateFilter('ten', $event)"
      @update:phanTramGiam="updateFilter('phanTramGiam', $event)"
      @update:ngayBatDau="updateFilter('ngayBatDau', $event)"
      @update:ngayKetThuc="updateFilter('ngayKetThuc', $event)"
      @update:trangThai="updateFilter('trangThai', $event)"
    />

    <ProductTable 
      :products="state.products" 
      :paginationParams="state.paginationParams" 
      :totalItems="state.totalItems"
      @add="openAddModal" 
      @view="openViewModal" 
      @page-change="handlePageChange" 
      @change-status="handleChangeStatus" 
    />
    
    <!-- <ProductModal
      :open="state.isModalOpen" 
      :openChangeStatus="state.isModalChangeStatus"
      :productId="state.selectedProductId" 
      :title="modalTitle" 
      @closeChangeStatus="closeModalChangeStatus"
      @close="closeModal"
      @success="fetchProducts"
    /> -->
  </DivCustom>
</template>

<script setup lang="ts">
import BreadcrumbDefault from '@/components/ui/Breadcrumbs/BreadcrumbDefault.vue';
import ProductFilter from './DotGiamGiaFilter.vue';
import ProductTable from './DotGiamGiaTable.vue';
import ProductModal from './DotGiamGiaModal.vue';
import { computed, onMounted, reactive, watch } from 'vue';
import { GetDotGiamGia, type DotGiamGiaResponse, type ParamsGetDotGiamGia } from '@/services/api/admin/dotgiamgia.api';
import { debounce } from 'lodash';
import DivCustom from '@/components/custom/Div/DivCustomAll.vue'

const state = reactive({
  filters: {
    ma: '',
    ten: '',
    phanTramGiam: '',
    ngayBatDau: null as number | null,
    ngayKetThuc: null as number | null,
    trangThai: null as number | null
  },
  isModalOpen: false,
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  products: [] as DotGiamGiaResponse[],
  paginationParams: { page: 1, size: 10 },
  totalItems: 0
})

const modalTitle = computed(() => {
  return state.selectedProductId ? 'Cập nhật đợt giảm giá' : null
})

const updateFilter = (key: keyof typeof state.filters, value: any) => {
  state.filters[key] = value
}

const openAddModal = () => {
  state.selectedProductId = null
  state.isModalOpen = true
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
  try {
    const params: ParamsGetDotGiamGia = {
      page: state.paginationParams.page,
      size: state.paginationParams.size,
      ma: state.filters.ma || undefined,
      ten: state.filters.ten || undefined,
      phanTramGiam: state.filters.phanTramGiam || undefined,
      ngayBatDau: state.filters.ngayBatDau || undefined,
      ngayKetThuc: state.filters.ngayKetThuc || undefined,
      trangThai: state.filters.trangThai
    }
    
    // Remove empty string values
    Object.keys(params).forEach(key => {
      if (params[key as keyof ParamsGetDotGiamGia] === '') {
        delete params[key as keyof ParamsGetDotGiamGia]
      }
    })
    
    const response = await GetDotGiamGia(params)
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
  () => state.filters,
  () => {
    state.paginationParams.page = 1
    debouncedFetchProducts()
  },
  { deep: true }
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