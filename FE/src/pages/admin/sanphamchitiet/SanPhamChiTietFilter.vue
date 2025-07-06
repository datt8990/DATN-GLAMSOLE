<template>
  <DivCustom label="Bộ lọc">
    <div class="d-flex flex-column gap-3">
      <!-- Tìm kiếm theo từ khóa -->
      <div class="d-flex align-items-center gap-2">
        <div class="filter-item search-input-group">
          <label for="search-query" class="filter-label">Tìm kiếm sản phẩm </label>
          <a-input id="search-query" v-model:value="localSearchQuery" placeholder="Nhập mã / tên để tìm kiếm..."
            class="search-input" />
        </div>
        <div class="filter-item search-input-group">
          <label for="search-query" class="filter-label">Tìm kiếm sản phẩm </label>
          <a-slider range v-model:value="localPriceRange" :min="minPrice" :max="maxPrice" style="width: 350px"
            :tooltip-visible="false" />
          <div style="font-size: 13px; width: 160px">
            {{ localPriceRange[0].toLocaleString() }}₫ - {{ localPriceRange[1].toLocaleString() }}₫
          </div>
        </div>
      </div>

      <!-- Dòng chứa các bộ lọc: trạng thái, giá, màu, kích thước -->
      <div class="d-flex flex-wrap gap-4" style="margin-left: 30px">
        <!-- Trạng thái -->
        <div class="filter-item search-input-group">
          <label for="search-query" class="filter-label">Trạng thái:</label>
          <a-select class="select-input" v-model:value="localSearchStatus" allow-clear style="width: 300px" size="small"
            placeholder="Chọn trạng thái">
            <a-select-option :value="1">Hoạt động</a-select-option>
            <a-select-option :value="0">Ngừng hoạt động</a-select-option>
          </a-select>
        </div>

        <!-- Khoảng giá -->


        <!-- Màu -->
        <div class="filter-item search-input-group">
          <label for="search-query" class="filter-label">Màu:</label>
          <a-select v-model:value="localColor" @change="handleColorChange" allow-clear :options="ColorOptions"
            placeholder="Chọn màu sắc" style="width: 300px" size="small" />
        </div>

        <!-- Kích thước -->
        <div class="filter-item search-input-group">
          <label for="search-query" class="filter-label">Kích thước:</label>
          <a-select v-model:value="localSize" @change="handleSizeChange" allow-clear :options="SizeOptions"
            placeholder="Chọn kích thước" style="width: 300px" size="small" />
        </div>
        <div class="filter-item reset-button-group">
          <a-tooltip title="Làm mới bộ lọc">
            <a-button style="background-color: dimgrey; margin-left: 900px; color: white;" @click="resetFilters"
              class="reset-button">
              Đặt lại bộ lọc
              <ReloadOutlined />
            </a-button>
          </a-tooltip>
        </div>
      </div>

    </div>
  </DivCustom>

</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted } from 'vue'
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import { ReloadOutlined } from '@ant-design/icons-vue'
import { GetListColor, GetListSize } from '@/services/api/admin/sanphamchitiet.api'

const props = defineProps<{
  searchQuery: string
  searchStatus: number | null
  priceRange?: [number, number]
  searchColor?: string | null
  searchSize?: string | null
}>()

const emit = defineEmits([
  'update:searchQuery',
  'update:searchStatus',
  'update:priceRange',
  'update:searchColor',
  'update:searchSize',
])

// keyword, status, price
const localSearchQuery = ref(props.searchQuery)
const localSearchStatus = ref<number | null>(props.searchStatus)
const minPrice = 0
const maxPrice = 10000000
const localPriceRange = ref<[number, number]>(props.priceRange || [minPrice, maxPrice])

// color & size
const ColorOptions = ref<{ label: string; value: string }[]>([])
const SizeOptions = ref<{ label: string; value: string }[]>([])
const localColor = ref<string | null>(props.searchColor ?? null)
const localSize = ref<string | null>(props.searchSize ?? null)

// handlers
const handleStatusChange = (v: number | undefined) => { localSearchStatus.value = v ?? null }
const handleColorChange = (v: string | undefined) => { localColor.value = v ?? null }
const handleSizeChange = (v: string | undefined) => { localSize.value = v ?? null }

// watch tất cả emit luôn
watch(
  [localSearchQuery, localSearchStatus, localPriceRange, localColor, localSize],
  ([q, st, priceR, col, sz]) => {
    emit('update:searchQuery', q)
    emit('update:searchStatus', st ?? null)
    emit('update:priceRange', priceR)
    emit('update:searchColor', col ?? null)
    emit('update:searchSize', sz ?? null)
  }
)

// tải options màu & size
const fetchColor = async () => {
  try {
    const { data } = await GetListColor()
    ColorOptions.value = data.map(c => ({ label: c.ten, value: c.id }))
  } catch (e) { console.error(e) }
}
const fetchSize = async () => {
  try {
    const { data } = await GetListSize()
    SizeOptions.value = data.map(s => ({ label: s.ten, value: s.id }))
  } catch (e) { console.error(e) }
}

onMounted(() => {
  fetchColor()
  fetchSize()
})

// reset toàn bộ
const resetFilters = () => {
  localSearchQuery.value = ''
  localSearchStatus.value = null
  localPriceRange.value = [minPrice, maxPrice]
  localColor.value = null
  localSize.value = null
  emit('update:searchQuery', '')
  emit('update:searchStatus', null)
  emit('update:priceRange', [minPrice, maxPrice])
  emit('update:searchColor', null)
  emit('update:searchSize', null)
}
</script>
<style scoped lang="scss">
/* Use scoped style for better component encapsulation */

.filter-container {
  display: flex;
  flex-wrap: wrap;
  /* Allow items to wrap to the next line on smaller screens */
  gap: 20px;
  /* Space between filter items */
  align-items: flex-end;
  /* Align items to the bottom of the container */
  padding: 15px;
  /* Add some padding around the filter section */

  border-radius: 8px;
  /* Slightly rounded corners */
}

.filter-item {
  display: flex;
  flex-direction: column;
  /* Stack label above input/button */
  justify-content: flex-end;
  /* Push content to the bottom if container has extra space */
}

.filter-label {
  font-size: 14px;
  font-weight: bold;
  /* This will now be effective */
  margin-bottom: 5px;
  color: #555;
  white-space: nowrap;
}

.search-input {
  width: 700px;
  /* Adjust width as needed for better responsiveness */
  min-width: 200px;
  /* Minimum width for search input */
}

.reset-button {
  display: flex;
  /* Ensure icon and text are side-by-side */
  align-items: center;
  /* Vertically center icon and text */
  gap: 5px;
  /* Space between text and icon */
  height: 32px;
  /* Standard Ant Design button height */
  padding: 0 15px;
  /* Adjust padding for better look */
  margin-top: 25px;
  /* Align button baseline with input text. Adjust as needed based on actual font sizes/line heights */
}

/* Optional: If you want to match the Ant Design input height precisely for the button */
.ant-input {
  height: 32px;
  /* Default Ant Design input height */
}

// Basic Ant Design button styles often handle 'd-flex', 'justify-content-center', 'align-items-center', 'px-4'
// These are likely utility classes from another framework (like Bootstrap or Tailwind).
// If they are not working, you'd need to define them, e.g.:
/*
.d-flex { display: flex; }
.align-items-center { align-items: center; }
.justify-content-center { justify-content: center; }
.px-4 { padding-left: 1rem; padding-right: 1rem; }
*/

/* Global body font is okay, but usually specified in a global stylesheet */
body {
  font-family: 'Roboto', sans-serif;
}
</style>