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
      <a-form-item label="Tên Nhân viên" name="ten" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input v-if="product" v-model:value="product.ten" placeholder="Nhập tên nhân viên"
          style="border-radius: 4px;" />
      </a-form-item>

      <a-form-item label="Email" name="email" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input v-if="product" v-model:value="product.email" placeholder="Email" style="border-radius: 4px;" />
      </a-form-item>


      <a-form-item label="Địa chỉ" name="diaChi" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input v-if="product" v-model:value="product.diaChi" placeholder="địa chỉ" style="border-radius: 4px;" />
      </a-form-item>



      <a-form-item label="só điện thoại" name="sdt" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input v-if="product" v-model:value="product.sdt" placeholder="só điện thoại" style="border-radius: 4px;" />
      </a-form-item>


      <a-form-item label="Giới tính" name="gioiTinh" :label-col="{ span: 24 }">
        <a-radio-group v-model:value="product.gioiTinh">
          <a-radio :value="true">Nam</a-radio>
          <a-radio :value="false">Nữ</a-radio>
        </a-radio-group>
      </a-form-item>

      <a-form-item label="Ảnh đại diện" name="avatar" :label-col="{ span: 24 }" :rules="rules.avatar">
        <a-upload :before-upload="handleImageUpload" accept="image/*" list-type="picture-card"
          :show-upload-list="false">
          <div v-if="!imageUrl">
            <plus-outlined />
            <div style="margin-top: 8px">Upload</div>
          </div>
          <a-image v-else :src="imageUrl" width="100px" height="100px" />
        </a-upload>
        <p v-if="imageError" style="color: red; font-size: 14px;">Ảnh không được để trống!</p>
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import { getMember, type NhanVienResponse, modifyMember } from '@/services/api/admin/nhanvien.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';

const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);

const productForm = ref();
const colorPicker = ref<HTMLInputElement | null>(null);
const imageUrl = ref<string | null>(null);
const imageFile = ref<File | null>(null);
const imageError = ref(false);

const product = ref<NhanVienResponse>({
  id: '',
  ten: '',
  sdt: '',
  diaChi: '',
  email: '',
  ma: '',
  gioiTinh: true,
});

const rules = {
  name: [{ required: true, message: 'Tên không được để trống!', trigger: 'blur' }],
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getMember(id);
    const data = response.data;
    product.value = response.data;
    product.value = {
      ...data,
      ngayTuyenDung: data.ngayTuyenDung ? dayjs(data.ngayTuyenDung) : undefined,
      ngaySinh: data.ngaySinh ? dayjs(data.ngaySinh) : undefined,
      gioiTinh: data.gioiTimh === true || data.gioiTimh === 'true'
    };
    imageUrl.value = response.data.avatar || null;
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
        product.value = { name: '', mau: '' };
        imageUrl.value = null;
        imageFile.value = null;
        imageError.value = false
      }
    }
  },
  { immediate: true }
);


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

const handleSubmit = async () => {
  try {
    await productForm.value.validate();

    const formData = new FormData();
    formData.append('id', product?.value?.id?.trim() || '');
    formData.append('code', product?.value?.ma?.trim() || '');
    formData.append('ten', product?.value.ten?.trim());
    formData.append('email', product?.value.email?.trim() || '');
    formData.append('sdt', product?.value.sdt?.trim() || '');
    formData.append('diaChi', product?.value.diaChi?.trim() || '');
    formData.append('gioiTinh', product.value.gioiTinh?.toString());
    if (imageFile.value) {
      formData.append('avatar', imageFile.value);
    }
    const res = await modifyMember(formData);
    closeModal();
    emit('success');
    toast.success(res.message);
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    }
  }
};

const closeModal = () => emit('close');

const openColorPicker = () => {
  if (colorPicker.value) {
    colorPicker.value.click();
  }
};

const updateColor = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.value) {
    product.value.mau = target.value;
  }
};  
</script>

<style scoped></style>