<template>
  <div class="checkout-page container py-4">
    <div class="mb-3">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Xác nhận & Đặt hàng" />
    </div>

    <div class="row g-4">
      <div class="col-lg-5">
        <div class="bg-white p-4 rounded shadow-sm">
          <h5 class="fw-semibold mb-4">Thông tin nhận hàng</h5>

          <a-form layout="vertical" :model="form" :rules="rules" ref="formRef">
            <div class="row g-3">
              <div class="col-sm-6">
                <a-form-item label="Họ và tên" name="hoTen">
                  <a-input v-model:value="form.hoTen" placeholder="Nguyễn Văn A" />
                </a-form-item>
              </div>
              <div class="col-sm-6">
                <a-form-item label="Số điện thoại" name="soDienThoai">
                  <a-input v-model:value="form.soDienThoai" placeholder="0123 456 789" />
                </a-form-item>
              </div>

              <div class="col-sm-4">
                <a-form-item label="Tỉnh / TP" name="tinh">
                  <a-select v-model:value="form.tinh" placeholder="Chọn tỉnh" :options="provinceOptions"
                    @change="handleProvinceChange" :loading="loadingProvinces" />
                </a-form-item>
              </div>
              <div class="col-sm-4">
                <a-form-item label="Quận / Huyện" name="huyen">
                  <a-select v-model:value="form.huyen" placeholder="Chọn huyện" :options="districtOptions"
                    @change="handleDistrictChange" :disabled="!form.tinh" :loading="loadingDistricts" />
                </a-form-item>
              </div>
              <div class="col-sm-4">
                <a-form-item label="Phường / Xã" name="phuong">
                  <a-select v-model:value="form.phuong" placeholder="Chọn phường" :options="wardOptions"
                    :disabled="!form.huyen" :loading="loadingWards" />
                </a-form-item>
              </div>

              <div class="col-12">
                <a-form-item label="Địa chỉ cụ thể" name="diaChi">
                  <a-input v-model:value="form.diaChi" placeholder="Số nhà, tên đường..." />
                </a-form-item>
              </div>

              <div class="col-12">
                <a-form-item label="Ghi chú">
                  <a-textarea v-model:value="form.ghiChu" placeholder="Ghi chú thêm (nếu có)"
                    :auto-size="{ minRows: 5, maxRows: 8 }" />
                </a-form-item>
              </div>
            </div>
          </a-form>
        </div>
      </div>

      <div class="col-lg-7">
        <div class="bg-white p-4 rounded shadow-sm mb-4">
          <h5 class="fw-semibold mb-4">Đơn hàng ({{ listSanPham.length }} sản phẩm)</h5>

          <ul class="list-unstyled mb-3">
            <li class="d-flex align-items-center mb-3" v-for="item in listSanPham" :key="item.id">
              <img :src="item.imageUrl" class="rounded me-3" style="width: 50px; height: 50px; object-fit: cover" />
              <div class="flex-grow-1">
                <div class="fw-medium">{{ item.name }}</div>
                <div class="small text-muted">
                  Phân loại: Màu {{ item.color }} / Size {{ item.size }} - SL: {{ item.quantity }}
                </div>
              </div>
              <div class="fw-semibold">
                {{
                  ((item.discountPrice < item.originalPrice ? item.discountPrice : item.originalPrice) * item.quantity) |
                  localeString }}đ </div>
            </li>
          </ul>

          <div class="d-flex mb-3">
            <a-input v-model:value="form.maGiamGia" placeholder="Mã giảm giá" class="me-2" />
            <a-button @click="handleApplyDiscount">Áp dụng</a-button>
          </div>

          <div class="border-top pt-3">
            <div class="d-flex justify-content-between mb-2">
              <span class="fw-semibold">Tạm tính:</span>
              <span class="fw-semibold">{{ tongTien.toLocaleString() }}đ</span>
            </div>

            <div class="d-flex justify-content-between mb-2">
              <span class="fw-semibold">Phí vận chuyển:</span>
              <span class="fw-semibold text-success">{{ phiShip.toLocaleString() }}đ</span>
            </div>

            <div class="d-flex justify-content-between border-top pt-2 mt-2">
              <span class="fw-bold">Tổng cộng:</span>
              <span class="fw-bold text-danger">{{ tongCong.toLocaleString() }}đ</span>
            </div>
          </div>
        </div>

        <div class="bg-white p-4 rounded shadow-sm">
          <h5 class="fw-semibold mb-3">Hình thức thanh toán</h5>

          <a-radio-group v-model:value="form.thanhToan" class="d-flex flex-column gap-2">
            <a-radio value="COD">Thanh toán khi nhận hàng (COD)</a-radio>
            <a-radio value="VNPAY">Thanh toán VnPay</a-radio>
          </a-radio-group>

          <a-button type="primary" block class="mt-4" style="height: 52px; font-size: 1.15rem" @click="handleCheckout"
            :loading="loadingCheckout">
            ĐẶT HÀNG
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from "vue";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";
import BreadCrumbUser from "@/components/ui/Breadcrumbs/BreadCrumbUser.vue";
import {
  getGHNProvinces,
  getGHNDistricts,
  getGHNWards,
  calculateFee,
  getAvailableServices,
  Province,
  District,
  Ward,
  ShippingFeeRequest,
  GHNAvailableServiceRequest,
} from "@/services/api/ghn.api"; // Adjust the import path to where your GHN API functions are defined

import axios from "axios";
import type KhachHang from "@/pages/admin/khachhang/KhachHang.vue";
import { localStorageAction } from "@/utils/storage";
import { USER_INFO_STORAGE_KEY } from "@/constants/storageKey";
import { getPGG, ThanhToan } from "@/services/api/permitall/thanhtoan/thanhtoan.api";

const breadcrumbRoutes = [
  { name: "Trang chủ", path: "/" },
  { name: "Giỏ hàng", path: "/gio-hang" },
  { name: "Thanh toán", path: "/thanh-toan" },
];

// Interface definitions
interface CartItem {
  id: string;
  idSP: string;
  name: string;
  originalPrice: number;
  discountPrice: number;
  quantity: number;
  imageUrl: string;
  color: string;
  size: string;
  weight?: number;
  height?: number;
  length?: number;
  width?: number;
}

const formRef = ref();
const form = ref({
  hoTen: "",
  soDienThoai: "",
  tinh: null as number | null,
  huyen: null as number | null,
  phuong: null as string | null,
  diaChi: "",
  ghiChu: "",
  thanhToan: "COD",
  maGiamGia: "",
});

// GHN configuration
const GHN_TOKEN = "72f634c6-58a2-11f0-8a1e-1e10d8df3c04"; // Replace with your GHN API token
const SHOP_ID = 5872469; // Replace with your GHN Shop ID
const FROM_DISTRICT_ID = 3440; // Replace with your shop's district ID
const FROM_WARD_CODE = "13010"; // Replace with your shop's ward code
const idKH = localStorageAction.get(USER_INFO_STORAGE_KEY) || null

// Reactive state for GHN data
const provinces = ref<Province[]>([]);
const districts = ref<District[]>([]);
const wards = ref<Ward[]>([]);
const phiShip = ref(0);
const loadingProvinces = ref(false);
const loadingDistricts = ref(false);
const loadingWards = ref(false);
const selectedServiceId = ref<number | null>(null);
const loadingCheckout = ref(false);

// Computed options for select components
const provinceOptions = computed(() =>
  provinces.value.map((p) => ({
    value: p.ProvinceID,
    label: p.ProvinceName,
  }))
);

const districtOptions = computed(() =>
  districts.value.map((d) => ({
    value: d.DistrictID,
    label: d.DistrictName,
  }))
);

const wardOptions = computed(() =>
  wards.value.map((w) => ({
    value: w.WardCode,
    label: w.WardName,
  }))
);

// Cart items
const listSanPham = ref<CartItem[]>([]);
const giamGia = ref(0);

// Price calculations
const getPrice = (item: CartItem) => {
  return item.discountPrice < item.originalPrice ? item.discountPrice : item.originalPrice;
};

const tongTien = computed(() =>
  listSanPham.value.reduce((sum, sp) => sum + getPrice(sp) * sp.quantity, 0)
);

const tongCong = computed(() => tongTien.value + phiShip.value - giamGia.value <= 0 ? 0 : tongTien.value + phiShip.value - giamGia.value);
const tongTienTruocGiam = computed(() => tongTien.value + phiShip.value <= 0 ? 0 : tongTien.value + phiShip.value);
// Validation rules
const rules = {
  hoTen: [{ required: true, message: "Vui lòng nhập họ tên", trigger: "blur" }],
  soDienThoai: [{ required: true, message: "Vui lòng nhập số điện thoại", trigger: "blur" }],
  tinh: [{ required: true, message: "Chọn tỉnh", trigger: "change" }],
  huyen: [{ required: true, message: "Chọn huyện", trigger: "change" }],
  phuong: [{ required: true, message: "Chọn phường", trigger: "change" }],
  diaChi: [{ required: true, message: "Nhập địa chỉ cụ thể", trigger: "blur" }],
};

// Fetch provinces on mount
onMounted(async () => {
  try {
    loadingProvinces.value = true;
    provinces.value = await getGHNProvinces(GHN_TOKEN);
  } catch (error) {
    message.error("Không thể tải danh sách tỉnh/thành phố!");
  } finally {
    loadingProvinces.value = false;
  }

  const storedItems = localStorage.getItem("checkoutItems");
  if (storedItems) {
    listSanPham.value = JSON.parse(storedItems);
    console.log("Dữ liệu sản phẩm đã nhận từ Local Storage:", listSanPham.value);
  } else {
    console.warn("Không tìm thấy dữ liệu sản phẩm trong Local Storage.");
  }
});

// Handle province change
const handleProvinceChange = async (provinceId: number) => {
  form.value.huyen = null;
  form.value.phuong = null;
  districts.value = [];
  wards.value = [];
  phiShip.value = 30000;

  try {
    loadingDistricts.value = true;
    districts.value = await getGHNDistricts(provinceId, GHN_TOKEN);
  } catch (error) {
    message.error("Không thể tải danh sách quận/huyện!");
  } finally {
    loadingDistricts.value = false;
  }
};

// Handle district change
const handleDistrictChange = async (districtId: number) => {
  form.value.phuong = null;
  wards.value = [];
  phiShip.value = 30000;

  try {
    loadingWards.value = true;
    wards.value = await getGHNWards(districtId, GHN_TOKEN);
  } catch (error) {
    message.error("Không thể tải danh sách phường/xã!");
  } finally {
    loadingWards.value = false;
  }
};

// Calculate shipping fee
const calculateShippingFee = async () => {
  if (!form.value.tinh || !form.value.huyen || !form.value.phuong) return;

  try {
    const serviceRequest: GHNAvailableServiceRequest = {
      shop_id: SHOP_ID,
      from_district: FROM_DISTRICT_ID,
      to_district: form.value.huyen!,
    };

    const serviceResponse = await getAvailableServices(GHN_TOKEN, serviceRequest);
    if (serviceResponse.data && serviceResponse.data.length > 0) {
      selectedServiceId.value = serviceResponse.data[0].service_id;
    } else {
      message.error("Không tìm thấy dịch vụ vận chuyển phù hợp!");
      return;
    }

    const totalWeight = listSanPham.value.reduce((sum, item) => sum + (item.weight || 1000), 0);
    const totalHeight = listSanPham.value.reduce((sum, item) => sum + (item.height || 20), 0);
    const totalLength = listSanPham.value.reduce((sum, item) => sum + (item.length || 30), 0);
    const totalWidth = listSanPham.value.reduce((sum, item) => sum + (item.width || 20), 0);

    const feeRequest: ShippingFeeRequest = {
      myRequest: {
        FromDistrictID: FROM_DISTRICT_ID,
        FromWardCode: FROM_WARD_CODE,
        ServiceID: selectedServiceId.value!,
        ToDistrictID: form.value.huyen!,
        ToWardCode: form.value.phuong!,
        Height: totalHeight,
        Length: totalLength,
        Weight: totalWeight,
        Width: totalWidth,
        InsuranceValue: tongTien.value,
        Coupon: null,
        PickShift: null,
      },
    };

    const feeResponse = await calculateFee(feeRequest, GHN_TOKEN, SHOP_ID);
    phiShip.value = feeResponse.data.total;
    message.success(`Phí vận chuyển: ${phiShip.value.toLocaleString()}đ`);
  } catch (error) {
    message.error("Không thể tính phí vận chuyển!");
    phiShip.value = 30000;
  }
};

// Watch ward changes
watch(() => form.value.phuong, calculateShippingFee);

// Apply discount
const handleApplyDiscount = async () => {
  const ma = form.value.maGiamGia?.trim();


  if (!ma) {
    message.warning("⚠️ Vui lòng nhập mã giảm giá");
    return;
  }


  const feeRequest = {
    idKH: idKH != null ? idKH.userId : "khách lẻ",
    maPGG: ma,
    TongTien: tongTienTruocGiam.value,
  };

  const found = await getPGG(feeRequest);

  const data = found.data

  console.log(data)

  const initialListSanPham = JSON.parse(JSON.stringify(listSanPham.value));
  listSanPham.value = initialListSanPham;

  if (!found) {
    giamGia.value = 0;
    message.error("❌ Mã giảm giá không hợp lệ!");
    return;
  }

  if (found.message == "Phiếu giảm giá không tồn tại") {
    message.warning(found.message);
    return
  }

  if (found.message == "Phiếu giảm giá không áp dụng cho tài khoản này") {
    message.warning(found.message);
    return
  }

  if (found.message.startsWith("Đơn")) {
    message.warning(found.message);
    return
  }


  console.log(data.kieuGiam)
  if (data.kieuGiam == false) {
    giamGia.value = data.giaGiam;
    message.success(`✅ Giảm ${data.giaGiam.toLocaleString()}đ cho đơn hàng!`);
  } else if (data.kieuGiam == true) {

    giamGia.value = (tongTienTruocGiam.value / data.phanTramGiam);

    console.log(giamGia.value)

    if (giamGia.value > data.dieuKien) {
      giamGia.value = data.dieuKien
    }

    message.success(`✅ Giảm ${giamGia.value.toLocaleString()}đ cho đơn hàng!`);
  }
};

const router = useRouter();
// ... (your existing imports and component setup)


const handleCheckout = async () => {
  try {
    loadingCheckout.value = true;
    await formRef.value.validate();

    const selectedProvince = provinces.value.find((p) => p.ProvinceID === form.value.tinh);
    const selectedDistrict = districts.value.find((d) => d.DistrictID === form.value.huyen);
    const selectedWard = wards.value.find((w) => w.WardCode === form.value.phuong);

    console.log(idKH);

    const ListSP = listSanPham.value.map(item => ({
      id: item.idSP.toString(),
      quantity: item.quantity,
    }));

    const orderData = {
      hoTen: form.value.hoTen,
      soDienThoai: form.value.soDienThoai,
      diaChi: `${form.value.diaChi}, ${selectedWard?.WardName}, ${selectedDistrict?.DistrictName}, ${selectedProvince?.ProvinceName}`,
      ghiChu: form.value.ghiChu,
      maGiamGia: form.value.maGiamGia,
      hinhThucThanhToan: form.value.thanhToan,
      tongTien: tongTien.value,
      phiShip: phiShip.value,
      giamGia: giamGia.value,
      tongCong: tongCong.value,
      sanPham: ListSP,
      KhachHang: idKH != null ? idKH.userId : "khách lẻ",
    };
    console.log("Dữ liệu gửi đi:", JSON.stringify(orderData, null, 2));
    if (form.value.thanhToan === "VNPAY") {

      // 1. Call your backend API to create the VNPAY payment
      const response = await ThanhToan(orderData); // Assuming ThanhToan now returns the VNPAY URL

      // if (response.data == null) {
      //   message.error("số lượng sản phẩm không đủ")
      // }

      if (response && response.paymentUrl) {
        // 2. Redirect the user to the VNPAY payment URL
        window.location.href = response.paymentUrl;
      } else {
        message.error("❌ Không thể tạo liên kết thanh toán VNPAY. Vui lòng thử lại!"); 
      }
    } else {
      // Handle COD (Cash on Delivery)
      await ThanhToan(orderData); // This call would be for saving COD order
      message.success("✅ Đặt hàng thành công!");
      router.push({ name: "thanh-toan-thanh-cong" });
    }
  } catch (err) {
    message.error("❌ Vui lòng kiểm tra lại thông tin và thử lại!");
    console.error("Lỗi khi xử lý thanh toán:", err);
  } finally {
    loadingCheckout.value = false;
  }
};
</script>

<style scoped>
.text-decoration-line-through {
  text-decoration: line-through;
}
</style>