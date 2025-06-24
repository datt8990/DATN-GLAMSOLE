<template>
  <div class="form-container">
    <h2>THÊM SẢN PHẨM</h2>

    <a-form ref="productForm" name="productForm" autocomplete="off" class="product-form">

      <div class="form-row-single">
        <a-form-item label="Tên Sản Phẩm" name="ten" :label-col="{ span: 24 }">
          <a-input placeholder="Nhập tên sản phẩm" v-model:value="products.ten" v-if="products != null"
            :disabled="true" />
          <a-input placeholder="Nhập tên sản phẩm" v-model:value="product.ten" v-if="products == null"
            :disabled="false" />
        </a-form-item>

        <a-form-item label="Mô tả" name="mota" :label-col="{ span: 24 }">
          <a-textarea :rows="6" placeholder="Nhập mô tả" v-model:value="products.moTa" v-if="products != null"
            :disabled="true" />
          <a-textarea :rows="6" placeholder="Nhập mô tả" v-model:value="product.moTa" v-if="products == null"
            :disabled="false" />
        </a-form-item>
      </div>


      <div class="form-row">
        <a-form-item label="Danh mục" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button" v-if="products != null">
            <a-input placeholder="Nhập tên sản phẩm" v-model:value="products.tenDanhMuc" :disabled="true" />
          </div>
          <div class="input-with-button" v-if="products == null">
            <a-select v-model:value="product.idDanhMuc" :options="danhMucOptions" placeholder="Chọn danh mục" />
            <a-button class="input-button" @click="handleAddLoaiGiayClick">+</a-button>
          </div>
        </a-form-item>


        <a-form-item label="Loại đế" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button" v-if="products != null">
            <a-input placeholder="Nhập tên sản phẩm" v-model:value="products.tenLoaiDe" :disabled="true" />
          </div>
          <div class="input-with-button" v-if="products == null">
            <a-select v-model:value="product.idLoaiDe" :options="loaiDeOptions" placeholder="Chọn Loại đế" />
            <a-button class="input-button" @click="handleAddLoaiDeClick">+</a-button>
          </div>
        </a-form-item>
      </div>


      <div class="form-row">
        <a-form-item label="Thương hiệu" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button" v-if="products != null">
            <a-input placeholder="Nhập tên sản phẩm" v-model:value="products.tenThuongHieu" :disabled="true" />
          </div>
          <div class="input-with-button" v-if="products == null">
            <a-select v-model:value="product.idThuongHieu" :options="thuongHieuOptions"
              placeholder="Chọn thuong hiệu" />
            <a-button class="input-button" @click="handleAddXuatXuClick">+</a-button>
          </div>
        </a-form-item>

        <a-form-item label="Chất liệu" name="facilityId" :label-col="{ span: 24 }">
          <div class="input-with-button" v-if="products != null">
            <a-input placeholder="Nhập tên sản phẩm" v-model:value="products.tenChatLieu" :disabled="true" />
          </div>
          <div class="input-with-button " v-if="products == null">
            <a-select v-model:value="product.idChatLieu" :options="chatLieuOptions" placeholder="Chọn chất liệu" />
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

      <div v-if="selectedColors.length > 0 && selectedSizes.length > 0">
        <h4>Chi tiết sản phẩm</h4>
        <a-button type="primary" @click="handleSubmit" style="margin-top: 16px; width: 150px; float: right;" class="ht">
          Hoàn Tất
        </a-button>

        <div v-if="selectedRowKeys.length > 0" class="batch-action-container">
          <h4>Thao tác hàng loạt cho {{ selectedRowKeys.length }} sản phẩm đã chọn</h4>
          <div class="common-inputs">
            <a-form-item label="Giá chung" :label-col="{ span: 24 }">
              <a-input v-model:value="batchCommonPrice" type="number" placeholder="Nhập giá chung" />
            </a-form-item>
            <a-form-item label="Số lượng chung" :label-col="{ span: 24 }">
              <a-input v-model:value="batchCommonQuantity" type="number" placeholder="Nhập số lượng chung" />
            </a-form-item>
            <a-button type="primary" @click="applyBatchValues" class="apply-batch-btn">Áp dụng</a-button>
          </div>
        </div>
        <a-tabs v-model:activeKey="activeColorTab">
          <a-tab-pane v-for="color in selectedColors" :key="color.value" :tab="'Sản phẩm màu ' + color.label">

            <a-table :dataSource="productVariantsByColor(color.value)" :columns="columns" bordered rowKey="key"
              :row-selection="rowSelection">
              <template #bodyCell="{ column, record, index }">
                <template v-if="column.key === 'operation'">
                  <a-button danger @click="removeProductVariant(record.key)">xóa</a-button>
                </template>
                <template v-else-if="column.key === 'upload'">
                  <a-upload :before-upload="(file) => handleImageUpload(file, record)" accept="image/*"
                    list-type="picture-card" :show-upload-list="false">
                    <div v-if="!record.imagePreviewUrl">
                      <plus-outlined />
                      <div style="margin-top: 8px">Upload</div>
                    </div>
                    <a-image v-else :src="record.imagePreviewUrl" width="100px" height="100px" />
                  </a-upload>
                </template>
                <template v-else-if="column.key === 'giaBan'">
                  <a-input v-model:value="record.giaBan" placeholder="Nhập giá"
                    @blur="handleUpdatePrice(record.key, record.giaBan)" />
                </template>
                <template v-else-if="column.key === 'soLuong'">
                  <a-input v-model:value="record.soLuong" placeholder="Nhập số lượng"
                    @blur="handleUpdateQuantity(record.key, record.soLuong)" />
                </template>
              </template>
            </a-table>
          </a-tab-pane>
        </a-tabs>
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
      <a-button v-for="(size, index) in SizeOptions" :key="index" class="size-color-btn" @click="handleSelectSize(size)">
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
import { ref, watch, defineProps, defineEmits, onMounted, onBeforeUnmount, computed } from 'vue';
import { getSanPham, type SanPhamResponse, modifySanPham, GetListColor, GetListSize, } from '@/services/api/admin/sanphamchitiet.api';
import { toast } from 'vue3-toastify';
import type { TableColumnsType } from 'ant-design-vue';
import { router } from '@/routes/router';
import { GetListChatLieu, GetListDanhMuc, GetListLoaiDe, GetListThuongHieu, GetListXuatXu } from '@/services/api/admin/sanpham.api';
import { PlusOutlined } from '@ant-design/icons-vue';

const XuatXuOptions = ref<{ label: string; value: string }[]>([])
const ChatLieuOptions = ref<{ label: string; value: string }[]>([])
const LoaiGiayOptions = ref<{ label: string; value: string }[]>([])
const LoaiDeOptions = ref<{ label: string; value: string }[]>([])
const SizeOptions = ref<{ label: string; value: string }[]>([])
const ColorOptions = ref<{ label: string; value: string; color: string }[]>([])
let interval: number | null = null;
const productVariants = ref<any[]>([]); // This will hold all product variants (color-size combinations)
const selectedColors = ref<any[]>([]);
const selectedSizes = ref<any[]>([]);

const thuongHieuOptions = ref<{ label: string; value: string }[]>([])
const xuatXuOptions = ref<{ label: string; value: string }[]>([])
const loaiDeOptions = ref<{ label: string; value: string }[]>([])
const danhMucOptions = ref<{ label: string; value: string }[]>([])
const chatLieuOptions = ref<{ label: string; value: string }[]>([])
const isColorModalVisible = ref(false);
const isSizeModalVisible = ref(false);
const check = ref(0);

const activeColorTab = ref<string>(''); // To control the active tab for colors

// --- New state for row selection and batch actions ---
const selectedRowKeys = ref<string[]>([]); // Keys of selected product variants
const batchCommonPrice = ref<number | null>(null);
const batchCommonQuantity = ref<number | null>(null);

const onSelectChange = (keys: string[]) => {
  selectedRowKeys.value = keys;
};

const rowSelection = computed(() => {
  return {
    selectedRowKeys: selectedRowKeys.value,
    onChange: onSelectChange,
  };
});

const product = ref<SanPhamResponse>({
  id: '',
  ten: '',
  code: '',
  moTa: '',
  idThuongHieu: '',
  idLoaiDe: '',
  idXuatXu: '',
  idDanhMuc: '',
  idChatLieu: '',
});

// These handlers are now simpler as individual inputs are always editable
const handleUpdatePrice = (key: string, newPrice: string) => {
  const variant = productVariants.value.find(v => v.key === key);
  if (variant) {
    variant.giaBan = newPrice;
  }
};

const handleUpdateQuantity = (key: string, newQuantity: string) => {
  const variant = productVariants.value.find(v => v.key === key);
  if (variant) {
    variant.soLuong = newQuantity;
  }
};

// Function to apply common values to selected variants
const applyBatchValues = () => {
  if (selectedRowKeys.value.length === 0) {
    toast.warning("Vui lòng chọn ít nhất một sản phẩm để áp dụng.");
    return;
  }

  if (batchCommonPrice.value === null && batchCommonQuantity.value === null) {
    toast.warning("Vui lòng nhập giá chung hoặc số lượng chung.");
    return;
  }

  productVariants.value.forEach(variant => {
    if (selectedRowKeys.value.includes(variant.key)) {
      if (batchCommonPrice.value !== null) {
        variant.giaBan = batchCommonPrice.value;
      }
      if (batchCommonQuantity.value !== null) {
        variant.soLuong = batchCommonQuantity.value;
      }
    }
  });
  toast.success(`Đã áp dụng giá và số lượng cho ${selectedRowKeys.value.length} sản phẩm đã chọn.`);
  // Optionally, clear selection and batch inputs after applying
  selectedRowKeys.value = [];
  batchCommonPrice.value = null;
  batchCommonQuantity.value = null;
};


const generateProductVariants = () => {
  const newVariants: any[] = [];
  selectedColors.value.forEach(color => {
    selectedSizes.value.forEach(size => {
      const variantKey = `${color.value}-${size.value}`;
      const existingVariant = productVariants.value.find(v => v.key === variantKey);

      if (existingVariant) {
        // If variant already exists, keep its data (including image, price, quantity)
        newVariants.push(existingVariant);
      } else {
        // Otherwise, create a new variant with default values
        newVariants.push({
          idColor: color.value,
          idSize: size.value,
          key: variantKey,
          ten: `${props.products ? props.products.ten : product.value.ten} [${color.label}-${size.label}]`,
          soLuong: 1,
          giaBan: 10000,
          imageFile: null, // To store the actual file for upload
          imagePreviewUrl: null, // To store the URL for display
        });
      }
    });
  });
  productVariants.value = newVariants;

  // Set active tab to the first color if not already set or if active tab is removed
  if (selectedColors.value.length > 0 && (!activeColorTab.value || !selectedColors.value.some(c => c.value === activeColorTab.value))) {
    activeColorTab.value = selectedColors.value[0].value;
  } else if (selectedColors.value.length === 0) {
    activeColorTab.value = '';
  }
};

// Computed property to filter product variants by the active color tab
const productVariantsByColor = computed(() => (colorId: string) => {
  return productVariants.value.filter(variant => variant.idColor === colorId);
});


const props = defineProps<{
  products: any
  responseXX: any
  responseLG: any
  responseLD: any
  responseCL: any
}>();


const handleImageUpload = (file: File, record: any) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    const newImagePreviewUrl = e.target?.result as string;
    const colorIdToUpdate = record.idColor; // Get the color ID of the current variant

    // Update all variants of the same color - This behavior is retained as per original code.
    // If you wish for individual image per variant, this logic needs to be changed.
    productVariants.value.forEach(variant => {
      if (variant.idColor === colorIdToUpdate) {
        variant.imagePreviewUrl = newImagePreviewUrl;
        variant.imageFile = file; // Assign the same file to all variants of this color
      }
    });
  };
  reader.readAsDataURL(file);
  return false; // Prevent Ant Design from uploading
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
    selectedColors.value.push(color);
  }
};

const handleSelectSize = (size: any) => {
  if (!selectedSizes.value.some(item => item.value === size.value)) {
    selectedSizes.value.push(size);
  }
};

const removeColor = (index: number) => {
  const removedColor = selectedColors.value[index];
  selectedColors.value.splice(index, 1);
  // Remove variants associated with the removed color
  productVariants.value = productVariants.value.filter(v => v.idColor !== removedColor.value);
  generateProductVariants(); // Re-generate variants to ensure consistency
};

const handleCloseColorModal = () => {
  isColorModalVisible.value = false;
};


const removeSize = (index: number) => {
  const removedSize = selectedSizes.value[index];
  selectedSizes.value.splice(index, 1);
  // Remove variants associated with the removed size
  productVariants.value = productVariants.value.filter(v => v.idSize !== removedSize.value);
  generateProductVariants(); // Re-generate variants to ensure consistency
};

const handleCloseSizeModal = () => {
  isSizeModalVisible.value = false;
};

const removeProductVariant = (keyToRemove: string) => {
  productVariants.value = productVariants.value.filter(variant => variant.key !== keyToRemove);
};


const columns: TableColumnsType = [
  // Checkbox column is now handled by `rowSelection` prop
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 60, customRender: ({ index }) => index + 1 },
  { title: 'Tên sản phẩm', key: 'ten', dataIndex: 'ten' },
  { title: 'Kích thước', key: 'size', customRender: ({ record }) => selectedSizes.value.find(s => s.value === record.idSize)?.label }, // Display size label
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

const fetchChatLieu = async () => {
  try {
    const response = await GetListChatLieu()
    chatLieuOptions.value = response.data.map(thuongHieu => ({
      label: thuongHieu.ten,
      value: thuongHieu.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách cơ sở:', error)
  }
}


const fetchDanhMuc = async () => {
  try {
    const response = await GetListDanhMuc()
    danhMucOptions.value = response.data.map(thuongHieu => ({
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
    loaiDeOptions.value = response.data.map(thuongHieu => ({
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
    ColorOptions.value = response.data.map(color => ({
      label: color.ten,
      value: color.id,
      color: color.mau,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách màu:', error)
  }
}

const fetchSize = async () => {
  try {
    const response = await GetListSize()
    SizeOptions.value = response.data.map(size => ({
      label: size.ten,
      value: size.id,
    }))
  } catch (error) {
    console.error('Lỗi khi lấy danh sách kích cỡ:', error)
  }
}

onMounted(() => {
  fetchDanhMuc();
  fetchThuongHieu();
  fetchLoaiDe();
  fetchChatLieu();
  fetchColor();
  fetchSize();

  interval = setInterval(() => {
    fetchDanhMuc();
    fetchThuongHieu();
    fetchChatLieu();
    fetchLoaiDe();
    fetchColor();
    fetchSize();
  }, 1000);
})

onBeforeUnmount(() => {
  if (interval !== null) {
    clearInterval(interval);
  }
});

const handleSubmit = async () => {
  try {
    for (const item of productVariants.value) {
      const formData = new FormData();
      if (props.products != null) {
        formData.append('idSP', props.products.id);
        formData.append('soLuong', item.soLuong);
        formData.append('giaBan', item.giaBan);
        formData.append('idMau', item.idColor);
        formData.append('idSize', item.idSize);
        formData.append('check', check.value.toString());
        if (item.imageFile) {
          formData.append('anh', item.imageFile);
        }
      } else {
        formData.append('ten', product.value.ten?.trim() || '');
        formData.append('moTa', product.value.moTa?.trim() || '');
        formData.append('idThuongHieu', product.value.idThuongHieu?.trim() || '');
        formData.append('idChatLieu', product.value.idChatLieu?.trim() || '');
        formData.append('idLoaiDe', product.value.idLoaiDe?.trim() || '');
        formData.append('idXuatXu', product.value.idXuatXu?.trim() || '');
        formData.append('idDanhMuc', product.value.idDanhMuc?.trim() || '');
        formData.append('soLuong', item.soLuong);
        formData.append('giaBan', item.giaBan);
        formData.append('idMau', item.idColor);
        formData.append('idSize', item.idSize);
        formData.append('check', check.value.toString());
        if (item.imageFile) {
          formData.append('anh', item.imageFile);
        }
      }
      await modifySanPham(formData);
      check.value = check.value + 1;
    }

    if (props.products != null) {
      toast.success("Cập nhật sản phẩm thành công");
      router.push({
        name: 'san-pham-chi-tiet-admin',
        query: { id: props.products.id },
      });
    } else {
      toast.success("Thêm sản phẩm thành công");
      router.push({
        name: 'san-pham-admin',
      });
    }

  } catch (error: any) {
    console.error(error);
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      toast.error("Đã xảy ra lỗi khi thêm/cập nhật sản phẩm.");
    }
  }
};

watch([selectedColors, selectedSizes], () => {
  generateProductVariants();
}, { deep: true });

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

.batch-action-container {
  margin-top: 20px;
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  background-color: #f9f9f9;

  h4 {
    text-align: left;
    margin-bottom: 15px;
    font-size: 18px;
    font-weight: bold;
  }

  .common-inputs {
    display: flex;
    gap: 20px;
    align-items: flex-end; // Align button with inputs

    .ant-form-item {
      flex: 1;
      margin-bottom: 0;
    }

    .apply-batch-btn {
      background-color: #1890ff;
      color: white;
      border-radius: 5px;
      height: 32px; // Match input height
    }
  }
}
</style>