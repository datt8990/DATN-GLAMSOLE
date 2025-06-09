<template>
    <DivCustom label="Danh sách phiếu giảm giá" customClasses="mt-5">
        <template #extra>
            <a-tooltip title="Thêm phiếu giảm giá">
                <a-button type="primary" @click="handleAddClick"
                    class="d-flex justify-content-center align-items-center px-4">
                    <PlusCircleOutlined />
                </a-button>
            </a-tooltip>
        </template>
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
                            {{ record.status == 'ACTIVE' ? 'Hoạt động' : 'Không hoạt động' }}
                        </a-tag>
                    </template>
       
                    <template v-if="column.key === 'ngayBatDau'">
                        {{ record.ngayBatDau ? record.ngayBatDau.split('T')[0] : '' }}
                    </template>

                    
                    <template v-if="column.key === 'ngayKetThuc'">
                        {{ record.ngayKetThuc ? record.ngayKetThuc.split('T')[0] : '' }}
                    </template>

                    <div v-if="column.key === 'stt'">
                        {{ products.indexOf(record) + 1 }}
                    </div>
                    <template v-if="column.key === 'operation'">
                        <div class="d-flex gap-1 justify-center">
                            <a-tooltip title="Chỉnh sửa sản phẩm">
                                <a-button type="primary" @click="handleViewClick(record.id)"
                                    class="p-2 d-flex justify-content-center align-items-center">
                                    <EditOutlined />
                                </a-button>
                            </a-tooltip>
                            <a-popconfirm title="Bạn có chắc chắn muốn thay đổi trạng thái không?"
                                @confirm="handleChangeStatusClick(record.id)" ok-text="Đồng ý" cancel-text="Huỷ">
                                <a-button type="primary" class="p-2 d-flex justify-content-center align-items-center">
                                    <RedoOutlined />
                                </a-button>
                            </a-popconfirm>
                        </div>
                    </template>
                    <!-- <template v-if="column.key === 'operation'">
                        <div class="flex gap-1 justify-center">

                        </div>
                    </template> -->
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
import { modifyStatusSize } from '@/services/api/admin/voucher.api'
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
    { title: 'STT', key: 'stt', dataIndex: 'stt', width: 100, align: 'center' },
    { title: 'Mã phiếu giảm giá', key: 'ma', dataIndex: 'ma', width: 150, align: 'center' },
    { title: 'Tên phiếu giảm giá', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
    { title: 'giá trị giảm', key: 'phanTramGiam', dataIndex: 'phanTramGiam', width: 150, align: 'center' },
    { title: 'điều kiện giảm giá', key: 'dieuKien', dataIndex: 'dieuKien', width: 150, align: 'center' },
    { title: 'giá trị giảm tối đa', key: 'giaGiam', dataIndex: 'giaGiam', width: 150, align: 'center' },
    { title: 'số lượng phiếu giảm giá', key: 'soLuongPhieu', dataIndex: 'soLuongPhieu', width: 150, align: 'center' },
    { title: 'ngày bắt đầu', key: 'ngayBatDau', dataIndex: 'ngayBatDau', width: 150, align: 'center' },
    { title: 'ngày kết thúc', key: 'ngayKetThuc', dataIndex: 'ngayKetThuc', width: 150, align: 'center' },
    { title: 'trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
    {
        title: 'Hành động',
        key: 'operation',
        width: 150,
        align: 'center'
    }
]

const handlePageChange = (pagination: any) => {
    emit('page-change', { page: pagination.current, pageSize: pagination.pageSize })
}

const handleAddClick = () => {
    emit('add')
}



const handleChangeStatusClick = async (id: string) => {
    try {
        const res = await modifyStatusSize(id);
        emit('changeStatus');

        toast.success(res.message);

    } catch (error) {
        console.log(error);

        if (error?.response?.data?.message) {
            toast.error(error?.response?.data?.message);
        }
    }

}

const handleViewClick = (id: string) => {
    emit('view', id)
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