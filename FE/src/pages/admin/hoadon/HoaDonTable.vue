<template>
  <DivCustom label="Danh sách hóa đơn" customClasses="mt-5">
    <div class="min-h-[360px]">
      <a-table
        :columns="columns"
        :data-source="products"
        :pagination="{
          current: paginationParams.page,
          pageSize: paginationParams.size,
          total: totalItems,
          showSizeChanger: true,
          pageSizeOptions: ['10', '20', '30', '40', '50']
        }"
        :scroll="{ y: 240 }"
        @change="handlePageChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'ACTIVE' ? 'green' : 'red'">
              {{ record.status === 'ACTIVE' ? 'Đã thanh toán' : 'Chưa thanh toán' }}
            </a-tag>
          </template>

          <template v-if="column.key === 'loaiHD'">
            <a-tag :color="record.loaiHoaDon === 'OFFLINE' ? 'green' : 'red'">
              {{ record.loaiHoaDon === 'OFFLINE' ? 'OFFLINE' : 'ONLINE' }}
            </a-tag>
          </template>

          <template v-if="column.key === 'stt'">
            {{ products.indexOf(record) + 1 }}
          </template>

          <template v-if="column.key === 'tongTien'">
            {{ formatCurrency(record.tongTien) }}
          </template>

          <template v-if="column.key === 'ngayTao'">
            {{ formatDate(record.createdDate) }}
          </template>

          <template v-if="column.key === 'operation'">
            <div class="d-flex gap-1 justify-center w-100">
              <a-tooltip title="Chi tiết hóa đơn">
                <a-button
                  type="primary"
                  @click="handleViewClick(record.maHoaDon)"
                  class="p-2 d-flex justify-content-center align-items-center"
                  style="background-color: #54bddb; border-color: #54bddb; color: white;"
                >
                  <EyeOutlined style="font-size: 18px;" />
                </a-button>
              </a-tooltip>
            </div>
          </template>
        </template>
      </a-table>
    </div>
  </DivCustom>
</template>

<script setup lang="ts">
import DivCustom from '@/components/custom/Div/DivCustomTable.vue'
import { EditOutlined, EyeOutlined } from '@ant-design/icons-vue'
import type { TableColumnsType } from 'ant-design-vue'
import { defineEmits, defineProps } from 'vue'
import { useRouter } from 'vue-router'

defineProps<{
  paginationParams: { page: number; size: number }
  totalItems: number
  products: any[]
}>()

const router = useRouter()
const emit = defineEmits(['page-change', 'add', 'view', 'changeStatus'])

const columns: TableColumnsType = [
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 70, align: 'center'},
  { title: 'Mã HĐ', key: 'maHoaDon', dataIndex: 'maHoaDon', width: 100, align: 'center' },
  { title: 'Tên KH', key: 'tenKhachHang', dataIndex: 'tenKhachHang', width: 150, align: 'center' },
  { title: 'SDT KH', key: 'sdtKhachHang', dataIndex: 'sdtKhachHang', width: 150, align: 'center' },
  { title: 'Loại HĐ', key: 'loaiHD', dataIndex: 'loaiHoaDon', width: 150, align: 'center' },
  { title: 'Tên NV', key: 'tenNhanVien', dataIndex: 'tenNhanVien', width: 150, align: 'center' },
  { title: 'Tổng Tiền', key: 'tongTien', dataIndex: 'tongTien', width: 100, align: 'center' },
  { title: 'Ngày Tạo', key: 'ngayTao', dataIndex: 'createdDate', width: 150, align: 'center' },
  { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 100, align: 'center' },
  { title: 'Hành Động', key: 'operation', width: 100, align: 'center' }
]

const handlePageChange = (pagination: any) => {
  emit('page-change', { page: pagination.current, pageSize: pagination.pageSize })
}

const handleViewClick = (id: string) => {
  router.push({ name: 'hoa-don-detail-admin', params: { id } })
}

const formatCurrency = (value: number) => {
  return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}

const formatDate = (timestamp: number) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  const day = String(date.getDate()).padStart(2, '0')
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const year = date.getFullYear()
  return `${day}/${month}/${year}`
}
</script>

<style scoped lang="scss">
.color {
  border-radius: 50%;
}

.center-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
