<template>
    <DivCustom label="Danh sách nhân viên" customClasses="mt-5">
        <template #extra>
            <a-tooltip title="Thêm nhân viên">
                <a-button style="background-color: #54bddb;" type="primary" @click="handleAddClick"
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
                            {{ record.status == 'ACTIVE' ? 'Đang làm việc' : 'Nghỉ việc' }}
                        </a-tag>
                    </template> 

                    <template v-if="column.key === 'avatar'">
                        <div class="center-cell">
                            <img :src="record.avatar" class="avatar"
                                style="width: 50px; height: 50px; border-radius: 50%" />
                        </div>
                    </template>

                    <template v-if="column.key === 'gioiTimh'">
                        <a-tag :color="record.gioiTimh ? 'red' : 'yellow'">
                            {{ record.gioiTimh ? 'Nam' : 'Nữ' }}
                        </a-tag>
                    </template>

                    <div v-if="column.key === 'stt'">
                        {{ products.indexOf(record) + 1 }}
                    </div>
                    
                    <!-- Hiển thị ngày tháng cho createdDate -->
                    <template v-if="column.key === 'createdDate'">
                        {{ formatDate(record.createdDate) }}
                    </template>

                    <template v-if="column.key === 'operation'">
                        <div class="d-flex gap-1 justify-center">
                            <a-tooltip title="Chỉnh sửa sản phẩm">
                                <a-button style="background-color: #54bddb;" type="primary" @click="handleViewClick(record.id)"
                                    class="p-2 d-flex justify-content-center align-items-center">
                                    <EditOutlined />
                                </a-button>
                            </a-tooltip>
                            <a-popconfirm title="Bạn có chắc chắn muốn thay đổi trạng thái không?"
                                @confirm="handleChangeStatusClick(record.id)" ok-text="Đồng ý" cancel-text="Huỷ">
                                <a-button style="background-color: #54bddb;" type="primary" class="p-2 d-flex justify-content-center align-items-center">
                                    <RedoOutlined />
                                </a-button>
                            </a-popconfirm>
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
import { modifyStatusMember } from '@/services/api/admin/nhanvien.api'
import { EditOutlined, PlusCircleOutlined, RedoOutlined } from '@ant-design/icons-vue'
import type { TableColumnsType } from 'ant-design-vue'
import { defineEmits, defineProps } from 'vue'
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
    { title: 'STT', key: 'stt', dataIndex: 'stt', width: 150, align: 'center' },
    { title: 'Mã Nhân Viên', key: 'ma', dataIndex: 'ma', width: 150, align: 'center' },
    { title: 'Tên Nhân viên', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
    { title: 'Email', key: 'email', dataIndex: 'email', width: 150, align: 'center' },
    { title: 'Số điện thoại', key: 'sdt', dataIndex: 'sdt', width: 150, align: 'center' },
    { title: 'Ngày tham gia', key: 'createdDate', dataIndex: 'createdDate', width: 150, align: 'center' },
    { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
    {
        title: 'Hành động',
        key: 'operation',
        width: 80,
        align: 'center'
    }
]

const formatDate = (timestamp: number) => {
    const date = new Date(timestamp);
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: '2-digit', day: '2-digit' }; // Chỉ lấy ngày tháng năm
    return date.toLocaleString('vi-VN', options);
}
const handlePageChange = (pagination: any) => {
    emit('page-change', { page: pagination.current, pageSize: pagination.pageSize })
}

const handleAddClick = () => {
    router.push({
        name: 'them-nhan-vien-admin',
    });
}

const handleChangeStatusClick = async (id: string) => {
    try {
        const res = await modifyStatusMember(id);
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
    router.push({
        name: 'them-nhan-vien-admin',
        query: { id: id }
    });
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
