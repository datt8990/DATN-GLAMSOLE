<template>
  <a-modal :open="open" :title="props.title" width="600px">
    <template #footer>
      <a-popconfirm title="Bạn có chắc chắn muốn lưu thay đổi?" @confirm="handleSubmit" ok-text="Đồng ý"
        cancel-text="Huỷ">
        <a-button type="primary">Xác nhận</a-button>
      </a-popconfirm>
      <a-button @click="closeModal">Huỷ</a-button>
    </template>

    <a-form :model="product" ref="productForm" name="productForm" autocomplete="off">
      <a-form-item label="Tên Sản Phẩm" name="ten" :label-col="{ span: 24 }" :rules="rules.ten">
        <a-input v-model:value="product.ten" placeholder="Nhập tên sản phẩm" />
      </a-form-item>

      <a-form :model="product" ref="productForm" name="productForm" autocomplete="off">
        <a-form-item label="Tên thuong hiệu" name="facilityId" :label-col="{ span: 24 }">
          <a-select v-model:value="product.idThuongHieu" :options="thuongHieuOptions" placeholder="Chọn thuong hiệu" />
        </a-form-item>
      </a-form>

      <a-form-item label="Mô tả" name="mota" :label-col="{ span: 24 }">
        <a-textarea v-model:value="product.moTa" :rows="6" placeholder="Nhập mô tả" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted } from 'vue';
import { getSanPham, type SanPhamResponse, modifySanPham, GetListThuongHieu } from '@/services/api/admin/sanpham.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';
import { Row } from 'ant-design-vue';


const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);
const thuongHieuOptions = ref<{ label: string; value: string }[]>([])


const product = ref<SanPhamResponse>({
  id: '',
  ten: '',
  code: '',
  moTa: '',
  idThuongHieu: '',
});

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
    const response = await getSanPham(id);
    const data = response.data;

    product.value = {
      idThuongHieu: data.idThuongHieu,
      ...data
    };
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    }
  }
};


watch(
  () => [props.productId, props.open],
  async ([newId, isOpen]) => {
    if (isOpen) {
      if (productForm.value) {
        productForm.value.resetFields();
      }
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

onMounted(fetchThuongHieu)
const closeModal = () => emit('close');
</script>
