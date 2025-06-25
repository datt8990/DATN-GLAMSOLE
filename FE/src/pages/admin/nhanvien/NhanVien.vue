<template>
  <DivCustom>
  <BreadcrumbDefault 
    pageTitle="Quản lý nhân viên" 
  />
  <ProductFilter 
    :searchQuery="state.searchQuery" 
    :searchStatus="state.searchStatus"
    @update:searchQuery="updateSearchQuery" 
    @update:searchStatus="updateSearchStatus" 
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
import ProductFilter from './NhanVenFilter.vue';
import ProductTable from './NhanVIenTable.vue';
import ProductModal from './NhanVienModal.vue';
import { computed, onMounted, reactive, watch } from 'vue';
import { getMembers, type ParamsGetMember, type NhanVienResponse } from "@/services/api/admin/nhanvien.api";
import { GetKhachHangs, type KhachHangResponse, type ParamsGetKhachHang } from '@/services/api/admin/khachhang.api';
import { debounce } from 'lodash';
import DivCustom from '@/components/custom/Div/DivCustomAll.vue'
import { toast } from 'vue3-toastify';


const state = reactive({
  searchQuery: '',
  searchStatus: null as number | null,
  isModalOpen: false,
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  products: [] as NhanVienResponse[],
  paginationParams: { page: 1, size: 10 },
  totalItems: 0
})


const modalTitle = computed(() => {
  return state.selectedProductId ? 'Cập nhật sản phẩm' : 'Thêm sản phẩm'
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
    const params: ParamsGetMember = {
      page: state.paginationParams.page,
      size: state.paginationParams.size,
      q: state.searchQuery,
      status: state.searchStatus
    }
    const response = await getMembers(params)
    // const pagedData = response.data.data 

    state.products = response.data?.data
    state.totalItems = response.data?.totalElements
  } catch (error) {
    console.error('Failed to fetch products:', error)
  }
}

console.log(state.products)

const debouncedFetchProducts = debounce(fetchProducts, 300)

onMounted(() => {
  fetchProducts(); // Giữ nguyên việc gọi fetchProducts khi component mount

  // ---- THÊM ĐOẠN CODE NÀY ----
  const storedToast = sessionStorage.getItem('appToastMessage');
  if (storedToast) {
    try {
      const { message, type } = JSON.parse(storedToast);

      if (message) {
        // Hiển thị toast dựa trên type 
        if (type === 'success') {
          toast.success(message, {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            progress: undefined,
          });
        } else if (type === 'error') {
          toast.error(message, {
            position: "top-right",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            progress: undefined,
          });
        }
        // Có thể thêm các loại 'info', 'warn' nếu bạn sử dụng
      }
    } catch (e) {
      console.error("Error parsing stored toast message:", e);
    } finally {
      // Luôn xóa thông báo khỏi sessionStorage sau khi đã xử lý
      sessionStorage.removeItem('appToastMessage');
    }
  }
});

watch(
  () => [state.searchQuery, state.searchStatus],
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
