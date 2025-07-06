<template>
  <div class="breadcrumb-section">
    <BreadcrumbDefault :pageTitle="'Bán hàng'" :routes="[
      { path: '/ban-hang', name: 'Bán hàng' }
    ]" />
  </div>
  <div class="main-layout">
    <div class="left-column">
      <div class="top-header">
        <div class="search-and-create-section">
          <div class="search-box-top">
            <input type="text" placeholder="Tìm kiếm hóa đơn..." class="search-input-top" />
            <button class="search-icon-top">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search"
                viewBox="0 0 16 16">
                <path
                  d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.085.12c.047.061.096.119.146.177l3.85 3.85a1 1 0 0 0 1.415-1.415zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
              </svg>
            </button>
          </div>
          <button class="btn-create-new-invoice" @click="createInvoice">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-lg"
              viewBox="0 0 16 16">
              <path fill-rule="evenodd"
                d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2" />
            </svg>
            Tạo hóa đơn mới
          </button>
        </div>
      </div>
      <div class="card">
        <div class="card-header">
          <h3>Hóa đơn chờ</h3>
        </div>
        <div class="pending-invoices-container">
          <div v-for="tab in tabs" :key="tab.id" :class="['pending-invoice-card', { active: activeTab === tab.id }]"
            @click="clickkActiveTab(tab.id, tab.idHD)">
            <div class="invoice-id">{{ tab.ma }}</div>
            <div class="invoice-status">Chờ xử lý</div>
            <div class="invoice-product-count">{{ tab.soLuong == null ? 0 : tab.soLuong }} sản phẩm</div>
            <button class="delete-invoice-btn" @click.stop="deleteInvoice(tab.id, tab.idHD)">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash"
                viewBox="0 0 16 16">
                <path
                  d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z" />
                <path
                  d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H9.5a1 1 0 0 1 1 1H14a1 1 0 0 1 1 1zM4.118 4 .5 3.993 1.5 2h13l-1 2H4.118z" />
              </svg>
            </button>
          </div>
        </div>
      </div>




      <div class="card mt-4">
        <div class="card-header">
          <h3>Giỏ hàng</h3>
        </div>

        <div class="tab-content-display" v-if="activeTab">
          <div class="actions-add-product">
            <button class="btn-add-product">
              <p class="quet">
                Quét QR
              </p>
            </button>
          </div>
          <div v-if="state.gioHang.length > 0">
            <a-table :columns="columnsGiohang" :data-source="state.gioHang" :scroll="{ y: 240 }"
              :pagination="{ pageSize: 5, hideOnSinglePage: true }">
              <template #bodyCell="{ column, record }">
                <div v-if="column.key === 'stt'">
                  {{ state.gioHang.indexOf(record) + 1 }}
                </div>
                <template v-if="column.key === 'anh'">
                  <div class="center-cell">
                    <img :src="record.anh" class="anh" style="width: 50px; height: 50px; border-radius: 50%" />
                  </div>
                </template>

                <template v-if="column.key === 'soLuong'">
                  <div class="center-cell">
                    <button class="quantity-btn" @click="decreaseQuantity(record.id)">-</button>
                    <input type="text" v-model="record.soLuong" class="quantity-input" readonly />
                    <button class="quantity-btn" @click="increaseQuantity(record.id)">+</button>
                  </div>
                </template>

                <template v-if="column.key === 'mau'">
                  <div class="center-cell">
                    <div class="color"
                      :style="{ width: '30px', height: '30px', backgroundColor: record.mau, border: '1px solid #000' }">
                    </div>
                  </div>
                </template>
                <template v-if="column.key === 'status'">
                  <div class="center-cell">
                    <b class="highlight">{{ record.giaBan * record.soLuong }} VNĐ</b>
                  </div>
                </template>
                <template v-if="column.key === 'operation'">
                  <div class="d-flex gap-1 justify-center">
                    <a-tooltip title="Hủy sản phẩm">
                      <a-button type="primary" @click="deleteProduc(record.id)"
                        style="background-color: #54bddb; color: white;"
                        class="p-2 d-flex justify-content-center align-items-center">
                        Hủy
                      </a-button>
                    </a-tooltip>
                  </div>
                </template>
              </template>
            </a-table>
          </div>
          <div class="empty-cart" v-else>
            <p>Không có sản phẩm nào trong giỏ hàng</p>
          </div>
        </div>
      </div>

      <div class="card mt-4">
        <div class="card-header">
          <h3>Danh sách sản phẩm</h3>
        </div>
        <div class="card-body">
          <div class="product-selection-table">
            <a-table :columns="columns" :data-source="state.products" :pagination="{
              current: state.paginationParams.page,
              pageSize: state.paginationParams.size,
              total: state.totalItems,
              showSizeChanger: true,
              pageSizeOptions: ['10', '20', '30', '40', '50']
            }" :scroll="{ y: 240 }">
              <template #bodyCell="{ column, record }">
                <template v-if="column.key === 'status'">
                  <a-tag :color="record.status == 'ACTIVE' ? 'green' : 'red'">
                    {{ record.status == 'ACTIVE' ? 'Hoạt động' : 'Không hoạt động' }}
                  </a-tag>
                </template>
                <div v-if="column.key === 'stt'">
                  {{ state.products.indexOf(record) + 1 }}
                </div>
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
                  <div class="center-cell">
                    <div class="d-flex gap-1 justify-center">
                      <a-tooltip title="chọn sản phẩm">
                        <a-button type="primary" @click="selectProduct(record.id)"
                          style="background-color: #54bddb; color: white;"
                          class="p-2 d-flex justify-content-center align-items-center btn-choose-product">
                          Chọn
                        </a-button>
                      </a-tooltip>
                    </div>
                  </div>
                </template>
              </template>
            </a-table>
          </div>
        </div>
      </div>

    </div>

    <div class="right-column">
      <div class="card">

        <div class="card-body">
          <div class="card mt-4">
            <div class="card-header">
              <h3>Khách hàng</h3>
            </div>

            <div v-if="state.detailKhachHang">
              <div class="form-group">
                <label for="customer-name">Tên khách hàng <span class="required">*</span></label>
                <div class="input-wrapper">
                  <input :value="state.detailKhachHang.ten" type="text" id="customer-name" class="input-customer"
                    placeholder="Tên khách hàng" />
                </div>
              </div>

              <div class="form-group">
                <label for="phone">Số điện thoại <span class="required">*</span></label>
                <div class="input-wrapper">
                  <input :value="state.detailKhachHang.sdt" type="text" id="phone" placeholder="Số điện thoại"
                    class="input-customer" />
                </div>
              </div>
            </div>
            <div v-else>
              <div class="form-group">
                <label for="customer-name">Tên khách hàng <span class="required">*</span></label>
                <div class="input-wrapper">
                  <input type="text" id="customer-name" placeholder="Tên khách hàng" class="input-customer" />
                </div>
              </div>

              <div class="form-group">
                <label for="phone">Số điện thoại <span class="required">*</span></label>
                <div class="input-wrapper">
                  <input type="text" id="phone" placeholder="Số điện thoại" class="input-customer" />
                </div>
              </div>
            </div>
            <div class="button-group-customer">
              <button class="add-button" @click="showKhachHangModal = true">Chọn khách hàng</button>
              <button class="add-button" @click="">Thêm khách hàng</button>
            </div>
          </div>
        </div>

        <div class="card-body">
          <div class="card mt-4">
            <div class="card-header">
              <h3>Thông tin đơn</h3>
            </div>
            <div class="card-body payment-section-content">
              <div class="form-group-payment">
                <label for="payment-method">Chọn hộp mã giảm giá</label>
                <select id="payment-method" class="select-discount">
                  <option value="">Không</option>
                </select>
              </div>
              <div class="payment-summary">
                <div class="summary-item">
                  <label>Tổng tiền hàng:</label>
                  <span>{{ tienHang }} VND</span>
                </div>
                <div class="summary-item">
                  <label>Giảm giá:</label>
                  <span>{{ giamGia }} VND</span>
                </div>
                <div class="summary-item total-amount">
                  <label>Tổng tiền:</label>
                  <span>{{ tongTien }} VND</span>
                </div>
              </div>
              <button class="btn-confirm-payment" @click="selectThanhToan">Xác nhận thanh toán</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="showProductModal" class="modal-backdrop" @click.self="showProductModal = false">
      <div class="modal-content">
        <h3 class="modal-title">Chọn sản phẩm</h3>
        <div class="min-h-[360px] ">
          <a-table :columns="columns" :data-source="state.products" :pagination="{
            current: state.paginationParams.page,
            pageSize: state.paginationParams.size,
            total: state.totalItems,
            showSizeChanger: true,
            pageSizeOptions: ['10', '20', '30', '40', '50']
          }" :scroll="{ y: 240 }">
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'status'">
                <a-tag :color="record.status == 'ACTIVE' ? 'green' : 'red'">
                  {{ record.status == 'ACTIVE' ? 'Hoạt động' : 'Không hoạt động' }}
                </a-tag>
              </template>
              <div v-if="column.key === 'stt'">
                {{ state.products.indexOf(record) + 1 }}
              </div>
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
                <div class="center-cell">
                  <div class="d-flex gap-1 justify-center">
                    <a-tooltip title="chọn sản phẩm">
                      <a-button type="primary" @click="selectProduct(record.id)"
                        class="p-2 d-flex justify-content-center align-items-center">
                        Chọn
                      </a-button>
                    </a-tooltip>
                  </div>
                </div>
              </template>
            </template>
          </a-table>
        </div>
      </div>
    </div>

    <div v-if="showKhachHangModal" class="modal-backdrop" @click.self="showKhachHangModal = false">
      <div class="modal-content">
        <h3 class="modal-title">Khách Hàng</h3>
        <div class="min-h-[360px] ">
          <a-table :columns="columnsKhachHang" :data-source="state.khachHang" :pagination="{
            current: state.paginationParams.page,
            pageSize: state.paginationParams.size,
            total: state.totalItems,
            showSizeChanger: true,
            pageSizeOptions: ['10', '20', '30', '40', '50']
          }" :scroll="{ y: 240 }">
            <template #bodyCell="{ column, record }">
              <div v-if="column.key === 'stt'">
                {{ state.khachHang.indexOf(record) + 1 }}
              </div>
              <template v-if="column.key === 'operation'">
                <div class="center-cell">
                  <div class="d-flex gap-1 justify-center">
                    <a-tooltip title="chọn khách hàng">
                      <a-button type="primary" @click="selectKhachHang(record.id)"
                        class="p-2 d-flex justify-content-center align-items-center">
                        Chọn
                      </a-button>
                    </a-tooltip>
                  </div>
                </div>
              </template>
            </template>
          </a-table>
        </div>
      </div>
    </div>

    <a-modal :open="state.isModalOpen" title="Chọn số lượng" width="400px">
      <template #footer>
        <a-popconfirm title="Bạn có chắc chắn muốn chọn bằng này số lượng sản phẩm ?" @confirm="confirmQuantity"
          ok-text="Đồng ý" cancel-text="Huỷ">
          <a-button type="primary">Xác nhận</a-button>
        </a-popconfirm>
        <a-button @click="closeModal">Huỷ</a-button>
      </template>
      <a-form ref="productForm" name="productForm" autocomplete="off">
        <a-form-item label="nhập số lượng" name="ten" :label-col="{ span: 24 }">
          <a-input v-model:value="selectedProduct.soLuong" placeholder="nhập số lượng" style="border-radius: 4px;" />
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal :open="state.isModaThanhToanlOpen" title="Thanh toán" width="600px">
      <div class="payment-modal-content">
        <div class="payment-info">
          <span><strong>Số tiền:</strong> {{ soTien }} VND</span>
        </div>
        <div class="tabs-payment-method">
          <button @click="clickPhuongThuc('0')"
            :class="['tab-button-payment', { 'active': state.currentPaymentMethod === '0' }]">
            Tiền mặt
          </button>
          <button @click="clickPhuongThuc('1')"
            :class="['tab-button-payment', { 'active': state.currentPaymentMethod === '1' }]">
            Chuyển khoản
          </button>
          <button @click="clickPhuongThuc('2')"
            :class="['tab-button-payment', { 'active': state.currentPaymentMethod === '2' }]">
            Thẻ
          </button>
        </div>
        <div class="payment-method-summary" style="color: red; font-weight: bold; font-size: 15px;">
          Khách cần trả: {{ state.tongTien?.tongTien }} VND
        </div>

        <div class="payment-details-table">
          <a-table :columns="columnsThanhToan" :data-source="state.phuongThuThanhToan" :scroll="{ y: 240 }">
            <template #bodyCell="{ column, record }">
              <div v-if="column.key === 'stt'">
                {{ state.phuongThuThanhToan.indexOf(record) + 1 }}
              </div>
              <template v-if="column.key === 'status'">
                <a-tag v-if="record.phuongThucThanhToan == 'TIEN_MAT'" :color="'green'">
                  Tiền Mặt
                </a-tag>
                <a-tag v-if="record.phuongThucThanhToan == 'CHUYEN_KHOAN'" :color="'green'">
                  Chuyển khoản
                </a-tag>
                <a-tag v-if="record.phuongThucThanhToan == 'THE'" :color="'green'">
                  Thẻ
                </a-tag>
              </template>
              <template v-if="column.key === 'operation'">
                <div class="center-cell">
                  <div class="d-flex gap-1 justify-center">
                    <a-tooltip title="Xóa phương thức thanh toán">
                      <a-button type="primary" @click="deletePaymentMethod(record.id)"
                        class="p-2 d-flex justify-content-center align-items-center">
                        <DeleteOutlined />
                      </a-button>
                    </a-tooltip>
                  </div>
                </div>
              </template>
            </template>
          </a-table>
        </div>

        <div class="footer-payment-summary">
          <div class="footer-info">
            <div><strong>Khách thanh toán:</strong> {{ tienKhachThanhToan }} VND</div>
            <div><strong>Tiền thiếu:</strong> {{ tienThieu }} VND</div>
          </div>
        </div>
      </div>

      <template #footer>
        <a-button type="primary" @click="sumitThanhToan">Xác nhận</a-button>
        <a-button @click="closeModalThanhToan">Huỷ</a-button>
      </template>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, watch } from 'vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import { GetHoaDons, getCreateHoaDon, themSanPham, GetGioHang, xoaSP, type ParamsXoaSP, themSL, xoaSL, type KhachHangResponse, GetKhachHang, themKhachHang, GeOneKhachHang, type thanhToanResponse, getThanhToan, type tongTienResponse, getPhuongThucThanhToan, type PhuongThucThanhToanResponse, themPTTT, thanhToanThanhCong } from '@/services/api/admin/banhang.api'
import type { TableColumnsType } from 'ant-design-vue'
import { GetSanPhams, type ParamsGetSanPham, type SanPhamResponse } from '@/services/api/admin/sanphamchitiet.api'
import { CreditCardOutlined, DeleteOutlined } from '@ant-design/icons-vue'
import BreadcrumbDefault from '@/components/ui/Breadcrumbs/BreadcrumbDefault.vue'

const soTien = ref(0)
const tienKhachThanhToan = ref(0)
const tienThieu = ref(0)
const tongTien = ref(0)
const giamGia = ref(0) // Giá trị giảm giá này cần được cập nhật dựa trên logic mã giảm giá của bạn
const tienHang = ref(0)
const idSP = ref('')
const idHDS = ref('')
const tabs = ref([])
const activeTab = ref(0)
let nextTabId = 1

const state = reactive({
  searchQuery: '',
  idSP: '',
  searchStatus: null as number | null,
  isModalOpen: false,
  isModaThanhToanlOpen: false,
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  khachHang: [] as KhachHangResponse[],
  thanhToan: [] as thanhToanResponse[],
  phuongThuThanhToan: [] as PhuongThucThanhToanResponse[],
  tongTien: null as tongTienResponse | null,
  detailKhachHang: null as KhachHangResponse | null,
  products: [] as SanPhamResponse[],
  gioHang: [] as SanPhamResponse[],
  paginationParams: { page: 1, size: 10 },
  totalItems: 0,
  currentPaymentMethod: '0' // Thêm trạng thái cho phương thức thanh toán đang chọn
})

const clickPhuongThuc = async (pt: string) => {
  try {
    state.currentPaymentMethod = pt; // Cập nhật phương thức thanh toán hiện tại
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('phuongThucThanhToan', pt);
    await themPTTT(formData)
    const response = await getPhuongThucThanhToan(idHDS.value);
    state.phuongThuThanhToan = response

    // Logic tính toán lại tiền thiếu, tiền khách thanh toán
    let totalPaid = 0;
    state.phuongThuThanhToan.forEach(item => {
      totalPaid += item.tongTien; // Giả sử item.tongTien là số tiền cho phương thức đó
    });
    tienKhachThanhToan.value = totalPaid;
    tienThieu.value = (state.tongTien?.tongTien || 0) - tienKhachThanhToan.value;
    soTien.value = state.tongTien?.tongTien; // Cập nhật số tiền tổng cần trả
  } catch (error) {
    console.error('Failed to add payment method:', error);
    toast.error('Thêm phương thức thanh toán thất bại!');
  }
}

// Thêm hàm xóa phương thức thanh toán
const deletePaymentMethod = async (idPTTT: string) => {
  try {
    // Gọi API để xóa phương thức thanh toán theo idPTTT
    // await xoaPTTT(idPTTT); // Bạn cần thêm API này vào banhang.api.ts
    // Sau khi xóa thành công, cập nhật lại danh sách phương thức
    state.phuongThuThanhToan = state.phuongThuThanhToan.filter(item => item.id !== idPTTT);

    // Cập nhật lại tổng tiền khách đã thanh toán và tiền thiếu
    let totalPaid = 0;
    state.phuongThuThanhToan.forEach(item => {
      totalPaid += item.tongTien;
    });
    tienKhachThanhToan.value = totalPaid;
    tienThieu.value = (state.tongTien?.tongTien || 0) - tienKhachThanhToan.value;

    toast.success('Xóa phương thức thanh toán thành công!');
  } catch (error) {
    console.error('Failed to delete payment method:', error);
    toast.error('Xóa phương thức thanh toán thất bại!');
  }
};


const clickkActiveTab = async (id: number, hd: string) => {
  idHDS.value = hd
  activeTab.value = id
  console.log("hd", hd)
  const response = await GetGioHang(hd);
  const responseKH = await GeOneKhachHang(hd);
  const idKH = responseKH.id
  if (idKH == null) {
    state.detailKhachHang = null;
  } else {
    state.detailKhachHang = responseKH;
  }
  state.gioHang = response;
  // Sau khi cập nhật giỏ hàng, gọi hàm tính toán tổng tiền
  calculateTotalAmounts();
}

const columnsKhachHang: TableColumnsType = [
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 80, align: 'center' },
  { title: 'Tên khách hàng', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
  { title: 'Số điện thoại', key: 'sdt', dataIndex: 'sdt', width: 150, align: 'center' },
  {
    title: 'Thao tác',
    key: 'operation',
    width: 90,
    align: 'center'
  }
]

const columnsThanhToan: TableColumnsType = [
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 80, align: 'center' },
  { title: 'Số tiền', key: 'tongTien', dataIndex: 'tongTien', width: 150, align: 'center' },
  { title: 'Phương thức', key: 'phuongThucThanhToan', dataIndex: 'phuongThucThanhToan', width: 150, align: 'center' },
  {
    title: 'Hành động',
    key: 'operation',
    width: 90,
    align: 'center'
  }
]

const columnsGiohang: TableColumnsType = [
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 80, align: 'center' },
  { title: 'Tên sản phẩm', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
  { title: 'Số lượng', key: 'soLuong', dataIndex: 'soLuong', width: 150, align: 'center' },
  { title: 'Giá bán', key: 'giaBan', dataIndex: 'giaBan', width: 150, align: 'center' },
  { title: 'Kích thước', key: 'kichThuoc', dataIndex: 'kichThuoc', width: 150, align: 'center' },
  { title: 'Màu sắc', key: 'mau', dataIndex: 'mau', width: 150, align: 'center' },
  { title: 'Ảnh', key: 'anh', dataIndex: 'anh', width: 150, align: 'center' },
  { title: 'tổng tiền', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
  {
    title: 'Thao tác',
    key: 'operation',
    width: 90,
    align: 'center'
  }
]

const columns: TableColumnsType = [
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 80, align: 'center' },
  { title: 'Tên sản phẩm', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
  { title: 'Số lượng', key: 'soLuong', dataIndex: 'soLuong', width: 150, align: 'center' },
  { title: 'Giá bán', key: 'giaBan', dataIndex: 'giaBan', width: 150, align: 'center' },
  { title: 'Kích thước', key: 'kichThuoc', dataIndex: 'kichThuoc', width: 150, align: 'center' },
  { title: 'Màu sắc', key: 'mau', dataIndex: 'mau', width: 150, align: 'center' },
  { title: 'Ảnh', key: 'anh', dataIndex: 'anh', width: 150, align: 'center' },
  { title: 'Trạng thái', key: 'status', dataIndex: 'status', width: 150, align: 'center' },
  {
    title: 'Hành động',
    key: 'operation',
    width: 90,
    align: 'center'
  }
]

const increaseQuantity = async (idSPS: any) => {
  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSPS);
    await themSL(formData);
    const updatedProduct = state.gioHang.find((item) => item.id === idSPS);
    if (updatedProduct) {
      updatedProduct.soLuong++;
      await calculateTotalAmounts();
    }
  } catch (error) {
    console.error('Failed to increase quantity:', error);
    toast.error('Tăng số lượng thất bại!');
  }
}

const xacNhan = async () => {
  try {
    console.log(idHDS.value)
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('tongTien', tongTien.value.toString()); // Chuyển số sang chuỗi

    const res = await thanhToanThanhCong(formData);
    fetchProducts();

    tongTien.value = 0;
    giamGia.value = 0;
    tienHang.value = 0;
    soTien.value = 0;
    tienKhachThanhToan.value = 0;
    tienThieu.value = 0;

    toast.success("Thanh toán thành công");

    // Delay 2 giây (2000ms) trước khi reload trang
    setTimeout(() => {
      location.reload(); // Reload trang
    }, 2000);

  } catch (error: any) { // Sử dụng 'any' để truy cập thuộc tính 'response'
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      toast.error('Có lỗi xảy ra khi xác nhận thanh toán!');
    }
  }
}

const sumitThanhToan = async () => {
  state.isModaThanhToanlOpen = false;
  tienHang.value = state.tongTien?.tongTien || 0; // Đảm bảo giá trị không null/undefined
  tongTien.value = tienHang.value - giamGia.value;
}

const decreaseQuantity = async (idSPS: any) => {
  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSPS);
    await xoaSL(formData);
    const updatedProduct = state.gioHang.find((item) => item.id === idSPS);
    if (updatedProduct && updatedProduct.soLuong > 1) {
      updatedProduct.soLuong--;
      await calculateTotalAmounts();
    } else {
      state.gioHang = state.gioHang.filter((item) => item.id !== idSPS);
      await calculateTotalAmounts();
    }
  } catch (error) {
    console.error('Failed to decrease quantity:', error);
    toast.error('Giảm số lượng thất bại!');
  }
}

// Hàm tạo hóa đơn
async function createInvoice() {
  if (tabs.value.length >= 5) {
    toast.warning('Chỉ được tạo tối đa 5 hóa đơn!', { autoClose: 3000 })
    return
  }
  try {
    const newInvoice = await getCreateHoaDon(); // Lấy thông tin hóa đơn mới từ API
    tabs.value.push({ id: nextTabId++, idHD: newInvoice.id, products: [] }); // Thêm idHD vào tab
    activeTab.value = tabs.value[tabs.value.length - 1].id;
    idHDS.value = newInvoice.id; // Cập nhật idHDS cho tab mới
    state.gioHang = []; // Đặt giỏ hàng trống cho hóa đơn mới
    state.detailKhachHang = null; // Đặt khách hàng về null cho hóa đơn mới
    calculateTotalAmounts(); // Tính toán lại tổng tiền

    toast.success("Tạo hóa đơn thành công!");
  } catch (error) {
    console.error("Failed to create invoice:", error);
    toast.error("Tạo hóa đơn thất bại!");
  }
}


const closeModal = () => {
  state.isModalOpen = false
}

const closeModalThanhToan = () => {
  state.isModaThanhToanlOpen = false
}

const showKhachHangModal = ref(false)
const showProductModal = ref(false)

// Sản phẩm đã chọn
const selectedProduct = ref({ soLuong: 0 });
const quantity = ref(1) // có vẻ không dùng đến quantity nữa vì dùng selectedProduct.soLuong

// Chọn sản phẩm (cho modal)
const selectThanhToan = async () => {
  if (!idHDS.value) {
    toast.error("Vui lòng chọn hoặc tạo hóa đơn trước khi thanh toán!");
    return;
  }
  const response = await getThanhToan(idHDS.value)
  state.tongTien = response
  soTien.value = state.tongTien?.tongTien || 0; // Đảm bảo giá trị không null/undefined
  tienThieu.value = state.tongTien?.tongTien || 0; // Đảm bảo giá trị không null/undefined
  state.isModaThanhToanlOpen = true
}

// Chọn sản phẩm (từ danh sách sản phẩm cho vào giỏ hàng)
function selectProduct(idSPS: any) {
  idSP.value = idSPS
  selectedProduct.value.soLuong = 1; // Đặt số lượng mặc định là 1 khi chọn sản phẩm
  state.isModalOpen = true
  showProductModal.value = false
}

const selectKhachHang = async (getIdKH: any) => {
  const formData = new FormData();
  formData.append('idHD', idHDS.value);
  formData.append('idKH', getIdKH);
  await themKhachHang(formData);
  const responseKH = await GeOneKhachHang(idHDS.value);
  state.detailKhachHang = responseKH;
  showKhachHangModal.value = false
  toast.success("Chọn khách hàng thành công!");
}

const deleteProduc = async (idSPS: any) => {
  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSPS);

    await xoaSP(formData);

    state.gioHang = state.gioHang.filter((item) => item.id !== idSPS);
    calculateTotalAmounts(); // Tính toán lại tổng tiền sau khi xóa SP
    toast.success('Xóa sản phẩm thành công!');
  } catch (error) {
    console.error('Failed to delete product:', error);
    toast.error('Xóa sản phẩm thất bại!');
  }
}

const fetchProducts = async () => {
  try {
    const params: ParamsGetSanPham = {
      page: state.paginationParams.page,
      size: state.paginationParams.size,
      q: state.searchQuery,
      status: state.searchStatus
    }
    const responseKhachHang = await GetKhachHang()
    const response = await GetSanPhams(params)
    state.products = response.data?.data
    state.khachHang = responseKhachHang
    state.totalItems = response.data?.totalElements

  } catch (error) {
    console.error('Failed to fetch products:', error)
  }
}

const confirmQuantity = async () => {
  try {
    if (selectedProduct.value.soLuong <= 0) {
      toast.error("Số lượng không hợp lệ!");
      return;
    }

    const formData = new FormData();
    formData.append('soLuong', selectedProduct.value.soLuong.toString());
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSP.value);

    const res = await themSanPham(formData);
    state.isModalOpen = false
    toast.success(res.message);
    const response = await GetGioHang(idHDS.value);
    state.gioHang = response;
    calculateTotalAmounts(); // Tính toán lại tổng tiền sau khi thêm SP
    fetchProducts() // Cập nhật lại danh sách sản phẩm (để hiển thị số lượng tồn kho đúng)
  } catch (error: any) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      toast.error('Thêm sản phẩm vào giỏ hàng thất bại!');
    }
  }
}

// Hàm tính toán lại tổng tiền hàng, giảm giá, tổng tiền cuối cùng
const calculateTotalAmounts = () => {
  tienHang.value = state.gioHang.reduce((sum, item) => sum + (item.giaBan * item.soLuong), 0);
  // Giảm giá cần được tính toán dựa trên logic của bạn (ví dụ: voucher, khuyến mãi)
  // Hiện tại giamGia đang là ref(0)
  tongTien.value = tienHang.value - giamGia.value;
};

// Gọi hàm tính toán khi giỏ hàng thay đổi
watch(() => state.gioHang, calculateTotalAmounts, { deep: true });


onMounted(async () => {
  const response = await GetHoaDons();
  fetchProducts();

  if (response && Array.isArray(response)) {
    tabs.value = response.map((invoice, index) => ({
      id: index + 1,
      idHD: invoice.id,
      ma: invoice.ma,
      soLuong: invoice.soLuong,
      products: invoice.data?.products || []
    }));

    activeTab.value = null;

    if (tabs.value.length > 0) {
      // Set the active tab to the first one or a default
      activeTab.value = tabs.value[0].id;
      idHDS.value = tabs.value[0].idHD; // Gán idHDS ban đầu

      const initialResponse = await GetGioHang(tabs.value[0]?.idHD);
      state.gioHang = initialResponse; // Cập nhật đúng ở đây
      // Tính toán tổng tiền ban đầu
      calculateTotalAmounts();

      const responseKH = await GeOneKhachHang(idHDS.value);
      const idKH = responseKH.id
      if (idKH == null) {
        state.detailKhachHang = null;
      } else {
        state.detailKhachHang = responseKH;
      }
    }
  }
})

</script>

<style scoped>
.main-layout {
  display: flex;
  gap: 20px;
  padding: 20px;

  background-color: #f0f2f5;
  width: 100%;
}

/* Card Styling */
.card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}


/* Top Header */
.top-header {
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-and-create-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-box-top {
  display: flex;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  overflow: hidden;
  width: 60%;
}

.search-input-top {
  border: none;
  padding: 8px 12px;
  flex-grow: 1;
  outline: none;
}

.search-icon-top {
  background-color: #fff;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-create-new-invoice {
  background-color: #54bddb;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Pending Invoices */
.pending-invoices-container {
  display: flex;
  gap: 15px;
  padding: 15px 20px;
  overflow-x: auto;
  white-space: nowrap;
}

.pending-invoice-card {
  flex-shrink: 0;
  width: 180px;
  /* Adjust width as needed */
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  gap: 5px;
  cursor: pointer;
  transition: all 0.2s;
  position: relative;
  background-color: #fdfdfd;
}

.pending-invoice-card.active {
  border-color: #54bddb;
  box-shadow: 0 0 0 2px rgba(64, 150, 255, 0.2);
}

.pending-invoice-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.invoice-id {
  font-weight: bold;
  color: #333;
}

.invoice-status {
  background-color: #ffe7ba;
  color: #fa8c16;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  width: fit-content;
}

.invoice-product-count {
  font-size: 14px;
  color: #666;
}

.delete-invoice-btn {
  position: absolute;
  top: 8px;
  right: 8px;
  background: none;
  border: none;
  color: #999;
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  transition: background-color 0.2s;
}

.delete-invoice-btn:hover {
  background-color: #f0f0f0;
  color: #e50000;
}

/* Search Box (General) */
.search-box {
  display: flex;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  overflow: hidden;
  width: 250px;
}

.search-input {
  border: none;
  padding: 8px 12px;
  flex-grow: 1;
  outline: none;
}

.search-icon {
  background-color: #fff;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Cart Actions */
.actions-add-product {
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 15px;
}



.empty-cart {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 200px;
  color: #999;
}

.empty-cart img {
  margin-bottom: 10px;
}

/* Quantity Controls in Cart */
.center-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.quantity-btn {
  background-color: #f0f0f0;
  border: 1px solid #d9d9d9;
  padding: 4px 8px;
  cursor: pointer;
  font-weight: bold;
}

.quantity-input {
  width: 40px;
  text-align: center;
  border: 1px solid #d9d9d9;
  border-left: none;
  border-right: none;
  padding: 4px 0;
  -moz-appearance: textfield;
  /* Firefox */
  appearance: textfield;
}

.quantity-input::-webkit-outer-spin-button,
.quantity-input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Customer Search */
.search-customer {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  align-items: center;
  width: 100%;
}

.search-input-customer {
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding: 8px 12px;
  flex-grow: 1;
  outline: none;
}

.search-icon-customer {
  background-color: #fff;
  border: 1px solid #d9d9d9;
  border-left: none;
  padding: 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 4px 4px 0;
}

.btn-add-customer {
  background-color: #54bddb;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  white-space: nowrap;
}

.customer-info-display {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.customer-info-display h4 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
}

.info-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
}

.info-group span {
  font-weight: bold;
  color: #555;
  margin-bottom: 5px;
}

.input-full-width,
textarea.input-full-width {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  outline: none;
  resize: vertical;
}

/* Payment Section */
.payment-section-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group-payment label {
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
}

.select-discount {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background-color: #fff;
}

.payment-summary {
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  font-size: 16px;
}

.summary-item label {
  font-weight: normal;
  color: #555;
}

.summary-item span {
  font-weight: bold;
  color: #333;
}

.total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #000;
  border-top: 1px dashed #e0e0e0;
  padding-top: 10px;
  margin-top: 5px;
}

.btn-confirm-payment {
  background-color: #54bddb;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 12px 20px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 15px;
}

/* Modals */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  max-width: 800px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-title {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 20px;
  color: #333;
  text-align: center;
}

/* Payment Modal Specific Styles */
.payment-modal-content {
  padding: 10px;
}

.payment-info {
  margin-bottom: 20px;
  font-size: 18px;
  text-align: center;
}

.tabs-payment-method {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.tab-button-payment {
  background-color: #f0f2f5;
  border: 1px solid #d9d9d9;
  border-bottom: none;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 16px;
  border-top-left-radius: 6px;
  border-top-right-radius: 6px;
  margin-bottom: -1px;
  /* Overlap border */
}

.tab-button-payment.active {
  background-color: #fff;
  border-color: #54bddb;
  color: #54bddb;
  border-bottom-color: #fff;
}

.payment-method-summary {
  text-align: center;
  margin-bottom: 20px;
  font-size: 16px;
  padding: 10px;
  background-color: #fffbe6;
  border: 1px solid #ffe58f;
  border-radius: 4px;
}

.payment-details-table {
  margin-bottom: 20px;
}

.footer-payment-summary {
  border-top: 1px solid #f0f0f0;
  padding-top: 15px;
  text-align: right;
  font-size: 16px;
}

.footer-info div {
  margin-bottom: 5px;
}

.top-header {

  border-radius: 5px;
  background-color: #54bddb;
  height: 60PX;
  display: flex;
  justify-content: flex-end;
  /* Căn phải nội dung bên trong top-header */
  margin-bottom: 20px;
}

.search-and-create-section {

  display: flex;
  align-items: center;
  gap: 15px;
  /* Khoảng cách giữa ô tìm kiếm và nút */
  width: 100%;
  /* Đảm bảo nó chiếm đủ không gian để căn chỉnh */
  justify-content: flex-end;
  /* Căn phải các item bên trong */
}


.search-box-top {
  display: flex;
  align-items: center;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  margin-left: 15px;
  /* Bo tròn các góc */
  padding: 5px 10px;
  height: 30px;
  width: 100%;
  background-color: #fff;
  flex-grow: 1;
  /* Cho phép nó mở rộng để chiếm không gian còn lại */
  max-width: 1420px;
  /* Giới hạn độ rộng tối đa */
}

.search-input-top {
  width: 100%;
  /* Đảm bảo ô input chiếm toàn bộ chiều rộng của search-box */
  border: none;
  outline: none;
  height: 30px;
  flex-grow: 1;

  padding: 5px;
  font-size: 14px;
  /* Điều chỉnh kích thước font */
}

.search-icon-top {
  background-color: white;
  background: none;
  border: none;
  cursor: pointer;
  color: #8c8c8c;
  padding-left: 5px;
}

.btn-create-new-invoice {
  color: black;
  background-color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  height: 30px;
  align-items: center;
  gap: 5px;
  margin-right: 30px;
  white-space: nowrap;
}

/* Header trên cùng */
.top-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}


.search-icon-top {
  background: none;
  border: none;
  cursor: pointer;
  color: #8c8c8c;
  padding-left: 5px;
}


.left-column {
  flex: 1;
  width: 70%;
}

.right-column {
  flex: 2;
  width: 100%;
  /* Mỗi cột chiếm 1 phần bằng nhau */
}

/* Card chung */
.card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.card-header {
  padding: 15px 20px;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
}

/* Tab info (chỉ có trong card "Hóa đơn chờ") */
.tab-info {
  display: flex;
  gap: 10px;
}

.tab-item {
  background-color: #e6f7ff;
  /* Light blue */
  color: #54bddb;
  /* Blue text */
  padding: 5px 10px;
  border-radius: 4px;
  font-size: 12px;
}

.tab-item.active {
  background-color: #54bddb;
  color: white;
}

/* Tabs cho hóa đơn */
.tabs-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px 0;
  /* Chỉ padding trên và ngang */
  border-bottom: 1px solid #f0f0f0;
}

.tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  /* Đảm bảo các tab xuống dòng nếu quá nhiều */
}

.tab-button {
  text-align: center;
  padding: 8px 15px;
  /* Tăng padding để nút to hơn */
  background: #f0f0f0;
  /* Màu nền xám nhạt */
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  color: #595959;
  transition: all 0.3s ease;
}

.tab-button.active {
  background: #54bddb;
  color: white;
  box-shadow: 0 2px 0 rgba(45, 140, 240, 0.1);
}

.btn-close-tab {
  background: none;
  border: none;
  cursor: pointer;
  color: #8c8c8c;
  font-size: 18px;
  padding: 5px;
}

/* Nội dung hiển thị tab (giỏ hàng) */
.tab-content-display {
  padding: 20px;
}

.actions-add-product {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 15px;
}

.btn-add-product {
  background-color: #54bddb;
  color: white;
  border: none;
  padding: 8px 15px;
  border-radius: 4px;
  cursor: pointer;
  height: 40px;
  width: 120px;


}


.empty-cart {
  text-align: center;
  color: #777;
  padding: 50px 0;
}

/* Ant Design Table Overrides */
:deep(.ant-table-wrapper) {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
  /* Ensure rounded corners */
}

:deep(.ant-table-thead > tr > th) {
  background-color: #fafafa !important;
  font-weight: bold;
  text-align: center !important;
}

:deep(.ant-table-tbody > tr > td) {
  text-align: center;
}

.center-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.quantity-btn {
  padding: 4px 10px;
  /* Nhỏ hơn một chút */
  background-color: #54bddb;
  color: white;
  border: none;
  font-size: 16px;
  /* Nhỏ hơn một chút */
  cursor: pointer;
  border-radius: 4px;
  margin: 0 2px;
}

.quantity-input {
  width: 40px;
  /* Nhỏ hơn */
  text-align: center;
  font-size: 16px;
  border: 1px solid #d9d9d9;
  padding: 4px;
  border-radius: 4px;
  margin: 0 5px;
}

.highlight {
  color: red;
  font-weight: bold;
}

.btn-choose-product,
.btn-delete-product {
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 14px;
}

/* Phần "Giỏ hàng" (dưới "Hóa đơn chờ") */
.search-box {
  display: flex;
  align-items: center;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding: 5px 10px;
  background-color: #fff;
  width: 250px;
  /* Điều chỉnh độ rộng */
}

.search-input {
  border: none;
  outline: none;
  flex-grow: 1;
  padding: 3px;
}

.search-icon {
  background: none;
  border: none;
  cursor: pointer;
  color: #8c8c8c;
  padding-left: 5px;
}

/* Phần "Khách hàng" (cột phải) */
.search-customer {
  display: flex;
  align-items: center;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  padding: 5px 10px;
  background-color: #fff;
  width: 100%;
  /* Chiếm toàn bộ chiều rộng */
  margin-bottom: 10px;
}

.search-input-customer {
  border: none;
  outline: none;
  flex-grow: 1;
  padding: 3px;
}

.search-icon-customer {
  background: none;
  border: none;
  cursor: pointer;
  color: #8c8c8c;
  padding-left: 5px;
}

.btn-add-customer {
  background-color: #54bddb;
  color: white;
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 14px;
  margin-bottom: 20px;
}

.customer-info-display h4 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.info-group {
  margin-bottom: 15px;
}

.info-group span {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

.input-full-width {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  box-sizing: border-box;
  /* Đảm bảo padding không làm tăng chiều rộng */
}

textarea.input-full-width {
  min-height: 60px;
  resize: vertical;
}

/* Phần "Thanh toán" (cột phải) */
.payment-section-content {
  padding-top: 20px;
}

.form-group-payment {
  margin-bottom: 15px;
}

.form-group-payment label {
  font-weight: bold;
  display: block;
  margin-bottom: 5px;
}

.select-discount {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  background-color: #fff;
}

.payment-summary {
  margin-top: 20px;
  border-top: 1px dashed #e0e0e0;
  /* Đường nét đứt */
  padding-top: 15px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 15px;
}

.summary-item label {
  font-weight: normal;
  color: #595959;
}

.summary-item span {
  font-weight: bold;
}

.total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #ff4d4f;
  /* Màu đỏ nổi bật */
  border-top: 1px solid #f0f0f0;
  padding-top: 10px;
  margin-top: 15px;
}

.btn-confirm-payment {
  background-color: #54bddb;
  /* Màu xanh Ant Design */
  color: white;
  padding: 12px 20px;
  border: none;
  border-radius: 8px;
  /* Bo tròn nhiều hơn */
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  width: 100%;
  /* Chiếm toàn bộ chiều rộng */
  margin-top: 20px;
  transition: background-color 0.3s;
}

.btn-confirm-payment:hover {
  background-color: #54bddb;
}

/* Modal Thanh toán */
.tabs-payment-method {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.tab-button-payment {
  flex: 1;
  /* Chia đều không gian */
  background-color: #f0f0f0;
  /* Màu nền xám nhạt */
  color: #595959;
  padding: 10px 15px;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  /* Bo tròn góc */
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-button-payment.active {
  background-color: #54bddb;
  /* Màu xanh Ant Design khi active */
  color: white;
  border-color: #007bff;
}

.tab-button-payment:hover:not(.active) {
  background-color: #e6f7ff;
  /* Light blue on hover for non-active */
}


.payment-method-summary {
  margin-bottom: 15px;
  text-align: center;
}

.payment-details-table {
  margin-bottom: 20px;
}

.footer-payment-summary {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.footer-info div {
  margin-bottom: 5px;
  font-size: 15px;
}

.footer-info strong {
  font-weight: bold;
}

/* Your existing styles (like .modal-backdrop, .modal-content, etc.) should be kept if they are general and work well. */
/* Just make sure there are no conflicting styles */

.khach-hang-cart {
  color: black;
}

/* Các style ban đầu của bạn, giữ lại và có thể điều chỉnh nếu cần */
.btn-create {
  background-color: #54bddb;
  color: white;
  padding: 8px 14px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}


.required {
  color: red;
}

.input-wrapper {
  position: relative;
  height: 30px;
}

.input-wrapper .icon {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #888;
}

.input-wrapper input {
  width: 100%;
  padding: 10px 10px 10px 35px;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #f2f2f2;
}

.add-button {
  width: 100%;
  padding: 12px;
  background-color: #54bddb;
  color: white;
  border: none;
  border-radius: 6px;
  margin-top: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

.add-button:hover {
  background-color: #54bddb;
}

/* Trong phần CSS của bạn */

/* Bổ sung cho giao diện mềm mại hơn (có thể điều chỉnh thêm) */
.card {
  border-radius: 12px;
  /* Tăng bo góc cho card */
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  /* Bóng đổ mềm hơn */
}

.input-wrapper input,
.select-discount,
.quantity-input {
  border-radius: 8px;
  /* Bo góc cho input và select */
}

.quantity-btn {
  border-radius: 6px;
  /* Bo góc cho nút tăng/giảm số lượng */
}

/* CSS cho nhóm nút khách hàng */
.button-group-customer {
  display: flex;
  /* Biến container thành flex container */
  gap: 10px;
  /* Khoảng cách giữa các nút */
  margin-top: 16px;
  /* Giữ khoảng cách trên như ban đầu của add-button */
  padding: 0 20px 20px;
  /* Thêm padding ngang và dưới để căn chỉnh với body card */
  /* Nếu bạn muốn button-group-customer nằm trong card-body, hãy di chuyển nó vào trong đó */
  /* Hiện tại tôi đang giả định nó nằm ngay sau div đóng của detailKhachHang/v-else */
}

.button-group-customer .add-button {
  flex: 1;
  /* Mỗi nút sẽ chiếm một phần bằng nhau trong không gian khả dụng */
  width: auto;
  /* Ghi đè width: 100% để flex hoạt động */
  padding: 12px;
  /* Giữ padding như cũ */
  font-size: 15px;
  /* Điều chỉnh cỡ chữ cho vừa phải */
  border-radius: 8px;
  /* Bo góc cho nút */
  margin-top: 0;
  /* Bỏ margin-top ở đây vì đã có trên button-group-customer */
}


.form-group {

  width: 380px;
  display: block;
  justify-self: center;
  margin-top: 10px;
  margin-bottom: 30px;
}

/* Đảm bảo .card-body chứa được button-group-customer nếu bạn muốn nó nằm trong padding của card-body */
.card-body {
  padding: 20px;
  /* Đã có */
}

.input-customer {
  margin-top: 5px;
  background-color: white;
  height: 40px;
}

.page-container {
  padding: 20px;
  /* Overall padding for the page content */
}

.breadcrumb-section { 
  margin-bottom: 25px;
  /* Space below the breadcrumb and above the first section */
  background-color: #fff;
  /* White background for the breadcrumb box */
  padding: 15px 20px;
  /* Padding inside the breadcrumb box */
  border-radius: 8px;
  /* Rounded corners */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  /* Subtle shadow */
}

.section-title {
  margin-top: 30px;
  /* Space above each main section title */
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  /* Space below the title */
  margin-left: 0px;
  /* Remove left margin if section-title is directly under padding */
  color: #333;
  /* Darker color for titles */
  display: flex;
  /* To align icon and text */
  align-items: center;
  /* Vertically center icon and text */
  gap: 8px;
  /* Space between icon and text */
}

/* Remove or adjust body styles if they are global.
   Scoped styles prevent them from affecting the entire app. */
body {
  font-family: 'Roboto', sans-serif;
}

</style>