<template>
  <DivCustom>
    <div class="d-flex align-items-center gap-4">
      <!-- Label: Tìm kiếm -->
      <div class="d-flex flex-column">
        <span class="text-sm text-gray-600 mb-1">Tìm kiếm</span>
        <a-input v-model:value="localSearchQuery"  placeholder="Nhập tên khách hàng, sdt, tên nhân viên..."
          style="width: 800px;" />
      </div>

      <!-- Label: Khoảng thời gian -->
      <div class="d-flex flex-column">
        <span class="text-sm text-gray-600 mb-1">Chọn khoảng thời gian</span>
        <a-range-picker v-model:value="localDateRange" format="DD/MM/YYYY" :allowClear="true" style="width: 400px;"
          placeholder="Chọn khoảng thời gian" />
      </div>

      <!-- Nút làm mới -->
      <div class="filter-item reset-button-group">
        <a-tooltip title="Làm mới bộ lọc">
          <a-button style="background-color: dimgrey; color: white;" @click="resetFilters" class="reset-button">
            Đặt lại bộ lọc
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
<style scoped lang="scss">

.filter-item {
  display: flex;
  flex-direction: column;
  /* Stack label above input/button */
  justify-content: flex-end;
  /* Push content to the bottom if container has extra space */
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

</style> 