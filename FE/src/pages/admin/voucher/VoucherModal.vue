<template>
  <div class="breadcrumb-section">
    <BreadcrumbDefault :pageTitle="pathName" :routes="[
      { path: '/admin/voucher', name: 'Quản lý phiếu giảm giá' },
      { path: '/admin/them-phieu-giam-gia', name: pathName }
    ]" />
  </div>
  <DivCustom label="Thêm Phiếu giảm giá" customClasses="mt-5">

    <a-row gutter={16}>

      <a-col :span="8">

        <a-form :model="product" ref="productForm" name="productForm" autocomplete="off">
          <a-form-item label="Tên phiếu giảm giá" name="ten" :label-col="{ span: 24 }" :rules="rules.name">
            <a-input v-if="product" v-model:value="product.ten" placeholder="Nhập tên phiếu giảm giá"
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


          <a-form-item label="Ngày bắt đầu" name="ngayBatDau" :rules="rules.ngayBatDau" :label-col="{ span: 24 }">
            <a-date-picker v-model:value="product.ngayBatDau" format="YYYY-MM-DD" style="width: 100%;" />
          </a-form-item>

          <a-form-item label="Ngày kết thúc" name="ngayKetThuc" :rules="rules.ngayKetThuc" :label-col="{ span: 24 }">
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

                 {{ console.log('Record ID from table:', record.id, ' | Selected IDs:',
                  state.selectedRows, ' | Match:', state.selectedRows.includes(record.id)) }} <a-checkbox
                  :checked="state.selectedRows.includes(record.id)"
                  @change="onCheckboxChange(record.id, $event.target.checked)" />

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


// Đảm bảo đường dẫn import cho các API là chính xác
import { getSize, type SizeResponse, modifySize, getListKH } from '@/services/api/admin/voucher.api';
import { toast } from 'vue3-toastify';
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
  kieuGiam: false, // Mặc định là 'Tiền'
  loaiGiam: false, // Mặc định là 'Công khai'
  phanTramGiam: 0,
  soLuongPhieu: undefined,
  dieuKien: undefined,
  giaGiam: undefined,
  ngayBatDau: undefined,
  ngayKetThuc: undefined,
});
const pathName = ref('');
const productForm = ref();
const label = ref('');
import dayjs from 'dayjs';
import BreadcrumbDefault from '@/components/ui/Breadcrumbs/BreadcrumbDefault.vue';

const rules = {
  name: [{ required: true, message: 'Trường này không được để trống!', trigger: 'blur' }],
  soLuongPhieu: [
    { required: true, message: 'Số lượng phiếu không được để trống', trigger: 'blur' },
    { type: 'number', min: 1, message: 'Số lượng phiếu phải lớn hơn 0', trigger: 'blur' }
  ],
  dieuKien: [
    { required: true, message: 'Điều kiện giảm giá không được để trống', trigger: 'blur' },
    { type: 'number', min: 1, message: 'Điều kiện phải lớn hơn 0', trigger: 'blur' }
  ],
  giaGiam: [
    { required: true, message: 'Giá trị giảm không được để trống', trigger: 'blur' },
    { type: 'number', min: 1, message: 'Giá trị giảm phải lớn hơn 0', trigger: 'blur' }
  ],
  ngayBatDau: [
    {
      required: true,
      message: 'Vui lòng chọn ngày bắt đầu',
      trigger: 'change'
    },
    {
      validator: (rule: any, value: any) => {
        const ngayKetThuc = product.value?.ngayKetThuc;
        if (ngayKetThuc && value && dayjs(value).isAfter(dayjs(ngayKetThuc))) {
          return Promise.reject('Ngày bắt đầu phải trước ngày kết thúc');
        }
        return Promise.resolve();
      },
      trigger: 'change'
    }
  ],
  ngayKetThuc: [
    {
      required: true,
      message: 'Vui lòng chọn ngày kết thúc',
      trigger: 'change'
    },
    {
      validator: (rule: any, value: any) => {
        if (value && dayjs(value).isBefore(dayjs(), 'day')) {
          return Promise.reject('Ngày kết thúc không được là ngày quá khứ');
        }
        const ngayBatDau = product.value?.ngayBatDau;
        if (ngayBatDau && dayjs(value).isBefore(dayjs(ngayBatDau))) {
          return Promise.reject('Ngày kết thúc phải sau ngày bắt đầu');
        }
        return Promise.resolve();
      },
      trigger: 'change'
    }
  ]
};


const fetchProductDetails = async (id: string) => {
  try {
    const response = await getSize(id);
    const data = response.data;
    product.value = {
      ...data,
      ngayBatDau: data.ngayBatDau ? dayjs(data.ngayBatDau) : undefined,
      ngayKetThuc: data.ngayKetThuc ? dayjs(data.ngayKetThuc) : undefined,
      // Đảm bảo loaiGiam và kieuGiam là boolean
      loaiGiam: data.loaiGiam === true || data.loaiGiam === 'true',
      kieuGiam: data.kieuGiam === true || data.kieuGiam === 'true',
      phanTramGiam: data.phanTramGiam ?? 0,
    };
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      console.error('Lỗi khi lấy chi tiết phiếu giảm giá:', error);
      toast.error('Đã xảy ra lỗi khi lấy chi tiết phiếu giảm giá.');
    }
  }
};

const fetchListKHDetails = async (id: string) => {
  try {
    // Đặt tên biến là 'apiResult' để rõ ràng đây là kết quả trực tiếp từ API call
    const apiResult = await getListKH(id);

    console.log("Raw result from getListKH (for debugging):", apiResult); // LOG API response to see its structure

    let processedIds: string[] = [];

    // Case 1: API returns a direct array of IDs (most common with getListKH)
    if (Array.isArray(apiResult)) {
      processedIds = apiResult as string[];
    }
    // Case 2: API returns an object where keys are numeric indices (like a JS array-like object)
    else if (apiResult && typeof apiResult === 'object' && Object.keys(apiResult).every(key => !isNaN(Number(key)))) {
      processedIds = Object.values(apiResult) as string[];
    }
    // Case 3: API returns an object with a 'data' property that is an array of IDs
    else if (apiResult && (apiResult as any).data && Array.isArray((apiResult as any).data)) {
      processedIds = (apiResult as any).data as string[];
    }
    // Case 4: API returns an object with a 'data' property that is an array of customer objects (need to extract 'id')
    else if (apiResult && (apiResult as any).data && Array.isArray((apiResult as any).data) && (apiResult as any).data.length > 0 && typeof (apiResult as any).data[0] === 'object' && 'id' in (apiResult as any).data[0]) {
      processedIds = (apiResult as any).data.map((item: any) => item.id) as string[];
    }
    // Fallback: If none of the above, assume no IDs or unexpected format
    else {
      console.warn("Unexpected data format from getListKH, setting selectedRows to empty:", apiResult);
      processedIds = [];
    }

    state.selectedRows = processedIds;
    console.log("Selected customer IDs after processing:", state.selectedRows);
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      console.error("Error fetching list of customer IDs:", error);
      toast.error('Đã xảy ra lỗi khi lấy danh sách khách hàng liên kết.');
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
  selectAll: false, // Hiện tại không sử dụng, nhưng có thể giữ lại cho tương lai
  selectedRows: [] as string[], // Mảng này sẽ chứa các ID của khách hàng được chọn
  searchQuery: '',
  searchStatus: null as number | null,
  isModalOpen: false, // Các biến modal này có vẻ không được dùng trong component này nữa, cân nhắc xóa nếu không cần
  isModalChangeStatus: false,
  selectedProductId: null as string | null,
  products: [] as KhachHangResponse[], // Dữ liệu cho bảng khách hàng
  paginationParams: { page: 1, size: 10 },
  totalItems: 0
})

const resetFilters = () => {
  state.searchQuery = ''

  // Có thể thêm fetchProducts() ở đây nếu muốn làm mới bảng sau khi reset
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

    console.log("Raw response from GetKhachHangs (for table data):", response); // LOG THIS!
    console.log("Customer data for table (response.data?.data):", response.data?.data); // AND THIS!

    // Gán dữ liệu khách hàng cho bảng
    state.products = response.data?.data || []; // Đảm bảo là mảng rỗng nếu không có data
    state.totalItems = response.data?.totalElements || 0;
  } catch (error) {
    console.error('Failed to fetch customers for table:', error)
    toast.error('Đã xảy ra lỗi khi lấy danh sách khách hàng.');
  }
}

watch(
  () => [props.productId, props.open],
  async ([newId, isOpen]) => {
    if (isOpen) {
      if (productForm.value) {
        productForm.value.resetFields();
      }
      // Luôn đảm bảo reset selectedRows khi mở form (đặc biệt khi thêm mới)
      state.selectedRows = [];

      if (newId) {
        // Edit mode
        label.value = 'Sửa phiếu giảm giá';
        await fetchProductDetails(newId);
        // Sau khi lấy chi tiết phiếu giảm giá, gọi để lấy danh sách khách hàng liên quan
        // Đảm bảo fetchListKHDetails được gọi sau khi product.loaiGiam đã được cập nhật
        // Nếu loaiGiam là 'Cá nhân' thì mới cần fetch danh sách KH
        if (product.value.loaiGiam === true) {
          await fetchListKHDetails(newId);
        }

      } else {

        // Add new mode
        label.value = 'Thêm phiếu giảm giá';
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
      // Luôn fetch danh sách sản phẩm để hiển thị bảng (nếu loaiGiam là cá nhân)
      await fetchProducts();
    }
  },
  { immediate: true }
);



// Hàm xử lý thay đổi ô kiểm
const onCheckboxChange = (id: string, checked: boolean) => {
  if (checked) {
    // Chỉ thêm vào nếu chưa tồn tại (để tránh trùng lặp)
    if (!state.selectedRows.includes(id)) {
      state.selectedRows.push(id);
    }
  } else {
    state.selectedRows = state.selectedRows.filter(rowId => rowId !== id);
  }
  console.log('Current selected rows:', state.selectedRows);
};


const handleSubmit = async () => {
  try {
    await productForm.value.validate();

    const formData = new FormData();
    formData.append('id', product?.value?.id?.trim() || '');
    formData.append('ma', product?.value?.ma?.trim() || '');
    formData.append('ten', product?.value.ten?.trim() || ''); // Đảm bảo không gửi undefined

    // Logic cho phanTramGiam và giaGiam
    if (product.value?.kieuGiam === true) { // Nếu kiểu giảm là phần trăm
      const phanTramGiamValue = product?.value?.phanTramGiam != null ? product?.value?.phanTramGiam.toString() : '0';
      formData.append('phanTramGiam', phanTramGiamValue);
    } else {
      const giaGiamValue = product?.value?.giaGiam != null ? product?.value?.giaGiam.toString() : '0';
      formData.append('phanTramGiam', giaGiamValue);
    }

    formData.append('soLuongPhieu', product?.value?.soLuongPhieu != null ? product.value.soLuongPhieu.toString() : '');
    formData.append('dieuKien', product?.value?.dieuKien != null ? product.value.dieuKien.toString() : '');

    formData.append('giaGiamToiDa', product?.value?.giaGiam != null ? product.value.giaGiam.toString() : '');
    formData.append('ngayKetThuc', product.value?.ngayKetThuc ? dayjs(product.value?.ngayKetThuc).format('YYYY-MM-DD') : '');
    formData.append('ngayBatDau', product.value?.ngayBatDau ? dayjs(product.value?.ngayBatDau).format('YYYY-MM-DD') : '');
    formData.append('loaiGiam', product.value?.loaiGiam?.toString() || 'false');
    formData.append('kieuGiam', product.value?.kieuGiam?.toString() || 'false');

    if (product.value.loaiGiam === true) {
      if (state.selectedRows.length === 0) {
        toast.error('Vui lòng chọn ít nhất một khách hàng cho phiếu giảm giá cá nhân.');
        return; // Dừng hàm nếu không có khách hàng nào được chọn
      }

      state.selectedRows.forEach(id => {
        formData.append('khachHangIds', id);
      });
      console.log('ID khách hàng đã chọn gửi đi:', state.selectedRows);
    }

    const res = await modifySize(formData);
    closeModal();
    emit('success');
    toast.success(res.message);
  } catch (error: any) { // Thêm type any cho error để truy cập thuộc tính
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      console.error('Lỗi trong quá trình gửi form:', error);
      toast.error('Đã xảy ra lỗi không mong muốn.');
    }
  }
};

const router = useRouter()
const closeModal = () => {
  router.push({
    name: 'voucher-admin',
  });
};

onMounted(() => {
  idSanPham.value = route.query.id as string;

  if (idSanPham.value) {
    label.value = 'Sửa phiếu giảm giá';
    fetchProductDetails(idSanPham.value)
    fetchListKHDetails(idSanPham.value)
    pathName.value = 'Sửa phiếu giảm giá';
  } else {
    pathName.value = 'Thêm phiếu giảm giá';
    label.value = 'Thêm phiếu giảm giá';
  }

  fetchProducts()
});

</script>


<style scoped>
.page-container {
  padding: 20px;
  /* Overall padding for the page content */
}

.breadcrumb-section {
  margin-bottom: 25px;
  /* Space below the breadcrumb and above the first section */
  background-color: #fff;
  /* White background for the breadcrumb box */
  padding: 15px 20px;
  /* Padding inside the breadcrumb box */
  border-radius: 8px;
  /* Rounded corners */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  /* Subtle shadow */
}

.section-title {
  margin-top: 30px;
  /* Space above each main section title */
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  /* Space below the title */
  margin-left: 0px;
  /* Remove left margin if section-title is directly under padding */
  color: #333;
  /* Darker color for titles */
  display: flex;
  /* To align icon and text */
  align-items: center;
  /* Vertically center icon and text */
  gap: 8px;
  /* Space between icon and text */
}

/* Remove or adjust body styles if they are global.
   Scoped styles prevent them from affecting the entire app. */
body {
  font-family: 'Roboto', sans-serif;
}
</style>