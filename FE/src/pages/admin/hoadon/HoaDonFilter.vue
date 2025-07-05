<template>
  <DivCustom label="Bộ lọc">
    <div class="d-flex align-items-center gap-4">
      <!-- Label: Tìm kiếm -->
      <div class="d-flex flex-column">
        <span class="text-sm text-gray-600 mb-1">Tìm kiếm</span>
        <a-input
          v-model:value="localSearchQuery"
          placeholder="Nhập tên khách hàng, sdt, tên nhân viên..."
          style="width: 600px;"
        />
      </div>

      <!-- Label: Khoảng thời gian -->
      <div class="d-flex flex-column">
        <span class="text-sm text-gray-600 mb-1">Chọn khoảng thời gian</span>
        <a-range-picker
          v-model:value="localDateRange"
          format="DD/MM/YYYY"
          :allowClear="true"
          style="width: 400px;"
          placeholder="Chọn khoảng thời gian"
        />
      </div>

      <!-- Nút làm mới -->
      <div class="d-flex flex-column">
        <span class="text-sm text-gray-600 mb-1 invisible">Làm mới</span>
        <a-tooltip title="Làm mới bộ lọc">
          <a-button @click="resetFilters" style="margin-bottom: 22px;" class="d-flex align-items-center">
            <ReloadOutlined />
          </a-button>
        </a-tooltip>
      </div>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted } from 'vue'
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import { ReloadOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'

const props = defineProps<{ searchQuery: string; searchStatus: number | null }>()
const emit = defineEmits(['update:searchQuery', 'update:searchStatus', 'update:startDate', 'update:endDate'])

const localSearchQuery = ref(props.searchQuery)
const localSearchStatus = ref(props.searchStatus)
const localDateRange = ref<[dayjs.Dayjs, dayjs.Dayjs] | null>(null)

watch(localDateRange, (newRange) => {
  if (newRange && newRange.length === 2) {
    const [start, end] = newRange
    emit('update:startDate', start.startOf('day').valueOf())
    emit('update:endDate', end.endOf('day').valueOf())
  } else {
    emit('update:startDate', null)
    emit('update:endDate', null)
  }
})

const resetFilters = () => {
  localSearchQuery.value = ''
  localSearchStatus.value = null
  localDateRange.value = null
  emit('update:searchQuery', '')
  emit('update:searchStatus', null)
  emit('update:startDate', null)
  emit('update:endDate', null)
}

// Tự động chọn ngày hiện tại khi load
onMounted(() => {
  const today = dayjs()
  localDateRange.value = [today, today]
})
</script>
