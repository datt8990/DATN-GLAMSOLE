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

      <div class="col-lg-7">
        <div class="bg-white p-4 rounded shadow-sm mb-4">
          <h5 class="fw-semibold mb-4">Đơn hàng ({{ listSanPham.length }} sản phẩm)</h5>

          <ul class="list-unstyled mb-3">
            <li class="d-flex align-items-center mb-3" v-for="item in listSanPham" :key="item.id">
              <img
                :src="item.imageUrl"
                class="rounded me-3"
                style="width: 50px; height: 50px; object-fit: cover"
              />
              <div class="flex-grow-1">
                <div class="fw-medium">{{ item.name }}</div>
                <div class="small text-muted">
                  Phân loại: Màu {{ item.color }} / Size {{ item.size }} - SL: {{ item.quantity }}
                </div>
              </div>
              <div class="fw-semibold">
                {{
                  ((item.discountPrice < item.originalPrice
                    ? item.discountPrice
                    : item.originalPrice) *
                    item.quantity) |
                    localeString
                }}đ
              </div>
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
            <a-radio value="MOMO">Thanh toán MoMo</a-radio>
          </a-radio-group>

          <a-button
            type="primary"
            block
            class="mt-4"
            style="height: 52px; font-size: 1.15rem"
            @click="handleCheckout"
          >
            ĐẶT HÀNG
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";
import BreadCrumbUser from "@/components/ui/Breadcrumbs/BreadCrumbUser.vue";

const breadcrumbRoutes = [
  { name: "Trang chủ", path: "/" },
  { name: "Giỏ hàng", path: "/gio-hang" },
  { name: "Thanh toán", path: "/thanh-toan" },
];

// Định nghĩa lại CartItem interface hoặc import nếu có sẵn
interface CartItem {
  id: number;
  name: string;
  originalPrice: number;
  discountPrice: number;
  quantity: number;
  imageUrl: string;
  color: string;
  size: string;
}

const formRef = ref();
const form = ref({
  hoTen: "",
  soDienThoai: "",
  tinh: "",
  huyen: "",
  phuong: "",
  diaChi: "",
  ghiChu: "",
  thanhToan: "COD",
  maGiamGia: "",
});

// Danh sách mã giảm giá
const maGiamGiaList = [
  {
    code: "GIAM50K",
    loai: "donHang", // giảm theo đơn hàng
    giam: 50000,
  },
  {
    code: "SP10%",
    loai: "sanPham", // giảm theo sản phẩm
    giam: 0.1, // 10%
  },
];

// Sử dụng ref cho listSanPham để nó có thể phản ứng và gán giá trị từ history.state
const listSanPham = ref<CartItem[]>([]);
const giamGia = ref(0);

// Hàm để lấy giá đúng của một sản phẩm (giá khuyến mãi hoặc giá gốc)
const getPrice = (item: CartItem) => {
  return item.discountPrice < item.originalPrice ? item.discountPrice : item.originalPrice;
};

const tongTien = computed(() =>
  listSanPham.value.reduce((sum, sp) => sum + getPrice(sp) * sp.quantity, 0)
);

const phiShip = 30000;
const tongCong = computed(() => tongTien.value + phiShip - giamGia.value);

const rules = {
  hoTen: [{ required: true, message: "Vui lòng nhập họ tên", trigger: "blur" }],
  soDienThoai: [{ required: true, message: "Vui lòng nhập số điện thoại", trigger: "blur" }],
  tinh: [{ required: true, message: "Chọn tỉnh", trigger: "change" }],
  huyen: [{ required: true, message: "Chọn huyện", trigger: "change" }],
  phuong: [{ required: true, message: "Chọn phường", trigger: "change" }],
  diaChi: [{ required: true, message: "Nhập địa chỉ cụ thể", trigger: "blur" }],
};

const handleApplyDiscount = () => {
  const ma = form.value.maGiamGia?.trim().toUpperCase();
  const found = maGiamGiaList.find((m) => m.code === ma);

  if (!ma) {
    message.warning("⚠️ Vui lòng nhập mã giảm giá");
    return;
  }

  // Tạo một bản sao ban đầu của danh sách sản phẩm để reset trước khi áp dụng mã mới
  const initialListSanPham = JSON.parse(JSON.stringify(history.state.selectedItems || []));
  listSanPham.value = initialListSanPham;

  if (!found) {
    giamGia.value = 0;
    message.error("❌ Mã giảm giá không hợp lệ!");
    return;
  }

  if (found.loai === "donHang") {
    giamGia.value = found.giam;
    message.success(`✅ Giảm ${found.giam.toLocaleString()}đ trên đơn hàng!`);
  } else if (found.loai === "sanPham") {
    listSanPham.value = listSanPham.value.map((sp) => ({
      ...sp,
      // Áp dụng giảm giá vào giá có hiệu lực (discountPrice hoặc originalPrice)
      discountPrice: Math.round(getPrice(sp) * (1 - found.giam)),
      originalPrice: sp.originalPrice, // Giữ nguyên originalPrice nếu chỉ discountPrice bị ảnh hưởng
    }));
    giamGia.value = 0;
    message.success(`✅ Giảm ${found.giam * 100}% cho từng sản phẩm!`);
  }
};

const router = useRouter();
const handleCheckout = async () => {
  try {
    await formRef.value.validate();

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
      tongCong: tongCong.value,
      items: listSanPham.value, // Bao gồm các mặt hàng đã chọn trong dữ liệu thanh toán
    };

    console.log("✅ Dữ liệu thanh toán:", dataThanhToan);
    message.success("✅ Thông tin hợp lệ, chuẩn bị thanh toán!");
    router.push({ name: "thanh-toan-thanh-cong" });
  } catch (err) {
    message.error("❌ Vui lòng kiểm tra lại thông tin!");
  }
};

// Khi component được mount, lấy các sản phẩm đã chọn từ history.state
onMounted(() => {
  // Lấy dữ liệu từ Local Storage
  const storedItems = localStorage.getItem("checkoutItems");
  if (storedItems) {
    listSanPham.value = JSON.parse(storedItems);
    console.log("Dữ liệu sản phẩm đã nhận từ Local Storage:", listSanPham.value);
  } else {
    console.warn("Không tìm thấy dữ liệu sản phẩm trong Local Storage.");
    // Có thể chuyển hướng hoặc hiển thị thông báo
    // router.push('/gio-hang');
  }
});
</script>

<style scoped>
.text-decoration-line-through {
  text-decoration: line-through;
}
</style>
