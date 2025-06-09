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
      <a-form-item label="Tên phiếu giảm giá" name="ten" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input v-if="product" v-model:value="product.ten" placeholder="Nhập tên màu sắc"
          style="border-radius: 4px;" />
      </a-form-item>

      <a-form-item label="Điều kiện giảm giá" name="dieuKien" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input-number v-if="product" v-model:value="product.dieuKien" placeholder="Nhập Điều kiện giảm giá"
          style="border-radius: 4px ; width: 100%;" />
      </a-form-item>

      <a-form-item label="Giá trị giảm tối đa" name="giaGiam" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input-number v-if="product" v-model:value="product.giaGiam" placeholder="Nhập Giá trị giảm tối đa"
          style="border-radius: 4px; width: 100%;" />
      </a-form-item>

      <a-form-item label="Số lượng" name="soLuongPhieu" :label-col="{ span: 24 }" :rules="rules.name">
        <a-input-number v-if="product" v-model:value="product.soLuongPhieu" placeholder="Nhập Số lượng"
          style="border-radius: 4px; width: 100%;" />
      </a-form-item>

      <a-form-item label="Ngày bắt đầu" name="ngayBatDau" :label-col="{ span: 24 }">
        <a-date-picker v-model:value="product.ngayBatDau" format="YYYY-MM-DD" style="width: 100%;" />
      </a-form-item>

      <a-form-item label="Ngày kết thúc" name="ngayKetThuc" :label-col="{ span: 24 }">
        <a-date-picker v-model:value="product.ngayKetThuc" format="YYYY-MM-DD" style="width: 100%;" />
      </a-form-item>

      <a-form-item v-if="product && product.loaiGiam === true" label="Phần trăm giảm giá" name="phanTramGiam"
        :label-col="{ span: 24 }">
        <a-input-number v-model:value="product.phanTramGiam" style="width: 100%; border-radius: 4px;" min="0" max="100"
          placeholder="Nhập phần trăm giảm giá" />
      </a-form-item>

      <a-form-item label="Loại giảm giá" name="loaiGiam" :label-col="{ span: 24 }">
        <a-radio-group v-model:value="product.loaiGiam">
          <a-radio :value="true">Phần trăm</a-radio>
          <a-radio :value="false">Tiền</a-radio>
        </a-radio-group>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits } from 'vue';
import { getSize, type SizeResponse, modifySize } from '@/services/api/admin/voucher.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';
const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);

const product = ref<SizeResponse>();
const productForm = ref();
const colorPicker = ref<HTMLInputElement | null>(null);

const rules = {
  name: [{ required: true, message: 'Tên không được để trống!', trigger: 'blur' }],
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getSize(id);
    const data = response.data;
    product.value = response.data;
    product.value = {
      ...data,
      ngayBatDau: data.ngayBatDau ? dayjs(data.ngayBatDau) : undefined,
      ngayKetThuc: data.ngayKetThuc ? dayjs(data.ngayKetThuc) : undefined,
      loaiGiam: data.loaiGiam === true || data.loaiGiam === 'true',
      phanTramGiam: data.phanTramGiam ?? 0,
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
        product.value = { name: '', mau: '#000000' };
      }
    }
  },
  { immediate: true }
);

const handleSubmit = async () => {
  try {
    await productForm.value.validate();

    const formData = new FormData();

    formData.append('id', product?.value?.id?.trim() || '');
    formData.append('ma', product?.value?.ma?.trim() || '');
    formData.append('ten', product?.value.ten?.trim());
    formData.append('phanTramGiam', product?.value?.phanTramGiam != null ? product.value.phanTramGiam.toString() : product?.value?.giaGiamToiDa);
    formData.append('soLuongPhieu', product?.value?.soLuongPhieu != null ? product.value.soLuongPhieu.toString() : '');
    formData.append('dieuKien', product?.value?.dieuKien != null ? product.value.dieuKien.toString() : '');
    formData.append('giaGiamToiDa', product?.value?.giaGiamToiDa != null ? product.value.giaGiamToiDa.toString() : '');
    formData.append('ngayKetThuc', product.value?.ngayKetThuc ? new Date(product.value?.ngayKetThuc).toISOString().split('T')[0] : '');
    formData.append('ngayBatDau', product.value?.ngayBatDau ? new Date(product.value?.ngayBatDau).toISOString().split('T')[0] : '');
    formData.append('loaiGiam', product.value?.loaiGiam?.toString());

    const res = await modifySize(formData);
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

</script>

<style scoped></style>