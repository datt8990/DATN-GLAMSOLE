<template>
  <a-modal :open="open" :title="props.title" width="600px">
    <template #footer>
      <a-popconfirm title="Bạn có chắc chắn muốn lưu thay đổi?" @confirm="handleSubmit" ok-text="Đồng ý" cancel-text="Huỷ">
        <a-button type="primary">Xác nhận</a-button>
      </a-popconfirm>
      <a-button @click="closeModal">Huỷ</a-button>
    </template>

    <a-form :model="product" ref="productForm" name="productForm" autocomplete="off">
      <a-form-item label="Họ và tên" name="ten" :label-col="{ span: 24 }" :rules="rules.ten">
        <a-input v-model:value="product.ten" placeholder="Nhập họ và tên" />
      </a-form-item>

      <a-form-item label="Số điện thoại" name="sdt" :label-col="{ span: 24 }">
        <a-input v-model:value="product.sdt" placeholder="Nhập số điện thoại" />
      </a-form-item>

      <a-form-item label="Địa chỉ" name="diaChi" :label-col="{ span: 24 }">
        <a-input v-model:value="product.diaChi" placeholder="Nhập địa chỉ" />
      </a-form-item>

      <a-form-item label="Ngày sinh" name="ngaySinh" :label-col="{ span: 24 }">
        <a-date-picker v-model:value="product.ngaySinh" format="YYYY-MM-DD" style="width: 100%;" />
      </a-form-item>

      <a-form-item label="Giới tính" name="gioiTinh" :label-col="{ span: 24 }">
        <a-radio-group v-model:value="product.gioiTinh">
          <a-radio :value="true">Nam</a-radio>
          <a-radio :value="false">Nữ</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import { getKhachHang, type KhachHangResponse, modifyKhachHang } from '@/services/api/admin/khachhang.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';

const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);

const product = ref<KhachHangResponse>({
  id: '',
  ten: '',
  code: '',
  sdt: '',
  diaChi: '',
  ngaySinh: undefined,
  gioiTinh: true,
});

const productForm = ref();

const rules = {
  ten: [{ required: true, message: 'Tên không được để trống!', trigger: 'blur' }],
};

const generateCode = () => {
  const randomNumber = Math.floor(1000 + Math.random() * 9000); // đảm bảo luôn 4 chữ số
  return 'KH_' + randomNumber;
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getKhachHang(id);
    const data = response.data;

    product.value = {
      ...data,
      ngaySinh: data.ngaySinh ? dayjs(data.ngaySinh) : undefined,
      gioiTinh: data.gioiTimh === true || data.gioiTimh === 'true'
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
          ma: generateCode(),
          mau: '#000000',
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
    formData.append('sdt', product.value.sdt?.trim() || '');
    formData.append('diaChi', product.value.diaChi?.trim() || '');
    formData.append('gioiTinh', product.value.gioiTinh?.toString());
    formData.append('ngaySinh', product.value.ngaySinh ? new Date(product.value.ngaySinh).toISOString().split('T')[0] : '');

    const res = await modifyKhachHang(formData);
    closeModal();
    emit('success');
    toast.success(res.message);
  } catch (error) {
    toast.error(error?.response?.data?.message || 'Lỗi khi lưu khách hàng');
  }
};


const closeModal = () => emit('close');
</script>
