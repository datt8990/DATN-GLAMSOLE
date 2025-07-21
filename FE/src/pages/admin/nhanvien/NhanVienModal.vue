<template>
  <div class="page-container">
    <div class="breadcrumb-section">
      <BreadcrumbDefault :pageTitle="pathName" :routes="[
        { path: '/admin/nhan-vien', name: 'Quản lý khách hàng' },
        { path: '/admin/them-nhan-vien', name: pathName }
      ]" />
    </div>
    <DivCustom label="Thêm nhân viên" customClasses="mt-5">
      <a-form :model="product" ref="productForm" name="productForm" autocomplete="off">
        <a-button style=" margin-left: 900px; color: white; background-color: #54bddb; width: 150px;" @click="openQrModal">Quét
          QR</a-button>
        <a-row gutter={16}>

          <a-col :span="8">
            <a-form-item label="Ảnh đại diện" name="avatar" :label-col="{ span: 24 }">
              <a-upload :before-upload="handleImageUpload" accept="image/*" :show-upload-list="false">
                <div v-if="!imageUrl" class="image-upload-container">
                  Chọn ảnh đại diện
                </div>
                <a-image v-else :src="imageUrl" class="image-upload-container"
                  style="width: 250px;   margin-left: 80px; height: 250px; border-radius: 10px; border-radius: 50%;" />
              </a-upload>

            </a-form-item>

            <a-form-item label="Tên Nhân viên" name="ten" :label-col="{ span: 24 }" :rules="rules.name">
              <a-input v-if="product" v-model:value="product.ten" placeholder="Nhập tên nhân viên"
                style="border-radius: 4px;" />
            </a-form-item>
          </a-col>

          <a-col :span="16" style="margin-top: 25px;">
            <a-row gutter={16}>
              <a-col :span="12">
                <a-form-item label="Mã định danh (Số CMND/CCCD)" name="cccd" :label-col="{ span: 24 }"
                  style="margin-left: 15px;" :rules="rules.cccd">
                  <a-input v-if="product" v-model:value="product.cccd" placeholder="Mã định danh"
                    style="border-radius: 4px;" />
                </a-form-item>

                <a-form-item label="Ngày sinh" name="ngaySinh" :label-col="{ span: 24 }" style="margin-left: 15px;"
                  :rules="rules.ngaySinh">
                  <a-date-picker v-model:value="product.ngaySinh" style="width: 100%" />
                </a-form-item>

                <a-form-item label="Số điện thoại" name="sdt" :label-col="{ span: 24 }" style="margin-left: 15px;"
                  :rules="rules.sdt">
                  <a-input v-if="product" v-model:value="product.sdt" placeholder="Số điện thoại"
                    style="border-radius: 4px;" />
                </a-form-item>
              </a-col>

              <a-col :span="12">
                <a-form-item label="Giới tính" name="gioiTinh" :label-col="{ span: 24 }" style="margin-left: 15px;">
                  <a-radio-group v-model:value="product.gioiTinh">
                    <a-radio :value="true">Nam</a-radio>
                    <a-radio :value="false">Nữ</a-radio>
                  </a-radio-group>
                </a-form-item>

                <a-form-item label="Email" name="email" :label-col="{ span: 24 }" :rules="rules.email"
                  style="margin-left: 15px;">
                  <a-input v-if="product" v-model:value="product.email" placeholder="Email"
                    style="border-radius: 4px;" />
                </a-form-item>

                <a-form-item label="Địa chỉ" name="diaChi" :label-col="{ span: 24 }" :rules="rules.diaChi"
                  style="margin-left: 15px;">
                  <a-input v-if="product" v-model:value="product.diaChi" placeholder="Địa chỉ"
                    style="border-radius: 4px;" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row gutter={16}>

              <a-col :span="8">
                <a-form-item style="width: 250px; margin-left: 15px;" label="Tỉnh/thành phố" name="tinhThanhPho"
                  :label-col="{ span: 24 }">
                  <a-select v-model:value="product.tinh" placeholder="Chọn tỉnh/thành phố">
                    <a-select-option v-for="province in tinhThanh" :key="province.code" :value="province.name">
                      {{ province.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>


              <a-col :span="8">
                <a-form-item style="width: 250px; margin-left: 15px;" label="Quận/huyện" name="quanHuyen"
                  :label-col="{ span: 24 }">
                  <a-select v-model:value="product.huyen" placeholder="Chọn quận/huyện">
                    <a-select-option v-for="district in quanHuyen" :key="district.code" :value="district.name">
                      {{ district.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>


              <a-col :span="8">
                <a-form-item style="width: 250px ; margin-left: 15px;" label="Xã/phường/Thị trấn" name="xaPhuong"
                  :label-col="{ span: 24 }">
                  <a-select v-model:value="product.xa" placeholder="Chọn xã/phường/thi trấn">
                    <a-select-option v-for="commune in phuongXa" :key="commune.code" :value="commune.name">
                      {{ commune.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
        </a-row>

        <a-form-item
          style="text-align: right; display: flex; justify-content: flex-end; margin-top: 30px; margin-right: 120px;">
          <a-button style="color: white; background-color: #54bddb; width: 150px; margin-right: 600px;" @click="closeModal">Quay
            lại</a-button>
          <a-button style="color: white; background-color: #54bddb; margin-right: 30px; width: 150px;" html-type="submit"
            @click="handleSubmit">{{ label }}</a-button>

        </a-form-item>
      </a-form>
      <a-modal v-model:visible="isQrModalVisible" title="Quét QR" @cancel="closeQrModal">
        <div id="reader" style="width: 100%; height: 300px;"></div>
      </a-modal>
    </DivCustom>
  </div>
</template>


<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted, nextTick } from 'vue';
import { getMember, type NhanVienResponse, modifyMember } from '@/services/api/admin/nhanvien.api';
import { toast } from 'vue3-toastify';
import dayjs from 'dayjs';
import DivCustom from '@/components/custom/Div/DivCustom.vue';
import axios from 'axios';
import { Html5Qrcode } from 'html5-qrcode';
import { useRoute, useRouter } from 'vue-router';
import BreadcrumbDefault from '@/components/ui/Breadcrumbs/BreadcrumbDefault.vue';

const route = useRoute();
const router = useRouter();
const pathName = ref('');
const idSanPham = ref('');

const props = defineProps<{ open: boolean; productId: string | null; title: string }>();
const emit = defineEmits(['close', 'success']);

const productForm = ref();
const imageUrl = ref<string | null>(null);
const imageFile = ref<File | null>(null);
const imageError = ref(false);

interface AdministrativeUnit {
  code: string;
  name: string;
}

const tinhThanh = ref<AdministrativeUnit[]>([]);
const quanHuyen = ref<AdministrativeUnit[]>([]);
const phuongXa = ref<AdministrativeUnit[]>([]);

const label = ref('');

const isQrModalVisible = ref(false);
const qrData = ref('');

let html5QrCode: Html5Qrcode;

// Khởi tạo product với các trường địa chỉ là chuỗi rỗng
const product = ref<NhanVienResponse>({
  id: '',
  ten: '',
  sdt: '',
  diaChi: '',
  email: '',
  ma: '',
  gioiTinh: true,
  tinh: '', // Bây giờ sẽ lưu tên tỉnh
  huyen: '', // Bây giờ sẽ lưu tên huyện
  xa: '', // Bây giờ sẽ lưu tên xã
});

const handleQrData = (message: string) => {
  const parts = message.split('|');
  product.value.cccd = parts[0];
  product.value.ten = parts[2];
  product.value.ngaySinh = parts[3] ? dayjs(parts[3], 'DDMMYYYY') : undefined;
  product.value.gioiTinh = parts[4] === 'Nam';
  product.value.diaChi = parts[5];
  // QR data might not contain specific province/district/commune names,
  // so these might still need manual selection or complex parsing.
};

const openQrModal = () => {
  isQrModalVisible.value = true;
  nextTick(() => {
    startQrScanning();
  });
};

const closeQrModal = () => {
  isQrModalVisible.value = false;
  stopQrScanning();
};

const startQrScanning = () => {
  const qrRegionId = "reader";
  const qrRegionElement = document.getElementById(qrRegionId);
  if (!qrRegionElement) {
    console.error("Không tìm thấy phần tử với id 'reader'");
    return;
  }

  html5QrCode = new Html5Qrcode(qrRegionId);

  Html5Qrcode.getCameras().then((cameras: { id: string, label: string }[]) => {
    if (cameras && cameras.length) {
      const cameraId = cameras[0].id;
      html5QrCode.start(
        cameraId,
        { fps: 10, qrbox: 250 },
        (qrCodeMessage: string) => {
          qrData.value = qrCodeMessage;
          console.log("QR Code message: ", qrData.value);
          handleQrData(qrCodeMessage);
          html5QrCode.stop();
          closeQrModal();
        },
        errorMessage => {
          console.warn('Lỗi đọc QR: ', errorMessage);
        }
      );
    } else {
      console.warn("Không tìm thấy camera nào!");
    }
  }).catch((error: any) => {
    console.error("Lỗi khi lấy camera: ", error);
  });
};

const stopQrScanning = () => {
  if (html5QrCode) {
    html5QrCode.stop().catch(err => console.error('Không thể dừng scanner:', err));
  }
};

const fetchTinhThanh = async () => {
  try {
    const response = await axios.get('https://provinces.open-api.vn/api/p');
    tinhThanh.value = response.data;
  } catch (error) {
    toast.error('Không lấy được danh sách tỉnh.');
  }
};

// Hàm này nhận MÃ CODE tỉnh để fetch quận/huyện
const fetchQuanHuyen = async (provinceCode: string) => {
  try {
    const response = await axios.get(`https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`);
    quanHuyen.value = response.data.districts;
    phuongXa.value = []; // Clear phuongXa when province changes
  } catch (error) {
    toast.error('Không lấy được danh sách quận/huyện.');
  }
};

// Hàm này nhận MÃ CODE huyện để fetch xã/phường
const fetchPhuongXa = async (districtCode: string) => {
  try {
    const response = await axios.get(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`);
    phuongXa.value = response.data.wards;
  } catch (error) {
    toast.error('Không lấy được danh sách phường/xã.');
  }
};

const rules = {
  name: [{ required: true, message: 'Tên không được để trống!', trigger: 'blur' }],
  avatar: [{ required: true, message: 'Ảnh đại diện không được để trống!', trigger: 'blur' }],
  cccd: [
    { required: true, message: 'Mã định danh không được để trống!', trigger: 'blur' },
    { pattern: /^[0-9]{9,12}$/, message: 'Mã định danh phải là số và có độ dài từ 9 đến 12 ký tự!', trigger: 'blur' },
  ],
  ngaySinh: [{ required: true, message: 'Ngày sinh không được để trống!', trigger: 'blur' }],
  sdt: [
    { required: true, message: 'Số điện thoại không được để trống!', trigger: 'blur' },
    { pattern: /^[0-9]{10}$/, message: 'Số điện thoại phải là 10 chữ số!', trigger: 'blur' },
  ],
  gioiTinh: [{ required: true, message: 'Giới tính không được để trống!', trigger: 'blur' }],
  email: [
    { required: true, message: 'Email không được để trống!', trigger: 'blur' },
    { type: 'email', message: 'Email không hợp lệ!', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9._%+-]+@gmail\.com$/, message: 'Email phải có đuôi @gmail.com!', trigger: 'blur' }
  ],
  diaChi: [{ required: true, message: 'Địa chỉ không được để trống!', trigger: 'blur' }],
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getMember(id);
    const data = response.data;

    product.value = {
      ...data,
      ngayTuyenDung: data.ngayTuyenDung ? dayjs(data.ngayTuyenDung) : undefined,
      ngaySinh: data.ngaySinh ? dayjs(data.ngaySinh) : undefined,
      gioiTinh: data.gioiTinh === true || data.gioiTinh === 'true',
    };

    imageUrl.value = data.avatar || null;

    // Sau khi gán product.value (đã có tên tỉnh/huyện/xã từ API của bạn)
    // chúng ta cần tìm code của chúng để gọi API bên thứ 3 (provinces.open-api.vn)
    if (product.value.tinh) {
      const provinceFound = tinhThanh.value.find(p => p.name === product.value.tinh);
      if (provinceFound) {
        await fetchQuanHuyen(provinceFound.code);
        // Đảm bảo quanHuyen đã được populate trước khi tìm district
        if (product.value.huyen && quanHuyen.value.length > 0) {
          const districtFound = quanHuyen.value.find(d => d.name === product.value.huyen);
          if (districtFound) {
            await fetchPhuongXa(districtFound.code);
          }
        }
      }
    }

  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    }
  }
};

watch(
  () => [idSanPham, props.open],
  async ([id, isOpen]) => {
    if (isOpen) {
      if (productForm.value) {
        productForm.value.resetFields();
      }
      // Luôn fetch danh sách tỉnh đầu tiên
      await fetchTinhThanh();

      if (id) {
        // Sau khi có danh sách tỉnh, mới fetch chi tiết sản phẩm
        // để có thể tìm code từ tên tỉnh/huyện đã lưu
        await fetchProductDetails(id as string);
      } else {
        // Nếu không có id (thêm mới), reset product và ảnh
        product.value = {
          id: '', ten: '', sdt: '', diaChi: '', email: '', ma: '', gioiTinh: true, tinh: '', huyen: '', xa: ''
        };
        imageUrl.value = null;
        imageFile.value = null;
        imageError.value = false;
        quanHuyen.value = [];
        phuongXa.value = [];
      }
    }
  },
  { immediate: true }
);

const handleImageUpload = (file: File) => {
  const reader = new FileReader();
  reader.onload = (e) => {
    imageUrl.value = e.target?.result as string;
    imageError.value = false;
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
    formData.append('code', product?.value.ma?.trim() || '');
    formData.append('cccd', product?.value?.cccd?.trim() || '');
    formData.append('user', product?.value.user?.trim() || '');
    formData.append('ten', product?.value.ten?.trim() || '');
    formData.append('ngaySinh', product.value?.ngaySinh ? dayjs(product.value?.ngaySinh).format('YYYY-MM-DD') : '');
    formData.append('email', product?.value.email?.trim() || '');
    formData.append('sdt', product?.value.sdt?.trim() || '');
    formData.append('diaChi', product?.value.diaChi?.trim() || '');
    formData.append('gioiTinh', product.value.gioiTinh?.toString() || 'true');

    // Gửi trực tiếp tên tỉnh, huyện, xã đã chọn
    formData.append('tinh', product.value.tinh || '');
    formData.append('huyen', product.value.huyen || '');
    formData.append('xa', product.value.xa || '');

    if (imageFile.value) {
      formData.append('avatar', imageFile.value);
    }

    const res = await modifyMember(formData);

    nextTick(() => {
      sessionStorage.setItem('appToastMessage', JSON.stringify({
        message: res.message || 'Thao tác thành công!',
        type: 'success'
      }));
      router.push({
        name: 'nhan-vien-admin',
      });
    });
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else {
      toast.error('Có lỗi xảy ra khi gửi dữ liệu!');
    }
  }
};

onMounted(() => {
  idSanPham.value = route.query.id as string;
  if (idSanPham.value) {
    label.value = 'Sửa nhân viên';
     pathName.value = 'Sửa nhân viên';
  } else {
    label.value = 'Thêm nhân viên';
    pathName.value = 'Thêm nhân viên';
  }
  if (idSanPham.value) {
    fetchProductDetails(idSanPham.value);  // Gọi hàm lấy chi tiết
  }
  fetchTinhThanh(); // Fetch provinces on mount
  // Việc fetch data ban đầu và cascade đã được handle bởi watcher của idSanPham và props.open
  // (Không cần gọi fetchProductDetails/fetchTinhThanh trực tiếp ở đây nữa để tránh trùng lặp)
});

// Watch cho thay đổi của product.value.tinh (tên tỉnh)
watch(() => product.value.tinh, async (newVal, oldVal) => {
  // Tránh việc chạy watcher khi component khởi tạo và product.tinh chưa có giá trị
  // hoặc khi giá trị không đổi (trường hợp load ban đầu có thể trùng tên)
  if (newVal !== oldVal) {
    // Reset huyện và xã khi tỉnh thay đổi, trừ khi là lần đầu load dữ liệu
    // Kiểm tra oldVal để tránh reset khi product được gán lần đầu
    if (oldVal !== undefined && oldVal !== null && oldVal !== '') {
      product.value.huyen = '';
      product.value.xa = '';
      quanHuyen.value = []; // Clear list for new province
      phuongXa.value = []; // Clear list for new province
    }

    if (newVal) { // Nếu có giá trị mới (tên tỉnh)
      const provinceFound = tinhThanh.value.find(p => p.name === newVal);
      if (provinceFound) {
        await fetchQuanHuyen(provinceFound.code);
      } else {
        // Tên tỉnh không hợp lệ hoặc không tìm thấy, xóa danh sách huyện/xã
        quanHuyen.value = [];
        phuongXa.value = [];
      }
    } else { // Nếu giá trị mới là rỗng (người dùng xóa chọn)
      quanHuyen.value = [];
      phuongXa.value = [];
    }
  }
});

// Watch cho thay đổi của product.value.huyen (tên huyện)
watch(() => product.value.huyen, async (newVal, oldVal) => {
  if (newVal !== oldVal) {
    // Reset xã khi huyện thay đổi, trừ khi là lần đầu load dữ liệu
    if (oldVal !== undefined && oldVal !== null && oldVal !== '') {
      product.value.xa = '';
      phuongXa.value = []; // Clear list for new district
    }

    if (newVal && product.value.tinh) { // Chỉ fetch nếu có tên huyện và tỉnh đã được chọn
      const districtFound = quanHuyen.value.find(d => d.name === newVal);
      if (districtFound) {
        await fetchPhuongXa(districtFound.code);
      } else {
        phuongXa.value = [];
      }
    } else {
      phuongXa.value = [];
    }
  }
});

const closeModal = () => {
  router.push({
    name: 'nhan-vien-admin',
  });
};
</script>

<style scoped>
/* (Giữ nguyên phần style của bạn) */
.input-item {
  margin-left: 10px;
  margin-bottom: 16px;
  /* Tạo khoảng cách giữa các input */
}

.image-upload-container {

  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 50%;
  width: 250px;
  height: 250px;
  border: 2px dashed #1890ff;
  cursor: pointer;
}



/* Thêm màu xanh nước biển nhạt cho các phần chọn ảnh */
.image-upload-container {
  margin-left: 80px;
  width: 250px;
  /* Điều chỉnh kích thước của vùng chọn ảnh */
  height: 250px;
  border-radius: 10px;
  /* Bo tròn góc để trông mềm mại hơn */
  border: 2px dashed #1890ff;
  /* Đặt viền màu xanh với gạch chéo */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background-color: #f5f5f5;
  /* Thêm màu nền nhẹ */
  text-align: center;
  border-radius: 50%;
}


a-image .avatar-upload {
  /* width:  300px;
  height:  300px; */
  border-radius: 50%;
  /* border: 2px dashed #1890ff; */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.avatar-image {
  /* width: 300px;
  height: 300px; */
  border-radius: 50%;
}

.upload-text {
  color: #66b3ff;
  margin-top: 8px;
  font-size: 14px;
}

.upload-text:hover {
  text-decoration: underline;
}

/* Tăng kích thước cho a-upload */
.a-upload {
  width: 150px;
  /* Kích thước lớn hơn cho vùng chọn ảnh */
  height: 150px;
  border-radius: 50%;
  /* Tạo hình tròn */
  border: none !important;
  /* Loại bỏ viền */
  outline: none !important;
  /* Loại bỏ outline */
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background-color: #f5f5f5;
  /* Thêm màu nền nhẹ để nổi bật hơn */
}

.a-upload .ant-upload-trigger {
  width: 100%;
  /* Đảm bảo vùng chọn ảnh chiếm toàn bộ không gian */
  height: 100%;
  border: none !important;
  /* Loại bỏ viền của button */
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-container {
  padding: 20px; /* Overall padding for the page content */
}

.breadcrumb-section {
  margin-bottom: 25px; /* Space below the breadcrumb and above the first section */
  background-color: #fff; /* White background for the breadcrumb box */
  padding: 15px 20px; /* Padding inside the breadcrumb box */
  border-radius: 8px; /* Rounded corners */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09); /* Subtle shadow */
}

.section-title {
  margin-top: 30px; /* Space above each main section title */
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px; /* Space below the title */
  margin-left: 0px; /* Remove left margin if section-title is directly under padding */
  color: #333; /* Darker color for titles */
  display: flex; /* To align icon and text */
  align-items: center; /* Vertically center icon and text */
  gap: 8px; /* Space between icon and text */
}
 
/* Remove or adjust body styles if they are global.
   Scoped styles prevent them from affecting the entire app. */
body {
  font-family: 'Roboto', sans-serif;
}

.a-upload .ant-upload-list-picture-card {
  display: none;
  /* Ẩn hình ảnh nếu không có ảnh */
}

/* Tăng kích thước icon */
.ant-upload .ant-upload-trigger a-icon {
  font-size: 40px;
  /* Tăng kích thước icon "+" */
}
</style>