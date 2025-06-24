<template>
  <DivCustom>
    <BreadcrumbDefault pageTitle="Quản lý hóa đơn" />
    <ProductFilter
      :searchQuery="state.searchQuery"
      :searchStatus="state.searchStatus"
      @update:searchQuery="updateSearchQuery"
      @update:searchStatus="updateSearchStatus"
      @update:startDate="updateStartDate"
      @update:endDate="updateEndDate"
    />

    <ProductTable
      :products="state.products"
      :paginationParams="state.paginationParams"
      :totalItems="state.totalItems"
      :countByStatus="state.countByStatus"
      @add="openAddModal"
      @view="openViewModal"
      @page-change="handlePageChange"
      @change-status="handleChangeStatus"
      @tab-change="handleTabChange"
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
import BreadcrumbDefault from "@/components/ui/Breadcrumbs/BreadcrumbDefault.vue";
import ProductFilter from "./HoaDonFilter.vue";
import ProductTable from "./HoaDonTable.vue";
import ProductModal from "./HoaDonModal.vue";
import { computed, onMounted, reactive, watch } from "vue";
import {
  GetHoaDons,
  type HoaDonResponse,
  type ParamsGetHoaDon,
} from "@/services/api/admin/hoadon.api";
import { debounce } from "lodash";
import DivCustom from "@/components/custom/Div/DivCustomAll.vue";

const state = reactive({
  searchQuery: "",
  searchStatus: null as number | null,
  startDate: null as number | null,
  endDate: null as number | null,
  isModalOpen: false,
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  products: [] as HoaDonResponse[],
  paginationParams: { page: 1, size: 10 },
  totalItems: 0,
  countByStatus: {} as Record<string, number>,
});

const handleTabChange = (tabKey: string) => {
  state.searchStatus = tabKey === "ALL" ? null : tabKey; // ALL không filter
  state.paginationParams.page = 1; // reset về trang 1 khi đổi tab
  fetchProducts();
};

const modalTitle = computed(() => {
  return state.selectedProductId ? "Chi tiết hóa đơn" : null;
});

const updateSearchQuery = (newQuery: string) => {
  state.searchQuery = newQuery;
};

const updateSearchStatus = (newStatus: number | null) => {
  state.searchStatus = newStatus;
};

const updateStartDate = (newStartDate: number | null) => {
  state.startDate = newStartDate;
};

const updateEndDate = (newEndDate: number | null) => {
  state.endDate = newEndDate;
};

const openAddModal = () => {
  state.selectedProductId = null;
  state.isModalOpen = true;
};

const openViewModal = (id: string) => {
  state.selectedProductId = id;
  state.isModalOpen = true;
};

const openChangeStatusModal = (id: string) => {
  state.selectedProductId = id;
  state.isModalChangeStatus = true;
};

const closeModal = () => {
  state.isModalOpen = false;
};

const closeModalChangeStatus = () => {
  fetchProducts();
  state.isModalChangeStatus = false;
};

const fetchProducts = async () => {
  try {
    const params: ParamsGetHoaDon = {
      page: state.paginationParams.page,
      size: state.paginationParams.size,
      q: state.searchQuery,
      status: state.searchStatus,
      startDate: state.startDate,
      endDate: state.endDate,
    };
    const response = await GetHoaDons(params);
    // const pagedData = response.data.data

    state.products = response.data?.page?.content;
    state.totalItems = response.data?.totalElements;
    state.countByStatus = response.data?.countByStatus || {};
  } catch (error) {
    console.error("Failed to fetch products:", error);
  }
};

const debouncedFetchProducts = debounce(fetchProducts, 300);

onMounted(() => {
  fetchProducts();
});

watch(
  () => [state.searchQuery, state.searchStatus, state.startDate, state.endDate],
  () => {
    state.paginationParams.page = 1;
    debouncedFetchProducts();
  }
);

const handlePageChange = ({
  page,
  pageSize,
}: {
  page: number;
  pageSize?: number;
}) => {
  state.paginationParams.page = page;
  if (pageSize) {
    state.paginationParams.size = pageSize;
  }
  fetchProducts();
};

const handleChangeStatus = async () => {
  fetchProducts();
};
</script>
