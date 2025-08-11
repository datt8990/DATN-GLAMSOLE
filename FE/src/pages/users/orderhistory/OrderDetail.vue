<template>
  <div class="order-detail-container">
    <!-- Header -->
    <div class="order-header">
      <button class="back-button" @click="goBack">
        <i class="icon-arrow-left">←</i>
        TRỞ LẠI
      </button>

      <div class="order-info">
        <span class="order-code">Mã đơn hàng: {{ orderDetail?.maHoaDon }}</span>
        <span
          :class="[
            'order-status',
            getStatusClass(orderDetail?.trangThaiHoaDon),
          ]"
        >
          {{ getStatusText(orderDetail?.trangThaiHoaDon) }}
        </span>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Đang tải chi tiết đơn hàng...</p>
    </div>

    <!-- Order Detail Content -->
    <div v-else-if="orderDetail" class="order-content">
      <!-- Order Timeline -->
      <div class="timeline-section">
        <div class="timeline-container">
          <div
            v-for="(step, index) in orderTimeline"
            :key="index"
            :class="[
              'timeline-step',
              {
                completed: step.completed,
                current: step.current,
                pending: !step.completed && !step.current,
              },
            ]"
          >
            <div class="timeline-icon">
              <span class="timeline-emoji">{{ step.icon }}</span>
            </div>
            <div class="timeline-content">
              <h4 class="timeline-title">{{ step.title }}</h4>
              <p v-if="step.time" class="timeline-time">{{ step.time }}</p>
              <p v-if="step.note" class="timeline-note">{{ step.note }}</p>
            </div>
            <div
              v-if="index < orderTimeline.length - 1"
              class="timeline-line"
            ></div>
          </div>
        </div>
      </div>

      <!-- Products List -->
      <div class="products-section">
        <h3 class="section-title">Sản phẩm đã đặt</h3>
        <div class="products-list">
          <div
            v-for="(product, index) in orderDetail.products"
            :key="index"
            class="product-item"
          >
            <div class="product-image">
              <img
                v-if="product.anhSanPham"
                :src="product.anhSanPham"
                :alt="product.tenSanPham"
                @error="handleImageError"
              />
              <div v-else class="product-image-placeholder">
                <span>📦</span>
              </div>
            </div>
            <div class="product-details">
              <h4 class="product-name">{{ product.tenSanPham }}</h4>
              <p class="product-brand">{{ product.thuongHieu }}</p>
              <p class="product-variant">
                Size: {{ product.size }} - Màu: {{ product.mauSac }}
              </p>
              <p class="product-quantity">Số lượng: {{ product.soLuong }}</p>
            </div>
            <div class="product-price">
              <span class="price">{{ formatCurrency(product.giaBan) }}</span>
              <p v-if="product.soLuong > 1" class="subtotal">
                Tổng: {{ formatCurrency(product.giaBan * product.soLuong) }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- Delivery Info -->
      <div class="delivery-section">
        <h3 class="section-title">
          <i class="icon-location">📍</i>
          Thông tin giao hàng
        </h3>
        <div class="delivery-info">
          <div class="delivery-details">
            <h4 class="customer-name">{{ orderDetail.tenKhachHang }}</h4>
            <p class="customer-phone">{{ orderDetail.sdtKH }}</p>
            <p class="customer-email" v-if="orderDetail.email">
              {{ orderDetail.email }}
            </p>
            <p class="customer-address">{{ orderDetail.diaChi }}</p>
          </div>
          <div class="delivery-icon">
            <button
              class="btn-view-delivery"
              @click="showDeliveryModal"
              :disabled="isNotPending"
            >
              <i>✏️</i>
            </button>
          </div>
        </div>
      </div>

      <!-- Delivery Modal -->
      <a-modal
        v-model:visible="deliveryModalVisible"
        title="Chỉnh sửa thông tin giao hàng"
        :width="500"
        centered
        @ok="handleSaveDelivery"
        @cancel="handleCancelDelivery"
      >
        <a-form :model="deliveryForm" layout="vertical" ref="deliveryFormRef">
          <a-form-item
            label="Họ tên"
            name="tenKhachHang"
            :rules="[{ required: true, message: 'Vui lòng nhập họ tên' }]"
          >
            <a-input
              v-model:value="deliveryForm.tenKhachHang"
              placeholder="Nhập họ tên"
            />
          </a-form-item>
          <a-form-item
            label="Số điện thoại"
            name="sdtKH"
            :rules="[
              {
                required: true,
                message: 'Vui lòng nhập số điện thoại',
                pattern: /^[0-9]{10}$/,
                message: 'Số điện thoại phải là 10 số',
              },
            ]"
          >
            <a-input
              v-model:value="deliveryForm.sdtKH"
              placeholder="Nhập số điện thoại"
            />
          </a-form-item>
          <a-form-item label="Email" name="email">
            <a-input
              v-model:value="deliveryForm.email"
              placeholder="Nhập email (không bắt buộc)"
            />
          </a-form-item>
          <a-form-item
            label="Tỉnh/Thành phố"
            name="provinceId"
            :rules="[
              { required: true, message: 'Vui lòng chọn tỉnh/thành phố' },
            ]"
          >
            <a-select
              v-model:value="deliveryForm.provinceId"
              placeholder="Chọn tỉnh/thành phố"
              @change="handleProvinceChange"
              :disabled="isNotPending"
            >
              <a-select-option
                v-for="province in provinces"
                :key="province.ProvinceID"
                :value="province.ProvinceID"
              >
                {{ province.ProvinceName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="Quận/Huyện"
            name="districtId"
            :rules="[{ required: true, message: 'Vui lòng chọn quận/huyện' }]"
          >
            <a-select
              v-model:value="deliveryForm.districtId"
              placeholder="Chọn quận/huyện"
              @change="handleDistrictChange"
              :disabled="isNotPending || !deliveryForm.provinceId"
            >
              <a-select-option
                v-for="district in districts"
                :key="district.DistrictID"
                :value="district.DistrictID"
              >
                {{ district.DistrictName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="Phường/Xã"
            name="wardCode"
            :rules="[{ required: true, message: 'Vui lòng chọn phường/xã' }]"
          >
            <a-select
              v-model:value="deliveryForm.wardCode"
              placeholder="Chọn phường/xã"
              :disabled="isNotPending || !deliveryForm.districtId"
            >
              <a-select-option
                v-for="ward in wards"
                :key="ward.WardCode"
                :value="ward.WardCode"
              >
                {{ ward.WardName }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="Địa chỉ cụ thể"
            name="diaChiCuThe"
            :rules="[
              { required: true, message: 'Vui lòng nhập địa chỉ cụ thể' },
            ]"
          >
            <a-input
              v-model:value="deliveryForm.diaChiCuThe"
              placeholder="Nhập địa chỉ cụ thể (số nhà, đường)"
              :disabled="isNotPending"
            />
          </a-form-item>
        </a-form>
      </a-modal>

      <!-- Payment Info -->
      <div class="payment-section">
        <h3 class="section-title">Thanh toán</h3>
        <div class="payment-summary">
          <div class="payment-row">
            <span class="payment-label">Tổng tiền hàng:</span>
            <span class="payment-value">{{
              formatCurrency(orderDetail.thanhTien || 0)
            }}</span>
          </div>
          <div class="payment-row">
            <span class="payment-label"
              >Voucher ({{ orderDetail.tenVoucher || "Không áp dụng" }}):</span
            >
            <span class="payment-value discount"
              >-{{ formatCurrency(voucherValue) }}</span
            >
          </div>
          <div class="payment-row">
            <span class="payment-label">Phí vận chuyển:</span>
            <span class="payment-value">{{
              formatCurrency(orderDetail.phiVanChuyen || 0)
            }}</span>
          </div>
          <div class="payment-row total">
            <span class="payment-label">Tổng thanh toán:</span>
            <span class="payment-value total-amount">{{
              formatCurrency(orderDetail.tongTienSauGiam)
            }}</span>
          </div>
          <div class="payment-method">
            <span class="payment-method-label">Phương thức thanh toán:</span>
            <span class="payment-method-value">{{
              getPaymentMethodText(orderDetail.loaiHoaDon)
            }}</span>
          </div>
        </div>
      </div>

      <!-- Order Actions -->
      <div class="order-actions">
        <button
          v-if="orderDetail.trangThaiHoaDon === '0'"
          class="btn-cancel"
          @click="cancelOrder"
        >
          Hủy đơn hàng
        </button>
        <button
          v-if="orderDetail.trangThaiHoaDon === '4'"
          class="btn-reorder"
          @click="reorder"
        >
          Mua lại
        </button>
        <button class="btn-contact" @click="contactSupport">
          Liên hệ hỗ trợ
        </button>
      </div>
    </div>

    <!-- Error state -->
    <div v-else class="error-state">
      <div class="error-icon">❌</div>
      <h3>Không tìm thấy đơn hàng</h3>
      <p>Đơn hàng có thể đã bị xóa hoặc không tồn tại.</p>
      <button class="btn-back" @click="goBack">Quay lại</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, reactive, watch, nextTick } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Modal, message } from "ant-design-vue";
import {
  getHoaDonChiTiets,
  GetLSTTHD,
  getSuaThongTin,
  changeStatus,
  type ParamsGetHoaDonCT,
} from "@/services/api/permitall/donmua/donmua.api";
import {
  getGHNProvinces,
  getGHNDistricts,
  getGHNWards,
  calculateFee,
  type Province,
  type District,
  type Ward,
  type ShippingFeeRequest,
  type ShippingFeeResponse,
  type AvailableServiceRequest,
  getAvailableServices,
} from "@/services/api/ghn.api";
import axios from "axios";

// Constants for GHN
const GHN_TOKEN = "72f634c6-58a2-11f0-8a1e-1e10d8df3c04";
const SHOP_ID = 5872469;
const SHOP_DISTRICT_ID = 3440;
const SHOP_WARD_CODE = "13010";
const DEFAULT_SERVICE_ID = 53321;
const DEFAULT_WEIGHT = 500;
const DEFAULT_HEIGHT = 15;
const DEFAULT_LENGTH = 15;
const DEFAULT_WIDTH = 15;

// Types
interface OrderDetail {
  maHoaDon: string;
  tenHoaDon: string;
  trangThaiHoaDon: string;
  tenKhachHang: string;
  sdtKH: string;
  email?: string;
  diaChi: string;
  tongTien: number;
  phiVanChuyen: number;
  thanhTien: number;
  tongTienSauGiam: number;
  loaiHoaDon: string;
  maVoucher?: string;
  tenVoucher?: string;
  giaTriVoucher?: number;
  ngayTao: number;
  products: ProductItem[];
  toDistrictID?: number;
  toWardCode?: string;
}

interface ProductItem {
  maHoaDonChiTiet: string;
  tenSanPham: string;
  thuongHieu: string;
  mauSac: string;
  size: string;
  soLuong: number;
  giaBan: number;
  anhSanPham?: string;
  xuatSu?: string;
}

interface TimelineStep {
  title: string;
  time?: string;
  note?: string;
  icon: string;
  completed: boolean;
  current: boolean;
}

interface TimelineData {
  trangThai: string;
  note: string;
  thoiGian: string;
}

// Router
const router = useRouter();
const route = useRoute();

// Reactive data
const loading = ref(false);
const orderDetail = ref<OrderDetail | null>(null);
const timelineStatusData = ref<TimelineData[]>([]);
const deliveryModalVisible = ref(false);
const deliveryFormRef = ref();
const deliveryForm = reactive({
  tenKhachHang: "",
  sdtKH: "",
  email: "",
  provinceId: undefined as number | undefined,
  districtId: undefined as number | undefined,
  wardCode: "" as string,
  diaChiCuThe: "",
});
const provinces = ref<Province[]>([]);
const districts = ref<District[]>([]);
const wards = ref<Ward[]>([]);

// Computed
const isNotPending = computed(() => orderDetail.value?.trangThaiHoaDon !== "0");
const voucherValue = computed(() => {
  if (orderDetail.value) {
    return (
      orderDetail.value.thanhTien +
      orderDetail.value.phiVanChuyen -
      orderDetail.value.tongTienSauGiam
    );
  }
  return 0;
});

const orderTimeline = computed<TimelineStep[]>(() => {
  if (!orderDetail.value) return [];

  const currentStatus = orderDetail.value.trangThaiHoaDon;

  const statusSteps = [
    { code: "0", title: "Đơn hàng đã tạo", icon: "📝" },
    { code: "1", title: "Đã xác nhận", icon: "✅" },
    { code: "2", title: "Chuẩn bị hàng", icon: "📦" },
    { code: "3", title: "Đang giao hàng", icon: "🚚" },
    { code: "4", title: "Hoàn thành", icon: "🎉" },
  ];

  if (currentStatus === "5") {
    const cancelledStep = timelineStatusData.value.find(
      (item) => item.trangThai === "5"
    );
    return [
      {
        title: "Đơn hàng đã tạo",
        time: formatApiDateTime(orderDetail.value.ngayTao),
        note: "Đơn hàng đã được tạo",
        icon: "📝",
        completed: true,
        current: false,
      },
      {
        title: "Đơn hàng đã hủy",
        time: cancelledStep ? formatApiDateTime(cancelledStep.thoiGian) : "",
        note: cancelledStep?.note || "Đơn hàng đã bị hủy",
        icon: "❌",
        completed: true,
        current: false,
      },
    ];
  }

  return statusSteps.map((step, index) => {
    const statusData = timelineStatusData.value.find(
      (item) => item.trangThai === step.code
    );
    const stepStatus = parseInt(step.code);
    const currentStatusNum = parseInt(currentStatus);

    return {
      title: step.title,
      time: statusData ? formatApiDateTime(statusData.thoiGian) : "",
      note: statusData?.note || "",
      icon: step.icon,
      completed: stepStatus < currentStatusNum,
      current: stepStatus === currentStatusNum,
    };
  });
});

// Load provinces
const loadProvinces = async () => {
  try {
    provinces.value = await getGHNProvinces(GHN_TOKEN);
  } catch (error) {
    message.error("Không thể tải danh sách tỉnh/thành phố");
  }
};

// Handle province change
const handleProvinceChange = async (value: number) => {
  districts.value = [];
  wards.value = [];
  deliveryForm.districtId = undefined;
  deliveryForm.wardCode = "";
  try {
    districts.value = await getGHNDistricts(value, GHN_TOKEN);
  } catch (error) {
    message.error("Không thể tải danh sách quận/huyện");
  }
};

// Handle district change
const handleDistrictChange = async (value: number) => {
  wards.value = [];
  deliveryForm.wardCode = "";
  try {
    wards.value = await getGHNWards(value, GHN_TOKEN);
  } catch (error) {
    message.error("Không thể tải danh sách phường/xã");
  }
};

// API Functions
const fetchOrderDetail = async (orderId: string) => {
  try {
    loading.value = true;
    const maHoaDon = route.params.maHoaDon as string;
    const idHoaDon = route.params.id as string;

    const response = await getHoaDonChiTiets({ maHoaDon });

    if (response.status === "OK" && response.data && response.data.length > 0) {
      const firstItem = response.data[0];
      const products = response.data.map((item) => ({
        maHoaDonChiTiet: item.maHoaDonChiTiet,
        tenSanPham: item.tenSanPham,
        thuongHieu: item.thuongHieu,
        mauSac: item.mauSac,
        size: item.size,
        soLuong: item.soLuong,
        giaBan: item.giaBan,
        anhSanPham: item.anhSanPham,
        xuatSu: item.xuatSu,
      }));

      orderDetail.value = {
        maHoaDon: firstItem.maHoaDon,
        tenHoaDon: firstItem.tenHoaDon,
        trangThaiHoaDon: firstItem.trangThaiHoaDon,
        tenKhachHang: firstItem.tenKhachHang,
        sdtKH: firstItem.sdtKH,
        email: firstItem.email,
        diaChi: firstItem.diaChi,
        tongTien: firstItem.tongTien,
        phiVanChuyen: firstItem.phiVanChuyen,
        thanhTien: firstItem.thanhTien,
        tongTienSauGiam: firstItem.tongTienSauGiam,
        loaiHoaDon: firstItem.loaiHoaDon,
        maVoucher: firstItem.maVoucher,
        tenVoucher: firstItem.tenVoucher,
        giaTriVoucher: firstItem.giaTriVoucher,
        ngayTao: firstItem.ngayTao,
        products: products,
        toDistrictID: firstItem.toDistrictID,
        toWardCode: firstItem.toWardCode,
      };

      try {
        const statusResponse = await GetLSTTHD(idHoaDon);
        if (
          statusResponse &&
          statusResponse.status === "OK" &&
          statusResponse.data
        ) {
          timelineStatusData.value = statusResponse.data;
        }
      } catch (statusError) {
        console.warn("Lỗi khi lấy dữ liệu timeline:", statusError);
      }
    } else {
      message.error("Không thể tải chi tiết đơn hàng");
      orderDetail.value = null;
    }
  } catch (error) {
    console.error("Error fetching order detail:", error);
    message.error("Có lỗi xảy ra khi tải chi tiết đơn hàng");
    orderDetail.value = null;
  } finally {
    loading.value = false;
  }
};

// Event Handlers
const goBack = () => {
  router.go(-1);
};

const cancelOrder = () => {
  const maHoaDon = route.params.maHoaDon as string;

  Modal.confirm({
    title: "Xác nhận hủy đơn hàng",
    content: "Bạn có chắc chắn muốn hủy đơn hàng này không?",
    async onOk() {
      try {
        const response = await changeStatus({
          maHoaDon: maHoaDon,
          status: "DA_HUY", // Enum từ backend
          note: "Khách hàng hủy đơn hàng",
        });

        if (response.success) {
          message.success("Hủy đơn hàng thành công");
          fetchOrderDetail(route.params.id as string);
        } else {
          message.error(response.message || "Hủy đơn hàng thất bại");
        }
      } catch (error) {
        message.error("Có lỗi xảy ra khi hủy đơn hàng");
      }
    },
  });
};

const reorder = () => {
  message.info("Chức năng mua lại đang được phát triển");
};

const contactSupport = () => {
  message.info("Liên hệ hỗ trợ: 1900-xxxx");
};

const handleImageError = (event: Event) => {
  const target = event.target as HTMLImageElement;
  target.style.display = "none";
  const placeholder = target.nextElementSibling as HTMLElement;
  if (placeholder) {
    placeholder.style.display = "flex";
  }
};

const showDeliveryModal = async () => {
  if (orderDetail.value) {
    deliveryForm.tenKhachHang = orderDetail.value.tenKhachHang;
    deliveryForm.sdtKH = orderDetail.value.sdtKH;
    deliveryForm.email = orderDetail.value.email || "";
    deliveryForm.diaChiCuThe = "";
    const addressParts = orderDetail.value.diaChi
      .split(", ")
      .map((part) => part.trim());
    if (addressParts.length === 4 && !isNotPending.value) {
      deliveryForm.diaChiCuThe = addressParts[0];
      const wardName = addressParts[1];
      const districtName = addressParts[2];
      const provinceName = addressParts[3];

      await loadProvinces();
      const selectedProvince = provinces.value.find(
        (p) => p.ProvinceName === provinceName
      );
      if (selectedProvince) {
        deliveryForm.provinceId = selectedProvince.ProvinceID;
        await handleProvinceChange(selectedProvince.ProvinceID);
        const selectedDistrict = districts.value.find(
          (d) => d.DistrictName === districtName
        );
        if (selectedDistrict) {
          deliveryForm.districtId = selectedDistrict.DistrictID;
          await handleDistrictChange(selectedDistrict.DistrictID);
          const selectedWard = wards.value.find((w) => w.WardName === wardName);
          if (selectedWard) {
            deliveryForm.wardCode = selectedWard.WardCode;
          }
        }
      }
    } else {
      await loadProvinces();
    }
    deliveryModalVisible.value = true;
  }
};

const handleSaveDelivery = async () => {
  try {
    await deliveryFormRef.value.validate();

    if (orderDetail.value) {
      if (orderDetail.value.trangThaiHoaDon !== "0") {
        message.warning(
          "Chỉ có thể chỉnh sửa thông tin giao hàng khi đơn hàng đang chờ xác nhận"
        );
        return;
      }

      const selectedProvince = provinces.value.find(
        (p) => p.ProvinceID === deliveryForm.provinceId
      );
      const selectedDistrict = districts.value.find(
        (d) => d.DistrictID === deliveryForm.districtId
      );
      const selectedWard = wards.value.find(
        (w) => w.WardCode === deliveryForm.wardCode
      );

      let fullAddress = "";
      if (selectedProvince && selectedDistrict && selectedWard) {
        fullAddress = `${deliveryForm.diaChiCuThe}, ${selectedWard.WardName}, ${selectedDistrict.DistrictName}, ${selectedProvince.ProvinceName}`;
      }

      const availableServicesRequestBody: AvailableServiceRequest = {
        shop_id: SHOP_ID,
        from_district: SHOP_DISTRICT_ID,
        to_district: deliveryForm.districtId!,
      };
      const availableServicesResponse = await getAvailableServices(
        GHN_TOKEN,
        availableServicesRequestBody
      );

      const selectedServiceId = availableServicesResponse.data[0].service_id;

      const insuranceValue = orderDetail.value.thanhTien || 0;
      const myRequest: ShippingFeeRequest = {
        myRequest: {
          FromDistrictID: SHOP_DISTRICT_ID,
          FromWardCode: SHOP_WARD_CODE,
          ServiceID: selectedServiceId,
          ToDistrictID: deliveryForm.districtId!,
          ToWardCode: deliveryForm.wardCode,
          Height: DEFAULT_HEIGHT,
          Length: DEFAULT_LENGTH,
          Weight: DEFAULT_WEIGHT,
          Width: DEFAULT_WIDTH,
          InsuranceValue: insuranceValue,
          Coupon: null,
          PickShift: null,
        },
      };

      const feeResponse: ShippingFeeResponse = await calculateFee(
        myRequest,
        GHN_TOKEN,
        SHOP_ID
      );
      const newPhi = feeResponse.data.total;

      const currentVoucherValue = orderDetail.value.giaTriVoucher || 0;
      const newTongTienSauGiam =
        orderDetail.value.thanhTien + newPhi - currentVoucherValue;

      const updateDeliveryDTO = {
        maHoaDon: orderDetail.value.maHoaDon,
        tenKhachHang: deliveryForm.tenKhachHang,
        sdtKhachHang: deliveryForm.sdtKH,
        email: deliveryForm.email,
        diaChi: fullAddress,
        phiVanChuyen: newPhi,
        tongTienSauGiam: newTongTienSauGiam,
      };
      const response = await getSuaThongTin(updateDeliveryDTO);
      console.log("Update response:", response);
      console.log("Update response:", response.message);
      console.log("Update response:", response.data);
      orderDetail.value = {
        ...orderDetail.value,
        tenKhachHang: deliveryForm.tenKhachHang,
        sdtKH: deliveryForm.sdtKH,
        email: deliveryForm.email,
        diaChi: fullAddress,
        phiVanChuyen: newPhi,
        tongTienSauGiam: newTongTienSauGiam,
      };

      message.success(
        "Cập nhật thông tin giao hàng và phí vận chuyển thành công"
      );
      deliveryModalVisible.value = false;
    }
  } catch (error) {
    console.error("Error updating delivery:", error);
    message.error("Có lỗi xảy ra khi cập nhật thông tin");
  }
};

const handleCancelDelivery = () => {
  deliveryModalVisible.value = false;
};

// Utility Functions
const getStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    "0": "pending",
    "1": "confirmed",
    "2": "preparing",
    "3": "shipping",
    "4": "completed",
    "5": "cancelled",
  };
  return classMap[status] || "default";
};

const getStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    "0": "Chờ xác nhận",
    "1": "Đã xác nhận",
    "2": "Chuẩn bị hàng",
    "3": "Đang giao hàng",
    "4": "Hoàn thành",
    "5": "Đã hủy",
  };
  return textMap[status] || "Không rõ";
};

const getPaymentMethodText = (method?: string) => {
  const methodMap: Record<string, string> = {
    "1": "Thanh toán online",
    "2": "Thanh toán khi nhận hàng (COD)",
    "0": "Tại quầy",
  };
  return methodMap[method || ""] || "Chưa xác định";
};

const formatCurrency = (value: number) => {
  if (!value && value !== 0) return "0 ₫";
  return value.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

const formatApiDateTime = (dateTimeString: string | number) => {
  if (!dateTimeString) return "";

  let date: Date;
  if (typeof dateTimeString === "number") {
    date = new Date(dateTimeString);
  } else {
    date = new Date(dateTimeString);
  }

  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  return `${hours}:${minutes} ${day}/${month}/${year}`;
};

// Lifecycle
onMounted(() => {
  const orderId = route.params.id as string;
  if (orderId) {
    fetchOrderDetail(orderId);
  } else {
    message.error("Không tìm thấy mã đơn hàng");
    router.push({ name: "don-mua" });
  }
});

// Expose functions
defineExpose({
  fetchOrderDetail,
  refresh: () => fetchOrderDetail(route.params.id as string),
});
</script>
<style scoped lang="scss">
.order-detail-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.order-header {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .back-button {
    display: flex;
    align-items: center;
    gap: 8px;
    background: none;
    border: none;
    color: #666;
    cursor: pointer;
    font-size: 14px;
    margin-bottom: 16px;
    padding: 0;

    &:hover {
      color: #58bddb;
    }

    .icon-arrow-left {
      font-size: 16px;
    }
  }

  .order-info {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;

    .order-code {
      font-weight: 600;
      color: #333;
      font-size: 18px;
    }

    .order-status {
      padding: 6px 12px;
      border-radius: 6px;
      font-size: 12px;
      font-weight: 500;
      white-space: nowrap;

      &.pending {
        background-color: #fff7e6;
        color: #fa8c16;
        border: 1px solid #ffd591;
      }

      &.confirmed {
        background-color: #fff7e6;
        color: #faad14;
        border: 1px solid #ffd591;
      }

      &.preparing {
        background-color: #e6f7ff;
        color: #1890ff;
        border: 1px solid #91d5ff;
      }

      &.shipping {
        background-color: #e6fffb;
        color: #13c2c2;
        border: 1px solid #87e8de;
      }

      &.completed {
        background-color: #f6ffed;
        color: #52c41a;
        border: 1px solid #b7eb8f;
      }

      &.cancelled {
        background-color: #fff2f0;
        color: #ff4d4f;
        border: 1px solid #ffadd2;
      }
    }
  }
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .loading-spinner {
    width: 40px;
    height: 40px;
    border: 3px solid #f3f3f3;
    border-top: 3px solid #58bddb;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 16px;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }

    100% {
      transform: rotate(360deg);
    }
  }

  p {
    color: #666;
    margin: 0;
    font-size: 14px;
  }
}

.order-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.timeline-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .timeline-container {
    position: relative;
  }

  .timeline-step {
    display: flex;
    align-items: flex-start;
    position: relative;
    padding-bottom: 24px;

    &:last-child {
      padding-bottom: 0;

      .timeline-line {
        display: none;
      }
    }

    .timeline-icon {
      width: 48px;
      height: 48px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      flex-shrink: 0;
      z-index: 2;
      position: relative;
      border: 2px solid transparent;

      .timeline-emoji {
        font-size: 20px;
        display: block;
        line-height: 1;
      }
    }

    .timeline-content {
      flex: 1;
      padding-top: 8px;

      .timeline-title {
        margin: 0 0 4px 0;
        font-size: 16px;
        font-weight: 600;
        color: #333;
      }

      .timeline-time {
        margin: 0 0 4px 0;
        font-size: 13px;
        color: #666;
        font-weight: 500;
      }

      .timeline-note {
        margin: 0;
        font-size: 12px;
        color: #999;
        font-style: italic;
        line-height: 1.4;
      }
    }

    .timeline-line {
      position: absolute;
      left: 24px;
      top: 48px;
      bottom: -24px;
      width: 2px;
      z-index: 1;
    }

    &.completed {
      .timeline-icon {
        background: linear-gradient(135deg, #52c41a, #389e0d);
        color: white;
        border-color: #52c41a;
      }

      .timeline-line {
        background: linear-gradient(180deg, #52c41a, #f0f0f0);
      }

      .timeline-title {
        color: #52c41a;
      }
    }

    &.current {
      .timeline-icon {
        background: linear-gradient(135deg, #58bddb, #3fa3b9);
        color: white;
        animation: pulse 2s infinite;
        border-color: #58bddb;
      }

      .timeline-line {
        background: linear-gradient(180deg, #58bddb, #f0f0f0);
      }

      .timeline-title {
        color: #58bddb;
        font-weight: 700;
      }
    }

    &.pending {
      .timeline-icon {
        background: #f5f5f5;
        color: #999;
        border-color: #d9d9d9;
      }

      .timeline-line {
        background: #f0f0f0;
      }

      .timeline-title {
        color: #999;
      }
    }
  }

  @keyframes pulse {
    0% {
      box-shadow: 0 0 0 0 rgba(88, 189, 219, 0.4);
    }

    70% {
      box-shadow: 0 0 0 10px rgba(88, 189, 219, 0);
    }

    100% {
      box-shadow: 0 0 0 0 rgba(88, 189, 219, 0);
    }
  }
}

.products-section,
.delivery-section,
.payment-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .section-title {
    margin: 0 0 20px 0;
    font-size: 18px;
    font-weight: 600;
    color: #333;
    display: flex;
    align-items: center;
    gap: 8px;
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 12px;

    i {
      font-size: 20px;
    }
  }
}

.products-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  gap: 15px;
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  }

  .product-image {
    width: 80px;
    height: 80px;
    border-radius: 8px;
    overflow: hidden;
    flex-shrink: 0;
    background-color: #f5f5f5;
    position: relative;
    border: 1px solid #e8e8e8;

    .product-image-placeholder {
      width: 100%;
      height: 100%;
      background-color: #f5f5f5;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      color: #ccc;
      border: 1px dashed #ddd;
    }

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.05);
      }
    }
  }

  .product-details {
    flex: 1;
    min-width: 0;

    .product-name {
      margin: 0 0 4px 0;
      font-size: 16px;
      font-weight: 600;
      color: #333;
      line-height: 1.4;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .product-brand {
      margin: 0 0 4px 0;
      color: #666;
      font-size: 14px;
      font-weight: 500;
    }

    .product-variant {
      margin: 0 0 4px 0;
      color: #999;
      font-size: 13px;
      background: #e6f7ff;
      padding: 2px 6px;
      border-radius: 4px;
      display: inline-block;
    }

    .product-quantity {
      margin: 0;
      color: #666;
      font-size: 13px;
      font-weight: 500;
    }
  }

  .product-price {
    text-align: right;
    flex-shrink: 0;

    .price {
      font-size: 16px;
      font-weight: 700;
      color: #58bddb;
      display: block;
      margin-bottom: 4px;
    }

    .subtotal {
      font-size: 13px;
      color: #999;
      margin: 0;
      font-style: italic;
    }
  }
}

.delivery-info {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  gap: 16px;

  .delivery-details {
    flex: 1;

    .customer-name {
      margin: 0 0 8px 0;
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }

    .customer-phone {
      margin: 0 0 6px 0;
      color: #666;
      font-size: 14px;
      font-weight: 500;
    }

    .customer-email {
      margin: 0 0 6px 0;
      color: #666;
      font-size: 14px;
    }

    .customer-address {
      margin: 0;
      color: #333;
      font-size: 14px;
      line-height: 1.5;
      background: #fff;
      padding: 12px;
      border-radius: 6px;
      border: 1px solid #e8e8e8;
    }
  }

  .delivery-icon {
    width: 40px;
    height: 40px;
    flex-shrink: 0;

    .btn-view-delivery {
      width: 100%;
      height: 100%;
      background: linear-gradient(135deg, #58bddb, #3fa3b9);
      border-radius: 50%;
      border: none;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s ease;

      i {
        font-size: 20px;
        color: white;
      }

      &:hover {
        background: linear-gradient(135deg, #69c0ff, #58bddb);
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(88, 189, 219, 0.3);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }
}

.payment-summary {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;

  .payment-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    border-bottom: 1px solid #e8e8e8;

    &:last-child {
      border-bottom: none;
    }

    &.total {
      margin-top: 12px;
      padding-top: 16px;
      border-top: 2px solid #58bddb;
      border-bottom: none;

      .payment-label {
        font-size: 16px;
        font-weight: 700;
        color: #333;
      }

      .total-amount {
        font-size: 20px;
        font-weight: 700;
        color: #58bddb;
      }
    }

    .payment-label {
      font-size: 14px;
      color: #666;
      font-weight: 500;
    }

    .payment-value {
      font-size: 14px;
      font-weight: 600;
      color: #333;

      &.discount {
        color: #52c41a;
      }
    }
  }

  .payment-method {
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #e8e8e8;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .payment-method-label {
      font-size: 14px;
      color: #666;
      font-weight: 500;
    }

    .payment-method-value {
      font-size: 14px;
      font-weight: 600;
      color: #333;
      background: #e6f7ff;
      padding: 4px 8px;
      border-radius: 4px;
    }
  }
}

.order-actions {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 12px;
  flex-wrap: wrap;

  button {
    padding: 12px 24px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    border: none;
    transition: all 0.3s ease;
    flex: 1;
    min-width: 120px;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    &:active {
      transform: translateY(0);
    }
  }

  .btn-cancel {
    background: linear-gradient(135deg, #ff4d4f, #cf1322);
    color: white;

    &:hover {
      background: linear-gradient(135deg, #ff7875, #ff4d4f);
    }
  }

  .btn-reorder {
    background: linear-gradient(135deg, #58bddb, #3fa3b9);
    color: white;

    &:hover {
      background: linear-gradient(135deg, #69c0ff, #58bddb);
    }
  }

  .btn-contact {
    background: linear-gradient(135deg, #52c41a, #389e0d);
    color: white;

    &:hover {
      background: linear-gradient(135deg, #73d13d, #52c41a);
    }
  }
}

.error-state {
  background: white;
  border-radius: 12px;
  padding: 60px 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  .error-icon {
    font-size: 48px;
    margin-bottom: 16px;
  }

  h3 {
    margin: 0 0 12px 0;
    font-size: 20px;
    font-weight: 600;
    color: #333;
  }

  p {
    margin: 0 0 24px 0;
    color: #666;
    font-size: 14px;
    line-height: 1.5;
  }

  .btn-back {
    background: linear-gradient(135deg, #58bddb, #3fa3b9);
    color: white;
    border: none;
    padding: 12px 32px;
    border-radius: 8px;
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      background: linear-gradient(135deg, #69c0ff, #58bddb);
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(88, 189, 219, 0.3);
    }

    &:active {
      transform: translateY(0);
    }
  }
}

@media (max-width: 768px) {
  .order-detail-container {
    padding: 16px;
  }

  .order-header {
    padding: 16px;

    .order-info {
      flex-direction: column;
      align-items: flex-start;
      gap: 8px;

      .order-code {
        font-size: 16px;
      }
    }
  }

  .timeline-section,
  .products-section,
  .delivery-section,
  .payment-section {
    padding: 16px;

    .section-title {
      font-size: 16px;
    }
  }

  .product-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;

    .product-image {
      width: 60px;
      height: 60px;
      align-self: center;
    }

    .product-details {
      text-align: center;
      width: 100%;

      .product-name {
        font-size: 15px;
      }
    }

    .product-price {
      text-align: center;
      width: 100%;

      .price {
        font-size: 16px;
      }
    }
  }

  .delivery-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
    gap: 12px;

    .delivery-icon {
      order: -1;
    }
  }

  .payment-row {
    font-size: 13px !important;

    &.total {
      .payment-label {
        font-size: 15px !important;
      }

      .total-amount {
        font-size: 18px !important;
      }
    }
  }

  .order-actions {
    padding: 16px;

    button {
      min-width: 100px;
      font-size: 13px;
      padding: 10px 20px;
    }
  }

  .timeline-step {
    .timeline-icon {
      width: 40px;
      height: 40px;

      .timeline-emoji {
        font-size: 18px;
      }
    }

    .timeline-content {
      .timeline-title {
        font-size: 15px;
      }

      .timeline-time {
        font-size: 12px;
      }

      .timeline-note {
        font-size: 11px;
      }
    }

    .timeline-line {
      left: 20px;
      top: 40px;
    }
  }
}

@media (max-width: 480px) {
  .order-detail-container {
    padding: 12px;
  }

  .order-header,
  .timeline-section,
  .products-section,
  .delivery-section,
  .payment-section,
  .order-actions {
    padding: 12px;
  }

  .order-actions {
    flex-direction: column;

    button {
      width: 100%;
      min-width: unset;
    }
  }

  .payment-method {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>