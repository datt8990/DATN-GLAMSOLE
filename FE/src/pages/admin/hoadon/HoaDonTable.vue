<template>
    <DivCustom label="Danh sách hóa đơn" customClasses="mt-5">
        <div class="min-h-[360px] ">
            <a-table :columns="columns" :data-source="products" :pagination="{
                current: paginationParams.page,
                pageSize: paginationParams.size,
                total: totalItems,
                showSizeChanger: true,
                pageSizeOptions: ['10', '20', '30', '40', '50']
            }" :scroll="{ y: 240 }" @change="handlePageChange">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'status'">
        <a-tag :color="record.status == 'ACTIVE' ? 'green' : 'red'">
            {{ record.status == 'ACTIVE' ? 'Đã thanh toán' : 'Chưa thanh toán' }}
        </a-tag>    
    </template>

                    <div v-if="column.key === 'stt'">
                        {{ products.indexOf(record) + 1 }}
                    </div>

                    <!-- <template v-if="column.key === 'gioiTimh'">
                        <a-tag :color="record.gioiTimh ? 'red' : 'yellow'">
                            {{ record.gioiTimh ? 'Nam' : 'Nữ' }}
                        </a-tag>
                    </template> -->

                    <!-- <template v-if="column.key === 'ngaySinh'">
                        {{ record.ngaySinh ? record.ngaySinh.split('T')[0] : '' }}
                    </template> -->

                    <template v-if="column.key === 'operation'">
                        <div class="d-flex gap-1 justify-center">
                            <a-tooltip title="Chi tiết hóa đơn">
                                <a-button type="primary" @click="handleViewClick(record.maHoaDon)"
                                    class="p-2 d-flex justify-content-center align-items-center">
                                    <EditOutlined />
                                </a-button>
                            </a-tooltip>
                            <!-- <a-popconfirm title="Bạn có chắc chắn muốn thay đổi trạng thái không?"
                @confirm="handleChangeStatusClick(record.id)" ok-text="Đồng ý" cancel-text="Huỷ">
                <a-button type="primary" class="p-2 d-flex justify-content-center align-items-center">
                    <RedoOutlined />
                </a-button>
            </a-popconfirm> -->
                        </div>
                    </template>
                </template>
            </a-table>
        </div>
    </DivCustom>
</template>

<script setup lang="ts">
//   import DivCustom from '@/components/custom/Div/DivCustom.vue'
import DivCustom from '@/components/custom/Div/DivCustomTable.vue'
import { EditOutlined, PlusCircleOutlined, RedoOutlined } from '@ant-design/icons-vue'
import type { TableColumnsType } from 'ant-design-vue'
import { defineEmits, defineProps, h } from 'vue'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'

defineProps<{
    paginationParams: { page: number; size: number }
    totalItems: number
    products: any[]
}>()

const router = useRouter()

const emit = defineEmits(['page-change', 'add', 'view', 'changeStatus'])

const columns: TableColumnsType = [
    { title: 'STT', key: 'stt', dataIndex: 'stt', width: 50, align: 'center' },
    { title: 'Mã HĐ', key: 'maHoaDon', dataIndex: 'maHoaDon', width: 70, align: 'center' },
    { title: 'Tên KH', key: 'tenKhachHang', dataIndex: 'tenKhachHang', width: 150, align: 'center' },
    { title: 'SDT KH', key: 'sdtKhachHang', dataIndex: 'sdtKhachHang', width: 150, align: 'center' },
    { title: 'Tên NV', key: 'tenNhanVien', dataIndex: 'tenNhanVien', width: 150, align: 'center' },
    { title: 'Tổng Tiền', key: 'tongTien', dataIndex: 'tongTien', width: 150, align: 'center' },
    { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
    // { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
    {
        title: 'HĐ',
        key: 'operation',
        width: 50,
        align: 'center'
    }
]

const handlePageChange = (pagination: any) => {
    emit('page-change', { page: pagination.current, pageSize: pagination.pageSize })
}

const handleAddClick = () => {
    emit('add')
}


const handleViewClick = (id: string) => {
  router.push({ name: 'hoa-don-detail-admin', params: { id } })
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