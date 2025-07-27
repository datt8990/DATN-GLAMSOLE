```vue
<template>
  <div class="customer-profile">
    <div class="container">
      <div class="header">
        <h1>Thông Tin Cá Nhân</h1>
        <p>Quản lý thông tin tài khoản và đơn hàng của bạn</p>
      </div>

      <div class="nav-tabs">
        <button
          class="nav-tab"
          :class="{ active: activeTab === 'profile' }"
          @click="activeTab = 'profile'"
        >
          Thông Tin Cá Nhân
        </button>
        <button
          class="nav-tab"
          :class="{ active: activeTab === 'orders' }"
          @click="activeTab = 'orders'"
        >
          Lịch Sử Đơn Hàng
        </button>
        <button
          class="nav-tab"
          :class="{ active: activeTab === 'password' }"
          @click="activeTab = 'password'"
        >
          Đổi Mật Khẩu
        </button>
      </div>

      <!-- Tab Thông Tin Cá Nhân -->
      <div class="tab-content" v-if="activeTab === 'profile'">
        <div class="profile-section">
          <div class="avatar-section">
            <img
              :src="
                product.avatar ||
                'https://via.placeholder.com/200x200/667eea/ffffff?text=Avatar'
              "
              alt="Avatar"
              class="avatar"
            />
          </div>

          <div class="form-container">
            <a-form
              ref="productForm"
              :model="product"
              :rules="rules"
              layout="vertical"
              @finish="handleSubmit"
            >
              <div class="form-grid">
                <div class="form-group">
                  <a-form-item label="Avatar" name="avatar">
                    <input
                      type="file"
                      accept="image/*"
                      class="form-control"
                      ref="avatarInput"
                      @change="handleAvatarChange"
                      style="display: none"
                    />
                    <button
                      type="button"
                      class="upload-btn"
                      @click="$refs.avatarInput.click()"
                    >
                      Thay Đổi Avatar
                    </button>
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Mã Khách Hàng" name="ma">
                    <input
                      type="text"
                      class="form-control"
                      v-model="product.ma"
                      readonly
                      placeholder="Mã sẽ được tự động tạo"
                    />
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Họ và Tên" name="ten">
                    <input
                      type="text"
                      class="form-control"
                      v-model="product.ten"
                      placeholder="Nhập họ và tên"
                    />
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Email" name="email">
                    <input
                      type="email"
                      class="form-control"
                      v-model="product.email"
                      placeholder="Nhập địa chỉ email"
                    />
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Số Điện Thoại" name="sdt">
                    <input
                      type="tel"
                      class="form-control"
                      v-model="product.sdt"
                      placeholder="Nhập số điện thoại (10 chữ số)"
                    />
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="CCCD/CMND" name="cccd">
                    <input
                      type="text"
                      class="form-control"
                      v-model="product.cccd"
                      placeholder="Nhập số CCCD/CMND (9-12 chữ số)"
                    />
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Ngày Sinh" name="ngaySinh">
                    <input
                      type="date"
                      class="form-control"
                      v-model="product.ngaySinh"
                      :max="new Date().toISOString().split('T')[0]"
                    />
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Giới Tính" name="gioiTinh">
                    <select class="form-control" v-model="product.gioiTinh">
                      <option value="" disabled>Chọn giới tính</option>
                      <option :value="false">Nam</option>
                      <option :value="true">Nữ</option>
                    </select>
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Tỉnh/thành phố" name="tinh">
                    <a-select
                      v-model:value="product.tinh"
                      placeholder="Chọn tỉnh/thành phố"
                      @change="handleProvinceChange"
                      :options="tinhThanhOptions"
                      label-in-value
                      :key="tinhThanhOptions.length"
                      class="form-control-select"
                      show-search
                      :filter-option="
                        (input, option) =>
                          option.label.toLowerCase().includes(input.toLowerCase())
                      "
                    >
                      <template v-if="tinhThanhOptions.length === 0">
                        <a-select-option value="" disabled>
                          Đang tải dữ liệu...
                        </a-select-option>
                      </template>
                    </a-select>
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Quận/huyện" name="huyen">
                    <a-select
                      v-model:value="product.huyen"
                      placeholder="Chọn quận/huyện"
                      @change="handleDistrictChange"
                      :options="quanHuyenOptions"
                      label-in-value
                      :key="quanHuyenOptions.length"
                      class="form-control-select"
                      :disabled="!product.tinh"
                      show-search
                      :filter-option="
                        (input, option) =>
                          option.label.toLowerCase().includes(input.toLowerCase())
                      "
                    >
                      <template v-if="quanHuyenOptions.length === 0">
                        <a-select-option value="" disabled>
                          {{
                            product.tinh
                              ? 'Đang tải dữ liệu...'
                              : 'Vui lòng chọn tỉnh/thành phố trước'
                          }}
                        </a-select-option>
                      </template>
                    </a-select>
                  </a-form-item>
                </div>

                <div class="form-group">
                  <a-form-item label="Xã/phường/Thị trấn" name="xa">
                    <a-select
                      v-model:value="product.xa"
                      placeholder="Chọn xã/phường/thị trấn"
                      @change="handleCommuneChange"
                      :options="phuongXaOptions"
                      label-in-value
                      :key="phuongXaOptions.length"
                      class="form-control-select"
                      :disabled="!product.huyen"
                      show-search
                      :filter-option="
                        (input, option) =>
                          option.label.toLowerCase().includes(input.toLowerCase())
                      "
                    >
                      <template v-if="phuongXaOptions.length === 0">
                        <a-select-option value="" disabled>
                          {{
                            product.huyen
                              ? 'Đang tải dữ liệu...'
                              : 'Vui lòng chọn quận/huyện trước'
                          }}
                        </a-select-option>
                      </template>
                    </a-select>
                  </a-form-item>
                </div>

                <div class="form-group full-width">
                  <a-form-item label="Địa Chỉ Chi Tiết" name="diaChi">
                    <textarea
                      class="form-control"
                      v-model="product.diaChi"
                      rows="3"
                      placeholder="Nhập địa chỉ chi tiết (số nhà, tên đường...)"
                    ></textarea>
                  </a-form-item>
                </div>
              </div>

              <!-- Form Actions -->
              <div class="form-actions">
                <a-popconfirm
                  title="Bạn có chắc chắn muốn lưu thay đổi?"
                  @confirm="handleSubmit"
                  ok-text="Đồng ý"
                  cancel-text="Hủy"
                >
                  <a-button
                    type="primary"
                    size="large"
                    style="
                      background-color: #54bddb;
                      border-color: #54bddb;
                      margin-top: 2%;
                    "
                  >
                    Cập Nhật Thông Tin
                  </a-button>
                </a-popconfirm>
              </div>
            </a-form>
          </div>
        </div>
      </div>

      <!-- Tab Lịch Sử Đơn Hàng -->
      <div class="tab-content" v-if="activeTab === 'orders'">
        <div class="stats-grid">
          <div class="stat-card">
            <div class="stat-number">{{ orderStats.total }}</div>
            <div class="stat-label">Tổng Đơn Hàng</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ orderStats.completed }}</div>
            <div class="stat-label">Đã Hoàn Thành</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ formatCurrency(orderStats.totalAmount) }}</div>
            <div class="stat-label">Tổng Tiền</div>
          </div>
          <div class="stat-card">
            <div class="stat-number">{{ orderStats.processing }}</div>
            <div class="stat-label">Đã Xác Nhận</div>
          </div>
        </div>

        <div class="order-filter">
          <select
            v-model="filterStatus"
            class="form-control"
            style="max-width: 300px"
          >
            <option value="">Tất cả trạng thái</option>
            <option value="CHO_XAC_NHAN">Chờ xác nhận</option>
            <option value="DA_XAC_NHAN">Đã xác nhận</option>
            <option value="CHO_GIAO">Chờ giao</option>
            <option value="DANG_GIAO">Đang giao</option>
            <option value="HOAN_THANH">Hoàn thành</option>
            <option value="DA_HUY">Đã hủy</option>
          </select>
        </div>

        <div class="orders-list">
          <div
            v-for="order in filteredOrders"
            :key="order.id"
            class="order-card"
          >
            <div class="order-header">
              <div>
                <div class="order-code">{{ order.ma }}</div>
                <div class="order-date">{{ formatDate(order.ngayTao) }}</div>
              </div>
              <div
                class="order-status"
                :class="getStatusClass(order.trangThaiHoaDon)"
              >
                {{ getStatusText(order.trangThaiHoaDon) }}
              </div>
            </div>

            <div class="order-details">
              <div class="detail-item">
                <div class="detail-label">Tên đơn hàng</div>
                <div class="detail-value">
                  {{ order.ten || "Đơn hàng online" }}
                </div>
              </div>
              <div class="detail-item">
                <div class="detail-label">Số điện thoại</div>
                <div class="detail-value">{{ order.sdt }}</div>
              </div>
              <div class="detail-item">
                <div class="detail-label">Phương thức thanh toán</div>
                <div class="detail-value">
                  {{ getPaymentMethod(order.phuongThucThanhToan) }}
                </div>
              </div>
              <div class="detail-item">
                <div class="detail-label">Tổng tiền</div>
                <div class="detail-value price">
                  {{ formatCurrency(order.tongTienSauGiam || order.tongTien) }}
                </div>
              </div>
            </div>

            <div class="order-address">
              <div class="detail-label">Địa chỉ giao hàng</div>
              <div class="detail-value">{{ order.diaChi }}</div>
            </div>

            <div class="order-note" v-if="order.ghiChu">
              <div class="detail-label">Ghi chú</div>
              <div class="detail-value">{{ order.ghiChu }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- Tab Đổi Mật Khẩu -->
      <div class="tab-content" v-if="activeTab === 'password'">
        <div class="password-form">
          <div
            v-if="passwordMessage.text"
            :class="[
              'alert',
              passwordMessage.type === 'success'
                ? 'alert-success'
                : 'alert-error',
            ]"
          >
            {{ passwordMessage.text }}
          </div>

          <form @submit.prevent="changePassword">
            <!-- Mật khẩu hiện tại -->
            <div class="mb-3">
              <div class="position-relative">
                <input
                  v-model="passwordForm.currentPassword"
                  :type="showPasswords.current ? 'text' : 'password'"
                  class="form-control"
                  placeholder="Mật khẩu hiện tại"
                  autocomplete="current-password"
                  required
                />
                <span
                  class="toggle-eye"
                  @click="showPasswords.current = !showPasswords.current"
                  :title="showPasswords.current ? 'Ẩn mật khẩu' : 'Hiện mật khẩu'"
                >
                  <svg
                    v-if="showPasswords.current"
                    xmlns="http://www.w3.org/2000/svg"
                    width="22"
                    height="22"
                    fill="currentColor"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288a13.133 13.133 0 0 1-1.66 2.043C11.88 11.332 10.12 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8z"
                    />
                    <path
                      d="M8 5.5a2.5 2.5 0 1 1 0 5 2.5 2.5 0 0 1 0-5zm0 1a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3z"
                    />
                  </svg>
                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    width="22"
                    height="22"
                    fill="currentColor"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M13.359 11.238l2.122 2.122-.708.708-2.122-2.122C11.12 12.331 9.44 13.5 8 13.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8a13.133 13.133 0 0 1 1.66-2.043c.465-.488 1.008-.976 1.619-1.432L2.146 3.854l.708-.708 12 12-.708.708-2.122-2.122zm-1.033-1.033l-2.07-2.07a2 2 0 0 0-2.364-2.364l-2.07-2.07C4.12 4.669 5.88 3.5 8 3.5c2.12 0 3.88 1.169 5.168 2.457A13.133 13.133 0 0 1 14.828 8a13.133 13.133 0 0 1-1.66 2.043c-.368.386-.783.772-1.221 1.162z"
                    />
                  </svg>
                </span>
              </div>
              <small class="text-danger" v-if="errors.currentPassword">
                {{ errors.currentPassword }}
              </small>
            </div>

            <!-- Mật khẩu mới -->
            <div class="mb-3">
              <div class="position-relative">
                <input
                  v-model="passwordForm.newPassword"
                  :type="showPasswords.new ? 'text' : 'password'"
                  class="form-control"
                  placeholder="Mật khẩu mới"
                  autocomplete="new-password"
                  required
                />
                <span
                  class="toggle-eye"
                  @click="showPasswords.new = !showPasswords.new"
                  :title="showPasswords.new ? 'Ẩn mật khẩu' : 'Hiện mật khẩu'"
                >
                  <svg
                    v-if="showPasswords.new"
                    xmlns="http://www.w3.org/2000/svg"
                    width="22"
                    height="22"
                    fill="currentColor"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288a13.133 13.133 0 0 1-1.66 2.043C11.88 11.332 10.12 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8z"
                    />
                    <path
                      d="M8 5.5a2.5 2.5 0 1 1 0 5 2.5 2.5 0 0 1 0-5zm0 1a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3z"
                    />
                  </svg>
                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    width="22"
                    height="22"
                    fill="currentColor"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M13.359 11.238l2.122 2.122-.708.708-2.122-2.122C11.12 12.331 9.44 13.5 8 13.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8a13.133 13.133 0 0 1 1.66-2.043c.465-.488 1.008-.976 1.619-1.432L2.146 3.854l.708-.708 12 12-.708.708-2.122-2.122zm-1.033-1.033l-2.07-2.07a2 2 0 0 0-2.364-2.364l-2.07-2.07C4.12 4.669 5.88 3.5 8 3.5c2.12 0 3.88 1.169 5.168 2.457A13.133 13.133 0 0 1 14.828 8a13.133 13.133 0 0 1-1.66 2.043c-.368.386-.783.772-1.221 1.162z"
                    />
                  </svg>
                </span>
              </div>
              <small class="text-danger" v-if="errors.newPassword">
                {{ errors.newPassword }}
              </small>
            </div>

            <!-- Xác nhận mật khẩu mới -->
            <div class="mb-3">
              <div class="position-relative">
                <input
                  v-model="passwordForm.confirmPassword"
                  :type="showPasswords.confirm ? 'text' : 'password'"
                  class="form-control"
                  placeholder="Xác nhận mật khẩu mới"
                  autocomplete="new-password"
                  required
                />
                <span
                  class="toggle-eye"
                  @click="showPasswords.confirm = !showPasswords.confirm"
                  :title="showPasswords.confirm ? 'Ẩn mật khẩu' : 'Hiện mật khẩu'"
                >
                  <svg
                    v-if="showPasswords.confirm"
                    xmlns="http://www.w3.org/2000/svg"
                    width="22"
                    height="22"
                    fill="currentColor"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288a13.133 13.133 0 0 1-1.66 2.043C11.88 11.332 10.12 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8z"
                    />
                    <path
                      d="M8 5.5a2.5 2.5 0 1 1 0 5 2.5 2.5 0 0 1 0-5zm0 1a1.5 1.5 0 1 0 0 3 1.5 1.5 0 0 0 0-3z"
                    />
                  </svg>
                  <svg
                    v-else
                    xmlns="http://www.w3.org/2000/svg"
                    width="22"
                    height="22"
                    fill="currentColor"
                    viewBox="0 0 16 16"
                  >
                    <path
                      d="M13.359 11.238l2.122 2.122-.708.708-2.122-2.122C11.12 12.331 9.44 13.5 8 13.5c-2.12 0-3.879-1.168-5.168-2.457A13.133 13.133 0 0 1 1.172 8a13.133 13.133 0 0 1 1.66-2.043c.465-.488 1.008-.976 1.619-1.432L2.146 3.854l.708-.708 12 12-.708.708-2.122-2.122zm-1.033-1.033l-2.07-2.07a2 2 0 0 0-2.364-2.364l-2.07-2.07C4.12 4.669 5.88 3.5 8 3.5c2.12 0 3.88 1.169 5.168 2.457A13.133 13.133 0 0 1 14.828 8a13.133 13.133 0 0 1-1.66 2.043c-.368.386-.783.772-1.221 1.162z"
                    />
                  </svg>
                </span>
              </div>
              <small class="text-danger" v-if="errors.confirmPassword">
                {{ errors.confirmPassword }}
              </small>
            </div>

            <!-- Nút Đổi Mật Khẩu -->
            <div class="d-grid mb-2">
              <button
                type="submit"
                class="btn text-white d-flex justify-content-center align-items-center"
                :disabled="loading"
                style="background-color: #4fc3f7; height: 38px;"
              >
                <span v-if="!loading">Đổi Mật Khẩu</span>
                <div
                  v-else
                  class="spinner-border spinner-border-sm text-light"
                  role="status"
                ></div>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import { toast } from "vue3-toastify";
import axios from "axios";
import dayjs from "dayjs";
import {
  getKhachHang,
  modifyKhachHang,
  getKhachHangs,
  type KhachHangResponse,
} from "@/services/api/permitall/profile/profile.api";
import {
  ChangePass,
  type ChangePassWordForm,
} from "@/services/api/auth/authentication.api";
import {
  getGHNProvinces,
  getGHNDistricts,
  getGHNWards,
} from "@/services/api/ghn.api";
import { localStorageAction } from "@/utils/storage";
import { USER_INFO_STORAGE_KEY } from "@/constants/storageKey";
import { useAuthStore } from "@/stores/auth";
import { ROUTES_CONSTANTS } from "@/constants/path";

const GHN_TOKEN = "72f634c6-58a2-11f0-8a1e-1e10d8df3c04"; // Replace with actual token

const idKH = localStorageAction.get(USER_INFO_STORAGE_KEY);

const router = useRouter();
const authStore = useAuthStore();
const imageFile = ref<File | null>(null);
const productForm = ref();
const activeTab = ref("profile");
const loading = ref(false);

const product = ref<KhachHangResponse>({
  id: "",
  ten: "",
  sdt: "",
  diaChi: "",
  email: "",
  ma: "",
  cccd: "",
  gioiTinh: false, // Default to false (Nam) to avoid undefined
  tinh: "",
  huyen: "",
  xa: "",
  ngaySinh: "",
  avatar: "",
});

const passwordForm = reactive({
  currentPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const errors = reactive({
  currentPassword: "",
  newPassword: "",
  confirmPassword: "",
});

const showPasswords = reactive({
  current: false,
  new: false,
  confirm: false,
});

const passwordMessage = reactive({
  text: "",
  type: "",
});

const tinhThanh = ref<Province[]>([]);
const quanHuyen = ref<District[]>([]);
const phuongXa = ref<Ward[]>([]);

interface Province {
  ProvinceID: number;
  ProvinceName: string;
}

interface District {
  DistrictID: number;
  DistrictName: string;
  ProvinceID: number;
}

interface Ward {
  WardCode: string;
  WardName: string;
  DistrictID: number;
}

const tinhThanhOptions = computed(() => {
  return tinhThanh.value.map((item) => ({
    value: item.ProvinceID.toString(),
    label: item.ProvinceName,
  }));
});

const quanHuyenOptions = computed(() => {
  return quanHuyen.value.map((item) => ({
    value: item.DistrictID.toString(),
    label: item.DistrictName,
  }));
});

const phuongXaOptions = computed(() => {
  return phuongXa.value.map((item) => ({
    value: item.WardCode,
    label: item.WardName,
  }));
});

const rules = {
  ten: [
    { required: true, message: "Tên không được để trống!", trigger: "blur" },
  ],
  cccd: [
    {
      required: true,
      message: "Mã định danh không được để trống!",
      trigger: "blur",
    },
    {
      pattern: /^[0-9]{9,12}$/,
      message: "Mã định danh phải là số và có độ dài từ 9 đến 12 ký tự!",
      trigger: "blur",
    },
  ],
  ngaySinh: [
    {
      required: true,
      message: "Ngày sinh không được để trống!",
      trigger: "blur",
    },
  ],
  sdt: [
    {
      required: true,
      message: "Số điện thoại không được để trống!",
      trigger: "blur",
    },
    {
      pattern: /^[0-9]{10}$/,
      message: "Số điện thoại phải là 10 chữ số!",
      trigger: "blur",
    },
  ],
  gioiTinh: [
    {
      validator: async (_rule: any, value: any) => {
        if (value === undefined || value === null || value === "") {
          return Promise.reject("Giới tính không được để trống!");
        }
        if (typeof value !== "boolean") {
          return Promise.reject("Giới tính phải là Nam hoặc Nữ!");
        }
        return Promise.resolve();
      },
      trigger: "change",
    },
  ],
  email: [
    { required: true, message: "Email không được để trống!", trigger: "blur" },
    { type: "email", message: "Email không hợp lệ!", trigger: "blur" },
    {
      pattern: /^[a-zA-Z0-9._%+-]+@gmail\.com$/,
      message: "Email phải có đuôi @gmail.com!",
      trigger: "blur",
    },
  ],
  diaChi: [
    {
      required: true,
      message: "Địa chỉ không được để trống!",
      trigger: "blur",
    },
  ],
  avatar: [
    {
      validator: async (_rule: any, _value: any) => {
        if (imageFile.value) {
          const validTypes = ["image/jpeg", "image/png", "image/gif"];
          if (!validTypes.includes(imageFile.value.type)) {
            return Promise.reject("File phải là định dạng JPEG, PNG hoặc GIF!");
          }
          if (imageFile.value.size > 5 * 1024 * 1024) {
            return Promise.reject("Kích thước file không được vượt quá 5MB!");
          }
        }
        return Promise.resolve();
      },
      trigger: "change",
    },
  ],
};

const orders = ref<KhachHangResponse[]>([]);

const filterStatus = ref("");

const filteredOrders = computed(() => {
  if (!filterStatus.value) {
    return orders.value;
  }
  return orders.value.filter(
    (order) => order.trangThaiHoaDon === filterStatus.value
  );
});

const orderStats = computed(() => {
  const total = orders.value.length;
  const completed = orders.value.filter(
    (order) => order.trangThaiHoaDon === "HOAN_THANH"
  ).length;
  const processing = orders.value.filter(
    (order) => order.trangThaiHoaDon === "DA_XAC_NHAN"
  ).length;
  const totalAmount = orders.value
    .filter((order) => order.trangThaiHoaDon === "HOAN_THANH")
    .reduce((sum, order) => sum + (order.tongTienSauGiam || order.tongTien), 0);

  return { total, completed, processing, totalAmount };
});

const fetchGHNProvinces = async () => {
  try {
    const provinces = await getGHNProvinces(GHN_TOKEN);
    tinhThanh.value = provinces;
    console.log("Fetched provinces:", provinces);
  } catch (error) {
    console.error("Lỗi khi lấy danh sách tỉnh:", error);
    toast.error("Không lấy được danh sách tỉnh từ GHN.");
  }
};

const handleProvinceChange = (value: { value: string; label: string }) => {
  product.value.tinh = value.value;
  product.value.huyen = "";
  product.value.xa = "";
  quanHuyen.value = [];
  phuongXa.value = [];
  if (value.value) {
    fetchGHNDistricts(parseInt(value.value));
  }
};

const handleDistrictChange = (value: { value: string; label: string }) => {
  product.value.huyen = value.value;
  product.value.xa = "";
  phuongXa.value = [];
  if (value.value && product.value.tinh) {
    fetchGHNWards(parseInt(value.value));
  }
};

const handleCommuneChange = (value: { value: string; label: string }) => {
  product.value.xa = value.value;
};

const fetchGHNDistricts = async (provinceId: number) => {
  try {
    const districts = await getGHNDistricts(provinceId, GHN_TOKEN);
    quanHuyen.value = districts;
    console.log("Fetched districts:", districts);
  } catch (error) {
    console.error("Lỗi khi lấy danh sách quận/huyện:", error);
    toast.error("Không lấy được danh sách quận/huyện.");
  }
};

const fetchGHNWards = async (districtId: number) => {
  try {
    const wards = await getGHNWards(districtId, GHN_TOKEN);
    phuongXa.value = wards;
    console.log("Fetched wards:", wards);
  } catch (error) {
    console.error("Lỗi khi lấy danh sách phường/xã:", error);
    toast.error("Không lấy được danh sách phường/xã.");
  }
};

const mapTrangThaiHoaDon = (status: string | number): string => {
  const statusMap: { [key: string]: string } = {
    "0": "CHO_XAC_NHAN",
    "1": "DA_XAC_NHAN",
    "2": "CHO_GIAO",
    "3": "DANG_GIAO",
    "4": "HOAN_THANH",
    "5": "DA_HUY",
  };
  return statusMap[status.toString()] || "UNKNOWN";
};

const fetchOrderHistory = async (orders: any, userId: string) => {
  try {
    const response = await getKhachHangs(userId);
    const { status, data, message, success } = response;

    if (status === "OK" && success) {
      orders.value = data.map((order: any) => ({
        id: order.id || "",
        ma: order.ma || "",
        ten: order.ten || "Đơn hàng online",
        sdt: order.sdt || "",
        tenKH: order.tenKH || "",
        phiVanChuyen: order.phiVanChuyen || 0,
        diaChi: order.diaChi || "",
        tongTienSauGiam: order.tongTienSauGiam || order.tongTien || 0,
        tongTien: order.tongTien || 0,
        ghiChu: order.ghiChu || "",
        phuongThucThanhToan: order.phuongThucThanhToan || "TIEN_MAT",
        loaiHoaDon: "ONLINE",
        trangThaiHoaDon: mapTrangThaiHoaDon(order.trangThaiHoaDon),
        ngayTao: order.ngayTao ? new Date(order.ngayTao).toISOString() : "",
      }));

    } else {
      toast.error(message || "Không thể lấy lịch sử đơn hàng!");
    }
  } catch (error: any) {
    console.error("Error fetching order history:", error);
    if (error.response?.status === 401) {
      toast.error("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại!");
      authStore.logout();
      router.push({ name: ROUTES_CONSTANTS.USERS.children.LOGIN.name });
    } else {
      const errorMessage = error.response?.data?.message || "Có lỗi xảy ra khi lấy lịch sử đơn hàng!";
      toast.error(errorMessage);
    }
  }
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getKhachHang(id);
    const data = response.data;

    console.log("API Response:", data);

    await fetchGHNProvinces();
    await nextTick();

    let formattedDate = "";
    if (data.ngaySinh) {
      const date = new Date(data.ngaySinh);
      formattedDate =
        date.getFullYear() +
        "-" +
        String(date.getMonth() + 1).padStart(2, "0") +
        "-" +
        String(date.getDate()).padStart(2, "0");
    }

    product.value = {
      ...product.value,
      id: data.id || "",
      ten: data.ten || "",
      sdt: data.sdt || "",
      diaChi: data.diaChi || "",
      email: data.email || "",
      ma: data.ma || "",
      cccd: data.cccd || "",
      gioiTinh: data.gioiTinh ?? false, // Fallback to false if undefined
      ngaySinh: formattedDate,
      avatar: data.avatar || "",
    };

    if (data.tinh && tinhThanh.value.length > 0) {
      const province = tinhThanh.value.find(
        (p) => p.ProvinceID.toString() === data.tinh.toString()
      );
      if (province) {
        product.value.tinh = province.ProvinceID.toString();
        await fetchGHNDistricts(province.ProvinceID);
        await nextTick();

        if (data.huyen && quanHuyen.value.length > 0) {
          const district = quanHuyen.value.find(
            (d) => d.DistrictID.toString() === data.huyen.toString()
          );
          if (district) {
            product.value.huyen = district.DistrictID.toString();
            await fetchGHNWards(district.DistrictID);
            await nextTick();

            if (data.xa && phuongXa.value.length > 0) {
              const ward = phuongXa.value.find((w) => w.WardCode === data.xa);
              if (ward) {
                product.value.xa = ward.WardCode;
              }
            }
          }
        }
      }
    }
  } catch (error) {
    console.error("Error fetching product details:", error);
    if (error?.response?.data?.message) {
      toast.error(error.response.data.message);
    }
  }
};

const validateForm = (): boolean => {
  if (!product.value.ten?.trim()) {
    toast.error("Vui lòng nhập họ và tên!");
    return false;
  }

  if (!product.value.email?.trim()) {
    toast.error("Vui lòng nhập email!");
    return false;
  }

  if (!product.value.sdt?.trim()) {
    toast.error("Vui lòng nhập số điện thoại!");
    return false;
  }

  if (!product.value.diaChi?.trim()) {
    toast.error("Vui lòng nhập địa chỉ!");
    return false;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(product.value.email)) {
    toast.error("Email không đúng định dạng!");
    return false;
  }

  const phoneRegex = /^[0-9]{10}$/;
  if (!phoneRegex.test(product.value.sdt)) {
    toast.error("Số điện thoại phải là 10 chữ số!");
    return false;
  }

  if (product.value.cccd && product.value.cccd.trim()) {
    const cccdRegex = /^[0-9]{9,12}$/;
    if (!cccdRegex.test(product.value.cccd)) {
      toast.error("CCCD/CMND phải là số và có độ dài từ 9 đến 12 ký tự!");
      return false;
    }
  }

  if (product.value.gioiTinh === undefined || product.value.gioiTinh === null) {
    toast.error("Vui lòng chọn giới tính!");
    return false;
  }

  return true;
};

const handleAvatarChange = (event: Event) => {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];
  if (file) {
    imageFile.value = file;
    const reader = new FileReader();
    reader.onload = (e) => {
      product.value.avatar = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  }
};

const handleSubmit = async () => {
  try {
    if (!validateForm()) {
      return;
    }

    const formData = new FormData();
    if (product.value.id) {
      formData.append("id", product.value.id.trim());
    }
    if (product.value.ma) {
      formData.append("code", product.value.ma.trim());
    }
    if (product.value.user) {
      formData.append("user", product.value.user.trim());
    }
    formData.append("ten", product.value.ten.trim());
    formData.append("email", product.value.email.trim());
    formData.append("sdt", product.value.sdt.trim());
    formData.append("diaChi", product.value.diaChi.trim());
    formData.append("gioiTinh", String(product.value.gioiTinh)); // Ensure boolean is converted to string
    if (product.value.cccd && product.value.cccd.trim()) {
      formData.append("cccd", product.value.cccd.trim());
    }
    if (product.value.ngaySinh) {
      const formattedDate = dayjs(product.value.ngaySinh).format("YYYY-MM-DD");
      formData.append("ngaySinh", formattedDate);
    }
    if (product.value.tinh) {
      formData.append("tinh", product.value.tinh);
    }
    if (product.value.huyen) {
      formData.append("huyen", product.value.huyen);
    }
    if (product.value.xa) {
      formData.append("xa", product.value.xa);
    }
    if (imageFile.value) {
      formData.append("avatar", imageFile.value);
    }

    console.log("Sending form data:");
    for (let [key, value] of formData.entries()) {
      console.log(key, value);
    }

    const res = await modifyKhachHang(formData);
    if (res.success && res.data?.avatar) {
      product.value.avatar = res.data.avatar; // Update avatar URL from backend response
    }
    toast.success(res.message || "Cập nhật thông tin thành công!");
    await nextTick();
    sessionStorage.setItem(
      "appToastMessage",
      JSON.stringify({
        message: res?.message || "Cập nhật thông tin thành công!",
        type: "success",
      })
    );

    if (product.value.id) {
      await fetchProductDetails(product.value.id);
    }
  } catch (error: any) {
    console.error("Error updating profile:", error);
    if (error?.response) {
      const errorMessage =
        error.response.data?.message || "Có lỗi xảy ra từ server!";
      toast.error(errorMessage);
    } else if (error?.request) {
      toast.error("Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng!");
    } else {
      toast.error("Có lỗi không xác định xảy ra!");
    }
  }
};

const changePassword = async () => {
  loading.value = true;
  errors.currentPassword = "";
  errors.newPassword = "";
  errors.confirmPassword = "";
  passwordMessage.text = "";
  passwordMessage.type = "";

  // Validation
  if (!passwordForm.currentPassword.trim()) {
    errors.currentPassword = "Vui lòng nhập mật khẩu hiện tại";
    loading.value = false;
    return;
  }
  if (!passwordForm.newPassword.trim()) {
    errors.newPassword = "Vui lòng nhập mật khẩu mới";
    loading.value = false;
    return;
  }
  if (!passwordForm.confirmPassword.trim()) {
    errors.confirmPassword = "Vui lòng xác nhận mật khẩu mới";
    loading.value = false;
    return;
  }
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    errors.confirmPassword = "Mật khẩu xác nhận không khớp";
    loading.value = false;
    return;
  }
  if (passwordForm.newPassword.length < 6) {
    errors.newPassword = "Mật khẩu mới phải có ít nhất 6 ký tự";
    loading.value = false;
    return;
  }

  const formData: ChangePassWordForm = {
    currentPassword: passwordForm.currentPassword,
    newPassword: passwordForm.newPassword,
  };

  try {
    console.log("Access Token:", authStore.accessToken);
    console.log("Sending change password request:", formData);
    const response = await ChangePass(formData);
    console.log("Response:", response);

    if (response.success) {
      toast.success("Đổi mật khẩu thành công!");
      passwordMessage.text = "Đổi mật khẩu thành công!";
      passwordMessage.type = "success";
      passwordForm.currentPassword = "";
      passwordForm.newPassword = "";
      passwordForm.confirmPassword = "";
    } else {
      toast.error(response.message || "Có lỗi xảy ra!");
      passwordMessage.text = response.message || "Có lỗi xảy ra!";
      passwordMessage.type = "error";
    }
  } catch (error: any) {
    console.error("Error changing password:", error);
    if (error.response?.status === 401) {
      toast.error("Phiên đăng nhập hết hạn. Đang thử làm mới phiên...");
      try {
        const newToken = await refreshAccessToken();
        if (newToken) {
          console.log("Retrying with new token:", newToken);
          const retryResponse = await ChangePass(formData);
          if (retryResponse.success) {
            toast.success("Đổi mật khẩu thành công!");
            passwordMessage.text = "Đổi mật khẩu thành công!";
            passwordMessage.type = "success";
            passwordForm.currentPassword = "";
            passwordForm.newPassword = "";
            passwordForm.confirmPassword = "";
          } else {
            toast.error(retryResponse.message || "Có lỗi xảy ra!");
            passwordMessage.text = retryResponse.message || "Có lỗi xảy ra!";
            passwordMessage.type = "error";
          }
        } else {
          throw new Error("No new token received");
        }
      } catch (refreshError) {
        console.error("Token refresh failed:", refreshError);
        toast.error("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại!");
        authStore.logout();
        router.push({ name: ROUTES_CONSTANTS.USERS.children.LOGIN.name });
      }
    } else {
      const errorMessage =
        error.response?.data?.message || "Có lỗi xảy ra khi đổi mật khẩu!";
      toast.error(errorMessage);
      passwordMessage.text = errorMessage;
      passwordMessage.type = "error";
    }
  } finally {
    loading.value = false;
  }
};

const refreshAccessToken = async () => {
  try {
    const refreshToken = authStore.refreshToken;
    if (!refreshToken) {
      throw new Error("No refresh token available");
    }
    const response = await axios.post(
      "http://your-backend-url/api/auth/refresh-token",
      { refreshToken }
    );
    const { accessToken, refreshToken: newRefreshToken } = response.data;
    authStore.login({
      user: authStore.user,
      accessToken,
      refreshToken: newRefreshToken,
    });
    return accessToken;
  } catch (error) {
    console.error("Error refreshing token:", error);
    return null;
  }
};

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat("vi-VN", {
    style: "currency",
    currency: "VND",
  }).format(amount);
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString("vi-VN");
};

const getStatusText = (status: string) => {
  const statusMap = {
    CHO_XAC_NHAN: "Chờ xác nhận",
    DA_XAC_NHAN: "Đã xác nhận",
    CHO_GIAO: "Chờ giao",
    DANG_GIAO: "Đang giao",
    HOAN_THANH: "Hoàn thành",
    DA_HUY: "Đã hủy",
  };
  return statusMap[status] || status;
};

const getStatusClass = (status: string) => {
  const classMap = {
    CHO_XAC_NHAN: "status-pending",
    DA_XAC_NHAN: "status-processing",
    DANG_GIAO: "status-processing",
    CHO_GIAO: "status-processing",
    HOAN_THANH: "status-completed",
    DA_HUY: "status-cancelled",
  };
  return classMap[status] || "status-pending";
};

const getPaymentMethod = (method: string) => {
  const methodMap = {
    TIEN_MAT: "Tiền mặt",
    CHUYEN_KHOAN: "Chuyển khoản",
    THE_NGAN_HANG: "Thẻ ngân hàng",
    VI_DIEN_TU: "Ví điện tử",
  };
  return methodMap[method] || method;
};

onMounted(() => {
  fetchGHNProvinces();
  if (idKH?.userId) {
    fetchProductDetails(idKH.userId);
    fetchOrderHistory(orders, idKH.userId);
  }
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.customer-profile {
  min-height: 100vh;
  background: white;
  padding: 20px;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.header {
  background: white;
  color: #333;
  padding: 30px;
  text-align: center;
  position: relative;
  border-bottom: 2px solid #f0f0f0;
}

.header::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grain" width="100" height="100" patternUnits="userSpaceOnUse"><circle cx="50" cy="50" r="1" fill="white" opacity="0.1"/></pattern></defs><rect width="100" height="100" fill="url(%23grain)"/></svg>');
  opacity: 0.3;
}

.header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  position: relative;
  z-index: 1;
}

.header p {
  font-size: 1.1rem;
  opacity: 0.9;
  position: relative;
  z-index: 1;
}

.nav-tabs {
  display: flex;
  background: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
}

.nav-tab {
  flex: 1;
  padding: 20px;
  text-align: center;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.1rem;
  font-weight: 600;
  color: #6c757d;
  transition: all 0.3s ease;
  position: relative;
}

.nav-tab:hover {
  background: #e9ecef;
  color: #495057;
}

.nav-tab.active {
  color: #58bddb;
  background: white;
}

.nav-tab.active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: #58bddb;
}

.tab-content {
  padding: 40px;
}

.profile-section {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 40px;
  margin-bottom: 40px;
}

.avatar-section {
  text-align: center;
}

.avatar {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
  border: 6px solid #58bddb;
  margin-bottom: 20px;
  box-shadow: 0 10px 30px rgba(88, 189, 219, 0.3);
}

.upload-btn {
  background: #58bddb;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  cursor: pointer;
  font-weight: 600;
  transition: transform 0.3s ease;
}

.upload-btn:hover {
  transform: translateY(-2px);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
}

.full-width {
  grid-column: 1 / -1;
}

.form-group {
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #495057;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 2px solid #e9ecef;
  border-radius: 10px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #f8f9fa;
  min-height: 50px;
}

.form-control-select {
  width: 100%;
}

.form-control-select .ant-select {
  width: 100% !important;
  height: 50px !important;
}

.form-control-select .ant-select-selector {
  height: 50px !important;
  padding: 0 15px !important;
  border: 2px solid #e9ecef !important;
  border-radius: 10px !important;
  background: #f8f9fa !important;
  font-size: 1rem !important;
  display: flex !important;
  align-items: center !important;
}

.form-control-select .ant-select-selection-item {
  line-height: 50px !important;
  height: 50px !important;
  font-size: 1rem !important;
  color: #495057 !important;
  padding: 0 !important;
}

.form-control-select .ant-select-selection-placeholder {
  line-height: 50px !important;
  height: 50px !important;
  font-size: 1rem !important;
  color: #6c757d !important;
  padding: 0 !important;
}

.btn-primary {
  background: #58bddb;
  color: white;
  border: none;
  padding: 15px 30px;
  border-radius: 10px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 20px;
}

.btn-primary:hover {
  background: #4ba8c7;
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(88, 189, 219, 0.3);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: #58bddb;
  color: white;
  padding: 25px;
  border-radius: 15px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: "";
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle,
    rgba(255, 255, 255, 0.1) 0%,
    transparent 70%
  );
  animation: shimmer 3s ease-in-out infinite;
}

@keyframes shimmer {
  0%,
  100% {
    transform: rotate(0deg);
  }
  50% {
    transform: rotate(180deg);
  }
}

.stat-number {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 10px;
  position: relative;
  z-index: 1;
}

.stat-label {
  font-size: 1rem;
  opacity: 0.9;
  position: relative;
  z-index: 1;
}

.order-filter {
  margin-bottom: 30px;
}

.orders-list {
  space-y: 20px;
}

.order-card {
  background: white;
  border: 2px solid #e9ecef;
  border-radius: 15px;
  padding: 25px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.order-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 5px;
  height: 100%;
  background: #58bddb;
}

.order-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
}

.order-code {
  font-size: 1.2rem;
  font-weight: 700;
  color: #58bddb;
}

.order-date {
  font-size: 0.9rem;
  color: #6c757d;
  margin-top: 4px;
}

.order-status {
  padding: 8px 16px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.9rem;
}

.status-pending {
  background: #fff3cd;
  color: #856404;
}

.status-processing {
  background: #cce5ff;
  color: #004085;
}

.status-completed {
  background: #d4edda;
  color: #155724;
}

.status-cancelled {
  background: #f8d7da;
  color: #721c24;
}

.order-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-top: 15px;
}

.detail-item {
  display: flex;
  flex-direction: column;
}

.detail-label {
  font-size: 0.9rem;
  color: #6c757d;
  margin-bottom: 5px;
}

.detail-value {
  font-weight: 600;
  color: #495057;
}

.detail-value.price {
  color: #58bddb;
  font-size: 1.1rem;
}

.order-address,
.order-note {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #e9ecef;
}

.password-form {
  max-width: 500px;
  margin: 0 auto;
}

.card {
  border-radius: 0.75rem;
}

.toggle-eye {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 12px;
  margin: auto 0;
  height: 22px;
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #757575;
  z-index: 2;
  user-select: none;
}

.toggle-eye:hover {
  color: #03a9f4;
}

.position-relative {
  position: relative;
}

.alert {
  padding: 15px;
  border-radius: 10px;
  margin-bottom: 20px;
  font-weight: 600;
}

.alert-success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.alert-error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

@media (max-width: 768px) {
  .profile-section {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .nav-tabs {
    flex-direction: column;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .tab-content {
    padding: 20px;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .order-details {
    grid-template-columns: 1fr;
  }
}
</style>
```