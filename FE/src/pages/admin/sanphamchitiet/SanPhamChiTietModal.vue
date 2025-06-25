<template>
  <a-modal :open="open" :title="props.title" width="1300px">
    <template #footer>
      <a-popconfirm title="Bạn có chắc chắn muốn lưu thay đổi?" @confirm="handleSubmit" ok-text="Đồng ý"
        cancel-text="Huỷ">
        <a-button type="primary">Xác nhận</a-button>
      </a-popconfirm>
      <a-button @click="closeModal">Huỷ</a-button>
    </template>

    <a-form :model="product" ref="productForm" name="productForm" autocomplete="off">
      <div class="form-row">
        <!-- hàng 1 -->
        <a-row gutter={24}>
          <a-col span="6">
            <a-form-item label="Tên sản phẩm" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.ten" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

          <a-col span="6">
            <a-form-item label="Danh mục" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.tenDanhMuc" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

          <a-col span="6">
            <a-form-item label="Xuất xứ" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.tenXuatXu" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

          <a-col span="6">
            <a-form-item label="Thương hiệu" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.tenThuongHieu" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- hàng 2 -->
        <a-row gutter={24}>
          <a-col span="6">
            <a-form-item label="Loại đế" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.tenLoaiDe" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

          <a-col span="6">
            <a-form-item label="Xuất xứ" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.tenXuatXu" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

          <a-col span="6">
            <a-form-item label="Chất liệu" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.tenChatLieu" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

              <a-col span="6">
            <a-form-item label="Giá bán" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.giaBan" />
              </div>
            </a-form-item>
          </a-col>
        </a-row>
        <!-- hàng 3 -->
        <a-row gutter={24}>
          <!-- <a-col span="6">
            <a-form-item label="Màu sắc" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-select v-model:value="product.idMauSac" :options="mauSacOptions" placeholder="Chọn Xuất xứ" />
              </div>
            </a-form-item>
          </a-col> -->
          <a-col span="6">
            <a-form-item label="Màu sắc" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-select v-model:value="product.idMauSac" placeholder="Chọn Màu Sắc">
                  <a-select-option v-for="option in mauSacOptions" :key="option.value" :value="option.value">
                    <template #default>
                      <div :style="{ display: 'flex', alignItems: 'center' }">
                        <!-- Vòng tròn màu sắc -->
                        <div :style="{
                          width: '20px',
                          height: '20px',
                          borderRadius: '50%',
                          backgroundColor: option.color,
                          marginRight: '10px'
                        }"></div>
                        <span>{{ option.label }}</span>
                      </div>
                    </template>
                  </a-select-option>
                </a-select>
              </div>
            </a-form-item>
          </a-col>




          <a-col span="6">
            <a-form-item label="Kích thước" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-select v-model:value="product.idKichThuoc" :options="kichThuocOptions" placeholder="Chọn Xuất xứ" />
              </div>
            </a-form-item>
          </a-col>

          <a-col span="6">
            <a-form-item label="Số lượng" name="facilityId" :label-col="{ span: 24 }">
              <div class="input-with-button">
                <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.soLuong" />
              </div>
            </a-form-item>
          </a-col>


        </a-row>
        <!-- hàng 4 -->
        <a-row gutter={24}>
          <a-col span="6">
            <div>
              <div style="margin-top: 20px;">
                <canvas ref="qrCanvas"></canvas>
              </div>

              <button @click="downloadQr" class="download-button">
                  Download GIF
              </button>
            </div>
          </a-col>

          <a-col span="6">

          </a-col>

          <a-col span="6">

          </a-col>

          <a-col span="6">

          </a-col>
        </a-row>
  <!-- hàng 5 -->
  
           <a-col >
            <a-form-item label="Mô tả" name="facilityId" :label-col="{ span: 24 }" >
              <div class="input-with-button">
                <a-textarea placeholder="Nhập tên sản phẩm" v-model:value="product.moTa" :disabled="true" />
              </div>
            </a-form-item>
          </a-col>

      </div>

    </a-form>
  </a-modal>
</template>


<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted } from 'vue';
import { getSanPham, type SanPhamResponse, modifySanPham, GetListThuongHieu } from '@/services/api/admin/sanpham.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';
import { Row } from 'ant-design-vue';
import { getDetailSanPham, GetListColor, GetListSize } from '@/services/api/admin/sanphamchitiet.api';
import QRCode from 'qrcode';

const textToEncode = ref('Hello Vue!')
const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);
const thuongHieuOptions = ref<{ label: string; value: string }[]>([])
const mauSacOptions = ref<{ label: string; value: string; color: string }[]>([])
const kichThuocOptions = ref<{ label: string; value: string }[]>([])
const qrCanvas = ref(null)

const product = ref<SanPhamResponse>({
  id: '',
  ten: '',
  code: '',
  moTa: '',
  idThuongHieu: '',
});




watch(
  () => product.value.id,
  (newId) => {
    if (qrCanvas.value) {
      QRCode.toCanvas(qrCanvas.value, newId, { width: 250 }, (err) => {
        if (err) console.error(err);
      });
    }
  }
);


const downloadQr = () => {
  if (!qrCanvas.value) return

  const link = document.createElement('a')
  link.href = qrCanvas.value.toDataURL('image/png')
  link.download = 'qr-code.png'
  link.click()
}

const productForm = ref();

const fetchThuongHieu = async () => {
  try {
    const response = await GetListThuongHieu()
    thuongHieuOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const rules = {
  ten: [{ required: true, message: 'Tên không được để trống!', trigger: 'blur' }],
};

const generateCode = () => {
  const randomNumber = Math.floor(1000 + Math.random() * 9000); // đảm bảo luôn 4 chữ số
  return 'KH_' + randomNumber;
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getDetailSanPham(id);
    const data = response.data;

    product.value = {
      ...data
    };
    textToEncode.value = product.value.id;
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    }
  }
};


const fetchKichThuoc = async () => {
  try {
    const response = await GetListSize()
    kichThuocOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const fetchMauSac = async () => {
  try {
    const response = await GetListColor()
    mauSacOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,

      value: thuongHieu.id,
      color: thuongHieu.mau
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

watch(
  () => [props.productId, props.open],
  async ([newId, isOpen]) => {
    if (isOpen) {
      if (productForm.value) {
        productForm.value.resetFields();
      }
      console.log('newId', newId)
      if (newId) {
        await fetchProductDetails(newId);
      } else {
        // reset product and auto-generate code
        product.value = {
          id: '',
          ten: '',
          ma: generateCode()
        };
      }
    }
  },
  { immediate: true }
);

const handleSubmit = async () => {
  try {
    await productForm.value.validate();

    const formData = new FormData();
    formData.append('id', product.value.id?.trim() || '');
    formData.append('code', product.value.code?.trim() || generateCode());
    formData.append('ten', product.value.ten?.trim() || '');
    formData.append('moTa', product.value.moTa?.trim() || '');
    formData.append('idThuongHieu', product.value.idThuongHieu?.trim() || '');
    const res = await modifySanPham(formData);
    closeModal();
    emit('success');
    toast.success(res.message);
  } catch (error) {
    toast.error(error?.response?.data?.message || 'Lỗi khi lưu sản phẩm');
  }
};
onMounted(() => {
  fetchThuongHieu();
  fetchMauSac();
  fetchKichThuoc();
});

const closeModal = () => emit('close');
</script>
<style scoped lang="scss">
.input-with-button {
  margin-right: 30px;
}

.download-button {
  background-color: #ff7f32; /* Orange color */
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 250px;
  text-align: center;
}

.download-button:hover {
  background-color: #e67e22; /* Darker orange on hover */
}

.download-button:active {
  background-color: #c45a10; /* Even darker when clicked */
}

.download-button:focus {
  outline: none;
}

</style>