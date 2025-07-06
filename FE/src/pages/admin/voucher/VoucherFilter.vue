<template>
  <DivCustom label="Bộ lọc">
    <div class="row">
      <!-- Row 1 -->
      <div class="col-md-4 mb-3">
        <label class="form-label d-flex align-items-center">
          <span class="ms-2">Mã/Tên Phiếu:</span>
        </label>
        <a-input 

          placeholder="Tìm kiếm"
          style="width: 100%;"
        />
      </div>
      
      <div class="col-md-4 mb-3">
        <label class="form-label">Từ ngày:</label>
        <a-date-picker 
   
          placeholder="Tìm kiếm"
          style="width: 100%;"
          format="DD/MM/YYYY"
          :inputReadOnly="true"
        />
      </div>

      <div class="col-md-4 mb-3">
        <label class="form-label">Đến ngày:</label>
        <a-date-picker 
       
          placeholder="Tìm kiếm"
          style="width: 100%;"
          format="DD/MM/YYYY"
          :inputReadOnly="true"
        />
      </div>
    </div>

    <!-- Row 2 -->
    <div class="row">
      <div class="col-md-4 mb-3">
        <label class="form-label">Giá trị giảm:</label>
        <a-input 
    
          placeholder="Tìm kiếm"
          style="width: 100%;"
        />
      </div>
      
      <div class="col-md-4 mb-3">
        <label class="form-label">Trạng thái:</label>
        <a-select 

          placeholder="Tất cả"
          style="width: 100%;"
          :options="statusOptions"
          allowClear
        />
      </div>
      
      <!-- Action buttons -->
    <div class="d-flex justify-content-center gap-2 mt-3 col-md-4 mb-3 align-self-end">
      <a-tooltip title="Làm mới bộ lọc">
        <a-button @click="resetFilters" class="d-flex align-items-center"
        style="background-color: #54bddb; border-color: #54bddb; color: white;">
          Làm mới
          <ReloadOutlined />
        </a-button>
      </a-tooltip>
    </div>
      
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue'
import DivCustom from '@/components/custom/Div/DivCustom.vue'
import { ReloadOutlined } from '@ant-design/icons-vue'

const props = defineProps<{ searchQuery: string; searchStatus: number | null }>()
const emit = defineEmits(['update:searchQuery', 'update:searchStatus'])

const localSearchQuery = ref(props.searchQuery)
const localSearchStatus = ref(props.searchStatus)

const options = [
  { label: 'Hoạt động', value: 0 },
  { label: 'Ngừng hoạt động', value: 1 }
]

watch([localSearchQuery, localSearchStatus], ([newQuery, newStatus]) => {
  emit('update:searchQuery', newQuery)
  emit('update:searchStatus', newStatus)
})
const statusOptions = [
  { label: 'Chưa kích hoạt', value: 'CHUA_KICH_HOAT' },
  { label: 'Đang kích hoạt', value: 'DANG_KICH_HOAT' },
]
const resetFilters = () => {
  localSearchQuery.value = ''
  localSearchStatus.value = null
  emit('update:searchQuery', '')
  emit('update:searchStatus', null)
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
  font-weight: bold; /* This will now be effective */
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