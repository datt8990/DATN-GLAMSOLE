<template>
  <div class="form-container">
    <h2>THÊM SẢN PHẨM</h2>

    <a-form ref="productForm" name="productForm" autocomplete="off" class="product-form">

      <div class="form-row-single">
        <a-form-item label="Tên Sản Phẩm" name="ten" :label-col="{ span: 24 }">
          <a-input placeholder="Nhập tên sản phẩm" v-model:value="products.ten" :disabled="true" />
        </a-form-item>

        <a-form-item label="Mô tả" name="mota" :label-col="{ span: 24 }">
          <a-textarea :rows="6" placeholder="Nhập mô tả" v-model:value="products.moTa" :disabled="true" />
        </a-form-item>
      </div>


      <div class="form-row">
        <a-form-item label="Loại giày" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button">
            <a-select placeholder="Chọn loại giày" v-model:value="products.idLG" :options="LoaiGiayOptions" />
            <a-button class="input-button" @click="handleAddLoaiGiayClick">+</a-button>
          </div>
        </a-form-item>


        <a-form-item label="Loại đế" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button">
            <a-select placeholder="Chọn loại đế" v-model:value="products.idLD" :options="LoaiDeOptions" />
            <a-button class="input-button" @click="handleAddLoaiDeClick">+</a-button>
          </div>
        </a-form-item>
      </div>


      <div class="form-row">
        <a-form-item label="Xuất xứ" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button">
            <a-select placeholder="Chọn xuất xứ" v-model:value="products.idXX" :options="XuatXuOptions" />
            <a-button class="input-button" @click="handleAddXuatXuClick">+</a-button>
          </div>
        </a-form-item>

        <a-form-item label="Chất liệu" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button">
            <a-select placeholder="Chọn chất liệu" v-model:value="products.idCL" :options="ChatLieuOptions" />
            <a-button class="input-button" @click="handleAddChatLieuClick">+</a-button>
          </div>
        </a-form-item>
      </div>

      <div class="form-container">
        <h2>KÍCH CỠ VÀ MÀU SẮC</h2>

        <div class="form-row1">
          <div class="selected-sizes">
            <div v-for="(size, index) in selectedSizes" :key="index" class="size-box">
              <a-button class="remove-btn" @click="removeSize(index)" shape="circle" icon="-" size="small" />
              {{ size.label }}
            </div>
            <a-button @click="isSizeModalVisible = true" class="add-btn">+</a-button>
          </div>

          <div class="selected-colors">
            <div v-for="(color, index) in selectedColors" :key="index" class="color-box"
              :style="{ backgroundColor: color.color }">
              <a-button class="remove-btn" @click="removeColor(index)" shape="circle" icon="-" size="small" />
            </div>
            <a-button @click="isColorModalVisible = true" class="add-btn">+</a-button>
          </div>



        </div>
      </div>

      <div>
        <h4>Chi tiết sản phẩm</h4>
        <a-button type="primary" @click="handleSubmit" style="margin-top: 16px; width: 150px; float: right;" class="ht">
          Hoàn Tất
        </a-button>

        <a-table :dataSource="productVariants" :columns="columns" bordered rowKey="key">
          <template #bodyCell="{ column, record, index }">
            <template v-if="column.key === 'operation'">
              <a-button danger @click="productVariants.splice(index, 1)">xóa</a-button>
            </template>
            <template v-else-if="column.key === 'upload'">
              <a-upload :before-upload="handleImageUpload" accept="image/*" list-type="picture-card"
                :show-upload-list="false">
                <div v-if="!imageUrl">
                  <plus-outlined />
                  <div style="margin-top: 8px">Upload</div>
                </div>
                <a-image v-else :src="imageUrl" width="100px" height="100px" />
              </a-upload>
            </template>
            <template v-else-if="column.key === 'giaBan'">
              <a-input v-model:value="record.giaBan" placeholder="Nhập giá"
                @blur="handleUpdatePrice(index, record.giaBan)" />
            </template>
            <template v-else-if="column.key === 'soLuong'">
              <a-input v-model:value="record.soLuong" placeholder="Nhập số lượng"
                @blur="handleUpdateQuantity(index, record.soLuong)" />
            </template>
          </template>
        </a-table>

      </div>

    </a-form>
  </div>

  <a-modal v-model:open="isColorModalVisible" title="Chọn màu sắc" width="400px" @cancel="handleCloseColorModal">
    <a-button class="input-button-model" @click="handleAddColorClick">+ Thêm màu sắc</a-button>
    <div class="color-picker">
      <a-button v-for="(color, index) in ColorOptions" :key="index" class="size-color-btn"
        :style="{ backgroundColor: color.color }" @click="handleSelectColor(color)">
        {{ color.label }}
      </a-button>
    </div>
    <template #footer>
      <a-button @click="handleCloseColorModal">Hủy</a-button>
      <a-button type="primary" @click="handleConfirmColorSelection">Xác nhận</a-button>
    </template>
  </a-modal>



  <a-modal v-model:open="isSizeModalVisible" title="Chọn kích cỡ" width="400px" @cancel="handleCloseSizeModal">
    <a-button class="input-button-model" @click="handleAddSizeClick">+ Thêm Kích thước</a-button>
    <div class="size-picker">
      <a-button v-for="(size, index) in SizeOptions" :key="index" class="size-color-btn"
        @click="handleSelectSize(size)">
        {{ size.label }}
      </a-button>
    </div>
    <template #footer>
      <a-button @click="handleCloseSizeModal">Hủy</a-button>
      <a-button type="primary" @click="handleConfirmSizeSelection">Xác nhận</a-button>
    </template>
  </a-modal>

</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted, onBeforeUnmount, reactive } from 'vue';
import { getSanPham, type SanPhamResponse, modifySanPham, GetListChatLieu, GetListLoaiGiay, GetListLoaiDe, GetListXuatXu, GetListColor, GetListSize,  } from '@/services/api/admin/sanphamchitiet.api';
import { toast } from 'vue3-toastify';
import type { TableColumnsType } from 'ant-design-vue';
import { router } from '@/routes/router';
const XuatXuOptions = ref<{ label: string; value: string }[]>([])
const ChatLieuOptions = ref<{ label: string; value: string }[]>([])
const LoaiGiayOptions = ref<{ label: string; value: string }[]>([])
const LoaiDeOptions = ref<{ label: string; value: string }[]>([])
const SizeOptions = ref<{ label: string; value: string }[]>([])
const ColorOptions = ref<{ label: string; value: string; color: string }[]>([])
let interval: number | null = null;
const productVariants = ref<any[]>([]);
const selectedColors = ref<any[]>([]);
const selectedSizes = ref<any[]>([]);
const imageUrl = ref<string | null>(null);
const imageFile = ref<File | null>(null);
const imageError = ref(false);

const isColorModalVisible = ref(false);
const isSizeModalVisible = ref(false);

const handleUpdatePrice = (index: number, newPrice: string) => {
  // Cập nhật lại giá bán của sản phẩm
  productVariants.value[index].giaBan = newPrice;
};

const handleUpdateQuantity = (index: number, newQuantity: string) => {
  // Cập nhật lại số lượng của sản phẩm
  productVariants.value[index].soLuong = newQuantity;
};


const generateProductVariants = () => {
  productVariants.value = []; // reset bảng

  selectedColors.value.forEach(color => {
    selectedSizes.value.forEach(size => {
      const variantKey = `${color.value}-${size.value}`;

      if (!productVariants.value.find(v => v.key === variantKey)) {
        productVariants.value.push({
          idColor: color.value,
          idSize: size.value,
          key: variantKey,
          ten: `${props.products.ten} [${color.label}-${size.label}]`,
          soLuong: 1,
          giaBan: 10000000,
          image: null,
        });
      }
    });
  });
};


const props = defineProps<{
  products: any
  responseXX: any
  responseLG: any
  responseLD: any
  responseCL: any
}>();



const handleImageUpload = (file: File) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    imageUrl.value = e.target?.result as string;
    imageError.value = false; // Reset lỗi khi người dùng chọn ảnh
  };
  reader.readAsDataURL(file);
  imageFile.value = file;
  return false;
};

const handleConfirmColorSelection = () => {
  isColorModalVisible.value = false;
  generateProductVariants();
};

const handleConfirmSizeSelection = () => {
  isSizeModalVisible.value = false;
  generateProductVariants();
};


const handleSelectColor = (color: any) => {
  if (!selectedColors.value.some(item => item.value === color.value)) {
    selectedColors.value.push(color); // Thêm màu vào danh sách
  }
};

const handleSelectSize = (size: any) => {
  if (!selectedSizes.value.some(item => item.value === size.value)) {
    selectedSizes.value.push(size); // Thêm kích cỡ vào danh sách
  }
};

const removeColor = (index: number) => {
  selectedColors.value.splice(index, 1);
};

const handleCloseColorModal = () => {
  isColorModalVisible.value = false;
};


const removeSize = (index: number) => {
  selectedSizes.value.splice(index, 1);
};

const handleCloseSizeModal = () => {
  isSizeModalVisible.value = false;
};



const columns: TableColumnsType = [
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 60, customRender: ({ index }) => index + 1 },
  { title: 'Tên sản phẩm', key: 'ten', dataIndex: 'ten' },
  { title: 'Số lượng', key: 'soLuong', dataIndex: 'soLuong' },
  { title: 'Giá bán', key: 'giaBan', dataIndex: 'giaBan' },
  { title: 'Hành động', key: 'operation' },
  { title: 'Upload ảnh', key: 'upload' },
];

const emit = defineEmits(['addCL', 'addXX', 'addLD', 'addLG', 'addColor', 'addSize'])

const handleAddChatLieuClick = () => {
  emit('addCL')
}


const handleAddXuatXuClick = () => {
  emit('addXX')
}


const handleAddLoaiGiayClick = () => {
  emit('addLG')
}


const handleAddLoaiDeClick = () => {
  emit('addLD')
}

const handleAddSizeClick = () => {
  emit('addSize')
}

const handleAddColorClick = () => {
  emit('addColor')
}


const fetchChatLieu = async () => {
  try {
    const response = await GetListChatLieu()
    ChatLieuOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const fetchLoaiGiay = async () => {
  try {
    const response = await GetListLoaiGiay()
    LoaiGiayOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const fetchLoaiDe = async () => {
  try {
    const response = await GetListLoaiDe()
    LoaiDeOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const fetchXuatXu = async () => {
  try {
    const response = await GetListXuatXu()
    XuatXuOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const fetchColor = async () => {
  try {
    const response = await GetListColor()
    ColorOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
      color: thuongHieu.mau,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

const fetchSize = async () => {
  try {
    const response = await GetListSize()
    SizeOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}

onMounted(() => {
  fetchChatLieu(),
    fetchLoaiGiay(),
    fetchLoaiDe(),
    fetchXuatXu(),
    fetchColor(),
    fetchSize()
  interval = setInterval(fetchColor, 1000);
  interval = setInterval(fetchSize, 1000);
  interval = setInterval(fetchChatLieu, 1000);
  interval = setInterval(fetchLoaiGiay, 1000);
  interval = setInterval(fetchLoaiDe, 1000);
  interval = setInterval(fetchXuatXu, 1000);
})

onBeforeUnmount(() => {
  if (interval !== null) {
    clearInterval(interval);
  }
});

const formDataList: FormData[] = [];

const handleSubmit = async () => {
  try {
    for (const item of productVariants.value) {
      const formData = new FormData();

      formData.append('idSP', props.products.id);
      formData.append('soLuong', item.soLuong);
      formData.append('giaBan', item.giaBan);
      formData.append('idCL', props.products.idCL);
      formData.append('idLG', props.products.idLG);
      formData.append('idLD', props.products.idLD);
      formData.append('idXX', props.products.idXX);
      formData.append('idMau', item.idColor);
      formData.append('idSize', item.idSize);
      if (imageFile.value) {
        formData.append('anh', imageFile.value);
      }

      const res = await modifySanPham(formData);

    }

    router.push({
      name: 'san-pham-chi-tiet-admin',
      query: { id: props.products.id },
    });

    toast.success("thêm sản phẩm thành công");

    formDataList.splice(0, formDataList.length); // Xóa dữ liệu sau khi gửi

  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    }
  }
};

</script>

<style scoped lang="scss">
.color {
  border-radius: 50%;
}

.center-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-with-button {
  display: flex;
  align-items: center;
  gap: 10px;
}

h4 {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}

/* Nút Hoàn Tất */
.finish-button-container {
  text-align: right;
  margin: 20px;
}

.finish-button {
  background-color: #1890ff;
  color: white;
  padding: 8px 16px;
  border-radius: 5px;
  font-weight: bold;
  font-size: 14px;
  width: 120px;

  &:hover {
    background-color: #40a9ff;
  }
}

.table {
  margin-left: 10px;
}

.form-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 100px;
  margin-right: 100px;
}

.size-color-btn {
  margin-top: 5px;
  margin-left: 10px;
}

h2 {
  margin-bottom: 20px;
  text-align: center;
}

.product-form {
  display: grid;
  grid-template-columns: 1fr;
  /* Single column layout for top fields */
  gap: 20px;
  /* Space between the rows */
}

.form-row-single {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  /* Two columns for dropdown fields */
  gap: 20px;
}

.a-form-item label {
  font-weight: bold;
  /* Make the label text bold */
}

.input-with-button {
  display: flex;
  align-items: center;
  gap: 10px;
}

.a-form-item label {
  font-weight: 900;
  /* Làm chữ đậm hơn */
}

.input-with-button .a-select {
  width: 100%;
  /* Ensure select takes full width */
  max-width: 450px;
  /* Limit the max width */
}

.input-button-model {
  right: -200px;
  margin-bottom: 20px;
  /* Adjust padding of the button */
  background-color: #1890ff;
  /* Button color */
  color: white;
}


.input-button {
  padding: 0 10px;
  /* Adjust padding of the button */
  background-color: #1890ff;
  /* Button color */
  color: white;
}



.form-container1 {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 100px;
  margin-right: 100px;
  text-align: center;
}

h2 {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}

.product-form1 {
  display: flex;
  flex-direction: column;
  gap: 20px;
  /* Space between the fields */
}

.selected-sizes,
.selected-colors {
  display: flex;
  flex-direction: row; // Cho nằm ngang
  flex-wrap: wrap; // Tự động xuống dòng nếu không đủ chỗ
  gap: 10px;
  margin-bottom: 12px;
}

.form-row1 {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.selected-sizes {
  margin-top: 10px;
}

.selected-colors {
  margin-top: 10px;
}

.a-form-item label {
  font-weight: bold;
  /* Make the label text bold */
}

.input-with-button1 {
  display: flex;
  align-items: center;
  gap: 10px;
}

.input-with-button .a-select {
  width: 100%;
  /* Ensure select takes full width */
  max-width: 350px;
  /* Limit the max width */
}

.input-button1 {
  padding: 0 10px;
  /* Adjust padding of the button */
  background-color: #1890ff;
  /* Button color */
  color: white;
  border-radius: 5px;
  /* Rounded corners for the button */
}



//
/* Kích cỡ */
.size-box {
  width: 45px;
  height: 45px;
  border-radius: 6px;
  background-color: white;
  border: 2px solid #d9d9d9;
  font-weight: bold;
  color: #000;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  font-size: 16px;
}

/* Màu sắc */
.color-box {
  width: 45px;
  height: 45px;
  border-radius: 6px;
  background-color: gray;
  /* Sẽ ghi đè bằng inline style */
  border: 2px solid #d9d9d9;
  position: relative;
}

/* Nút xoá */
.remove-btn {
  position: absolute;
  top: -10px;
  right: -18px;
  transform: translateX(-50%);
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 50%;
  width: 22px;
  height: 22px;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  padding: 0;
}


/* Nút thêm + */
.add-btn {
  width: 45px;
  height: 45px;
  background-color: #1890ff;
  color: white;
  border-radius: 6px;
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ht {
  background-color: #40a9ff;
  margin-bottom: 20px;
}
</style>