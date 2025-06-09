<template>
  <div class="container">
    <!-- Toastify auto hiển thị -->

    <!-- Nút tạo hóa đơn -->
    <div class="header">
      <button class="btn-create" @click="createInvoice">+ Tạo hóa đơn</button>
    </div>

    <!-- Tabs -->
    <div class="tabs">
      <button v-for="(tab, index) in tabs" :key="tab.id" :class="['tab-button', { active: activeTab === tab.id }]"
        @click="clickkActiveTab(tab.id, tab.idHD)">
        Hóa đơn {{ index + 1 }}
      </button>
    </div>

    <!-- Nội dung tab -->
    <div class="tab-content" v-if="activeTab">
      <div class="actions">
        <button class="btn-add" @click="showProductModal = true">Thêm sản phẩm</button>
      </div>
      <div v-if="state.gioHang.length > 0">
        <a-table :columns="columnsGiohang" :data-source="state.gioHang" :scroll="{ y: 240 }">
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
                <a-tooltip title="chọn sản phẩm">
                  <a-button type="primary" @click="deleteProduc(record.id)"
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
        <img src="https://cdn-icons-png.flaticon.com/512/2038/2038854.png" alt="empty" width="80" />
        <p>Không có sản phẩm nào trong giỏ</p>
      </div>
    </div>


    <div class="tab-content">
      <h3><b>Tài khoản</b></h3>
      <div class="actions">
        <button class="btn-add" @click="showKhachHangModal = true">Chọn tài khoản</button>
      </div>
      <hr>

      <div v-if="state.detailKhachHang">
        <span><strong>Tên khách hàng:</strong> {{ state.detailKhachHang.ten }}</span>
        <br>
        <span><strong>Số điện thoại khách hàng:</strong> {{ state.detailKhachHang.sdt }}</span>
      </div>
      <div v-else>
        <div class="khach-hang-cart">
          <span><strong>Tên khách hàng:</strong> Khách lẻ</span>
        </div>
      </div>
    </div>
    <br>
    <br>
    <br>
    <div class="tab-content-thanh-toan">
      <h3><b>Khách hàng</b></h3>
      <hr>
      <div class="row">
        <div class="column-left">

        </div>

        <div class="column-right">
          <div class="form-group">
            <label for="payment-method">Khách thanh toán</label>
            <button id="payment-method" @click="selectThanhToan" style="margin-left: 30px;" class="btn-payment">
              <CreditCardOutlined />
            </button>
            <span style="margin-left: 30px;">{{ tienHang }} VND</span>
          </div>
          <div class="form-group">
            <label for="price"><b>Tiền hàng: </b></label>
            <span style="margin-left: 30px;">{{ tienHang }} VND</span>
          </div>
          <div class="form-group">
            <label for="discount"><b>Giảm giá: </b></label>
            <span style="margin-left: 30px;">{{ giamGia }} VND</span>
          </div>
          <div class="form-group">
            <label for="total"><b>Tổng tiền: </b></label>
            <span style="color: red; margin-left: 30px;">{{ tongTien }} VND</span>
          </div>
          <button class="btn-confirm" @click="xacNhan">Xác nhận thanh toán</button>
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
                {{ state.products.indexOf(record) + 1 }}
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

        <!-- Các tab: Tiền mặt, Chuyển khoản, Thẻ -->
        <div class="tabs">
          <button @click="clickPhuongThuc('0')" :class="'tab-button1'">
            Tiền mặt
          </button>
          <button @click="clickPhuongThuc('1')" :class="'tab-button1'">
            Chuyển khoản
          </button>
          <button @click="clickPhuongThuc('2')" :class="'tab-button1'">
            Thẻ
          </button>
        </div>

        <div class="payment-method" style="color: red; font-weight: bold; font-size: 15px;">Khách cần trả: {{
          state.tongTien?.tongTien }} VND</div>

        <div class="payment-details">
          <a-table :columns="columnsThanhToan" :data-source="state.phuongThuThanhToan" :scroll="{ y: 240 }">
            <template #bodyCell="{ column, record }">

              <div v-if="column.key === 'stt'">
                {{ state.products.indexOf(record) + 1 }}
              </div>

              <template v-if="column.key === 'status'">

                <a-tag v-if="record.status == 'TIEN_MAT'" :color="'green'">
                  Tiền Mặt
                </a-tag>
                <a-tag v-if="record.status == 'CHUYEN_KHOAN'" :color="'green'">
                  Chuyển khoản
                </a-tag>
                <a-tag v-if="record.status == 'THE'" :color="'green'">
                  Thẻ
                </a-tag>
              </template>

              <template v-if="column.key === 'operation'">
                <div class="center-cell">
                  <div class="d-flex gap-1 justify-center">
                    <a-tooltip title="chọn khách hàng">
                      <a-button type="primary" @click="selectKhachHang(record.id)"
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

        <div class="footer-payment">
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
import { ref, onMounted, reactive } from 'vue'
import { toast } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'
import { GetHoaDons, getCreateHoaDon, themSanPham, GetGioHang, xoaSP, type ParamsXoaSP, themSL, xoaSL, type KhachHangResponse, GetKhachHang, themKhachHang, GeOneKhachHang, type thanhToanResponse, getThanhToan, type tongTienResponse, getPhuongThucThanhToan, type PhuongThucThanhToanResponse, themPTTT, thanhToanThanhCong } from '@/services/api/admin/banhang.api'
import type { TableColumnsType } from 'ant-design-vue'
import { GetSanPhams, type ParamsGetSanPham, type SanPhamResponse } from '@/services/api/admin/sanphamchitiet.api'
import { CreditCardOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const soTien = ref(0)
const tienKhachThanhToan = ref(0)
const tienThieu = ref(0)
const tongTien = ref(0)
const giamGia = ref(0)
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
  totalItems: 0
})

const clickPhuongThuc = async (pt: string) => {

  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('phuongThucThanhToan', pt);
    await themPTTT(formData)
    const response = await getPhuongThucThanhToan(idHDS.value);
    state.phuongThuThanhToan = response

    tienThieu.value = 0;
    soTien.value = 0
    tienKhachThanhToan.value = state.tongTien?.tongTien

  } catch (error) {
    console.error('Failed to delete product:', error);
    toast.error('Xóa sản phẩm thất bại!');
  }
}


const clickkActiveTab = async (id: number, hd: string) => {

  idHDS.value = hd
  activeTab.value = id
  console.log("hd",hd)
  const response = await GetGioHang(hd);
  const responseKH = await GeOneKhachHang(hd);
  const idKH = responseKH.id
  if (idKH == null) {
    state.detailKhachHang = null;
  } else {
    state.detailKhachHang = responseKH;

  }
  state.gioHang = response;

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


const increaseQuantity = async (idSPS: any, soLuong: Number) => {

  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSPS);
    await themSL(formData);
    const updatedProduct = state.gioHang.find((item) => item.id === idSPS);
    if (updatedProduct) {
      updatedProduct.soLuong++;
    }
  } catch (error) {
    console.error('Failed to delete product:', error);
    toast.error('Xóa sản phẩm thất bại!');
  }
}



const xacNhan = async () => {

  try {

    console.log(idHDS.value)
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('tongTien', tongTien.value);

  const res = await thanhToanThanhCong(formData);
  fetchProducts();

  tongTien.value = 0;
  giamGia.value = 0;
  tienHang.value = 0;
  soTien.value = 0;
  tienKhachThanhToan.value = 0;
  tienThieu.value = 0;

  // Delay 2 giây (2000ms) trước khi reload trang
  setTimeout(() => {
    location.reload(); // Reload trang
  }, 2000);

  toast.success("Thanh toán thành công");
} catch (error) {
  if (error?.response?.data?.message) {
    toast.error(error?.response?.data?.message);
  }
}


}

const sumitThanhToan = async () => {

  state.isModaThanhToanlOpen = false;

  tienHang.value = state.tongTien?.tongTien

  tongTien.value = tienHang.value - giamGia.value


}

const decreaseQuantity = async (idSPS: any, soLuong: Number) => {

  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSPS);
    await xoaSL(formData);
    fetchProducts()
    const updatedProduct = state.gioHang.find((item) => item.id === idSPS);
    if (updatedProduct && updatedProduct.soLuong > 1) {
      updatedProduct.soLuong--;
    } else {
      state.gioHang = state.gioHang.filter((item) => item.id !== idSPS);
    }

  } catch (error) {
    console.error('Failed to delete product:', error);
    toast.error('Xóa sản phẩm thất bại!');
  }

  if (record.soLuong > 1) {
    record.soLuong--
  }
}

// Hàm tạo hóa đơn
function createInvoice() {

  if (tabs.value.length >= 5) {
    toast.warning('Chỉ được tạo tối đa 5 hóa đơn!', { autoClose: 3000 })
    return
  }
  getCreateHoaDon()
  tabs.value.push({ id: nextTabId++, products: [] })
  activeTab.value = tabs.value[tabs.value.length - 1].id
  // saveDataToLocalStorage()
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
const quantity = ref(1)

// Chọn sản phẩm
const selectThanhToan = async () => {

  const response = await getThanhToan(idHDS.value)


  state.tongTien = response

  soTien.value = state.tongTien?.tongTien

  tienThieu.value = state.tongTien?.tongTien

  state.isModaThanhToanlOpen = true

}

// Chọn sản phẩm
function selectProduct(idSPS: any) {
  idSP.value = idSPS
  quantity.value = 1
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
}

const deleteProduc = async (idSPS: any) => {
  try {
    const formData = new FormData();
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSPS);

    await xoaSP(formData);

    state.gioHang = state.gioHang.filter((item) => item.id !== idSPS);
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
    const formData = new FormData();
    formData.append('soLuong', selectedProduct.value.soLuong.toString());
    formData.append('idHD', idHDS.value);
    formData.append('idSP', idSP.value);

    const res = await themSanPham(formData);
    state.isModalOpen = false
    toast.success(res.message);
    const response = await GetGioHang(idHDS.value);
    state.gioHang = response;
    fetchProducts()
  } catch (error) {

  }


}

onMounted(async () => {
  const response = await GetHoaDons();
  fetchProducts();

  if (response && Array.isArray(response)) {
    tabs.value = response.map((invoice, index) => ({
      id: index + 1,
      idHD: invoice.id,
      products: invoice.data?.products || []
    }));

    activeTab.value = null;

    if (tabs.value.length > 0) {
      const initialResponse = await GetGioHang(tabs.value[0]?.idHD);
      state.gioHang = initialResponse.data?.data || [];
    }
  }
})

</script>

<style scoped>
.khach-hang-cart {
  color: black;
}

.center-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.header {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.btn-create {
  background-color: #2d8cf0;
  color: white;
  padding: 8px 14px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 10px;
}

.tab-button {
  text-align: center;
  padding-bottom: 5px;
  padding: 6px 12px;
  background: #eee;
  border: none;
  border-radius: 15px;
  cursor: pointer;
}

.tab-button.active {
  background: #2d8cf0;
  color: white;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.actions button {
  background-color: #2d8cf0;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.empty-cart {
  text-align: center;
  color: #777;
}



/* Modal dùng chung */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 90%;
  max-width: 900px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
  overflow: auto;
  max-height: 90vh;
}

.modal-title {
  margin-bottom: 15px;
  font-weight: bold;
  font-size: 18px;
}

.product-table,
.selected-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
  margin-top: 10px;
}

.product-table th,
.product-table td,
.selected-table th,
.selected-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

.color-box {
  display: inline-block;
  width: 16px;
  height: 16px;
  border-radius: 4px;
}

.status {
  background-color: #d4edda;
  color: #155724;
  padding: 3px 8px;
  border-radius: 5px;
  font-size: 12px;
}

.btn-choose {
  background-color: orange;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.price {
  color: red;
  font-weight: bold;
}

.highlight {
  color: red;
}

.quantity-btn {
  padding: 5px 10px;
  background-color: #2d8cf0;
  color: white;
  border: none;
  font-size: 18px;
  cursor: pointer;
  border-radius: 4px;
}

.quantity-input {
  width: 60px;
  text-align: center;
  font-size: 18px;
  border: 1px solid #ccc;
  padding: 5px;
  border-radius: 4px;
  margin: 0 5px;
}


.tab-content-thanh-toan {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.row {
  display: flex;
  justify-content: space-between;
}

.column {
  width: 100%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  font-weight: bold;
}

.form-group span {
  font-size: 16px;
  color: #333;
}

.btn-payment {
  border: 1px solid #777;
  /* Thêm viền màu đen */
  border-radius: 5px;
  /* Tạo góc bo tròn */
  padding: 8px 16px;
  /* Điều chỉnh kích thước nút */
  background-color: transparent;
  /* Nền trong suốt */
  color: #000;
  /* Màu chữ */
  cursor: pointer;
  width: 50px;
  height: 40px;
}

.btn-confirm {
  background-color: #132414;
  color: white;
  padding: 8px 16px;
  font-size: 11px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 20px;
  width: 150px;
}

hr {
  margin: 20px 0;
}

.column {
  display: flex;
  flex-direction: column;
  gap: 10px;
}


.tab-content-thanh-toan {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.row {
  display: flex;
  justify-content: space-between;
}

.column-left,
.column-right {
  width: 48%;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  font-weight: bold;
}

.form-group span {
  font-size: 16px;
  color: #333;
}

.btn-payment {
  border: 1px solid #777;
  border-radius: 5px;
  padding: 8px 16px;
  background-color: transparent;
  color: #000;
  cursor: pointer;
  width: 50px;
  height: 40px;
}

.btn-confirm {
  background-color: #132414;
  color: white;
  padding: 8px 16px;
  font-size: 11px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
  margin-top: 20px;
  width: 150px;
}

hr {
  margin: 20px 0;
}

.column-left,
.column-right {
  display: flex;
  flex-direction: column;
  gap: 10px;
}


.payment-modal-content {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.payment-info {
  font-size: 16px;
  margin-bottom: 20px;
}

.tabs {
  margin-bottom: 20px;
}

.payment-method {
  font-size: 14px;
  margin-top: 10px;
}

.payment-details {
  margin-top: 20px;
}

.footer-payment {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.footer-info {
  font-size: 16px;
}

.footer-buttons {
  display: flex;
  gap: 10px;
}

a-button1 {
  width: 120px;
}

.tabs {
  display: flex;
  gap: 10px;
}

.tab-button1 {

  background-color: #007bff;
  width: 600px;
  color: white;
  /* White text */
  padding: 10px 20px;
  /* Padding to make the button bigger */
  border: none;
  /* No border */
  border-radius: 50px;
  /* Rounded corners */
  font-size: 16px;
  /* Text size */
  cursor: pointer;
  /* Pointer on hover */
  transition: background-color 0.3s;
  /* Smooth transition for background color */
}

.tab-button1:hover {
  background-color: #0056b3;
  /* Darker blue on hover */
}

.tab-button.active1 {
  background-color: #0056b3;
  /* Active button with dark blue background */
  color: white;
  /* White text */
  font-weight: bold;
  /* Bold text for the active tab */
}
</style>
