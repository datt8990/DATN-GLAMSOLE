<template>
  <div class="container mx-auto p-6 space-y-6">
    <a-page-header title="Chi tiết hóa đơn" @back="$router.back()" />

    <!-- Card thông tin hóa đơn -->
    <a-card title="Thông tin hóa đơn" bordered class="shadow-md rounded-lg bg-white p-4">
      <a-descriptions bordered column="2">
        <a-descriptions-item label="Mã hóa đơn">{{ hoaDon?.ma }}</a-descriptions-item>
        <a-descriptions-item label="Tên hóa đơn">{{ hoaDon?.tenHoaDon }}</a-descriptions-item>
        <a-descriptions-item label="Khách hàng">{{ hoaDon?.tenKhachHang || "Không rõ" }}</a-descriptions-item>
        <a-descriptions-item label="Ngày tạo">{{ hoaDon?.ngayTao || "Chưa rõ" }}</a-descriptions-item>
        <a-descriptions-item label="Trạng thái">
          <a-tag :color="hoaDon?.status === 'INACTIVE' ? 'green' : 'orange'">
            {{ hoaDon?.trangThai || "Chưa rõ" }}
          </a-tag>
        </a-descriptions-item>
      </a-descriptions>
    </a-card>

    <!-- Card danh sách sản phẩm -->
    <a-card title="Danh sách sản phẩm" bordered class="mt-4 shadow-lg p-4 rounded-lg bg-white">
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
        <a-card
          v-for="sp in chiTietList"
          :key="sp.maHoaDonChiTiet"
          class="transition-all transform hover:scale-105 shadow-xl rounded-lg bg-gradient-to-r from-indigo-50 to-indigo-200 hover:bg-indigo-100"
        >
          <template #cover>
            <img
              alt="Ảnh sản phẩm"
              :src="sp.anhSanPham || 'placeholder-image.png'"
              class="w-full h-48 object-cover rounded-lg"
            />
          </template>
          <div class="p-6 bg-white text-gray-800">
            <h3 class="text-xl font-bold text-blue-600 mb-3">{{ sp.tenSanPham }}</h3>
            <p class="text-gray-700 mb-2"><strong>Thương hiệu:</strong> {{ sp.thuongHieu }}</p>
            <p class="text-gray-700 mb-2"><strong>Xuất xứ:</strong> {{ sp.xuatSu }}</p>
            <p class="text-gray-700 mb-2"><strong>Màu sắc:</strong> {{ sp.mauSac }}</p>
            <p class="text-gray-700 mb-2"><strong>Size:</strong> {{ sp.size }}</p>
            <p class="text-gray-700 mb-2"><strong>Số lượng:</strong> {{ sp.soLuong }}</p>
            <div class="mt-4">
              <p class="text-lg font-semibold text-green-600">
                <strong>Giá bán:</strong> {{ sp.giaBan.toLocaleString("vi-VN") }} ₫
              </p>
              <p class="text-lg font-semibold text-red-600">
                <strong>Thành tiền:</strong> {{ sp.thanhTien.toLocaleString("vi-VN") }} ₫
              </p>
            </div>
          </div>
        </a-card>
      </div>
    </a-card>

    <!-- Tổng tiền -->
    <div class="text-right text-2xl font-extrabold mt-6">
      Tổng tiền: <span class="text-red-600">{{ tongTien.toLocaleString("vi-VN") }} ₫</span>
    </div>
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

onMounted(async () => {  
  const maHoaDon = route.params.id as string;  
  const res = await getHoaDonChiTiets(maHoaDon);  
  console.log(res); // Kiểm tra dữ liệu phản hồi  

  // Kiểm tra phản hồi để xử lý dữ liệu  
  if (res && res.length > 0) {  
    const hoaDonData = res[0]; // Lấy hóa đơn đầu tiên từ mảng  

    hoaDon.value = {  
      ma: maHoaDon,  
      tenHoaDon: hoaDonData.tenHoaDon || "Không rõ",  
      tenKhachHang: "Không rõ", // Nếu không có thông tin khách hàng  
      ngayTao: "Chưa rõ", // Nếu không có thông tin ngày tạo  
      trangThai: "Chưa rõ", // Nếu không có trạng thái rõ ràng  
    };  

    // Lấy danh sách sản phẩm từ đúng vị trí  
    chiTietList.value = hoaDonData.data || []; // Đây là danh sách sản phẩm từ hóa đơn  
    tongTien.value = chiTietList.value.reduce(  
      (sum: number, item: any) => sum + item.thanhTien,  
      0  
    ); // Tính tổng tiền  
  } else {  
    console.warn("Không có dữ liệu hóa đơn"); // Nếu không có dữ liệu  
  }  
});  
</script>  