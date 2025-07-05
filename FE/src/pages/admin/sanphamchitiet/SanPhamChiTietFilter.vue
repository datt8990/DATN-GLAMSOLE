<template>
  <DivCustom label="Bộ lọc">
    <div class="d-flex flex-column gap-3">
      <!-- Tìm kiếm theo từ khóa -->
      <div class="d-flex align-items-center gap-2">
        <a-input v-model:value="localSearchQuery" placeholder="Nhập mã/ tên sản phẩm..." style="width: 600px; margin-left: 30px"
          size="small" />
        <a-tooltip title="Làm mới bộ lọc">
          <a-button @click="resetFilters" size="small" class="d-flex align-items-center">
            <ReloadOutlined />
          </a-button>
        </a-tooltip>
        <div class="d-flex align-items-center gap-2" style="margin-left: 350px;">
          <span style="font-size: 13px; min-width: 70px">Khoảng giá:</span>
          <a-slider range v-model:value="localPriceRange" :min="minPrice" :max="maxPrice" style="width: 250px"
            :tooltip-visible="false" />
          <div style="font-size: 13px; min-width: 160px">
            {{ localPriceRange[0].toLocaleString() }}₫ - {{ localPriceRange[1].toLocaleString() }}₫
          </div>
        </div>
      </div>

      <!-- Dòng chứa các bộ lọc: trạng thái, giá, màu, kích thước -->
      <div class="d-flex flex-wrap gap-4" style="margin-left: 30px">
        <!-- Trạng thái -->
        <div class="d-flex align-items-center gap-2">
          <span style="font-size: 13px; min-width: 70px">Trạng thái:</span>
          <a-select v-model:value="localSearchStatus" @change="handleStatusChange" allow-clear style="width: 180px"
            size="small" placeholder="Chọn trạng thái">
            <a-select-option :value="1">Hoạt động</a-select-option>
            <a-select-option :value="0">Ngừng hoạt động</a-select-option>
          </a-select>
        </div>

        <!-- Khoảng giá -->


        <!-- Màu -->
        <div class="d-flex align-items-center gap-2" style="margin-left: 100px">
          <span style="font-size: 13px; min-width: 70px">Màu:</span>
          <a-select v-model:value="localColor" @change="handleColorChange" allow-clear :options="ColorOptions"
            placeholder="Chọn màu sắc" style="width: 180px" size="small" />
        </div>

        <!-- Kích thước -->
        <div class="d-flex align-items-center gap-2" style="margin-left: 100px">
          <span style="font-size: 13px; min-width: 70px">Kích thước:</span>
          <a-select v-model:value="localSize" @change="handleSizeChange" allow-clear :options="SizeOptions"
            placeholder="Chọn kích thước" style="width: 180px" size="small" />
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
