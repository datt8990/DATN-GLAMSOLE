
<template>
  <div class="page-container">
    <div class="breadcrumb-section">
      <BreadcrumbDefault
        :pageTitle="pathName"
        :routes="[
          { path: '/admin/nhan-vien', name: 'Quản lý khách hàng' },
          { path: '/admin/them-nhan-vien', name: pathName },
        ]"
      />
    </div>
    <DivCustom label="Thêm nhân viên" customClasses="mt-5">
      <a-form
        :model="product"
        ref="productForm"
        name="productForm"
        autocomplete="off"
      >
        <a-button
          style="
            margin-left: auto;
            color: white;
            background-color: #54bddb;
            width: 150px;
            border: none;
            display: block;
            margin-bottom: 20px;
          "
          @click="openQrModal"
          class="custom-button qr-button-responsive"
        >
          <QrcodeOutlined /> Quét QR</a-button
        >
        <a-row :gutter="16">
          <a-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
            <a-form-item
              label="Ảnh đại diện"
              name="avatar"
              :label-col="{ span: 24 }"
            >
              <a-upload
                :before-upload="handleImageUpload"
                accept="image/*"
                :show-upload-list="false"
              >
                <div v-if="!imageUrl" class="image-upload-container">
                  Chọn ảnh đại diện
                </div>
                <a-image
                  v-else
                  :src="imageUrl"
                  class="image-upload-container"
                  style="
                    width: 250px;
                    margin-left: auto;
                    margin-right: auto;
                    display: block;
                    height: 250px;
                    border-radius: 50%;
                  "
                />
              </a-upload>
            </a-form-item>

            <a-form-item
              label="Tên nhân viên"
              name="ten"
              :label-col="{ span: 24 }"
              :rules="rules.name"
            >
              <a-input
                v-if="product"
                v-model:value="product.ten"
                placeholder="Nhập tên nhân viên"
                style="border-radius: 4px"
              />
            </a-form-item>
          </a-col>

          <a-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16" class="right-section">
            <a-row :gutter="16">
              <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                <a-form-item
                  label="Mã định danh (Số CMND/CCCD)"
                  name="cccd"
                  :label-col="{ span: 24 }"
                  class="form-item-spacing"
                  :rules="rules.cccd"
                >
                  <a-input
                    v-if="product"
                    v-model:value="product.cccd"
                    placeholder="Nhập mã định danh"
                    style="border-radius: 4px"
                  />
                </a-form-item>

                <a-form-item
                  label="Ngày sinh"
                  name="ngaySinh"
                  :label-col="{ span: 24 }"
                  class="form-item-spacing"
                  :rules="rules.ngaySinh"
                >
                  <a-date-picker
                    v-model:value="product.ngaySinh"
                    placeholder="Chọn ngày sinh"
                    style="width: 100%"
                  />
                </a-form-item>

                <a-form-item
                  label="Số điện thoại"
                  name="sdt"
                  :label-col="{ span: 24 }"
                  class="form-item-spacing"
                  :rules="rules.sdt"
                >
                  <a-input
                    v-if="product"
                    v-model:value="product.sdt"
                    placeholder="Nhập số điện thoại"
                    style="border-radius: 4px"
                  />
                </a-form-item>
              </a-col>

              <a-col :xs="24" :sm="12" :md="12" :lg="12" :xl="12">
                <a-form-item
                  label="Giới tính"
                  name="gioiTinh"
                  :label-col="{ span: 24 }"
                  class="form-item-spacing"
                >
                  <a-radio-group
                    v-model:value="product.gioiTinh"
                    class="gender-radio-group"
                  >
                    <a-radio :value="true">Nam</a-radio>
                    <a-radio :value="false">Nữ</a-radio>
                  </a-radio-group>
                </a-form-item>

                <a-form-item
                  label="Email"
                  name="email"
                  :label-col="{ span: 24 }"
                  :rules="rules.email"
                  class="form-item-spacing"
                >
                  <a-input
                    v-if="product"
                    v-model:value="product.email"
                    placeholder="Nhập email"
                    style="border-radius: 4px"
                  />
                </a-form-item>

                <a-form-item
                  label="Địa chỉ"
                  name="diaChi"
                  :label-col="{ span: 24 }"
                  :rules="rules.diaChi"
                  class="form-item-spacing"
                >
                  <a-input
                    v-if="product"
                    v-model:value="product.diaChi"
                    placeholder="Nhập địa chỉ"
                    style="border-radius: 4px"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="16">
              <!-- Tỉnh/Thành phố -->
              <a-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
                <a-form-item
                  label="Tỉnh/thành phố"
                  name="tinh"
                  :label-col="{ span: 24 }"
                  :rules="rules.tinh"
                >
                  <a-select
                    v-model:value="product.tinh"
                    placeholder="Chọn tỉnh/thành phố"
                  >
                    <a-select-option
                      v-for="province in tinhThanh"
                      :key="province.code"
                      :value="province.name"
                    >
                      {{ province.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>

              <!-- Quận/Huyện -->
              <a-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
                <a-form-item
                  label="Quận/huyện"
                  name="huyen"
                  :label-col="{ span: 24 }"
                  :rules="rules.huyen"
                >
                  <a-select
                    v-model:value="product.huyen"
                    placeholder="Chọn quận/huyện"
                  >
                    <a-select-option
                      v-for="district in quanHuyen"
                      :key="district.code"
                      :value="district.name"
                    >
                      {{ district.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>

              <!-- Xã/Phường/Thị trấn -->
              <a-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8">
                <a-form-item
                  label="Xã/phường/Thị trấn"
                  name="xa"
                  :label-col="{ span: 24 }"
                  :rules="rules.xa"
                >
                  <a-select
                    v-model:value="product.xa"
                    placeholder="Chọn xã/phường/thị trấn"
                  >
                    <a-select-option
                      v-for="commune in phuongXa"
                      :key="commune.code"
                      :value="commune.name"
                    >
                      {{ commune.name }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
          </a-col>
        </a-row>

        <a-form-item class="form-buttons">
          <div class="button-container">
            <a-button
              style="
                color: white;
                background-color: #54bddb;
                width: 150px;
                border: none;
              "
              @click="closeModal"
              class="custom-button"
              >Quay lại</a-button
            >
            <a-button
              style="
                color: white;
                background-color: #54bddb;
                width: 150px;
                border: none;
              "
              html-type="submit"
              @click="handleSubmit"
              class="custom-button"
              >{{ label }}</a-button
            >
          </div>
        </a-form-item>
      </a-form>
      <a-modal
        v-model:visible="isQrModalVisible"
        title="Quét QR"
        @cancel="closeQrModal"
      >
        <div id="reader" style="width: 100%; height: 300px"></div>
      </a-modal>
    </DivCustom>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, defineProps, defineEmits, onMounted, nextTick } from "vue";
import {
  getMember,
  type NhanVienResponse,
  modifyMember,
} from "@/services/api/admin/nhanvien.api";
import { toast } from "vue3-toastify";
import dayjs from "dayjs";
import DivCustom from "@/components/custom/Div/DivCustom.vue";
import axios from "axios";
import { Html5Qrcode } from "html5-qrcode";
import { useRoute, useRouter } from "vue-router";
import BreadcrumbDefault from "@/components/ui/Breadcrumbs/BreadcrumbDefault.vue";
import { QrcodeOutlined } from "@ant-design/icons-vue";

const route = useRoute();
const router = useRouter();
const pathName = ref("");
const idSanPham = ref("");

const props = defineProps<{
  open: boolean;
  productId: string | null;
  title: string;
}>();
const emit = defineEmits(["close", "success"]);

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

const label = ref("");

const isQrModalVisible = ref(false);
const qrData = ref("");

let html5QrCode: Html5Qrcode;

// Khởi tạo product với các trường địa chỉ là chuỗi rỗng
const product = ref<NhanVienResponse>({
  id: "",
  ten: "",
  sdt: "",
  diaChi: "",
  email: "",
  ma: "",
  gioiTinh: true,
  tinh: null, // Bây giờ sẽ lưu tên tỉnh
  huyen: null, // Bây giờ sẽ lưu tên huyện
  xa: null, // Bây giờ sẽ lưu tên xã
});

const handleQrData = (message: string) => {
  const parts = message.split("|");
  product.value.cccd = parts[0];
  product.value.ten = parts[2];
  product.value.ngaySinh = parts[3] ? dayjs(parts[3], "DDMMYYYY") : undefined;
  product.value.gioiTinh = parts[4] === "Nam";
  product.value.diaChi = parts[5];
  // QR data might not contain specific province/district/commune names,
  // so these might still need manual selection or complex parsing.
};

const validateName = (rule: any, value: string) => {
  if (!value || value.trim() === "") {
    return Promise.reject("Tên không được để trống!");
  }

  // Kiểm tra độ dài tối thiểu (ít nhất 2 ký tự)
  if (value.trim().length < 2) {
    return Promise.reject("Tên phải có ít nhất 2 ký tự!");
  }

  // Kiểm tra chỉ chứa chữ cái và khoảng trắng
  const nameRegex =
    /^[a-zA-ZàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ\s]+$/;
  if (!nameRegex.test(value.trim())) {
    return Promise.reject("Tên chỉ được chứa chữ cái và khoảng trắng!");
  }

  return Promise.resolve();
};

const validateCCCD = async (rule: any, value: string) => {
  if (!value || value.trim() === "") {
    return Promise.reject("Mã định danh không được để trống!");
  }

  const cccdRegex = /^[0-9]{9,12}$/;
  if (!cccdRegex.test(value.trim())) {
    return Promise.reject(
      "Mã định danh phải là số và có độ dài từ 9 đến 12 ký tự!"
    );
  }

  // Kiểm tra trùng CCCD (chỉ khi thêm mới hoặc sửa với CCCD khác)
  try {
    const response = await checkDuplicateField(
      "cccd",
      value.trim(),
      product.value.id
    );
    if (response.data.exists) {
      return Promise.reject("Mã định danh đã tồn tại trong hệ thống!");
    }
  } catch (error) {
    console.error("Lỗi kiểm tra CCCD:", error);
  }

  return Promise.resolve();
};

const validatePhone = async (rule: any, value: string) => {
  if (!value || value.trim() === "") {
    return Promise.reject("Số điện thoại không được để trống!");
  }

  const phoneRegex = /^[0-9]{10}$/;
  if (!phoneRegex.test(value.trim())) {
    return Promise.reject("Số điện thoại phải là 10 chữ số!");
  }

  // Kiểm tra trùng số điện thoại
  try {
    const response = await checkDuplicateField(
      "sdt",
      value.trim(),
      product.value.id
    );
    if (response.data.exists) {
      return Promise.reject("Số điện thoại đã tồn tại trong hệ thống!");
    }
  } catch (error) {
    console.error("Lỗi kiểm tra số điện thoại:", error);
  }

  return Promise.resolve();
};

const validateEmail = async (rule: any, value: string) => {
  if (!value || value.trim() === "") {
    return Promise.reject("Email không được để trống!");
  }

  const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
  if (!emailRegex.test(value.trim())) {
    return Promise.reject(
      "Email phải có định dạng hợp lệ và có đuôi @gmail.com!"
    );
  }

  // Kiểm tra trùng email
  try {
    const response = await checkDuplicateField(
      "email",
      value.trim(),
      product.value.id
    );
    if (response.data.exists) {
      return Promise.reject("Email đã tồn tại trong hệ thống!");
    }
  } catch (error) {
    console.error("Lỗi kiểm tra email:", error);
  }

  return Promise.resolve();
};

const checkDuplicateField = async (
  fieldName: string,
  value: string,
  currentId?: string
) => {
  // Giả sử bạn có API để kiểm tra trùng lặp
  // Thay thế bằng API thực tế của bạn
  const response = await axios.post("localhost:8386:/api/v1/admin/nhan-vien/check-duplicate", {
    field: fieldName,
    value: value,
    excludeId: currentId || null,
  });
  return response;
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

  Html5Qrcode.getCameras()
    .then((cameras: { id: string; label: string }[]) => {
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
          (errorMessage) => {
            console.warn("Lỗi đọc QR: ", errorMessage);
          }
        );
      } else {
        console.warn("Không tìm thấy camera nào!");
      }
    })
    .catch((error: any) => {
      console.error("Lỗi khi lấy camera: ", error);
    });
};

const stopQrScanning = () => {
  if (html5QrCode) {
    html5QrCode
      .stop()
      .catch((err) => console.error("Không thể dừng scanner:", err));
  }
};

const fetchTinhThanh = async () => {
  try {
    const response = await axios.get("https://provinces.open-api.vn/api/p");
    tinhThanh.value = response.data;
  } catch (error) {
    toast.error("Không lấy được danh sách tỉnh.");
  }
};

// Hàm này nhận MÃ CODE tỉnh để fetch quận/huyện
const fetchQuanHuyen = async (provinceCode: string) => {
  try {
    const response = await axios.get(
      `https://provinces.open-api.vn/api/p/${provinceCode}?depth=2`
    );
    quanHuyen.value = response.data.districts;
    phuongXa.value = []; // Clear phuongXa when province changes
  } catch (error) {
    toast.error("Không lấy được danh sách quận/huyện.");
  }
};

// Hàm này nhận MÃ CODE huyện để fetch xã/phường
const fetchPhuongXa = async (districtCode: string) => {
  try {
    const response = await axios.get(
      `https://provinces.open-api.vn/api/d/${districtCode}?depth=2`
    );
    phuongXa.value = response.data.wards;
  } catch (error) {
    toast.error("Không lấy được danh sách phường/xã.");
  }
};

const rules = {
  name: [{ validator: validateName, trigger: "blur" }],
  avatar: [
    {
      required: true,
      message: "Ảnh đại diện không được để trống!",
      trigger: "blur",
    },
  ],
  cccd: [{ validator: validateCCCD, trigger: "blur" }],
  ngaySinh: [
    {
      required: true,
      message: "Ngày sinh không được để trống!",
      trigger: "blur",
    },
  ],
  sdt: [{ validator: validatePhone, trigger: "blur" }],
  gioiTinh: [
    {
      required: true,
      message: "Giới tính không được để trống!",
      trigger: "blur",
    },
  ],
  email: [{ validator: validateEmail, trigger: "blur" }],
  diaChi: [
    {
      required: true,
      message: "Địa chỉ không được để trống!",
      trigger: "blur",
    },
  ],
  // THÊM MỚI: Rules cho địa chỉ hành chính
  tinh: [
    {
      required: true,
      message: "Vui lòng chọn tỉnh/thành phố!",
      trigger: "change",
    },
  ],
  huyen: [
    {
      required: true,
      message: "Vui lòng chọn quận/huyện!",
      trigger: "change",
    },
  ],
  xa: [
    {
      required: true,
      message: "Vui lòng chọn xã/phường/thị trấn!",
      trigger: "change",
    },
  ],
};

const fetchProductDetails = async (id: string) => {
  try {
    const response = await getMember(id);
    const data = response.data;

    product.value = {
      ...data,
      ngayTuyenDung: data.ngayTuyenDung ? dayjs(data.ngayTuyenDung) : undefined,
      ngaySinh: data.ngaySinh ? dayjs(data.ngaySinh) : undefined,
      gioiTinh: data.gioiTinh === true || data.gioiTinh === "true",
    };

    imageUrl.value = data.avatar || null;

    // Sau khi gán product.value (đã có tên tỉnh/huyện/xã từ API của bạn)
    // chúng ta cần tìm code của chúng để gọi API bên thứ 3 (provinces.open-api.vn)
    if (product.value.tinh) {
      const provinceFound = tinhThanh.value.find(
        (p) => p.name === product.value.tinh
      );
      if (provinceFound) {
        await fetchQuanHuyen(provinceFound.code);
        // Đảm bảo quanHuyen đã được populate trước khi tìm district
        if (product.value.huyen && quanHuyen.value.length > 0) {
          const districtFound = quanHuyen.value.find(
            (d) => d.name === product.value.huyen
          );
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
          id: "",
          ten: "",
          sdt: "",
          diaChi: "",
          email: "",
          ma: "",
          gioiTinh: true,
          tinh: "",
          huyen: "",
          xa: "",
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
    // Validate form trước khi gửi
    await productForm.value.validate();

    // Kiểm tra ảnh đại diện
    if (!imageUrl.value && !imageFile.value) {
      toast.info("Vui lòng chọn ảnh đại diện!");
      return;
    }

    // Kiểm tra các trường địa chỉ
    if (!product.value.tinh) {
      toast.info("Vui lòng chọn tỉnh/thành phố!");
      return;
    }

    if (!product.value.huyen) {
      toast.info("Vui lòng chọn quận/huyện!");
      return;
    }

    if (!product.value.xa) {
      toast.info("Vui lòng chọn xã/phường/thị trấn!");
      return;
    }

    const formData = new FormData();
    formData.append("id", product?.value?.id?.trim() || "");
    formData.append("code", product?.value.ma?.trim() || "");
    formData.append("cccd", product?.value?.cccd?.trim() || "");
    formData.append("user", product?.value.user?.trim() || "");
    formData.append("ten", product?.value.ten?.trim() || "");
    formData.append(
      "ngaySinh",
      product.value?.ngaySinh
        ? dayjs(product.value?.ngaySinh).format("YYYY-MM-DD")
        : ""
    );
    formData.append("email", product?.value.email?.trim() || "");
    formData.append("sdt", product?.value.sdt?.trim() || "");
    formData.append("diaChi", product?.value.diaChi?.trim() || "");
    formData.append("gioiTinh", product.value.gioiTinh?.toString() || "true");

    // Gửi trực tiếp tên tỉnh, huyện, xã đã chọn
    formData.append("tinh", product.value.tinh || "");
    formData.append("huyen", product.value.huyen || "");
    formData.append("xa", product.value.xa || "");

    if (imageFile.value) {
      formData.append("avatar", imageFile.value);
    }

    const res = await modifyMember(formData);

    nextTick(() => {
      sessionStorage.setItem(
        "appToastMessage",
        JSON.stringify({
          message: res.message || "Thao tác thành công!",
          type: "success",
        })
      );
      router.push({
        name: "nhan-vien-admin",
      });
    });
  } catch (error) {
    if (error?.response?.data?.message) {
      toast.error(error?.response?.data?.message);
    } else if (error?.errorFields) {
      // Xử lý lỗi validation từ Ant Design
      const firstError = error.errorFields[0];
      toast.error(firstError.errors[0]);
    } else {
      toast.error("Có lỗi xảy ra khi gửi dữ liệu!");
    }
  }
};

watch(
  () => product.value.ten,
  () => {
    if (productForm.value) {
      productForm.value.clearValidate(["ten"]);
    }
  }
);

watch(
  () => product.value.cccd,
  () => {
    if (productForm.value) {
      productForm.value.clearValidate(["cccd"]);
    }
  }
);

watch(
  () => product.value.sdt,
  () => {
    if (productForm.value) {
      productForm.value.clearValidate(["sdt"]);
    }
  }
);

watch(
  () => product.value.email,
  () => {
    if (productForm.value) {
      productForm.value.clearValidate(["email"]);
    }
  }
);

onMounted(() => {
  idSanPham.value = route.query.id as string;
  if (idSanPham.value) {
    label.value = "Sửa nhân viên";
    pathName.value = "Sửa nhân viên";
  } else {
    label.value = "Thêm nhân viên";
    pathName.value = "Thêm nhân viên";
  }
  if (idSanPham.value) {
    fetchProductDetails(idSanPham.value); // Gọi hàm lấy chi tiết
  }
  fetchTinhThanh(); // Fetch provinces on mount
  // Việc fetch data ban đầu và cascade đã được handle bởi watcher của idSanPham và props.open
  // (Không cần gọi fetchProductDetails/fetchTinhThanh trực tiếp ở đây nữa để tránh trùng lặp)
});

// Watch cho thay đổi của product.value.tinh (tên tỉnh)
watch(
  () => product.value.tinh,
  async (newVal, oldVal) => {
    // Tránh việc chạy watcher khi component khởi tạo và product.tinh chưa có giá trị
    // hoặc khi giá trị không đổi (trường hợp load ban đầu có thể trùng tên)
    if (newVal !== oldVal) {
      // Reset huyện và xã khi tỉnh thay đổi, trừ khi là lần đầu load dữ liệu
      // Kiểm tra oldVal để tránh reset khi product được gán lần đầu
      if (oldVal !== undefined && oldVal !== null && oldVal !== "") {
        product.value.huyen = "";
        product.value.xa = "";
        quanHuyen.value = []; // Clear list for new province
        phuongXa.value = []; // Clear list for new province
      }

      if (newVal) {
        // Nếu có giá trị mới (tên tỉnh)
        const provinceFound = tinhThanh.value.find((p) => p.name === newVal);
        if (provinceFound) {
          await fetchQuanHuyen(provinceFound.code);
        } else {
          // Tên tỉnh không hợp lệ hoặc không tìm thấy, xóa danh sách huyện/xã
          quanHuyen.value = [];
          phuongXa.value = [];
        }
      } else {
        // Nếu giá trị mới là rỗng (người dùng xóa chọn)
        quanHuyen.value = [];
        phuongXa.value = [];
      }
    }
  }
);

// Watch cho thay đổi của product.value.huyen (tên huyện)
watch(
  () => product.value.huyen,
  async (newVal, oldVal) => {
    if (newVal !== oldVal) {
      // Reset xã khi huyện thay đổi, trừ khi là lần đầu load dữ liệu
      if (oldVal !== undefined && oldVal !== null && oldVal !== "") {
        product.value.xa = "";
        phuongXa.value = []; // Clear list for new district
      }

      if (newVal && product.value.tinh) {
        // Chỉ fetch nếu có tên huyện và tỉnh đã được chọn
        const districtFound = quanHuyen.value.find((d) => d.name === newVal);
        if (districtFound) {
          await fetchPhuongXa(districtFound.code);
        } else {
          phuongXa.value = [];
        }
      } else {
        phuongXa.value = [];
      }
    }
  }
);

const closeModal = () => {
  router.push({
    name: "nhan-vien-admin",
  });
};
</script>

<style scoped>
.page-container {
  padding: 20px;
}

.breadcrumb-section {
  margin-bottom: 25px;
  background-color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}

.right-section {
  margin-top: 25px;
}

.form-item-spacing {
  margin-left: 15px;
}

.form-buttons {
  text-align: right;
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
  margin-right: 120px;
}

.button-container {
  display: flex;
  gap: 30px;
  align-items: center;
  justify-content: space-between;
  width: 900px;
}

.qr-button-responsive {
  margin-left: 900px !important;
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
  margin-left: 80px;
}

/* Responsive cho mobile và tablet */
@media (max-width: 1200px) {
  .page-container {
    padding: 15px;
  }
  
  .qr-button-responsive {
    margin-left: auto !important;
    margin-right: 0 !important;
  }
  
  .button-container {
    width: 100%;
    justify-content: center;
  }
  
  .form-buttons {
    margin-right: 0;
    justify-content: center;
  }
}

@media (max-width: 992px) {
  .right-section {
    margin-top: 20px;
  }
  
  .form-item-spacing {
    margin-left: 0;
  }
  
  .image-upload-container {
    margin-left: auto;
    margin-right: auto;
    width: 200px;
    height: 200px;
  }
}

@media (max-width: 768px) {
  .page-container {
    padding: 10px;
  }
  
  .breadcrumb-section {
    padding: 10px 15px;
  }
  
  .qr-button-responsive {
    width: 100% !important;
    max-width: 200px;
    margin-left: auto !important;
    margin-right: auto !important;
  }
  
  .image-upload-container {
    width: 150px;
    height: 150px;
  }
  
  .button-container {
    flex-direction: column;
    gap: 15px;
    width: 100%;
  }
  
  .button-container .custom-button {
    width: 100% !important;
    max-width: 200px;
  }
}

@media (max-width: 576px) {
  .page-container {
    padding: 5px;
  }
  
  .image-upload-container {
    width: 120px;
    height: 120px;
    font-size: 12px;
  }
}

/* Input hover effects */
:deep(.ant-input:hover),
:deep(.ant-input:focus),
:deep(.ant-input-focused) {
  border-color: #58bddb !important;
  box-shadow: 0 0 0 2px rgba(88, 189, 219, 0.2) !important;
}

:deep(.ant-input-number:hover),
:deep(.ant-input-number:focus),
:deep(.ant-input-number-focused) {
  border-color: #58bddb !important;
  box-shadow: 0 0 0 2px rgba(88, 189, 219, 0.2) !important;
}

:deep(.ant-picker:hover),
:deep(.ant-picker-focused),
:deep(.ant-picker-focused .ant-picker-input > input),
:deep(.ant-picker:focus-within) {
  border-color: #58bddb !important;
  box-shadow: 0 0 0 2px rgba(88, 189, 219, 0.2) !important;
}

:deep(.ant-select-selector:hover),
:deep(.ant-select-focused .ant-select-selector) {
  border-color: #58bddb !important;
  box-shadow: 0 0 0 2px rgba(88, 189, 219, 0.2) !important;
}

.gender-radio-group :deep(.ant-radio-wrapper:hover .ant-radio-inner),
.gender-radio-group :deep(.ant-radio-wrapper-focused .ant-radio-inner) {
  border-color: #58bddb !important;
}

.gender-radio-group :deep(.ant-radio-checked .ant-radio-inner) {
  border-color: #58bddb !important;
  box-shadow: 0 0 0 2px rgba(88, 189, 219, 0.2) !important;
}

.custom-button {
  transition: transform 0.3s, background-color 0.3s;
}

.custom-button:hover {
  background-color: #58bddb !important;
  transform: scale(1.05);
}

a-image .avatar-upload {
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.avatar-image {
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

.a-upload {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: none !important;
  outline: none !important;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  background-color: #f5f5f5;
}

.a-upload .ant-upload-trigger {
  width: 100%;
  height: 100%;
  border: none !important;
  display: flex;
  align-items: center;
  justify-content: center;
}

.section-title {
  margin-top: 30px;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  margin-left: 0px;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

body {
  font-family: "Roboto", sans-serif;
}

.a-upload .ant-upload-list-picture-card {
  display: none;
}

.ant-upload .ant-upload-trigger a-icon {
  font-size: 40px;
}
</style>