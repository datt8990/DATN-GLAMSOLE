<template>
  <div class="container mx-auto p-6 space-y-6">
    <a-page-header title="Chi tiết hóa đơn" @back="$router.back()" />

    <a-card
      title="Thông tin hóa đơn"
      bordered
      class="shadow-md rounded-lg bg-white p-4"
    >
      <a-descriptions bordered column="2">
        <a-descriptions-item label="Mã hóa đơn">{{
          hoaDon?.maHoaDon
        }}</a-descriptions-item>
        <a-descriptions-item label="Tên hóa đơn">{{
          hoaDon?.tenHoaDon
        }}</a-descriptions-item>
        <a-descriptions-item label="Khách hàng">{{
          hoaDon?.tenKhachHang || "Không rõ"
        }}</a-descriptions-item>
        <a-descriptions-item label="SDT khách hàng">{{
          hoaDon?.sdtKH || "Không rõ"
        }}</a-descriptions-item>
        <a-descriptions-item label="Loại hóa đơn">
          <a-tag :color="hoaDon?.loaiHoaDon === '0' ? 'green' : 'orange'">
            {{ hoaDon?.loaiHoaDon || "Chưa rõ" }}
          </a-tag></a-descriptions-item
        >
        <a-descriptions-item label="Trạng thái">
          <a-tag :color="hoaDon?.trangThaiHoaDon === '0' ? 'green' : 'orange'">
            {{ hoaDon?.trangThaiHoaDon || "Chưa rõ" }}
          </a-tag>
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-card
      title="Sản phẩm có trong hóa đơn"
      bordered
      class="mt-4 shadow-lg rounded-lg bg-white"
    >
      <a-table
        :dataSource="chiTietList"
        :columns="columns"
        :pagination="false"
        :scroll="{ x: 1200 }"
        class="custom-table"
        rowKey="maHoaDonChiTiet"
      >
        <template #bodyCell="{ column, record, index }">
          <template v-if="column.key === 'stt'">
            {{ index + 1 }}
          </template>

          <template v-else-if="column.key === 'anhSanPham'">
            <div class="flex justify-center">
              <img
                :src="record.anhSanPham || 'placeholder-image.png'"
                alt="Ảnh sản phẩm"
                style="width: 80px; height: 80px; object-fit: cover"
                class="rounded-lg border-2 border-gray-200 shadow-md hover:shadow-lg transition-shadow duration-200"
              />
            </div>
          </template>

          <template v-else-if="column.key === 'sanPham'">
            <div>
              <h4 class="font-semibold text-gray-800 mb-1">
                {{ record.tenSanPham }}
              </h4>
              <p class="text-sm text-gray-600">
                {{ record.thuongHieu }} - {{ record.xuatSu }}
              </p>
              <p class="text-sm text-gray-500">{{ record.mauSac }} - {{ record.size }}</p>
            </div>
          </template>

          <template v-else-if="column.key === 'soLuong'">
            <span class="font-medium text-center block">{{
              record.soLuong
            }}</span>
          </template>

          <template v-else-if="column.key === 'thoiGian'">
            <span class="text-sm text-gray-600">{{
              formatDate(record.ngayTao)
            }}</span>
          </template>

          <template v-else-if="column.key === 'donGia'">
            <span class="font-semibold text-blue-600">
              {{ record.giaBan?.toLocaleString("vi-VN") || "0" }} ₫
            </span>
          </template>

          <template v-else-if="column.key === 'tongTien'">
            <span class="font-semibold text-green-600">
              {{ record.thanhTien?.toLocaleString("vi-VN") || "0" }} ₫
            </span>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-card title="" bordered class="shadow-md rounded-lg bg-white p-4">
  <a-descriptions bordered :column="1">
    <a-descriptions-item label="Tổng tiền hàng">
      {{ formatCurrency(tongTien) }}
    </a-descriptions-item>
    <a-descriptions-item label="Phí vận chuyển">
      {{ formatCurrency(hoaDon?.phiVanChuyen) }}
    </a-descriptions-item>
    <a-descriptions-item label="Mã Voucher">
      {{ hoaDon?.maPGG || 'Không áp dụng' }}
    </a-descriptions-item>
    <a-descriptions-item label="Tổng tiền được giảm">
      {{ formatCurrency(tongTien - (hoaDon?.tongTienSauGiam || 0)) }}
    </a-descriptions-item>
    <a-descriptions-item label="Tổng tiền sau giảm">
      {{ formatCurrency(hoaDon?.tongTienSauGiam) }}
    </a-descriptions-item>
    <a-descriptions-item label="Tổng tiền khách cần trả">
      {{ formatCurrency(hoaDon?.tongTienSauGiam) }}
    </a-descriptions-item>
  </a-descriptions>
</a-card>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";
import { getHoaDonChiTiets } from "@/services/api/admin/hoadon.api";

const route = useRoute();
const hoaDon = ref<any>(null);
const chiTietList = ref<any[]>([]);
const tongTien = ref(0);

const columns = [
  {
    title: "STT",
    key: "stt",
    width: 60,
    align: "center",
  },
  {
    title: "Ảnh sản phẩm",
    key: "anhSanPham",
    width: 120,
    align: "center",
  },
  {
    title: "Sản phẩm",
    key: "sanPham",
    width: 100,
    align: "center",
  },
  {
    title: "Số lượng",
    key: "soLuong",
    width: 70,
    align: "center",
  },
  {
    title: "Thời gian",
    key: "thoiGian",
    width: 120,
    align: "center",
  },
  {
    title: "Đơn giá",
    key: "donGia",
    width: 100,
    align: "center",
  },
  {
    title: "Tổng tiền",
    key: "tongTien",
    width: 100,
    align: "center",
  },
];

const formatDate = (dateString: string) => {
  if (!dateString) return null;
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString("vi-VN");
  } catch {
    return dateString;
  }
};

const formatCurrency = (value: number | undefined | null) => {
  if (value === undefined || value === null) return '0 ₫';
  return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
};


onMounted(async () => {
  const maHoaDon = route.params.id as string;
  const res = await getHoaDonChiTiets(maHoaDon);

  console.log(res);
  if (res && res.data && res.data.length > 0) {
    const hoaDonData = res.data[0];

    hoaDon.value = {
      maHoaDon: hoaDonData.maHoaDon,
      tenHoaDon: hoaDonData.tenHoaDon || "Không rõ",
      tenKhachHang: hoaDonData.tenKhachHang || "Không rõ",
      sdtKH: hoaDonData.sdtKH || "Không rõ",
      loaiHoaDon: hoaDonData.loaiHoaDon == "0" ? "OFFLINE" : "ONLINE",
      trangThaiHoaDon:
        hoaDonData.trangThaiHoaDon == "0" ? "Đã thanh toán" : "Chưa thanh toán",
      ngayTao: hoaDonData.ngayTao,
      phiVanChuyen: hoaDonData.phiVanChuyen,
      maPGG: hoaDonData.maVoucher,
      tongTienSauGiam: hoaDonData.tongTienSauGiam,
    };

    chiTietList.value = res.data;
    tongTien.value = chiTietList.value.reduce(
      (sum: number, item: any) => sum + item.thanhTien,
      0
    );
  } else {
    console.warn("Không có dữ liệu hóa đơn");
  }
});
</script>

<style scoped>
.custom-table :deep(.ant-table-thead > tr > th) {
  background-color: #f8fafc;
  font-weight: 600;
  color: #374151;
  border-bottom: 2px solid #e5e7eb;
}

.custom-table :deep(.ant-table-tbody > tr:hover > td) {
  background-color: #f9fafb;
}

.custom-table :deep(.ant-table-tbody > tr > td) {
  padding: 12px 8px;
  vertical-align: middle;
}

.custom-table :deep(.ant-table) {
  border-radius: 8px;
  overflow: hidden;
}

/* Đảm bảo ảnh có kích thước cố định */
.custom-table :deep(img) {
  max-width: 80px !important;
  max-height: 80px !important;
  width: 80px !important;
  height: 80px !important;
  object-fit: cover !important;
}
</style>