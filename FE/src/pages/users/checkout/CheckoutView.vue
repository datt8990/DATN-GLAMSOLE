<template>
  <div class="checkout-page container py-4">
    <div class="mb-3">
      <BreadCrumbUser :routes="breadcrumbRoutes" title="Xác nhận & Đặt hàng" />
    </div>

    <div class="row g-4">
      <!-- Form thông tin nhận hàng -->
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
                  <a-select v-model:value="form.tinh" placeholder="Chọn tỉnh">
                    <a-select-option value="HN">Hà Nội</a-select-option>
                    <a-select-option value="HCM">TP.HCM</a-select-option>
                  </a-select>
                </a-form-item>
              </div>
              <div class="col-sm-4">
                <a-form-item label="Quận / Huyện" name="huyen">
                  <a-select v-model:value="form.huyen" placeholder="Chọn huyện">
                    <a-select-option value="Q1">Quận 1</a-select-option>
                    <a-select-option value="Q2">Quận 2</a-select-option>
                  </a-select>
                </a-form-item>
              </div>
              <div class="col-sm-4">
                <a-form-item label="Phường / Xã" name="phuong">
                  <a-select v-model:value="form.phuong" placeholder="Chọn phường">
                    <a-select-option value="P1">Phường 1</a-select-option>
                    <a-select-option value="P2">Phường 2</a-select-option>
                  </a-select>
                </a-form-item>
              </div>

              <div class="col-12">
                <a-form-item label="Địa chỉ cụ thể" name="diaChi">
                  <a-input v-model:value="form.diaChi" placeholder="Số nhà, tên đường..." />
                </a-form-item>
              </div>

              <div class="col-12">
                <a-form-item label="Ghi chú">
                  <a-textarea
                    v-model:value="form.ghiChu"
                    placeholder="Ghi chú thêm (nếu có)"
                    :auto-size="{ minRows: 5, maxRows: 8 }"
                  />
                </a-form-item>
              </div>
            </div>
          </a-form>
        </div>
      </div>

      <!-- Đơn hàng + thanh toán -->
      <div class="col-lg-7">
        <div class="bg-white p-4 rounded shadow-sm mb-4">
          <h5 class="fw-semibold mb-4">Đơn hàng ({{ listSanPham.length }} sản phẩm)</h5>

          <ul class="list-unstyled mb-3">
            <li
              class="d-flex align-items-center mb-3"
              v-for="item in listSanPham"
              :key="item.id"
            >
              <img :src="item.anh" class="rounded me-3" style="width: 50px; height: 50px; object-fit: cover" />
              <div class="flex-grow-1">
                <div class="fw-medium">{{ item.ten }}</div>
                <div class="small text-muted">{{ item.bienThe }}</div>
              </div>
              <div class="fw-semibold">{{ item.gia.toLocaleString() }}đ</div>
            </li>
          </ul>

          <div class="d-flex mb-3">
            <a-input v-model:value="form.maGiamGia" placeholder="Mã giảm giá" class="me-2" />
            <a-button @click="handleApplyDiscount">Áp dụng</a-button>
          </div>

          <!-- Tính toán tạm tính + phí ship + tổng cộng -->
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
            <a-radio value="MOMO">Thanh toán MoMo</a-radio>
          </a-radio-group>

          <a-button
            type="primary"
            block
            class="mt-4"
            style="height: 52px; font-size: 1.15rem;"
            @click="handleCheckout"
          >
            ĐẶT HÀNG
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { message } from 'ant-design-vue'
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'

const breadcrumbRoutes = [
  { name: 'Trang chủ', path: '/' },
  { name: 'Giỏ hàng', path: '/gio-hang' },
  { name: 'Thanh toán', path: '/checkout' }
]

const formRef = ref()
const form = ref({
  hoTen: '',
  soDienThoai: '',
  tinh: '',
  huyen: '',
  phuong: '',
  diaChi: '',
  ghiChu: '',
  thanhToan: 'COD',
  maGiamGia: ''
})

// Danh sách mã giảm giá
const maGiamGiaList = [
  {
    code: 'GIAM50K',
    loai: 'donHang', // giảm theo đơn hàng
    giam: 50000
  },
  {
    code: 'SP10%',
    loai: 'sanPham', // giảm theo sản phẩm
    giam: 0.1 // 10%
  }
]

// Danh sách sản phẩm gốc (để reset nếu áp dụng lại mã khác)
const sanPhamGoc = [
  {
    id: 1,
    ten: 'Áo thun nam',
    bienThe: 'Size M / Màu đen',
    gia: 200000,
    anh: 'https://www.chuphinhsanpham.vn/wp-content/uploads/2021/06/chup-hinh-giay-dincox-shoes-c-photo-studio-4.jpg'
  },
  {
    id: 2,
    ten: 'Quần jeans nữ',
    bienThe: 'Size S / Màu xanh',
    gia: 300000,
    anh: 'https://tse4.mm.bing.net/th/id/OIP.C0b3zlLfZ0GD-5txQXOkzQHaE8?pid=Api&P=0&h=180'
  },
  {
    id: 3,
    ten: 'Áo khoác unisex',
    bienThe: 'Size L / Màu trắng',
    gia: 100000,
    anh: 'https://kingmedia.vn/wp-content/uploads/2022/01/Anh-san-pham-2-min.jpg'
  }
]

const listSanPham = ref([...sanPhamGoc])
const giamGia = ref(0)

const tongTien = computed(() =>
  listSanPham.value.reduce((sum, sp) => sum + sp.gia, 0)
)

const phiShip = 30000
const tongCong = computed(() => tongTien.value + phiShip - giamGia.value)

const rules = {
  hoTen: [{ required: true, message: 'Vui lòng nhập họ tên', trigger: 'blur' }],
  soDienThoai: [{ required: true, message: 'Vui lòng nhập số điện thoại', trigger: 'blur' }],
  tinh: [{ required: true, message: 'Chọn tỉnh', trigger: 'change' }],
  huyen: [{ required: true, message: 'Chọn huyện', trigger: 'change' }],
  phuong: [{ required: true, message: 'Chọn phường', trigger: 'change' }],
  diaChi: [{ required: true, message: 'Nhập địa chỉ cụ thể', trigger: 'blur' }]
}

const handleApplyDiscount = () => {
  const ma = form.value.maGiamGia?.trim().toUpperCase()
  const found = maGiamGiaList.find(m => m.code === ma)

  if (!ma) {
    message.warning('⚠️ Vui lòng nhập mã giảm giá')
    return
  }

  // Reset sản phẩm về giá gốc trước khi áp mã
  listSanPham.value = [...sanPhamGoc]

  if (!found) {
    giamGia.value = 0
    message.error('❌ Mã giảm giá không hợp lệ!')
    return
  }

  if (found.loai === 'donHang') {
    giamGia.value = found.giam
    message.success(`✅ Giảm ${found.giam.toLocaleString()}đ trên đơn hàng!`)
  } else if (found.loai === 'sanPham') {
    listSanPham.value = listSanPham.value.map(sp => ({
      ...sp,
      gia: Math.round(sp.gia * (1 - found.giam))
    }))
    giamGia.value = 0
    message.success(`✅ Giảm ${found.giam * 100}% cho từng sản phẩm!`)
  }
}
import { useRouter } from 'vue-router'

const router = useRouter()
const handleCheckout = async () => {
  try {
    await formRef.value.validate()

    const dataThanhToan = {
      hoTen: form.value.hoTen,
      soDienThoai: form.value.soDienThoai,
      diaChi: `${form.value.diaChi}, ${form.value.phuong}, ${form.value.huyen}, ${form.value.tinh}`,
      ghiChu: form.value.ghiChu,
      maGiamGia: form.value.maGiamGia,
      hinhThucThanhToan: form.value.thanhToan,
      tongTien: tongTien.value,
      phiShip,
      giamGia: giamGia.value,
      tongCong: tongCong.value
    }

    console.log('✅ Dữ liệu thanh toán:', dataThanhToan)
    message.success('✅ Thông tin hợp lệ, chuẩn bị thanh toán!')
    router.push({ name: 'thanh-toan-thanh-cong' })
      
 
  } catch (err) {
    message.error('❌ Vui lòng kiểm tra lại thông tin!')
  }
}
</script>

