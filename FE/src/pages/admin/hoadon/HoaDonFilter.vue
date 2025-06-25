<template>
  <DivCustom label="Bộ lọc">
    <div class="d-flex align-items-center gap-2">
      <a-input v-model:value="localSearchQuery" placeholder="Nhập tên khách hàng, sdt, tên nhân viên để tìm kiếm..."  style="width: 800px;"/>
      <a-tooltip title="Làm mới bộ lọc">
        <a-button @click="resetFilters" class="d-flex align-items-center">
          <ReloadOutlined />
        </a-button>
      </a-tooltip>
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

const resetFilters = () => {
  localSearchQuery.value = ''
  localSearchStatus.value = null
  emit('update:searchQuery', '')
  emit('update:searchStatus', null)
}
</script>