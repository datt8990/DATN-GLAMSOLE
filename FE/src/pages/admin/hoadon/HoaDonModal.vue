<template>
  <div class="container mx-auto p-6 space-y-6">
    <div class="breadcrumb-section">
      <BreadcrumbDefault
        :pageTitle="'Chi tiết hóa đơn'"
        :routes="[
          { path: '/admin/hoa-don', name: 'Quản lý hóa đơn' },
          { path: '/admin/hoa-don-detai', name: 'Chi tiết hóa đơn' },
        ]"
      />
    </div>
    <!-- Timeline Trạng thái đơn hàng - Ngang -->
    <a-card title="TRẠNG THÁI ĐƠN HÀNG" bordered class="order-info-card">
      <div class="order-timeline-horizontal">
        <div class="timeline-container-horizontal">
          <!-- Dynamic Timeline Steps -->
          <div
            v-for="(step, index) in timelineSteps"
            :key="step.key"
            class="timeline-step-horizontal"
            :class="getStepStatus(index)"
          >
            <div class="step-content-horizontal">
              <div class="step-icon-horizontal">
                <div
                  class="icon-circle-horizontal"
                  :class="['icon-circle-horizontal', getIconClass(index)]"
                >
                  <svg
                    width="20"
                    height="20"
                    viewBox="0 0 24 24"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <path
                      :d="step.icon"
                      stroke="currentColor"
                      stroke-width="2"
                      stroke-linecap="round"
                      stroke-linejoin="round"
                    />
                  </svg>
                </div>
              </div>
              <div class="step-title-horizontal">{{ step.title }}</div>

              <div class="step-note-horizontal">
                {{ getTimelineData(index).note }}
              </div>

              <div class="step-time-horizontal">
                {{ getTimelineData(index).time }}
              </div>
            </div>
            <div
              v-if="
                index < timelineSteps.length - 1 && !isLastActiveStep(index)
              "
              class="step-line-horizontal"
              :class="getLineClass(index)"
            ></div>
          </div>
        </div>
      </div>

      <!-- Buttons for status update -->
      <div class="mt-4 flex gap-2 justify-center">
        <a-button
          style="background-color: #58bddb"
          v-if="canConfirmOrder"
          type="primary"
          @click="openStatusModal(getNextStatus())"
          >{{ getConfirmButtonText() }}
        </a-button>
        <a-button
          style="background-color: #58bddb"
          v-if="canCompleteOrder"
          type="primary"
          @click="openStatusModal(getNextStatus())"
        >
          Hoàn thành đơn hàng
        </a-button>
        <a-button
          style="background-color: #58bddb; margin-left: 5px"
          v-if="canCancelOrder1"
          type="primary"
          danger
          @click="openStatusModal('DA_HUY')"
        >
          Hủy đơn hàng
        </a-button>
      </div>
    </a-card>

    <!-- Thông tin đơn hàng và khách hàng -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- Thông tin đơn hàng -->
      <a-card title="THÔNG TIN ĐƠN HÀNG" bordered class="order-info-card">
        <div class="order-info-container">
          <!-- Cột trái -->
          <div class="order-info-column">
            <div class="order-info-row">
              <span class="label">Trạng thái:</span>
              <span class="value status">{{ displayStatus }}</span>
            </div>
            <div class="order-info-row">
              <span class="label">Mã đơn hàng:</span>
              <span class="value code">{{ hoaDon?.maHoaDon }}</span>
            </div>
            <div class="order-info-row">
              <span class="label">Loại đơn hàng:</span>
              <span class="value status">
                {{
                  hoaDon?.loaiHoaDon === "OFFLINE"
                    ? "Mua tại cửa hàng"
                    : hoaDon?.loaiHoaDon === "GIAO_HANG"
                    ? "Giao hàng"
                    : hoaDon?.loaiHoaDon === "ONLINE"
                    ? "Mua online"
                    : "Chưa rõ"
                }}
              </span>
            </div>
          </div>
          <!-- Cột phải -->
          <div class="order-info-column">
            <div class="order-info-row">
              <span class="label">Phí vận chuyển:</span>
              <span class="value price">{{
                formatCurrency(hoaDon?.phiVanChuyen)
              }}</span>
            </div>
            <div class="order-info-row">
              <span class="label">Tổng tiền:</span>
              <span class="value price">{{
                formatCurrency(subtotalAmount)
              }}</span>
            </div>
            <div class="order-info-row">
              <span class="label">Phải thanh toán:</span>
              <span class="value total">{{
                formatCurrency(finalTotalAmount)
              }}</span>
            </div>
          </div>
        </div>
      </a-card>

      <!-- Thông tin khách hàng -->
      <a-card bordered class="order-info-card">
        <template #title>
          <div class="card-title">
            <span>THÔNG TIN KHÁCH HÀNG</span>
            <!-- <a-button
              v-if="canChangeCustomerInfo"
              type="primary"
              class="change-info-btn"
            >
              Thay đổi thông tin
            </a-button> -->
          </div>
        </template>

        <div class="order-info-container">
          <!-- Cột trái -->
          <div class="order-info-column">
            <div class="order-info-row">
              <span class="label">Tên khách hàng:</span>
              <span class="value code">{{ hoaDon?.tenKhachHang }}</span>
            </div>
            <div class="order-info-row">
              <span class="label">Email:</span>
              <span class="value email">{{ hoaDon?.email }}</span>
            </div>
          </div>
          <!-- Cột phải -->
          <div class="order-info-column">
            <div class="order-info-row">
              <span class="label">Số điện thoại:</span>
              <span class="value code">{{ hoaDon?.sdtKH }}</span>
            </div>
            <div class="order-info-row">
              <span class="label">Địa chỉ:</span>
              <span class="value address text-right max-w-xs">{{
                hoaDon?.diaChi
              }}</span>
            </div>
          </div>
        </div>
      </a-card>
    </div>

    <!-- Lịch sử thanh toán -->
    <a-card title="LỊCH SỬ THANH TOÁN" bordered class="order-info-card">
      <div class="flex justify-end mb-4">
        <a-button
          style="background-color: #58bddb"
          v-if="canConfirmPayment"
          type="primary"
          class="bg-yellow-500 hover:bg-yellow-600 border-yellow-500"
          @click="openPaymentModal"
        >
          Xác nhận thanh toán
        </a-button>
      </div>
      <a-table
        :dataSource="lichSuThanhToan"
        :columns="paymentColumns"
        :pagination="false"
        class="custom-table"
        rowKey="id"
      >
        <template #bodyCell="{ column, record, index }">
          <template v-if="column.key === 'stt'">
            {{ index + 1 }}
          </template>
          <template v-else-if="column.key === 'soTien'">
            <span class="font-semibold text-green-600">
              {{ formatCurrency(record.soTien) }}
            </span>
          </template>
          <template v-else-if="column.key === 'thoiGian'">
            <span class="text-sm">{{ formatDateTime(record.thoiGian) }}</span>
          </template>
        </template>
      </a-table>
    </a-card>
    <!-- Sản phẩm có trong hóa đơn -->
    <a-card title="DANH SÁCH SẢN PHẨM" bordered class="order-info-card">
      <template #extra>
        <!-- <a-button
          v-if="canAddProduct"
          type="primary"
          :disabled="isAddProductDisabled"
          class="bg-yellow-500 hover:bg-yellow-600 border-yellow-500"
          :class="{ 'opacity-50 cursor-not-allowed': isAddProductDisabled }"
        >
          Thêm sản phẩm
        </a-button> -->
      </template>

      <div class="product-list-container">
        <div
          v-for="(record, index) in chiTietList"
          :key="record.maHoaDonChiTiet"
          class="product-row"
        >
          <!-- STT -->
          <div class="product-stt">
            {{ index + 1 }}
          </div>

          <!-- Product Image -->
          <div class="product-image">
            <img
              :src="record.anhSanPham || 'placeholder-image.png'"
              alt="Ảnh sản phẩm"
              class="product-img"
            />
          </div>

          <!-- Product Details -->
          <div class="product-details">
            <h4 class="product-name">
              {{ record.tenSanPham }} [{{ record.mauSac }} - {{ record.size }}]
            </h4>
            <p class="product-code">
              {{ record.thuongHieu }}
            </p>
            <div class="product-info">
              <span class="product-price"
                >Đơn giá: {{ formatCurrency(record.giaBan) }}</span
              >
            </div>
          </div>

          <!-- Quantity Controls -->
          <div class="product-quantity">
            <a-input-number
              v-model:value="record.soLuong"
              :min="1"
              :max="999"
              size="small"
              class="quantity-input"
              :disabled="isAddProductDisabled"
            />
          </div>

          <!-- Total Price -->
          <div class="product-total">
            {{ formatCurrency(record.soLuong * record.giaBan) }}
          </div>
        </div>

        <!-- Pagination -->
        <div class="product-pagination">
          <div class="pagination-info">3 / page</div>
          <div class="pagination-controls">
            <a-button size="small" class="pagination-btn">
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M15 18L9 12L15 6" />
              </svg>
            </a-button>
            <a-button size="small" class="pagination-current">1</a-button>
            <a-button size="small" class="pagination-btn">
              <svg
                width="16"
                height="16"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
              >
                <path d="M9 18L15 12L9 6" />
              </svg>
            </a-button>
          </div>
        </div>
      </div>
    </a-card>
  </div>
  <a-modal
    v-model:open="showStatusModal"
    title="Nhập ghi chú"
    :footer="null"
    :width="400"
    :maskClosable="false"
    @cancel="closeStatusModal"
  >
    <div class="status-modal-content">
      <div class="status-selection">
        <p class="selection-label">*Chọn mẫu tin nhắn:</p>

        <a-radio-group
          v-model:value="selectedStatusTemplate"
          class="status-radio-group"
        >
          <div class="radio-option">
            <a-radio value="confirmed">Đã xác nhận đơn hàng</a-radio>
          </div>
          <div class="radio-option">
            <a-radio value="sap_shipped"
              >Đơn hàng của bạn đã sẵn sàng để vận chuyển</a-radio
            >
          </div>
          <div class="radio-option">
            <a-radio value="shipped">Đã bàn giao cho đơn vị vận chuyển</a-radio>
          </div>
          <div class="radio-option">
            <a-radio value="payment_confirmed"
              >Đã xác nhận thông tin thanh toán đơn hàng</a-radio
            >
          </div>
          <div class="radio-option">
            <a-radio value="delivered"
              >Đơn hàng đã được giao thành công</a-radio
            >
          </div>
          <div class="radio-option">
            <a-radio value="cancelled">Đơn hàng đã bị hủy</a-radio>
          </div>
          <div class="radio-option">
            <a-radio value="other">Khác</a-radio>
          </div>
        </a-radio-group>
      </div>

      <div class="note-section">
        <a-textarea
          v-model:value="statusNote"
          :rows="4"
          placeholder="Đơn hàng đã được giao thành công"
          class="status-textarea"
        />
      </div>

      <div class="modal-actions">
        <a-button @click="closeStatusModal" class="cancel-btn"> Hủy </a-button>
        <a-button
          type="primary"
          @click="confirmStatusChange"
          :loading="statusUpdateLoading"
          class="confirm-btn"
        >
          Xác nhận
        </a-button>
      </div>
    </div>
  </a-modal>

  <a-modal
    v-model:open="showPaymentModal"
    title="Xác nhận thanh toán"
    :footer="null"
    :width="500"
    :maskClosable="false"
    @cancel="closePaymentModal"
  >
    <div class="payment-modal-content">
      <!-- Tiền khách đưa -->
      <div class="payment-field">
        <label class="payment-label">
          <span class="required">*</span> Tiền khách đưa
        </label>
        <div class="payment-input-container">
          <a-input-number
            v-model:value="customerPayment"
            :min="0"
            :max="99999999"
            :formatter="
              (value) => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
            "
            :parser="(value) => value.replace(/\$\s?|(,*)/g, '')"
            placeholder="0"
            size="large"
            class="payment-input"
          />
          <span class="currency-label">VND</span>
        </div>
      </div>

      <!-- Ghi chú -->
      <div class="payment-field">
        <label class="payment-label">
          <span class="required">*</span> Ghi chú
        </label>
        <a-textarea
          v-model:value="paymentNote"
          :rows="4"
          placeholder="Nhập ghi chú..."
          class="payment-textarea"
        />
      </div>

      <!-- Phương thức thanh toán -->
      <div class="payment-methods">
        <div
          class="payment-method"
          :class="{ active: selectedPaymentMethod === 'cash' }"
          @click="selectedPaymentMethod = 'cash'"
        >
          <div class="method-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path
                d="M12 2C13.1 2 14 2.9 14 4V6H18C19.1 6 20 6.9 20 8V18C20 19.1 19.1 20 18 20H6C4.9 20 4 19.1 4 18V8C4 6.9 4.9 6 6 6H10V4C10 2.9 10.9 2 12 2Z"
                fill="currentColor"
              />
            </svg>
          </div>
          <span>Tiền mặt</span>
        </div>

        <div
          class="payment-method"
          :class="{ active: selectedPaymentMethod === 'transfer' }"
          @click="selectedPaymentMethod = 'transfer'"
        >
          <div class="method-icon">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path
                d="M20 4H4C2.9 4 2 4.9 2 6V18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z"
                fill="currentColor"
              />
            </svg>
          </div>
          <span>Chuyển khoản</span>
        </div>
      </div>

      <!-- Thông tin thanh toán -->
      <div class="payment-summary">
        <div class="summary-row">
          <span>Số tiền cần thanh toán:</span>
          <span class="amount-required">{{
            formatCurrency(finalTotalAmount)
          }}</span>
        </div>
        <div class="summary-row">
          <span>Tiền thừa trả khách:</span>
          <span class="amount-change" :class="{ negative: changeAmount < 0 }">
            {{ formatCurrency(changeAmount) }}
          </span>
        </div>
      </div>

      <!-- Buttons -->
      <div class="payment-actions">
        <a-button @click="closePaymentModal" class="cancel-btn"> Hủy </a-button>
        <a-button
          type="primary"
          @click="confirmPayment"
          :loading="paymentLoading"
          :disabled="!canConfirmPayment"
          class="confirm-payment-btn"
        >
          Thanh toán
        </a-button>
      </div>
    </div>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch, nextTick } from "vue";
import { useRoute } from "vue-router";
import {
  getHoaDonChiTiets,
  updateOrderStatusInDatabase,
  GetLSTTHD,
} from "@/services/api/admin/hoadon.api";
import { message } from "ant-design-vue";
import BreadcrumbDefault from "@/components/ui/Breadcrumbs/BreadcrumbDefault.vue";
import './HoaDon.css';

const route = useRoute();
const hoaDon = ref<any>(null);
const chiTietList = ref<any[]>([]);
const tongTien = ref(0);
const showStatusModal = ref(false);
const selectedStatusTemplate = ref("delivered");
const statusNote = ref("Đơn hàng đã được giao thành công");
const statusUpdateLoading = ref(false);
const pendingStatusChange = ref("");

// Payment modal state
const showPaymentModal = ref(false);
const customerPayment = ref(0);
const paymentNote = ref("");
const selectedPaymentMethod = ref("cash");
const paymentLoading = ref(false);

const closeStatusModal = () => {
  showStatusModal.value = false;
  pendingStatusChange.value = "";
  statusNote.value = "";
  selectedStatusTemplate.value = "delivered";
};

const currentStatus = ref(0);

enum EntityTrangThaiHoaDon {
  CHO_XAC_NHAN = 0,
  DA_XAC_NHAN = 1,
  CHO_GIAO = 2,
  DANG_GIAO = 3,
  XAC_NHAN_THANH_TOAN = 4,
  HOAN_THANH = 5,
  DA_HUY = 6,
}

enum EntityLoaiHoaDon {
  OFFLINE = "OFFLINE",
  GIAO_HANG = "GIAO_HANG",
  ONLINE = "ONLINE",
}

const statusMapping: { [key: string]: number } = {
  CHO_XAC_NHAN: EntityTrangThaiHoaDon.CHO_XAC_NHAN,
  DA_XAC_NHAN: EntityTrangThaiHoaDon.DA_XAC_NHAN,
  CHO_GIAO: EntityTrangThaiHoaDon.CHO_GIAO,
  DANG_GIAO: EntityTrangThaiHoaDon.DANG_GIAO,
  XAC_NHAN_THANH_TOAN: EntityTrangThaiHoaDon.XAC_NHAN_THANH_TOAN,
  HOAN_THANH: EntityTrangThaiHoaDon.HOAN_THANH,
  DA_HUY: EntityTrangThaiHoaDon.DA_HUY,
};

const statusTemplates = {
  confirmed: "Đơn hàng của bạn đã được xác nhận",
  sap_shipped: "Đơn hàng của bạn đã sẵn sàng để vận chuyển.",
  shipped: "Đơn hàng đã được bàn giao cho đơn vị vận chuyển.",
  payment_confirmed: "Thông tin thanh toán đơn hàng đã được xác nhận.",
  delivered: "Đơn hàng đã được giao đến khách hàng thành công",
  cancelled: "Đơn hàng đã bị hủy.",
  other: "",
};

watch(selectedStatusTemplate, (newValue) => {
  if (newValue !== "other") {
    statusNote.value = statusTemplates[newValue] || "";
  } else {
    statusNote.value = "";
  }
});

const openStatusModal = (status: string) => {
  pendingStatusChange.value = status;
  setDefaultTemplate(status);
  showStatusModal.value = true;
};

const setDefaultTemplate = (status: string) => {
  switch (status) {
    case "DA_XAC_NHAN":
      selectedStatusTemplate.value = "confirmed";
      break;
    case "CHO_GIAO":
      selectedStatusTemplate.value = "sap_shipped";
      break;
    case "DANG_GIAO":
      selectedStatusTemplate.value = "shipped";
      break;
    case "XAC_NHAN_THANH_TOAN":
      selectedStatusTemplate.value = "payment_confirmed";
      break;
    case "HOAN_THANH":
      selectedStatusTemplate.value = "delivered";
      break;
    case "DA_HUY":
      selectedStatusTemplate.value = "cancelled";
      break;
    default:
      selectedStatusTemplate.value = "other";
  }

  statusNote.value = statusTemplates[selectedStatusTemplate.value] || "";
};

const confirmStatusChange = async () => {
  if (!pendingStatusChange.value) return;

  statusUpdateLoading.value = true;

  try {
    const response = await updateOrderStatusInDatabase({
      maHoaDon: hoaDon.value.maHoaDon,
      status: pendingStatusChange.value,
      note: statusNote.value,
    });

    if (response.success || response.data) {
      // Cập nhật trạng thái ngay lập tức
      const newStatusValue = statusMapping[pendingStatusChange.value];

      // Cập nhật currentStatus trước
      currentStatus.value = newStatusValue;

      // Cập nhật hoaDon object với cách force reactivity
      hoaDon.value = {
        ...hoaDon.value,
        trangThaiHoaDon: pendingStatusChange.value,
      };

      message.success("Cập nhật trạng thái thành công");

      // Refresh timeline data
      const idHoaDon = route.params.id as string;
      try {
        const statusResponse = await GetLSTTHD(idHoaDon);
        if (statusResponse && statusResponse.success && statusResponse.data) {
          timelineStatusData.value = [...statusResponse.data]; // Force reactivity
        }
      } catch (refreshError) {
        console.warn("Lỗi khi refresh dữ liệu timeline:", refreshError);
      }

      closeStatusModal();
    } else {
      message.error("Cập nhật trạng thái thất bại");
    }
  } catch (error) {
    message.error(
      "Có lỗi xảy ra khi cập nhật trạng thái: " + (error as Error).message
    );
  } finally {
    statusUpdateLoading.value = false;
  }
};

const canCancelOrder1 = computed(() => {
  return (
    currentStatus.value === EntityTrangThaiHoaDon.CHO_XAC_NHAN ||
    currentStatus.value === EntityTrangThaiHoaDon.CHO_GIAO
  );
});

// Computed properties for payment
const changeAmount = computed(() => {
  return (customerPayment.value || 0) - finalTotalAmount.value;
});

const canConfirmPaymentButton = computed(() => {
  return customerPayment.value > 0 && paymentNote.value.trim() !== "";
});

// Payment modal methods
const openPaymentModal = () => {
  // Set default values
  customerPayment.value = finalTotalAmount.value;
  paymentNote.value = "Khách hàng thanh toán đơn hàng";
  selectedPaymentMethod.value = "cash";
  showPaymentModal.value = true;
};

const closePaymentModal = () => {
  showPaymentModal.value = false;
  customerPayment.value = 0;
  paymentNote.value = "";
  selectedPaymentMethod.value = "cash";
};

const confirmPayment = async () => {
  if (!canConfirmPaymentButton.value) {
    message.warning("Vui lòng nhập đầy đủ thông tin thanh toán");
    return;
  }

  if (changeAmount.value < 0) {
    message.warning("Số tiền khách đưa không đủ");
    return;
  }

  paymentLoading.value = true;

  try {
    // Gọi API xác nhận thanh toán
    const paymentData = {
      maHoaDon: hoaDon.value.maHoaDon,
      soTienKhachDua: customerPayment.value,
      soTienTraLai: Math.max(0, changeAmount.value),
      ghiChu: paymentNote.value,
      phuongThucThanhToan: selectedPaymentMethod.value,
      trangThai: "XAC_NHAN_THANH_TOAN",
    };

    // Thay thế bằng API thực tế
    const response = await confirmPaymentAPI(paymentData);

    if (response.success) {
      message.success("Xác nhận thanh toán thành công");

      // Cập nhật trạng thái đơn hàng
      currentStatus.value = EntityTrangThaiHoaDon.XAC_NHAN_THANH_TOAN;
      hoaDon.value.trangThaiHoaDon = "XAC_NHAN_THANH_TOAN";

      // Refresh timeline data
      const idHoaDon = route.params.id as string;
      try {
        const statusResponse = await GetLSTTHD(idHoaDon);
        if (statusResponse && statusResponse.success && statusResponse.data) {
          timelineStatusData.value = [...statusResponse.data];
        }
      } catch (refreshError) {
        console.warn("Lỗi khi refresh dữ liệu timeline:", refreshError);
      }

      closePaymentModal();
    } else {
      message.error("Xác nhận thanh toán thất bại");
    }
  } catch (error) {
    console.error("Lỗi xác nhận thanh toán:", error);
    message.error("Có lỗi xảy ra khi xác nhận thanh toán");
  } finally {
    paymentLoading.value = false;
  }
};

// Mock API function - thay thế bằng API thực tế
const confirmPaymentAPI = async (paymentData: any) => {
  // Simulate API call
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({ success: true, data: paymentData });
    }, 1000);
  });
};

// Update the existing canConfirmPayment computed to use the new name
const canConfirmPayment1 = computed(() => {
  return canConfirmPaymentButton.value;
});

const canAddProduct = computed(() => {
  return (
    currentStatus.value === EntityTrangThaiHoaDon.CHO_XAC_NHAN ||
    currentStatus.value === EntityTrangThaiHoaDon.CHO_GIAO
  );
});

const isAddProductDisabled = computed(() => {
  return !canAddProduct.value;
});

const canConfirmPayment = computed(() => {
  return currentStatus.value < EntityTrangThaiHoaDon.XAC_NHAN_THANH_TOAN;
});

const offlineTimelineSteps = [
  {
    key: "CHO_XAC_NHAN",
    title: "Chờ xác nhận",
    status: EntityTrangThaiHoaDon.CHO_XAC_NHAN,
    icon: "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z",
  },
  {
    key: "HOAN_THANH",
    title: "Hoàn thành",
    status: EntityTrangThaiHoaDon.HOAN_THANH,
    icon: "M9 12L11 14L15 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z",
  },
  {
    key: "DA_HUY",
    title: "Đã hủy",
    status: EntityTrangThaiHoaDon.DA_HUY,
    icon: "M18 6L6 18M6 6L18 18",
  },
];

const deliveryTimelineSteps = [
  {
    key: "CHO_XAC_NHAN",
    title: "Chờ xác nhận",
    status: EntityTrangThaiHoaDon.CHO_XAC_NHAN,
    icon: "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z",
  },
  {
    key: "DA_XAC_NHAN",
    title: "Đã xác nhận",
    status: EntityTrangThaiHoaDon.DA_XAC_NHAN,
    icon: "M9 12L11 14L15 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z",
  },
  {
    key: "CHO_GIAO",
    title: "Chờ giao",
    status: EntityTrangThaiHoaDon.CHO_GIAO,
    icon: "M1 3H3L3.4 5M7 13H17L21 5H5.4M7 13L5.4 5M7 13L4.7 15.3C4.3 15.7 4.6 16.5 5.1 16.5H17M17 13V17C17 18.1 17.9 19 19 19S21 18.1 21 17V13M17 13H21M9 19.5C9.8 19.5 10.5 20.2 10.5 21S9.8 22.5 9 22.5 7.5 21.8 7.5 21 8.2 19.5 9 19.5ZM20 19.5C20.8 19.5 21.5 20.2 21.5 21S20.8 22.5 20 22.5 18.5 21.8 18.5 21 19.2 19.5 20 19.5Z",
  },
  {
    key: "DANG_GIAO",
    title: "Đang giao",
    status: EntityTrangThaiHoaDon.DANG_GIAO,
    icon: "M13 2L3 14h9l-1 8 10-12h-9l1-8z",
  },
  {
    key: "XAC_NHAN_THANH_TOAN",
    title: "Xác nhận thanh toán",
    status: EntityTrangThaiHoaDon.XAC_NHAN_THANH_TOAN,
    icon: "M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 1H5C3.89 1 3 1.89 3 3V19C3 20.1 3.9 21 5 21H11V19H5V3H13V9H21Z",
  },
  {
    key: "HOAN_THANH",
    title: "Hoàn thành",
    status: EntityTrangThaiHoaDon.HOAN_THANH,
    icon: "M9 12L11 14L15 10M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z",
  },
  {
    key: "DA_HUY",
    title: "Đã hủy",
    status: EntityTrangThaiHoaDon.DA_HUY,
    icon: "M18 6L6 18M6 6L18 18",
  },
];

const timelineSteps = computed(() => {
  const orderType = hoaDon.value?.loaiHoaDon;
  if (orderType === EntityLoaiHoaDon.OFFLINE) {
    return offlineTimelineSteps;
  }
  return deliveryTimelineSteps;
});

const paymentColumns = [
  { title: "#", key: "stt", width: 60, align: "center" },
  { title: "Số tiền", key: "soTien", width: 120, align: "center" },
  { title: "Thời gian", key: "thoiGian", width: 150, align: "center" },
  { title: "Mã giao dịch", key: "maGiaoDich", width: 150, align: "center" },
  { title: "Loại giao dịch", key: "loaiGiaoDich", width: 150, align: "center" },
  {
    title: "Nhân viên xác nhận",
    key: "nhanVienXacNhan",
    width: 150,
    align: "center",
  },
  { title: "Ghi chú", key: "ghiChu", width: 200, align: "left" },
];

const lichSuThanhToan = ref([
  // Dữ liệu mẫu - sẽ được thay thế bằng dữ liệu thực từ API
]);

const getCurrentStepIndex = computed(() => {
  const current = currentStatus.value;
  console.log("Computing step index for status:", current);

  if (current === EntityTrangThaiHoaDon.DA_HUY) {
    const cancelIndex = timelineSteps.value.findIndex(
      (step) => step.key === "DA_HUY"
    );
    console.log("Cancel step index:", cancelIndex);
    return cancelIndex;
  }

  const stepIndex = timelineSteps.value.findIndex(
    (step) => step.status === current
  );
  console.log("Step index for status", current, ":", stepIndex);
  return stepIndex;
});

const forceUpdateUI = () => {
  hoaDon.value = { ...hoaDon.value };
  timelineStatusData.value = [...timelineStatusData.value];
};

const getStepStatus = (stepIndex: number) => {
  const currentStepIndex = getCurrentStepIndex.value;
  const currentStatusValue = currentStatus.value;

  if (currentStatusValue === EntityTrangThaiHoaDon.DA_HUY) {
    if (timelineSteps.value[stepIndex].key === "DA_HUY") return "current";
    return "pending";
  }

  if (stepIndex < currentStepIndex) return "completed";
  if (stepIndex === currentStepIndex) return "current";
  return "pending";
};

const getIconClass = (stepIndex: number) => {
  return getStepStatus(stepIndex);
};

const getLineClass = (stepIndex: number) => {
  const currentStepIndex = getCurrentStepIndex.value;

  if (currentStatus.value === EntityTrangThaiHoaDon.DA_HUY) {
    return "pending";
  }

  if (stepIndex < currentStepIndex) return "completed";
  return "pending";
};

const isLastActiveStep = (stepIndex: number) => {
  if (currentStatus.value === EntityTrangThaiHoaDon.DA_HUY) {
    return timelineSteps.value[stepIndex].key === "DA_HUY";
  }
  return stepIndex === timelineSteps.value.length - 1;
};

const getStatusText = (status: string | number) => {
  const statusTexts = [
    "Chờ xác nhận",
    "Đã xác nhận",
    "Chờ giao",
    "Đang giao",
    "Xác nhận thanh toán",
    "Hoàn thành",
    "Đã hủy",
  ];
  const s = parseInt(status as string);
  return statusTexts[s] || "Không rõ";
};

const getTimelineData = (stepIndex: number) => {
  const step = timelineSteps.value[stepIndex];
  if (!step) return { title: "", note: "", time: "--:-- --/--/----" };

  const statusData = timelineStatusData.value.find((item) => {
    const itemStatus = parseInt(item.trangThai);
    return itemStatus === step.status;
  });

  return {
    title: step.title,
    note: statusData ? statusData.note : "",
    time: statusData ? formatDateTime(statusData.thoiGian) : "--:-- --/--/----",
  };
};

const timelineStatusData = ref<any[]>([]);

// Button visibility and actions
const canConfirmOrder = computed(() => {
  if (hoaDon.value?.loaiHoaDon === EntityLoaiHoaDon.OFFLINE) {
    return currentStatus.value === EntityTrangThaiHoaDon.CHO_XAC_NHAN;
  }
  return (
    currentStatus.value === EntityTrangThaiHoaDon.CHO_XAC_NHAN ||
    currentStatus.value === EntityTrangThaiHoaDon.DA_XAC_NHAN ||
    currentStatus.value === EntityTrangThaiHoaDon.CHO_GIAO ||
    currentStatus.value === EntityTrangThaiHoaDon.DANG_GIAO
  );
});

const canCompleteOrder = computed(() => {
  if (hoaDon.value?.loaiHoaDon === EntityLoaiHoaDon.OFFLINE) {
    return false;
  }
  return currentStatus.value === EntityTrangThaiHoaDon.XAC_NHAN_THANH_TOAN;
});

const getNextStatus = () => {
  if (hoaDon.value?.loaiHoaDon === EntityLoaiHoaDon.OFFLINE) {
    return "HOAN_THANH";
  }

  switch (currentStatus.value) {
    case EntityTrangThaiHoaDon.CHO_XAC_NHAN:
      return "DA_XAC_NHAN";
    case EntityTrangThaiHoaDon.DA_XAC_NHAN:
      return "CHO_GIAO";
    case EntityTrangThaiHoaDon.CHO_GIAO:
      return "DANG_GIAO";
    case EntityTrangThaiHoaDon.DANG_GIAO:
      return "XAC_NHAN_THANH_TOAN";
    default:
      return "HOAN_THANH";
  }
};

const getConfirmButtonText = () => {
  if (hoaDon.value?.loaiHoaDon === EntityLoaiHoaDon.OFFLINE) {
    return "Xác nhận và hoàn thành";
  }

  switch (currentStatus.value) {
    case EntityTrangThaiHoaDon.CHO_XAC_NHAN:
      return "Xác nhận đơn hàng";
    case EntityTrangThaiHoaDon.DA_XAC_NHAN:
      return "Chuẩn bị giao hàng";
    case EntityTrangThaiHoaDon.CHO_GIAO:
      return "Bắt đầu giao hàng";
    case EntityTrangThaiHoaDon.DANG_GIAO:
      return "Xác nhận thanh toán";
    default:
      return "Tiếp tục";
  }
};

const formatDateTime = (dateString: string) => {
  if (!dateString) return "--:-- --/--/----";
  try {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
      console.error("Invalid date string:", dateString);
      return "--:-- --/--/----";
    }

    return date.toLocaleString("vi-VN", {
      hour: "2-digit",
      minute: "2-digit",
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  } catch (error) {
    console.error("Error formatting date:", dateString, error);
    return "--:-- --/--/----";
  }
};

const subtotalAmount = computed(() => {
  return chiTietList.value.reduce((sum, item) => {
    return sum + item.soLuong * item.giaBan;
  }, 0);
});

const finalTotalAmount = computed(() => {
  const subtotal = subtotalAmount.value;
  const shipping = hoaDon.value?.phiVanChuyen || 0;
  return subtotal + shipping;
});

watch([subtotalAmount, finalTotalAmount], ([newSubtotal, newFinalTotal]) => {
  if (hoaDon.value) {
    hoaDon.value.tongTienSauGiam = newFinalTotal;
  }
});

const displayStatus = computed(() => {
  return getStatusText(currentStatus.value);
});

const debugTimelineData = () => {
  console.log("=== DEBUG TIMELINE DATA ===");
  console.log("Current status:", currentStatus.value);
  console.log("Timeline steps:", timelineSteps.value);
  console.log("Timeline status data from API:", timelineStatusData.value);

  timelineSteps.value.forEach((step, index) => {
    const data = getTimelineData(index);
    console.log(`Step ${index} (${step.title}):`, data);
  });
  console.log("=== END DEBUG ===");
};

watch(
  [timelineStatusData, currentStatus],
  () => {
    console.log("Timeline data or status changed");
    debugTimelineData();
  },
  { deep: true }
);

const formatCurrency = (value: number | undefined | null) => {
  if (value === undefined || value === null) return "0 ₫";
  return value.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

onMounted(async () => {
  const maHoaDon = route.params.maHoaDon as string;
  const idHoaDon = route.params.id as string;

  console.log("Route params:", { maHoaDon, idHoaDon });

  try {
    if (!idHoaDon) {
      console.error("idHoaDon is missing from route params");
      message.error("Không tìm thấy ID hóa đơn");
      return;
    }

    // Gọi API GetLSTTHD
    console.log("Calling GetLSTTHD with idHoaDon:", idHoaDon);
    const statusResponse = await GetLSTTHD(idHoaDon);
    console.log("GetLSTTHD response:", statusResponse);

    if (statusResponse && statusResponse.success && statusResponse.data) {
      timelineStatusData.value = statusResponse.data;
      console.log("Timeline status data loaded:", timelineStatusData.value);
    } else {
      console.warn("GetLSTTHD returned invalid data:", statusResponse);
      timelineStatusData.value = [];
    }

    // Gọi API lấy chi tiết hóa đơn
    if (!maHoaDon) {
      console.error("maHoaDon is missing from route params");
      message.error("Không tìm thấy mã hóa đơn");
      return;
    }

    console.log("Calling getHoaDonChiTiets with maHoaDon:", maHoaDon);
    const res = await getHoaDonChiTiets(maHoaDon);
    console.log("getHoaDonChiTiets response:", res);

    if (res && res.data && res.data.length > 0) {
      const hoaDonData = res.data[0];

      hoaDon.value = {
        maHoaDon: hoaDonData.maHoaDon,
        tenHoaDon: hoaDonData.tenHoaDon || "Không rõ",
        tenKhachHang: hoaDonData.tenKhachHang || "Không rõ",
        sdtKH: hoaDonData.sdtKH || "Không rõ",
        email: hoaDonData.email,
        diaChi: hoaDonData.diaChi,
        loaiHoaDon:
          hoaDonData.loaiHoaDon == "0"
            ? EntityLoaiHoaDon.OFFLINE
            : hoaDonData.loaiHoaDon == "1"
            ? EntityLoaiHoaDon.GIAO_HANG
            : EntityLoaiHoaDon.ONLINE,
        trangThaiHoaDon: hoaDonData.trangThaiHoaDon,
        ngayTao: hoaDonData.ngayTao,
        phiVanChuyen: hoaDonData.phiVanChuyen,
        maPGG: hoaDonData.maVoucher,
        tongTienSauGiam: hoaDonData.tongTienSauGiam,
      };

      // Đảm bảo currentStatus được set đúng và trigger reactivity
      const statusValue = parseInt(hoaDonData.trangThaiHoaDon);
      currentStatus.value = statusValue;

      console.log("Current status set to:", currentStatus.value);
      console.log("Display status:", getStatusText(currentStatus.value));

      chiTietList.value = res.data;
      tongTien.value = chiTietList.value.reduce(
        (sum: number, item: any) => sum + item.thanhTien,
        0
      );

      console.log("Data loaded successfully");

      // Force một lần update để đảm bảo UI sync
      await nextTick();
      forceUpdateUI();
    } else {
      console.error("getHoaDonChiTiets returned empty or invalid data:", res);
      message.error("Không thể tải dữ liệu hóa đơn");
    }
  } catch (error) {
    console.error("Lỗi khi tải dữ liệu:", error);
    message.error("Có lỗi xảy ra khi tải dữ liệu: " + (error as Error).message);
  }
});
</script>
