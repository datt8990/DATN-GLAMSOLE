<template>
    <DivCustom label="Danh sách phiếu giảm giá" customClasses="mt-5">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <div style="font-size: 13px; color: #5FB3B3; margin-left: 15px;">
                {{ products.length }} phiếu giảm giá
            </div>

            <div>
                <a-tooltip title="Thêm phiếu giảm giá">
                    <a-button style="background-color: #54bddb;" type="primary" @click="handleAddClick"
                        class="d-flex justify-content-center align-items-center px-4">
                        <PlusCircleOutlined /> Thêm mới phiếu giảm giá
                    </a-button>
                </a-tooltip>
            </div>
        </div>
        <div class="min-h-[300px]">
            <a-table :columns="columns" :data-source="products" :pagination="{
                current: paginationParams.page,
                pageSize: paginationParams.size,
                total: totalItems,
                showSizeChanger: true,
                pageSizeOptions: ['10', '20', '30', '40', '50']
            }" :scroll="{ y: 300 }" @change="handlePageChange">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'status'">
                        <a-tag :color="record.status == 'ACTIVE' ? 'green' : 'red'">
                            {{ record.status == 'ACTIVE' ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
                        </a-tag>
                    </template>

                    <template v-if="column.key === 'phanTramGiam'">
                        {{ record.kieuGiam == true ? record.phanTramGiam + '%' : formatCurrencyVND(record.phanTramGiam) }}
                    </template>

                    <div v-if="column.key === 'stt'">
                        {{ products.indexOf(record) + 1 }}
                    </div>
                    <template v-if="column.key === 'operation'">
                        <div class="d-flex gap-1 justify-center">
                            <a-tooltip title="Chỉnh sửa phiếu giảm giá">
                                <a-button style="background-color: #54bddb;" type="primary"
                                    @click="handleViewClick(record.id)"
                                    class="p-2 d-flex justify-content-center align-items-center">
                                    <EditOutlined />
                                </a-button>
                            </a-tooltip>
                            <a-popconfirm title="Bạn có chắc chắn muốn thay đổi trạng thái không?"
                                @confirm="handleChangeStatusClick(record.id)" ok-text="Đồng ý" cancel-text="Huỷ">
                                <a-button style="background-color: #9b6dc7;" type="primary"
                                    class="p-2 d-flex justify-content-center align-items-center">
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
import DivCustom from '@/components/custom/Div/DivCustomTable.vue'
import { EditOutlined, PlusCircleOutlined, RedoOutlined } from '@ant-design/icons-vue'
import type { TableColumnsType } from 'ant-design-vue'
import { defineEmits, defineProps } from 'vue'
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
    { title: 'STT', key: 'stt', dataIndex: 'stt', width: 80, align: 'center' },
    { title: 'Mã', key: 'ma', dataIndex: 'ma', width: 100, align: 'center' },
    { title: 'Tên', key: 'ten', dataIndex: 'ten', width: 100, align: 'center' },
    { 
        title: 'Điều kiện giảm giá', 
        key: 'dieuKien', 
        dataIndex: 'dieuKien', 
        width: 100, 
        align: 'center', 
        customRender: ({ text }) => formatCurrencyVND(text) 
    },
    { 
        title: 'Giá trị giảm giá', 
        key: 'phanTramGiam', 
        dataIndex: 'phanTramGiam', 
        width: 100, 
        align: 'center' 
    },
    { title: 'Số lượng', key: 'soLuongPhieu', dataIndex: 'soLuongPhieu', width: 100, align: 'center' },
    { 
        title: 'Ngày bắt đầu', 
        key: 'ngayBatDau', 
        dataIndex: 'ngayBatDau', 
        width: 100, 
        align: 'center',
        customRender: ({ text }) => formatDate(text)
    },
    { 
        title: 'Ngày kết thúc', 
        key: 'ngayKetThuc', 
        dataIndex: 'ngayKetThuc', 
        width: 100, 
        align: 'center',
        customRender: ({ text }) => formatDate(text)
    },
    { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
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
    router.push({
        name: 'them-phieu-giam-gia-admin',
    });
}

const formatCurrencyVND = (amount: number) => {
    if (typeof amount !== 'number') {
        return amount;
    }
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
    }).format(amount);
};

const formatDate = (date: string | Date) => {
    if (!date) return '';
    const d = new Date(date);
    return d.toLocaleDateString('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
};

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
    router.push({
        name: 'them-phieu-giam-gia-admin',
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