<template>
  <div class="update-discount-page p-6">
    <!-- Header -->
    <div class="mb-6">
      <a-page-header
        title="Cập nhật đợt giảm giá"
        class="text-2xl font-bold text-gray-800"
        @back="$router.back()"
      />
    </div>

    <div class="grid grid-cols-1 xl:grid-cols-4 gap-6">
      <!-- Left Column - Form (1/3 width) -->
      <div class="xl:col-span-1 bg-white rounded-lg shadow-sm border p-6">
        <h2 class="text-lg font-semibold mb-4">Thông tin đợt giảm giá</h2>
        
        <div class="space-y-4">
          <!-- Mã đợt giảm giá (readonly) -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Mã đợt giảm giá
            </label>
            <a-input
              v-model:value="formData.code"
              placeholder="Mã đợt giảm giá"
              size="large"
              disabled
            />
          </div>

          <!-- Tên khuyến mãi -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Tên khuyến mãi
            </label>
            <a-input
              v-model:value="formData.tenKhuyenMai"
              placeholder="Tên khuyến mãi"
              size="large"
            />
          </div>

          <!-- Giá trị giảm -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Giá trị giảm
            </label>
            <a-input
              v-model:value="formData.giaTriGiam"
              placeholder="Giá trị giảm"
              size="large"
              suffix="%"
            />
          </div>

          <!-- Ngày bắt đầu -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Ngày bắt đầu
            </label>
            <a-date-picker
              v-model:value="formData.ngayBatDau"
              placeholder="Ngày bắt đầu"
              size="large"
              class="w-full"
              format="DD/MM/YYYY"
            />
          </div>

          <!-- Ngày kết thúc -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">
              Ngày kết thúc
            </label>
            <a-date-picker
              v-model:value="formData.ngayKetThuc"
              placeholder="Ngày kết thúc"
              size="large"
              class="w-full"
              format="DD/MM/YYYY"
            />
          </div>

          <!-- Submit Button -->
          <div class="pt-4 flex gap-3">
            <a-button
              type="primary"
              size="large"
              @click="handleSubmit"
              class="flex-1"
              :loading="submitting"
              style="background-color: #54bddb; border-color: #54bddb;"
            >
              Cập nhật
            </a-button>
            <a-button
              size="large"
              @click="handleCancel"
              class="flex-1"
            >
              Hủy
            </a-button>
          </div>
        </div>
      </div>

      <!-- Right Column - Products (2/3 width) -->
      <div class="xl:col-span-3 bg-white rounded-lg shadow-sm border p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-semibold">Sản phẩm</h2>
        </div>

        <!-- Search -->
        <div class="mb-4">
          <a-input
            v-model:value="searchTerm"
            placeholder="Mã hoặc tên sản phẩm"
            size="large"
            class="w-full"
            @pressEnter="handleSearch"
          >
            <template #prefix>
              <SearchOutlined />
            </template>
          </a-input>
        </div>

        <!-- Available Products Table -->
        <div class="mb-6">
          <div class="bg-gray-200 text-black px-4 py-2 rounded-t-lg">
            <div class="grid grid-cols-12 gap-2 text-sm font-medium">
              <div class="col-span-1 text-center">
                <a-checkbox
                  :indeterminate="indeterminate"
                  :checked="checkAll"
                  @change="onCheckAllChange"
                />
              </div>
              <div class="col-span-1 text-center">STT</div>
              <div class="col-span-4 text-center">Tên sản phẩm</div>
              <div class="col-span-2 text-center">Thương hiệu</div>
              <div class="col-span-2 text-center">Xuất xứ</div>
            </div>
          </div>
          
          <div class="border border-gray-200 rounded-b-lg max-h-64 overflow-y-auto">
            <div
              v-if="loading"
              class="p-8 text-center text-gray-500"
            >
              <a-spin />
              <div class="mt-2">Đang tải dữ liệu...</div>
            </div>
            
            <div
              v-else-if="filteredProducts.length === 0"
              class="p-8 text-center text-gray-500"
            >
              Không tìm thấy sản phẩm nào
            </div>
            
            <div
              v-else
              v-for="(product, index) in filteredProducts"
              :key="product.id"
              class="grid grid-cols-12 gap-2 p-3 border-b border-gray-100 hover:bg-gray-50 text-sm"
            >
              <div class="col-span-1 text-center">
                <a-checkbox
                  :checked="selectedProductIds.includes(product.id)"
                  :loading="loadingDetails.has(product.id)"
                  @change="(e) => handleProductSelect(product.id, e.target.checked)"
                />
              </div>
              <div class="col-span-1 text-center">{{ index + 1 }}</div>
              <div class="col-span-4 text-center">{{ product.ten }}</div>
              <div class="col-span-2 text-center">
                {{ product.thuongHieu.ten }}
              </div>
              <div class="col-span-2 text-center">{{ product.xuatSu.ten }}</div>
            </div>
          </div>
        </div>

        <!-- Pagination -->
        <div class="flex justify-center">
          <a-pagination
            :current="currentPage"
            :total="totalProducts"
            :page-size="pageSize"
            @change="handlePageChange"
            size="small"
            show-size-changer
            :page-size-options="['10', '20', '30', '50']"
            @show-size-change="(current, size) => { pageSize = size; currentPage = 1; fetchProducts(); }"
          />
        </div>

        <!-- Selected Products Details -->
        <div class="mt-6">
          <h3 class="text-md font-semibold mb-4">
            Chi tiết sản phẩm 
            <span class="text-gray-500 font-normal">({{ selectedProductDetails.length }} sản phẩm)</span>
          </h3>

          <div class="row">
            <!-- Row 1 - Search Filters -->
            <div class="col-md-3 mb-3">
              <label class="form-label d-flex align-items-center">
                <span class="ms-2">Tìm sản phẩm</span>
              </label>
              <a-input
                v-model:value="productNameSearch"
                placeholder="Nhập tên sản phẩm..."
                size="large"
                @input="handleDetailSearch"
                class="w-full shadow-sm hover:shadow-md transition-shadow duration-200"
              />
            </div>

            <div class="col-md-3 mb-3">
              <label class="form-label">Kích cỡ</label>
              <a-select
                v-model:value="selectedSize"
                placeholder="Chọn kích cỡ"
                size="large"
                class="w-full shadow-sm hover:shadow-md transition-shadow duration-200"
                @change="handleDetailSearch"
                :allowClear="true"
              >
                <a-select-option
                  v-for="size in sizes"
                  :key="size.id"
                  :value="size.id"
                >
                  {{ size.ten }}
                </a-select-option>
              </a-select>
            </div>

            <div class="col-md-3 mb-3">
              <label class="form-label">Màu sắc</label>
              <a-select
                v-model:value="selectedColor"
                placeholder="Chọn màu sắc"
                size="large"
                class="w-full shadow-sm hover:shadow-md transition-shadow duration-200"
                @change="handleDetailSearch"
                :allowClear="true"
              >
                <a-select-option
                  v-for="color in colors"
                  :key="color.id"
                  :value="color.id"
                >
                  {{ color.ten }}
                </a-select-option>
              </a-select>
            </div>

            <div class="col-md-2 mb-3" style="margin-top: 32px;">
              <a-button
              @click="clearDetailFilters"
              size="large"
              type="default"
              class="mr-2"
            >
              Reset
            </a-button>
            </div>
          </div>

          <div class="bg-gray-200 text-black px-4 py-2 rounded-t-lg">
            <div class="grid grid-cols-12 gap-2 text-sm font-medium">
              <div class="col-span-1 text-center">
                <a-checkbox 
                  :indeterminate="detailsIndeterminate"
                  :checked="detailsCheckAll"
                  @change="onCheckAllDetailsChange"
                />
              </div>
              <div class="col-span-1 text-center">STT</div>
              <div class="col-span-2 text-center">Ảnh sản phẩm</div>
              <div class="col-span-4 text-center">Tên sản phẩm</div>
              <div class="col-span-2 text-center">Số lượng</div>
              <div class="col-span-2 text-center">Đơn giá</div>
            </div>
          </div>

          <div class="border border-gray-200 rounded-b-lg min-h-32">
            <div
              v-if="loadingAnyDetails"
              class="p-8 text-center text-gray-500"
            >
              <a-spin />
              <div class="mt-2">Đang tải chi tiết sản phẩm...</div>
            </div>
            
            <div
              v-else-if="selectedProductDetails.length === 0"
              class="p-8 text-center text-gray-500"
            >
              Chưa có sản phẩm nào được chọn
            </div>
            
            <div
              v-else
              v-for="(detail, index) in filteredProductDetails"
              :key="detail.id"
              class="grid grid-cols-12 gap-2 p-3 border-b border-gray-100 text-sm hover:bg-gray-50"
            >
              <div class="col-span-1 text-center">
                <a-checkbox 
                  :checked="detail.selected"
                  @change="(e) => handleProductDetailSelect(detail.id, e.target.checked)"
                />
              </div>
              <div class="col-span-1 text-center">{{ index + 1 }}</div>
              <div class="col-span-2 text-center">
                <div class="w-10 h-10 bg-gray-200 rounded mx-auto flex items-center justify-center">
                  <img 
                    v-if="detail.anh || detail.hinhAnh" 
                    :src="detail.anh || detail.hinhAnh" 
                    :alt="getProductName(detail)"
                    class="w-full h-full object-cover rounded"
                    @error="onImageError"
                  />
                  <span v-else class="text-xs text-gray-400">N/A</span>
                </div>
              </div>
              <div class="col-span-4 text-center">
                {{ getProductName(detail) }} [{{ getColorCode(detail) }} -
                {{ getSizeName(detail) }}]
              </div>
              <div class="col-span-2 text-center">{{ detail.soLuong }}</div>
              <div class="col-span-2 text-center text-red-500">
                {{ formatCurrencyVND(detail.giaBan) }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick  } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message } from 'ant-design-vue';  
import { SearchOutlined } from '@ant-design/icons-vue';
import { 
  GetSanPham, 
  getSanPhamChiTiets,
  DetailDotGiamGia,
  getColorsFromAPI,
  getSizesFromAPI,
  type DotGiamGiaResponse, 
  type DotGiamGiaRequest 
} from '@/services/api/admin/dotgiamgia.api';
import type { Dayjs } from 'dayjs';
import dayjs from 'dayjs';

const router = useRouter();
const route = useRoute();
const dotGiamGiaId = route.params.id as string;

// Loading states
const loadingDetail = ref(false)
const submitting = ref(false)

// Form data
const formData = ref({
  code: '',
  tenKhuyenMai: '',
  giaTriGiam: '',
  trangThai: 'CHUA_KICH_HOAT',
  ngayBatDau: null as Dayjs | null,
  ngayKetThuc: null as Dayjs | null
});

// Product selection
const selectedProductIds = ref<string[]>([]); // Store selected product IDs
const selectedProductDetails = ref<any[]>([]);
const selectedProductIdsDetails = ref<string[]>([]); // Store detailed product info
const loadingDetails = ref<Set<string>>(new Set()); // Track loading state for each product
const searchTerm = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const loading = ref(false);
const selectedProductDetailIds = ref<number[]>([]);

// Detail search filters
const productNameSearch = ref("");
const selectedColor = ref(null);
const selectedSize = ref(null);
const sizes = ref([]);
const colors = ref([]);

const fetchColors = async () => {
  colors.value = await getColorsFromAPI(); // Implement the API call in your service
};

const fetchSizes = async () => {
  const result = await getSizesFromAPI();
  sizes.value = result; // Gán vào ref
};

// API data
const availableProducts = ref<any[]>([]);
const totalProducts = ref(0);

// Computed properties
const filteredProducts = computed(() => {
  return availableProducts.value.filter(
    (product) =>
      product.ma.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      product.ten.toLowerCase().includes(searchTerm.value.toLowerCase())
  );
});

// New computed property for filtering product details
const filteredProductDetails = computed(() => {
  return selectedProductDetails.value.filter((detail) => {
    // Product name search
    const productName = getProductName(detail).toLowerCase();
    const searchMatch = productNameSearch.value === "" || 
      productName.includes(productNameSearch.value.toLowerCase());
    
    // Size filter
    const sizeMatch = selectedSize.value === null || 
      detail.kichCo?.id === selectedSize.value;
    
    // Color filter
    const colorMatch = selectedColor.value === null || 
      detail.mauSac?.id === selectedColor.value;
    
    return searchMatch && sizeMatch && colorMatch;
  });
});

const handleDetailSearch = () => {
  // This function is called when any filter changes
  console.log("Detail search filters changed:", {
    productNameSearch: productNameSearch.value,
    selectedColor: selectedColor.value,
    selectedSize: selectedSize.value
  });
};

const clearDetailFilters = () => {
  productNameSearch.value = "";
  selectedColor.value = null;
  selectedSize.value = null;
};

// Store original data for comparison
const originalData = ref<DotGiamGiaResponse | null>(null)

// Store detailed product IDs to select based on API "promotionProductDetail"
const promotionProductDetailIds = ref<string[]>([])

const checkAll = computed(() => {
  return filteredProducts.value.length > 0 && 
         filteredProducts.value.every(product => selectedProductIds.value.includes(product.id))
})

const indeterminate = computed(() => {
  const selectedCount = filteredProducts.value.filter(product => 
    selectedProductIds.value.includes(product.id)
  ).length
  return selectedCount > 0 && selectedCount < filteredProducts.value.length
})

// Computed properties for product details check all
const detailsCheckAll = computed(() => {
  return selectedProductDetails.value.length > 0 && 
         selectedProductDetails.value.every(detail => detail.selected)
})

const detailsIndeterminate = computed(() => {
  const selectedCount = selectedProductDetails.value.filter(detail => detail.selected).length
  return selectedCount > 0 && selectedCount < selectedProductDetails.value.length
})

const loadingAnyDetails = computed(() => {
  return loadingDetails.value.size > 0
})

const formatCurrencyVND = (amount: number): string => {
  return amount.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
};

// Helper methods for displaying product details
const getProductName = (detail: any): string => {
  return detail.sanPham?.ten || detail.ten || 'N/A'
}

const getBrandName = (detail: any): string => {
  return detail.sanPham?.thuongHieu?.ten || detail.thuongHieu?.ten || 'N/A'
}

const getSizeName = (detail: any): string => {
  return detail.kichCo?.ten || detail.size || 'N/A'
}

const getColorCode = (detail: any): string => {
  return detail.mauSac?.ten || detail.color || '#cccccc'
}

const getStatusText = (detail: any): string => {
  const status = detail.status || detail.trangThai
  return status === 'ACTIVE' || status === 'Đang kinh doanh' ? 'Đang kinh doanh' : 'Ngừng kinh doanh'
}

const getStatusClass = (detail: any): string => {
  const status = detail.status || detail.trangThai
  return status === 'ACTIVE' || status === 'Đang kinh doanh' 
    ? 'bg-green-100 text-green-800' 
    : 'bg-red-100 text-red-800'
}

const onImageError = (event: Event) => {
  const target = event.target as HTMLImageElement
  target.style.display = 'none'
}

const loadDotGiamGiaDetail = async () => {
  try {
    loadingDetail.value = true;
    const response = await DetailDotGiamGia(dotGiamGiaId);
    
    if (response && typeof response === 'object' && !Array.isArray(response)) {
      const data = response;
      originalData.value = data;
      
      formData.value = {
        code: data.code || '',
        tenKhuyenMai: data.name || '',
        giaTriGiam: data.value?.toString() || '',
        trangThai: data.status || 'CHUA_KICH_HOAT',
        ngayBatDau: data.startDate ? dayjs(data.startDate) : null,
        ngayKetThuc: data.endDate ? dayjs(data.endDate) : null
      };
      
      const getIdsArray = (str: string | undefined) => {
        if (!str) return [];
        if (typeof str === 'string') {
          return str.split(',').map(id => id.trim()).filter(Boolean);
        }
        if (Array.isArray(str)) {
          return str.map(id => id.toString().trim()).filter(Boolean);
        }
        return [];
      }

      const selectedProductIdsArray = getIdsArray(data.product);
      selectedProductIds.value = [...selectedProductIdsArray];
      
      // QUAN TRỌNG: Lưu promotionProductDetailIds trước khi load details
      promotionProductDetailIds.value = getIdsArray(data.promotionProductDetail);
      
      console.log('Selected product IDs:', selectedProductIdsArray);
      console.log('Promotion product detail IDs to auto-select:', promotionProductDetailIds.value);

      // Clear old details
      selectedProductDetails.value = [];
      
      // Load product details và auto-select
      if (selectedProductIdsArray.length > 0) {
        for (const productId of selectedProductIdsArray) {
          await fetchProductDetails(productId);
        }
        
        // THÊM: Sau khi load xong tất cả details, force update selected state
        await nextTick(); // Đợi Vue update
        updateSelectedDetailsFromPromotionIds();
      }
      
    } else {
      message.error('Không thể tải thông tin đợt giảm giá');
    }
  } catch (error) {
    console.error('Error loading detail:', error);
    message.error('Lỗi khi tải thông tin đợt giảm giá');
  } finally {
    loadingDetail.value = false;
  }
};

const updateSelectedDetailsFromPromotionIds = () => {
  const promotionIdsSet = new Set(
    promotionProductDetailIds.value.map(id => id.toString().trim())
  );
  
  console.log('Updating selected details with promotion IDs:', promotionIdsSet);
  
  selectedProductDetails.value.forEach(detail => {
    const shouldBeSelected = promotionIdsSet.has(detail.id.toString().trim());
    detail.selected = shouldBeSelected;
    
    console.log(`Detail ID: ${detail.id}, Selected: ${shouldBeSelected}`);
  });
  
  // Force reactivity update
  selectedProductDetails.value = [...selectedProductDetails.value];
};


// Adjust fetchProductDetails to mark select detailed product by matching promotionProductDetailIds
const fetchProductDetails = async (productId: string) => {
  try {
    loadingDetails.value.add(productId);
    const response = await getSanPhamChiTiets(productId);
    
    if (response && Array.isArray(response) && response.length > 0) {
      // Tạo Set để so sánh nhanh
      const promotionIdsSet = new Set(
        promotionProductDetailIds.value.map(id => id.toString().trim())
      );
      
      const details = response.map(item => {
        const itemIdStr = item.id.toString().trim();
        const isSelected = promotionIdsSet.has(itemIdStr);
        
        return {
          id: item.id,
          productId: productId,
          status: item.status,
          ma: item.ma,
          giaBan: item.giaBan,
          anh: item.anh,
          soLuong: item.soLuong,
          sanPham: {
            id: item.sanPham.id,
            ma: item.sanPham.ma,
            ten: item.sanPham.ten,
            moTa: item.sanPham.moTa,
            thuongHieu: item.sanPham.thuongHieu,
            xuatSu: item.sanPham.xuatSu,
            danhMuc: item.sanPham.danhMuc,
            loaiDe: item.sanPham.loaiDe,
            chatLieu: item.sanPham.chatLieu,
          },
          kichCo: item.kichCo,
          mauSac: item.mauSac,
          selected: isSelected // Set selected state ngay từ đầu
        };
      });

      const detail = await DetailDotGiamGia(productId);
      
      // Remove existing details for this product
      selectedProductDetails.value = selectedProductDetails.value.filter(
        d => d.productId !== productId
      );
      
      // Add new details
      selectedProductDetails.value.push(...details);
      
      console.log(`Loaded ${details.length} details for product ${productId}`);
      console.log('Selected details:', details.filter(d => d.selected).map(d => d.id));
      
    }
  } catch (error) {
    console.error('Error fetching product details:', error);
    message.error(`Lỗi khi tải chi tiết sản phẩm ${productId}`);
  } finally {
    loadingDetails.value.delete(productId);
  }
};

const handleSubmit = async () => {
  try {
    submitting.value = true;
    
    // Validate form
    if (!formData.value.tenKhuyenMai.trim()) {
      message.error('Vui lòng nhập tên khuyến mãi')
      return
    }
    
    if (!formData.value.giaTriGiam.trim()) {
      message.error('Vui lòng nhập giá trị giảm')
      return
    }
    
    if (!formData.value.ngayBatDau) {
      message.error('Vui lòng chọn ngày bắt đầu')
      return
    }
    
    if (!formData.value.ngayKetThuc) {
      message.error('Vui lòng chọn ngày kết thúc')
      return
    }

    // Gather form data
    const requestData: DotGiamGiaRequest = {
      name: formData.value.tenKhuyenMai,
      value: parseFloat(formData.value.giaTriGiam),
      status: formData.value.trangThai,
      startDate: formData.value.ngayBatDau?.valueOf() || null,
      endDate: formData.value.ngayKetThuc?.valueOf() || null,
      idProductDetails: selectedProductDetails.value
        .filter(detail => detail.selected)
        .map(detail => ({ id: detail.id }))
    };

    console.log('Update request data:', requestData);

    // const response = await updateDotGiamGia(dotGiamGiaId, requestData)
    // console.log('Update response:', response)
    
    message.success({
      content: 'Cập nhật đợt giảm giá thành công!',
      duration: 1.5,
      style: {
        marginTop: '20vh',
        fontSize: '16px',
        fontWeight: '600',
      }
    })
    
    setTimeout(() => {
      router.push('/admin/dot-giam-gia')
    }, 1600)
    
  } catch (error) {
    console.error('Update error:', error)
    if (error.response && error.response.data) {
      message.error(error.response.data.message)
    } else {
      message.error('Lỗi khi cập nhật đợt giảm giá. Vui lòng thử lại.')
    }
  } finally {
    submitting.value = false
  }
}

const handleCancel = () => {
  router.push('/admin/dot-giam-gia')
}

// Product selection handling
const handleProductSelect = async (productId: string, checked: boolean) => {
  if (checked) {
    // Add productId if not already there
    if (!selectedProductIds.value.includes(productId)) {
      selectedProductIds.value.push(productId);
    }
    // Fetch product details - selection state will be determined by promotionProductDetailIds
    await fetchProductDetails(productId);
  } else {
    // Remove productId
    const index = selectedProductIds.value.indexOf(productId);
    if (index > -1) {
      selectedProductIds.value.splice(index, 1);
    }

    // Remove details of unchecked product
    selectedProductDetails.value = selectedProductDetails.value.filter(
      detail => detail.productId !== productId
    );
  }
};

// FIXED: Improved check all logic
const onCheckAllChange = async (e: any) => {
  if (e.target.checked) {
    const newProductIds = filteredProducts.value
      .map(product => product.id)
      .filter(id => !selectedProductIds.value.includes(id));

    selectedProductIds.value.push(...newProductIds);

    // Chỉ fetch detail nhưng không auto-select detail
    for (const productId of newProductIds) {
      await fetchProductDetails(productId); // chỉ load, không select
    }
  } else {
    const filteredProductIds = filteredProducts.value.map(product => product.id);
    selectedProductIds.value = selectedProductIds.value.filter(
      id => !filteredProductIds.includes(id)
    );

    // Bỏ tick chi tiết dựa trên productId của detail
    selectedProductDetailIds.value = selectedProductDetailIds.value.filter(
      detailId => {
        const detail = selectedProductDetails.value.find(d => d.id === detailId);
        return detail && !filteredProductIds.includes(detail.productId);
      }
    );
  }
};

const handleProductDetailSelect = (detailId: string, checked: boolean) => {
  const detail = selectedProductDetails.value.find(item => item.id === detailId)
  if (detail) {
    detail.selected = checked
  }
}

const onCheckAllDetailsChange = (e: any) => {
  const checked = e.target.checked
  selectedProductDetails.value.forEach(detail => {
    detail.selected = checked
  })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchProducts()
}

const handleSearch = () => {
  currentPage.value = 1
  fetchProducts()
}

const fetchProducts = async () => {
  try {
    loading.value = true
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      search: searchTerm.value
    }
    
    const response = await GetSanPham(params)
    
    if (response) {
      let products = []
      
      if (Array.isArray(response)) {
        products = response
      } else if (response.data && Array.isArray(response.data)) {
        products = response.data
      } else if (response.data && response.data.content && Array.isArray(response.data.content)) {
        products = response.data.content
        totalProducts.value = response.data.totalElements || response.data.content.length
      } else if (response.content && Array.isArray(response.content)) {
        products = response.content
        totalProducts.value = response.totalElements || response.content.length
      } else {
        products = []
      }
      
      availableProducts.value = products.map((product) => ({
        id: product.id,
        ma: product.ma,
        ten: product.ten,
        status: product.status,
        trangThai: product.status === 'ACTIVE' ? 'Đang kinh doanh' : 'Ngừng kinh doanh',
        thuongHieu: product.thuongHieu,
        xuatSu: product.xuatSu,
        danhMuc: product.danhMuc,
        loaiDe: product.loaiDe,
        chatLieu: product.chatLieu,
        moTa: product.moTa
      }))
      
      if (!totalProducts.value) {
        totalProducts.value = availableProducts.value.length
      }
      
    } else {
      availableProducts.value = []
      totalProducts.value = 0
    }
  } catch (error) {
    availableProducts.value = []
    totalProducts.value = 0
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  try {
    // Load colors và sizes trước
    await Promise.all([
      fetchColors(),
      fetchSizes()
    ]);
    
    // Load products list
    await fetchProducts();
    
    // Cuối cùng load discount detail (này sẽ auto-select products và details)
    await loadDotGiamGiaDetail();
    
    console.log('All data loaded successfully');
  } catch (error) {
    console.error('Error in onMounted:', error);
    message.error('Lỗi khi tải dữ liệu trang');
  }
});
</script>

<style scoped lang="scss">
.text-red-500{
  color: red;
}

.add-discount-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.ant-input-affix-wrapper .ant-input-prefix {
  color: #bfbfbf;
}

.grid {
  display: grid;
}

.grid-cols-1 {
  grid-template-columns: repeat(1, minmax(0, 1fr));
}

.grid-cols-12 {
  grid-template-columns: repeat(12, minmax(0, 1fr));
}

.grid-cols-4 {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

@media (min-width: 1280px) {
  .xl\:grid-cols-4 {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }
}

.xl\:col-span-1 {
  grid-column: span 1 / span 1;
}

.xl\:col-span-3 {
  grid-column: span 3 / span 3;
}

@media (min-width: 1280px) {
  .xl\:col-span-1 {
    grid-column: span 1 / span 1;
  }

  .xl\:col-span-3 {
    grid-column: span 3 / span 3;
  }
}

.col-span-1 {
  grid-column: span 1 / span 1;
}

.col-span-2 {
  grid-column: span 2 / span 2;
}

.col-span-4 {
  grid-column: span 4 / span 4;
}

.gap-2 {
  gap: 0.5rem;
}

.gap-6 {
  gap: 1.5rem;
}

.p-3 {
  padding: 0.75rem;
}

.p-6 {
  padding: 1.5rem;
}

.p-8 {
  padding: 2rem;
}

.h-10 {
  height: 2.5rem;
}

.px-2 {
  padding-left: 0.5rem;
  padding-right: 0.5rem;
}

.px-4 {
  padding-left: 1rem;
  padding-right: 1rem;
}

.py-1 {
  padding-top: 0.25rem;
  padding-bottom: 0.25rem;
}

.py-2 {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

.pt-4 {
  padding-top: 1rem;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.mb-4 {
  margin-bottom: 1rem;
}

.mb-6 {
  margin-bottom: 1.5rem;
}

.mt-2 {
  margin-top: 0.5rem;
}

.mt-6 {
  margin-top: 1.5rem;
}

.space-y-4 > * + * {
  margin-top: 1rem;
}

.w-full {
  width: 100%;
}

.w-4 {
  width: 1rem;
}

.w-8 {
  width: 2rem;
}

.w-10 {
  width: 2.5rem;
}

.h-4 {
  height: 1rem;
}

.h-8 {
  height: 2rem;
}

.max-h-32 {
  max-height: 8rem;
}

.max-h-64 {
  max-height: 16rem;
}

.min-h-32 {
  min-height: 8rem;
}

.overflow-y-auto {
  overflow-y: auto;
}

.text-xs {
  font-size: 0.75rem;
  line-height: 1rem;
}

.text-sm {
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.text-md {
  font-size: 1rem;
  line-height: 1.5rem;
}

.text-lg {
  font-size: 1.125rem;
  line-height: 1.75rem;
}

.text-2xl {
  font-size: 1.5rem;
  line-height: 2rem;
}

.font-medium {
  font-weight: 500;
}

.font-semibold {
  font-weight: 600;
}

.font-bold {
  font-weight: 700;
}

.font-normal {
  font-weight: 400;
}

.text-center {
  text-align: center;
}

.text-white {
  color: rgb(255 255 255);
}

.text-gray-400 {
  color: rgb(156 163 175);
}

.text-gray-500 {
  color: rgb(107 114 128);
}

.text-gray-700 {
  color: rgb(55 65 81);
}

.text-gray-800 {
  color: rgb(31 41 55);
}

.text-green-800 {
  color: rgb(22 101 52);
}

.text-red-800 {
  color: rgb(153 27 27);
}

.bg-red-100 {
  background-color: rgb(254 226 226);
}

.bg-white {
  background-color: rgb(255 255 255);
}

.bg-gray-50 {
  background-color: rgb(249 250 251);
}

.bg-gray-200 {
  background-color: rgb(229 231 235);
}

.bg-blue-500 {
  background-color: rgb(59 130 246);
}

.bg-orange-500 {
  background-color: rgb(249 115 22);
}

.bg-green-100 {
  background-color: rgb(220 252 231);
}

.border {
  border-width: 1px;
}

.border-b {
  border-bottom-width: 1px;
}

.border-gray-100 {
  border-color: rgb(243 244 246);
}

.border-gray-200 {
  border-color: rgb(229 231 235);
}

.border-gray-300 {
  border-color: rgb(209 213 219);
}

.rounded {
  border-radius: 0.25rem;
}

.rounded-lg {
  border-radius: 0.5rem;
}

.rounded-t-lg {
  border-top-left-radius: 0.5rem;
  border-top-right-radius: 0.5rem;
}

.rounded-b-lg {
  border-bottom-left-radius: 0.5rem;
  border-bottom-right-radius: 0.5rem;
}

.rounded-full {
  border-radius: 9999px;
}

.shadow-sm {
  box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
}

.hover\:bg-gray-50:hover {
  background-color: rgb(249 250 251);
}

.flex {
  display: flex;
}

.inline-flex {
  display: inline-flex;
}

.items-center {
  align-items: center;
}

.justify-center {
  justify-content: center;
}

.justify-between {
  justify-content: space-between;
}

.block {
  display: block;
}

.mx-auto {
  margin-left: auto;
  margin-right: auto;
}

.object-cover {
  object-fit: cover;
}
</style>