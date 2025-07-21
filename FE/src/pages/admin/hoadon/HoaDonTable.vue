<template>
  <DivCustom label="Danh sách hóa đơn" customClasses="mt-5">
    <div class="min-h-[300px]">
      <a-tabs
        v-model:activeKey="trangThaiTab"
        @change="handleTabChange"
        class="mb-4"
      >
        <a-tab-pane v-for="tab in tabs" :key="tab.key">
          <template #tab>
            <span class="flex items-center gap-1">
              {{ tab.label }}
              <a-badge
                :count="tab.count"
                :number-style="{ backgroundColor: '#f5222d' }"
                v-if="tab.count !== undefined"
              />
            </span>
          </template>
        </a-tab-pane>
      </a-tabs>
      <a-table
        :columns="columns"
        :data-source="products"
        :pagination="{
          current: paginationParams.page,
          pageSize: paginationParams.size,
          total: totalItems,
          showSizeChanger: true,
          pageSizeOptions: ['10', '20', '30', '40', '50'],
        }"
        :scroll="{ y: 300 }"
        @change="handlePageChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag
              :color="
                record.status === 'CHO_XAC_NHAN'
                  ? 'orange'
                  : record.status === 'DA_XAC_NHAN'
                  ? 'gold'
                  : record.status === 'CHO_GIAO'
                  ? 'blue'
                  : record.status === 'DANG_GIAO'
                  ? 'cyan'
                  : record.status === 'XAC_NHAN_THANH_TOAN'
                  ? 'purple'
                  : record.status === 'HOAN_THANH'
                  ? 'green'
                  : record.status === 'DA_HUY'
                  ? 'red'
                  : 'default'
              "
            >
              {{
                record.status === "CHO_XAC_NHAN"
                  ? "Chờ xác nhận"
                  : record.status === "DA_XAC_NHAN"
                  ? "Đã xác nhận"
                  : record.status === "CHO_GIAO"
                  ? "Chờ giao"
                  : record.status === "DANG_GIAO"
                  ? "Đang giao"
                  : record.status === "XAC_NHAN_THANH_TOAN"
                  ? "Xác nhận thanh toán"
                  : record.status === "HOAN_THANH"
                  ? "Hoàn thành"
                  : record.status === "DA_HUY"
                  ? "Đã hủy"
                  : "Không rõ"
              }}
            </a-tag>
          </template>

          <template v-if="column.key === 'loaiHD'">
            <a-tag :color="
    record.loaiHoaDon === 'OFFLINE' ? 'green' :
    record.loaiHoaDon === 'GIAO_HANG' ? 'blue' :
    record.loaiHoaDon === 'ONLINE' ? 'purple' : 'default'
  ">
    {{
      record.loaiHoaDon === 'OFFLINE' ? 'OFFLINE' :
      record.loaiHoaDon === 'GIAO_HANG' ? 'GIAO HÀNG' :
      record.loaiHoaDon === 'ONLINE' ? 'ONLINE' : 'Không rõ'
    }}
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
                   @click="handleViewClick(record.maHoaDon, record.id)"
                  class="p-2 d-flex justify-content-center align-items-center"
                  style="
                    background-color: #54bddb;
                    border-color: #54bddb;
                    color: white;
                  "
                >
                  <EyeOutlined style="font-size: 18px" />
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
import DivCustom from "@/components/custom/Div/DivCustomTable.vue";
import { EditOutlined, EyeOutlined } from "@ant-design/icons-vue";
import type { TableColumnsType } from "ant-design-vue";
import { defineEmits, defineProps, computed, ref } from "vue";
import { useRouter } from "vue-router";

const props = defineProps<{
  paginationParams: { page: number; size: number };
  totalItems: number;
  products: any[];
  countByStatus: Record<string, number>;
}>();

const router = useRouter();
const emit = defineEmits(["page-change", "add", "view", "changeStatus","tab-change"]);

const columns: TableColumnsType = [
  { title: "STT", key: "stt", dataIndex: "stt", width: 60, align: "center" },
  {
    title: "Mã Hóa Đơn",
    key: "maHoaDon",
    dataIndex: "maHoaDon",
    width: 115,
    align: "center",
  },
  {
    title: "Tên Khách Hàng",
    key: "tenKhachHang",
    dataIndex: "tenKhachHang",
    width: 135,
    align: "center",
  },
  {
    title: "SDT Khách Hàng",
    key: "sdtKhachHang",
    dataIndex: "sdtKhachHang",
    width: 138,
    align: "center",
  },
  {
    title: "Loại Hóa Đơn",
    key: "loaiHD",
    dataIndex: "loaiHoaDon",
    width: 119,
    align: "center",
  },
  {
    title: "Mã Nhân Viên",
    key: "maNhanVien",
    dataIndex: "maNhanVien",
    width: 125,
    align: "center",
  },
  {
    title: "Tên Nhân Viên",
    key: "tenNhanVien",
    dataIndex: "tenNhanVien",
    width: 125,
    align: "center",
  },
  {
    title: "Tổng Tiền",
    key: "tongTien",
    dataIndex: "tongTien",
    width: 120,
    align: "center",
  },
  {
    title: "Ngày Tạo",
    key: "ngayTao",
    dataIndex: "createdDate",
    width: 115,
    align: "center",
  },
  {
    title: "Trạng thái",
    key: "status",
    dataIndex: "status",
    width: 100,
    align: "center",
  },
  { title: "Hành Động", key: "operation", width: 120, align: "center" },
];

const trangThaiTab = ref<string>('ALL');

const handleTabChange = (key: string) => {
  trangThaiTab.value = key;
  emit('tab-change', key); 
};


const tabs = computed(() => {
  const statusMap = props.countByStatus || {};
  const total = Object.values(statusMap).reduce((sum, val) => sum + val, 0);

  return [
    { key: null, label: "Tất cả", count: total },
    {
      key: "CHO_XAC_NHAN",
      label: "Chờ xác nhận",
      count: statusMap.CHO_XAC_NHAN || 0,
    },
    {
      key: "DA_XAC_NHAN",
      label: "Đã xác nhận",
      count: statusMap.DA_XAC_NHAN || 0,
    },
    { key: "CHO_GIAO", label: "Chờ giao", count: statusMap.CHO_GIAO || 0 },
    { key: "DANG_GIAO", label: "Đang giao", count: statusMap.DANG_GIAO || 0 },
    {
      key: "HOAN_THANH",
      label: "Hoàn thành",
      count: statusMap.HOAN_THANH || 0,
    },
    { key: "DA_HUY", label: "Đã hủy", count: statusMap.DA_HUY || 0 },
  ];
});

const handlePageChange = (pagination: any) => {
  emit("page-change", {
    page: pagination.current,
    pageSize: pagination.pageSize,
  });
};

const handleViewClick = (maHoaDon: string, id: string) => {
  router.push({ name: "hoa-don-detail-admin", params: { maHoaDon, id } });
};

const formatCurrency = (value: number) => {
  return value.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

const formatDate = (timestamp: number) => {
  if (!timestamp) return "";
  const date = new Date(timestamp);
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
};
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
