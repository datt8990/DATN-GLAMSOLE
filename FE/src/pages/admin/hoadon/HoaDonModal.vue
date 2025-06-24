<template>
  <div class="container mx-auto p-6 space-y-6">
    <a-page-header title="Chi tiết hóa đơn" @back="$router.back()" />

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
          v-if="canConfirmOrder"
          type="primary"
          @click="openStatusModal(getNextStatus())"
        >
          {{ getConfirmButtonText() }}
        </a-button>
        <a-button
          v-if="canCompleteOrder"
          type="primary"
          @click="openStatusModal(getNextStatus())"
        >
          Hoàn thành đơn hàng
        </a-button>
        <a-button
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
              <span class="value status">{{
                getStatusText(hoaDon?.trangThaiHoaDon)
              }}</span>
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
            <a-button
              v-if="canChangeCustomerInfo"
              type="primary"
              class="change-info-btn"
            >
              Thay đổi thông tin
            </a-button>
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
          v-if="canConfirmPayment"
          type="primary"
          class="bg-yellow-500 hover:bg-yellow-600 border-yellow-500"
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
        <a-button
          v-if="canAddProduct"
          type="primary"
          :disabled="isAddProductDisabled"
          class="bg-yellow-500 hover:bg-yellow-600 border-yellow-500"
          :class="{ 'opacity-50 cursor-not-allowed': isAddProductDisabled }"
        >
          Thêm sản phẩm
        </a-button>
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
              {{ record.thuongHieu }} - {{ record.xuatSu }}
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
  
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import { useRoute } from "vue-router";
import {
  getHoaDonChiTiets,
  updateOrderStatusInDatabase,
  GetLSTTHD,
} from "@/services/api/admin/hoadon.api";
import { message } from "ant-design-vue";

const route = useRoute();
const hoaDon = ref<any>(null);
const chiTietList = ref<any[]>([]);
const tongTien = ref(0);
const showStatusModal = ref(false);
const selectedStatusTemplate = ref("delivered");
const statusNote = ref("Đơn hàng đã được giao thành công");
const statusUpdateLoading = ref(false);
const pendingStatusChange = ref("");

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
  if (!pendingStatusChange.value) return; // Ensure there's a pending status

  statusUpdateLoading.value = true; // Show loading state

  try {
    const response = await updateOrderStatusInDatabase({
      maHoaDon: hoaDon.value.maHoaDon,
      status: pendingStatusChange.value,
      note: statusNote.value, // Pass the note
    });

    if (response.success || response.data) {
      currentStatus.value = statusMapping[pendingStatusChange.value];
      hoaDon.value.trangThaiHoaDon = pendingStatusChange.value;
      message.success("Cập nhật trạng thái thành công");

      // Call the API to get the updated invoice data
      const idHoaDon = route.params.id as string;
      const maHoaDon = route.params.maHoaDon as string;
      const statusResponse = await GetLSTTHD(idHoaDon);
      await getHoaDonChiTiets(maHoaDon);
      if (statusResponse && statusResponse.success && statusResponse.data) {
        timelineStatusData.value = statusResponse.data;
      }

      closeStatusModal(); // Close the modal
    } else {
      message.error("Cập nhật trạng thái thất bại");
    }
  } catch (error) {
    message.error(
      "Có lỗi xảy ra khi cập nhật trạng thái: " + (error as Error).message
    );
  } finally {
    statusUpdateLoading.value = false; // Hide loading state
  }
};

const canCancelOrder1 = computed(() => {
  return (
    currentStatus.value === EntityTrangThaiHoaDon.CHO_XAC_NHAN ||
    currentStatus.value === EntityTrangThaiHoaDon.CHO_GIAO
  );
});

const canChangeCustomerInfo = computed(() => {
  return (
    currentStatus.value === EntityTrangThaiHoaDon.CHO_XAC_NHAN ||
    currentStatus.value === EntityTrangThaiHoaDon.CHO_GIAO
  );
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
  if (hoaDon.value?.loaiHoaDon === EntityLoaiHoaDon.OFFLINE) {
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

const getCurrentStepIndex = () => {
  if (currentStatus.value === EntityTrangThaiHoaDon.DA_HUY) {
    return timelineSteps.value.findIndex((step) => step.key === "DA_HUY");
  }

  return timelineSteps.value.findIndex(
    (step) => step.status === currentStatus.value
  );
};

const getStepStatus = (stepIndex: number) => {
  const currentStepIndex = getCurrentStepIndex();

  if (currentStatus.value === EntityTrangThaiHoaDon.DA_HUY) {
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
  const currentStepIndex = getCurrentStepIndex();

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
    return sum + (item.soLuong * item.giaBan)
  }, 0)
})

const finalTotalAmount = computed(() => {
  const subtotal = subtotalAmount.value
  const shipping = hoaDon.value?.phiVanChuyen || 0
  return subtotal + shipping
})

watch([subtotalAmount, finalTotalAmount], ([newSubtotal, newFinalTotal]) => {
  if (hoaDon.value) {
    hoaDon.value.tongTienSauGiam = newFinalTotal
  }
})

// const updateOrderStatus = async (newStatus: string) => {
//   try {
//     console.log("Updating order status to:", newStatus);

//     pendingStatusChange.value = newStatus;

//     setDefaultTemplate(newStatus);

//     showStatusModal.value = true;

//     const response = await updateOrderStatusInDatabase({
//       maHoaDon: hoaDon.value.maHoaDon,
//       status: newStatus,
//     });

//     console.log("Update status response:", response);

//     if (response.success || response.data) {
//       currentStatus.value = statusMapping[newStatus];
//       hoaDon.value.trangThaiHoaDon = newStatus;

//       // Refresh timeline data sau khi cập nhật
//       const idHoaDon = route.params.id as string;
//       console.log("Refreshing timeline data for idHoaDon:", idHoaDon);

//       try {
//         const statusResponse = await GetLSTTHD(idHoaDon);
//         console.log("Refreshed timeline data:", statusResponse);

//         // Sử dụng cùng logic xử lý như trong onMounted
//         if (statusResponse && statusResponse.success && statusResponse.data) {
//           timelineStatusData.value = statusResponse.data;
//           console.log("Timeline data refreshed successfully");
//         }
//       } catch (timelineError) {
//         console.error("Error refreshing timeline data:", timelineError);
//       }

//       message.success("Cập nhật trạng thái thành công");
//     } else {
//       console.error("Update failed:", response);
//       message.error("Cập nhật trạng thái thất bại");
//     }
//   } catch (error) {
//     console.error("Có lỗi xảy ra khi cập nhật trạng thái:", error);
//     message.error(
//       "Có lỗi xảy ra khi cập nhật trạng thái: " + (error as Error).message
//     );
//   }
// };

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

    // Gọi API GetLSTTHD - đã hoạt động OK
    console.log("Calling GetLSTTHD with idHoaDon:", idHoaDon);

    const statusResponse = await GetLSTTHD(idHoaDon);
    console.log("GetLSTTHD response:", statusResponse);

    // Sửa lại cách xử lý response - response có structure khác
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

      currentStatus.value = parseInt(hoaDonData.trangThaiHoaDon) || 0;
      console.log("Current status set to:", currentStatus.value);

      chiTietList.value = res.data;
      tongTien.value = chiTietList.value.reduce(
        (sum: number, item: any) => sum + item.thanhTien,
        0
      );

      console.log("Data loaded successfully");
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

.custom-table :deep(img) {
  max-width: 80px !important;
  max-height: 80px !important;
  width: 80px !important;
  height: 80px !important;
  object-fit: cover !important;
}

/* Timeline Horizontal Styles */
.order-timeline-horizontal {
  padding: 20px 0;
  overflow-x: auto;
}

.timeline-container-horizontal {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  min-width: 800px;
  padding: 20px 0;
  position: relative;
}

.timeline-step-horizontal {
  flex: 1;
  text-align: center;
  position: relative;
}

.timeline-step-horizontal:first-child {
  margin-left: 0;
}

.timeline-step-horizontal:last-child {
  margin-right: 0;
}

.step-content-horizontal {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  z-index: 2;
}

.step-icon-horizontal {
  margin: 0 auto 8px;
}

.timeline-step-horizontal {
  flex: 1;
  text-align: center;
  position: relative;
}

.icon-circle-horizontal {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #f0f0f0;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.icon-circle-horizontal.completed {
  background-color: #52c41a;
  color: white;
}

.icon-circle-horizontal.current {
  background-color: #1890ff;
  color: white;
  animation: pulse 2s infinite;
}

.icon-circle-horizontal.pending {
  background-color: #d9d9d9;
  color: #666;
}

.step-title-horizontal {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.step-note-horizontal {
  font-size: 12px;
  color: #666;
  padding: 0 8px;
  max-width: 140px;
  margin: 0 auto 4px;
  word-break: break-word;
}

.step-time-horizontal {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

.step-line-horizontal {
  position: absolute;
  top: 25px;
  left: calc(50% + 25px);
  right: calc(-50% + 25px);
  height: 2px;
  z-index: 1;
}

.timeline-step-horizontal:last-child .step-line-horizontal {
  display: none;
}

.step-line-horizontal.completed {
  background-color: #52c41a;
}

.step-line-horizontal.pending {
  background-color: #d9d9d9;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(24, 144, 255, 0.4);
  }
  70% {
    box-shadow: 0 0 0 10px rgba(24, 144, 255, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(24, 144, 255, 0);
  }
}

/* Responsive */
@media (max-width: 768px) {
  .order-timeline-horizontal {
    padding: 15px 0;
  }

  .timeline-container-horizontal {
    min-width: 600px;
  }

  .icon-circle-horizontal {
    width: 40px;
    height: 40px;
  }

  .step-line-horizontal {
    top: 20px;
    left: calc(50% + 20px);
    right: calc(-50% + 20px);
  }

  .step-title-horizontal {
    font-size: 13px;
  }

  .step-time-horizontal {
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .timeline-container-horizontal {
    min-width: 500px;
  }

  .timeline-step-horizontal {
    margin: 0 5px;
  }

  .icon-circle-horizontal {
    width: 35px;
    height: 35px;
  }

  .step-line-horizontal {
    top: 17px;
    left: calc(50% + 17px);
    right: calc(-50% + 17px);
  }

  .step-title-horizontal {
    font-size: 12px;
  }

  .step-time-horizontal {
    font-size: 10px;
  }
}
.order-info-card {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-info-container {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}

.order-info-column {
  width: 48%;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.label {
  color: #4b5563; /* text-gray-600 */
  font-size: 14px;
}

.value {
  font-size: 14px;
  font-weight: 600;
}

.value.status,
.value.code {
  color: #ef4444; /* red-500 */
}

.value.price {
  color: #ef4444; /* red-500 */
}

.value.total {
  color: #dc2626; /* red-600 */
  font-size: 16px;
  font-weight: bold;
}

.value.email,
.value.address {
  color: #ef4444; /* red-500 */
}

.change-info-btn {
  background-color: #f59e0b; /* yellow-500 */
  border-color: #f59e0b;
  color: #fff;
}

.change-info-btn:hover {
  background-color: #d97706; /* yellow-600 */
  border-color: #d97706;
}

.product-list-container {
  background: #fff;
}

.product-row {
  display: grid;
  grid-template-columns: 60px 120px 1fr 120px 150px;
  gap: 20px;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.product-row:last-child {
  border-bottom: none;
}

.product-stt {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  text-align: center;
}

.product-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.product-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.product-details {
  flex: 1;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 4px 0;
  line-height: 1.3;
}

.product-code {
  font-size: 12px;
  color: #999;
  margin: 0 0 8px 0;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.product-price {
  font-size: 14px;
  color: #e74c3c;
  font-weight: 500;
}

.product-quantity {
  display: flex;
  justify-content: center;
}

.quantity-input {
  width: 80px;
}

.quantity-input :deep(.ant-input-number-input) {
  text-align: center;
}

.product-total {
  font-size: 16px;
  font-weight: 700;
  color: #e74c3c;
  text-align: right;
}

.product-pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 0 10px 0;
  margin-top: 10px;
}

.pagination-info {
  font-size: 14px;
  color: #666;
}

.pagination-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination-btn {
  border: 1px solid #d9d9d9;
  background: #fff;
  color: #666;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

.pagination-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.pagination-current {
  border: 1px solid #1890ff;
  background: #1890ff;
  color: #fff;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0;
}

/* Responsive design */
@media (max-width: 768px) {
  .product-row {
    grid-template-columns: 40px 80px 1fr 80px 120px;
    gap: 10px;
    padding: 12px 0;
  }

  .product-img {
    width: 60px;
    height: 60px;
  }

  .product-name {
    font-size: 14px;
  }

  .quantity-input {
    width: 60px;
  }

  .product-total {
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  .product-row {
    grid-template-columns: 1fr;
    gap: 8px;
    padding: 16px;
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    margin-bottom: 12px;
  }

  .product-row:last-child {
    margin-bottom: 0;
  }

  .product-stt {
    position: absolute;
    top: 16px;
    left: 16px;
    background: #f0f0f0;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
  }

  .product-image {
    justify-content: flex-start;
    margin-left: 40px;
  }

  .product-details {
    margin-left: 40px;
  }

  .product-quantity,
  .product-total {
    margin-left: 40px;
    justify-content: flex-start;
    text-align: left;
  }
}
.status-modal-content {
  padding: 8px 0;
}

.status-selection {
  margin-bottom: 20px;
}

.selection-label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 12px;
}

.status-radio-group {
  width: 100%;
}

.radio-option {
  margin-bottom: 8px;
  padding: 4px 0;
}

.radio-option :deep(.ant-radio-wrapper) {
  font-size: 14px;
  color: #666;
  width: 100%;
  display: flex;
  align-items: center;
}

.radio-option :deep(.ant-radio-wrapper:hover) {
  color: #1890ff;
}

.radio-option :deep(.ant-radio-checked .ant-radio-inner) {
  border-color: #1890ff;
  background-color: #1890ff;
}

.note-section {
  margin-bottom: 24px;
}

.status-textarea {
  resize: none;
}

.status-textarea :deep(.ant-input) {
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  font-size: 14px;
  line-height: 1.5;
}

.status-textarea :deep(.ant-input:focus) {
  border-color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 8px;
}

.cancel-btn {
  min-width: 80px;
  height: 36px;
  border-radius: 6px;
  border: 1px solid #d9d9d9;
  background: #fff;
  color: #666;
}

.cancel-btn:hover {
  border-color: #1890ff;
  color: #1890ff;
}

.confirm-btn {
  min-width: 80px;
  height: 36px;
  border-radius: 6px;
  background-color: #1890ff;
  border-color: #1890ff;
}

.confirm-btn:hover {
  background-color: #40a9ff;
  border-color: #40a9ff;
}

/* Modal customization */
:deep(.ant-modal-header) {
  border-bottom: 1px solid #f0f0f0;
  padding: 16px 24px;
}

:deep(.ant-modal-title) {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

:deep(.ant-modal-body) {
  padding: 20px 24px;
}

:deep(.ant-modal-close-x) {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 46px;
  height: 46px;
}
</style>