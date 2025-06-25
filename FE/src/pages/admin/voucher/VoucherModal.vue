<template>
  <DivCustom label="Thêm Phiếu giảm giá" customClasses="mt-5">
    <a-row gutter={16}>

      <a-col :span="8">

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

          <a-form-item v-if="product && product.kieuGiam === true" label="Phần trăm giảm giá" name="phanTramGiam"
            :label-col="{ span: 24 }">
            <a-input-number v-model:value="product.phanTramGiam" style="width: 100%; border-radius: 4px;" min="0"
              max="100" placeholder="Nhập phần trăm giảm giá" />
          </a-form-item>

          <a-form-item label="Kiểu giảm giá" name="kieuGiam" :label-col="{ span: 24 }">
            <a-radio-group v-model:value="product.kieuGiam">
              <a-radio :value="true">Phần trăm</a-radio>
              <a-radio :value="false">Tiền</a-radio>
            </a-radio-group>
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

          <a-form-item label="Loại giảm giá" name="loaiGiam" :label-col="{ span: 24 }">
            <a-radio-group v-model:value="product.loaiGiam">
              <a-radio :value="false">Công khai</a-radio>
              <a-radio :value="true">Cá nhân</a-radio>
            </a-radio-group>
          </a-form-item>
        </a-form>

      </a-col>


      <a-col v-if="product && product.loaiGiam === true" :span="16">
        <div class="min-h-[360px] ">
          <div class="d-flex align-items-center gap-2" style="width: 700px; margin-left: 60px; margin-top: 40px;">
            <a-input v-model:value="state.searchQuery" placeholder="Nhập mã/tên/sdt/địa chỉ để tìm kiếm..."
              style="width: 800px;" />
            <a-tooltip title="Làm mới bộ lọc">
              <a-button @click="resetFilters" class="d-flex align-items-center">
                <ReloadOutlined />
              </a-button>
            </a-tooltip>
          </div>
          <a-table :columns="columns" :data-source="state.products" :pagination="{
            current: state.paginationParams.page,
            pageSize: state.paginationParams.size,
            total: state.totalItems,
            showSizeChanger: true,
            pageSizeOptions: ['10', '20', '30', '40', '50']
          }" :scroll="{ y: 240 }" style="margin-top: 10px;">

            <template #bodyCell="{ column, record }">
              <div v-if="column.key === 'select'">
                <a-checkbox
                  :checked="state.selectedRows.includes(record.id)"
                  @change="onCheckboxChange(record.id, $event.target.checked)"
                />
              </div>
              <div v-if="column.key === 'stt'">
                {{ state.products.indexOf(record) + 1 }}
              </div>
            </template>
          </a-table>

        </div>
      </a-col>
    </a-row>

    <a-form-item
      style="text-align: right; display: flex; justify-content: flex-end; margin-top: 30px; margin-right: 50px;">
      <a-button style="background-color: aqua; width: 150px; margin-right: 700px;" @click="closeModal">Quay
        lại</a-button>
      <a-button style="background-color: aqua; margin-right: 30px; width: 180px;" html-type="submit"
        @click="handleSubmit">{{ label }}</a-button>

    </a-form-item>
  </DivCustom>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, reactive, onMounted, h } from 'vue';
import { getSize, type SizeResponse, modifySize } from '@/services/api/admin/voucher.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import { GetKhachHangs, type KhachHangResponse, type ParamsGetKhachHang } from '@/services/api/admin/khachhang.api';
import type { TableColumnsType } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import { ReloadOutlined } from '@ant-design/icons-vue';
const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);
const idSanPham = ref('')
const route = useRoute();
const product = ref<SizeResponse>({
  id: '',
  ten: '',
  sdt: '',
  diaChi: '',
  email: '',
  ma: '',
  gioiTinh: true,
});
const productForm = ref();
const colorPicker = ref<HTMLInputElement | null>(null);
const label = ref('');
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



const columns: TableColumnsType = [
  {
    title: 'Chọn', key: 'select', dataIndex: 'select', width: 100, align: 'center',
  },
  { title: 'STT', key: 'stt', dataIndex: 'stt', width: 150, align: 'center' },
  { title: 'Mã KH', key: 'ma', dataIndex: 'ma', width: 150, align: 'center' },
  { title: 'Tên KH', key: 'ten', dataIndex: 'ten', width: 150, align: 'center' },
  { title: 'SDT', key: 'sdt', dataIndex: 'sdt', width: 150, align: 'center' },
  { title: 'Email', key: 'email', dataIndex: 'email', width: 150, align: 'center' },

]



const state = reactive({
  selectAll: false,
  selectedRows: [] as string[],
  searchQuery: '',
  searchStatus: null as number | null,
  isModalOpen: false,
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  products: [] as KhachHangResponse[],
  paginationParams: { page: 1, size: 10 },
  totalItems: 0
})

const resetFilters = () => {
  state.searchQuery = ''

}
const fetchProducts = async () => {
  try {
    const params: ParamsGetKhachHang = {
      page: state.paginationParams.page,
      size: state.paginationParams.size,
      q: state.searchQuery,
      status: state.searchStatus
    }
    const response = await GetKhachHangs(params)
    // const pagedData = response.data.data 

    state.products = response.data?.data
    state.totalItems = response.data?.totalElements
  } catch (error) {
    console.error('Failed to fetch products:', error)
  }
}


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
        // Khởi tạo sản phẩm với các giá trị mặc định cho mục mới
        product.value = {
          id: '',
          ten: '',
          sdt: '',
          diaChi: '',
          email: '',
          ma: '',
          gioiTinh: true,
          kieuGiam: false, // Mặc định là 'Tiền'
          loaiGiam: false, // Mặc định là 'Công khai'
          phanTramGiam: 0,
          soLuongPhieu: undefined,
          dieuKien: undefined,
          giaGiam: undefined,
          ngayBatDau: undefined,
          ngayKetThuc: undefined,
        };
        state.selectedRows = []; // Xóa các hàng đã chọn khi thêm mới
      }
    }
  },
  { immediate: true }
);

// Hàm mới để xử lý thay đổi ô kiểm
const onCheckboxChange = (id: string, checked: boolean) => {
  if (checked) {
    state.selectedRows.push(id);
  } else {
    state.selectedRows = state.selectedRows.filter(rowId => rowId !== id);
  }
};


// Trong hàm handleSubmit của bạn
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
    formData.append('giaGiamToiDa', product?.value?.giaGiam != null ? product.value.giaGiam.toString() : '');
    formData.append('ngayKetThuc', product.value?.ngayKetThuc ? dayjs(product.value?.ngayKetThuc).format('YYYY-MM-DD') : ''); // Sử dụng dayjs để format
    formData.append('ngayBatDau', product.value?.ngayBatDau ? dayjs(product.value?.ngayBatDau).format('YYYY-MM-DD') : ''); // Sử dụng dayjs để format
    formData.append('loaiGiam', product.value?.loaiGiam?.toString() || 'false'); // Đảm bảo luôn là 'true' hoặc 'false'
    formData.append('kieuGiam', product.value?.kieuGiam?.toString() || 'false'); // Đảm bảo luôn là 'true' hoặc 'false'


    if (product.value.loaiGiam === true) {
      // SỬA Ở ĐÂY: Thêm từng ID vào FormData
      state.selectedRows.forEach(id => {
        formData.append('khachHangIds', id);
      });
      console.log('ID khách hàng đã chọn gửi đi:', state.selectedRows);
    }

    const res = await modifySize(formData);
    closeModal();
    emit('success');
    toast.success(res.message);
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      console.error('Lỗi trong quá trình gửi form:', error);
      toast.error('Đã xảy ra lỗi không mong muốn.');
    }
  }
};


onMounted(() => {
  idSanPham.value = route.query.id as string;
  if (idSanPham.value) {
    label.value = 'Sửa phiếu giảm giá';
  } else {
    label.value = 'Thêm phiếu giảm giá';
  }
  fetchProducts()
})
const router = useRouter()
const closeModal = () => {
  router.push({
    name: 'voucher-admin',
  });
};

</script>