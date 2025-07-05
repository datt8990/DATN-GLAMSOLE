<template>
    <DivCustom label="Danh sách sản phẩm chi tiết" customClasses="mt-5">
        <template #extra>
            <div class="d-flex justify-content-between w-100">
                <a-tooltip title="Thêm sản phẩm chi tiết">

                    <a-button style="background-color: #54bddb;" type="primary" @click="handleAddClick(idSP)"

                        class="d-flex justify-content-center align-items-center px-4">
                        <PlusCircleOutlined />
                    </a-button>
                </a-tooltip>
            </div>
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

                    <div v-if="column.key === 'stt'">
                        {{ products.indexOf(record) + 1 }}
                    </div>

                    <template v-if="column.key === 'giaBan'">
                        {{ formatCurrencyVND(parseFloat(record.giaBan)) }}
                    </template>

                    <template v-if="column.key === 'anh'">
                        <div class="center-cell">
                            <img :src="record.anh" class="anh" style="width: 50px; height: 50px; border-radius: 50%" />
                        </div>
                    </template>



                    <template v-if="column.key === 'mau'">
                        <div class="center-cell">
                            <div class="color"
                                :style="{ width: '30px', height: '30px', backgroundColor: record.mau, border: '1px solid #000' }">
                            </div>
                        </div>
                    </template>

                    <template v-if="column.key === 'operation'">
                        <div class="d-flex gap-1 justify-center">
                            <a-popconfirm title="Bạn có chắc chắn muốn thay đổi trạng thái không?"
                                @confirm="handleChangeStatusClick(record.id)" ok-text="Đồng ý" cancel-text="Huỷ">
                                <a-button style="background-color: #54bddb;" type="primary" class="p-2 d-flex justify-content-center align-items-center">
                                    <RedoOutlined />
                                </a-button>
                            </a-popconfirm>
                            <a-tooltip title="xem chi tiết sản phẩm">
                                <a-button style="background-color: #54bddb;" type="primary" @click="handleClick(record.id)"
                                    class="p-2 d-flex justify-content-center align-items-center">
                                    <EyeOutlined />
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
//   import DivCustom from '@/components/custom/Div/DivCustom.vue'
import DivCustom from '@/components/custom/Div/DivCustomTable.vue'
import { EditOutlined, PlusCircleOutlined, RedoOutlined, SearchOutlined, EyeOutlined } from '@ant-design/icons-vue'
import type { TableColumnsType } from 'ant-design-vue'
import { defineEmits, defineProps, h } from 'vue'
import { modifyStatusKhachHang } from '@/services/api/admin/khachhang.api'
import { useRouter } from 'vue-router'
import { toast } from 'vue3-toastify'
import { modifyStatusSanPham } from '@/services/api/admin/sanphamchitiet.api'
import { ROUTES_CONSTANTS } from '@/constants/path'

defineProps<{
    paginationParams: { page: number; size: number }
    totalItems: number
    products: any[]
    idSP: string
}>()

const router = useRouter()

const formatCurrencyVND = (amount: number) => {
    if (typeof amount !== 'number') {
        return amount; // Trả về nguyên bản nếu không phải số
    }
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND',
        minimumFractionDigits: 0, // Không hiển thị số thập phân
        maximumFractionDigits: 0, // Không hiển thị số thập phân
    }).format(amount);
};

const emit = defineEmits(['page-change', 'add', 'view', 'changeStatus'])

const columns: TableColumnsType = [
    { title: 'STT', key: 'stt', dataIndex: 'stt', width: 80, align: 'center' },
    { title: 'Tên sản phẩm', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
    { title: 'Số lượng', key: 'soLuong', dataIndex: 'soLuong', width: 150, align: 'center' },
    { title: 'Giá bán', key: 'giaBan', dataIndex: 'giaBan', width: 150, align: 'center' },
    { title: 'Thương hiệu', key: 'tenThuongHieu', dataIndex: 'tenThuongHieu', width: 150, align: 'center' },
    { title: 'Loại đế', key: 'tenLoaiDe', dataIndex: 'tenLoaiDe', width: 150, align: 'center' },
    { title: 'Danh mục', key: 'tenDanhMuc', dataIndex: 'tenDanhMuc', width: 150, align: 'center' },
    { title: 'Chất liệu', key: 'tenChatLieu', dataIndex: 'tenChatLieu', width: 150, align: 'center' },

    { title: 'Kích thước', key: 'kichThuoc', dataIndex: 'kichThuoc', width: 150, align: 'center' },
    { title: 'Màu sắc', key: 'mau', dataIndex: 'mau', width: 150, align: 'center' },
    { title: 'ảnh', key: 'anh', dataIndex: 'anh', width: 150, align: 'center' },
    { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
    {
        title: 'Hành động',
        key: 'operation',
        width: 90,
        align: 'center'
    }
]

const handleChangeStatusClick = async (id: string) => {


    console.log('id', id)

    try {
        const res = await modifyStatusSanPham(id);
        emit('changeStatus');

        toast.success(res.message);

    } catch (error) {
        console.log(error);

        if (error?.response?.data?.message) {
            toast.error(error?.response?.data?.message);
        }
    }

}

const handlePageChange = (pagination: any) => {
    emit('page-change', { page: pagination.current, pageSize: pagination.pageSize })
}

const handleClick = (idSanPham: string) => {
    console.log('idSanPham', idSanPham)
    emit('view', idSanPham)
};


const handleAddClick = (idSP: string) => {
    if (idSP) {
        console.log('idSP', idSP)
        router.push({
            name: 'them-san-pham-chi-tiet-admin',
            query: { id: idSP }
        });
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