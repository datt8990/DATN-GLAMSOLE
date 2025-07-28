<template>
  <div class="border rounded p-3 bg-light shadow-sm">
    <h5 class="fw-bold mb-3">Bộ lọc sản phẩm</h5>

    <!-- Loading state -->
    <div v-if="isLoading" class="text-center py-3">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Đang tải...</span>
      </div>
    </div>

    <!-- Accordion Block -->
    <div v-else v-for="section in sections" :key="section.key" class="mb-3">
      <div
        class="d-flex justify-content-between align-items-center border-bottom pb-1 mb-2 cursor-pointer"
        @click="toggleSection(section.key)"
        style="user-select: none;"
      >
        <span class="fw-semibold">{{ section.label }}</span>
        <i class="bi" :class="sectionOpen[section.key] ? 'bi-chevron-up' : 'bi-chevron-down'"></i>
      </div>

      <transition name="fade">
        <div v-show="sectionOpen[section.key]">
          <div v-if="section.key === 'thuongHieu'">
            <div v-for="item in thuongHieuOptions" :key="item.value" class="form-check">
              <input 
                class="form-check-input" 
                type="checkbox" 
                :value="item.value" 
                :id="`thuongHieu-${item.value}`"
                v-model="filters.thuongHieu"
              />
              <label class="form-check-label" :for="`thuongHieu-${item.value}`">{{ item.label }}</label>
            </div>
          </div>

          <div v-else-if="section.key === 'chatLieu'">
            <div v-for="item in chatLieuOptions" :key="item.value" class="form-check">
              <input 
                class="form-check-input" 
                type="checkbox" 
                :value="item.value" 
                :id="`chatLieu-${item.value}`"
                v-model="filters.chatLieu"
              />
              <label class="form-check-label" :for="`chatLieu-${item.value}`">{{ item.label }}</label>
            </div>
          </div>

          <div v-else-if="section.key === 'loaiDe'">
            <div v-for="item in loaiDeOptions" :key="item.value" class="form-check">
              <input 
                class="form-check-input" 
                type="checkbox" 
                :value="item.value" 
                :id="`loaiDe-${item.value}`"
                v-model="filters.loaiDe"
              />
              <label class="form-check-label" :for="`loaiDe-${item.value}`">{{ item.label }}</label>
            </div>
          </div>

          <div v-else-if="section.key === 'danhMuc'">
            <div v-for="item in danhMucOptions" :key="item.value" class="form-check">
              <input 
                class="form-check-input" 
                type="checkbox" 
                :value="item.value" 
                :id="`danhMuc-${item.value}`"
                v-model="filters.danhMuc"
              />
              <label class="form-check-label" :for="`danhMuc-${item.value}`">{{ item.label }}</label>
            </div>
          </div>

          <div v-else-if="section.key === 'gia'">
            <div class="d-flex flex-column gap-2">
              <div>
                <label class="form-label small">Từ:</label>
                <input 
                  type="text" 
                  class="form-control form-control-sm" 
                  v-model.number="filters.giaTu" 
                  @input="validatePrice('giaTu', $event)" 
                  placeholder="Nhập giá từ (₫)"
                />
              </div>
              <div>
                <label class="form-label small">Đến:</label>
                <input 
                  type="text" 
                  class="form-control form-control-sm" 
                  v-model.number="filters.giaDen" 
                  @input="validatePrice('giaDen', $event)" 
                  placeholder="Nhập giá đến (₫)"
                />
              </div>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <div v-if="!isLoading" class="d-flex justify-content-between mt-3 gap-2">
      <button class="btn btn-outline-danger btn-sm" @click="resetFilters" title="Xóa bộ lọc" :disabled="isFiltersEmpty">
        <i class="bi bi-x-circle"></i> Xóa
      </button>
      <button class="btn btn-outline-success btn-sm" @click="applyFilters" title="Áp dụng bộ lọc">
        <i class="bi bi-funnel-fill"></i> Lọc
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { GetListChatLieu, GetListDanhMuc, GetListLoaiDe, GetListThuongHieu } from '@/services/api/permitall/sanpham/pmsanpham.api'
import { GetListColor, GetListSize } from '@/services/api/admin/sanphamchitiet.api'
import { reactive, ref, defineEmits, onMounted, computed } from 'vue'

const emit = defineEmits(['filter'])
const isLoading = ref(false)
const error = ref<string | null>(null)

const ColorOptions = ref<{ label: string; value: string; color: string; }[]>([])
const SizeOptions = ref<{ label: string; value: string }[]>([])
const thuongHieuOptions = ref<{ label: string; value: string }[]>([])
const loaiDeOptions = ref<{ label: string; value: string }[]>([])
const danhMucOptions = ref<{ label: string; value: string }[]>([])
const chatLieuOptions = ref<{ label: string; value: string }[]>([])

const filters = reactive({
  thuongHieu: [] as string[],
  mauSac: [] as string[],
  kichCo: [] as string[],
  chatLieu: [] as string[],
  loaiDe: [] as string[],
  danhMuc: [] as string[],
  xuatXu: [] as string[],
  giaTu: undefined as number | undefined, // Không có giá trị mặc định
  giaDen: undefined as number | undefined // Không có giá trị mặc định
})

const sectionOpen = reactive<Record<string, boolean>>({
  thuongHieu: true,
  mauSac: true,
  kichCo: true,
  chatLieu: true,
  loaiDe: true,
  danhMuc: true,
  xuatXu: false,
  gia: true
})

const sections = [
  { key: 'thuongHieu', label: 'Thương hiệu' },
  { key: 'chatLieu', label: 'Chất liệu' },
  { key: 'loaiDe', label: 'Loại đế' },
  { key: 'danhMuc', label: 'Danh mục' },
  { key: 'gia', label: 'Khoảng giá' }
]

const isFiltersEmpty = computed(() => {
  return !filters.thuongHieu.length &&
    !filters.mauSac.length &&
    !filters.kichCo.length &&
    !filters.chatLieu.length &&
    !filters.loaiDe.length &&
    !filters.danhMuc.length &&
    !filters.xuatXu.length &&
    filters.giaTu === undefined &&
    filters.giaDen === undefined
})

const fetchColor = async () => {
  const { data } = await GetListColor()
  ColorOptions.value = data.map((c: any) => ({ label: c.ten, value: c.id, color: c.mau }))
}

const fetchThuongHieu = async () => {
  const { data } = await GetListThuongHieu()
  thuongHieuOptions.value = data.map((item: any) => ({
    label: item.ten,
    value: item.id
  }))
}

const fetchChatLieu = async () => {
  const { data } = await GetListChatLieu()
  chatLieuOptions.value = data.map((item: any) => ({
    label: item.ten,
    value: item.id
  }))
}

const fetchDanhMuc = async () => {
  const { data } = await GetListDanhMuc()
  danhMucOptions.value = data.map((item: any) => ({
    label: item.ten,
    value: item.id
  }))
}

const fetchLoaiDe = async () => {
  const { data } = await GetListLoaiDe()
  loaiDeOptions.value = data.map((item: any) => ({
    label: item.ten,
    value: item.id
  }))
}

const fetchSize = async () => {
  const { data } = await GetListSize()
  SizeOptions.value = data.map((s: any) => ({ label: s.ten, value: s.id }))
}

onMounted(async () => {
  await fetchColor(),
  await fetchThuongHieu(),
  await fetchChatLieu(),
  await fetchDanhMuc(),
  await fetchLoaiDe(),
  await fetchSize()
})

const toggleSection = (key: string) => {
  sectionOpen[key] = !sectionOpen[key]
}

const toggleMauSac = (maMau: string) => {
  const index = filters.mauSac.indexOf(maMau)
  index >= 0 ? filters.mauSac.splice(index, 1) : filters.mauSac.push(maMau)
}

const toggleKichCo = (size: string) => {
  const index = filters.kichCo.indexOf(size)
  index >= 0 ? filters.kichCo.splice(index, 1) : filters.kichCo.push(size)
}

const validatePrice = (field: 'giaTu' | 'giaDen', event: Event) => {
  const input = event.target as HTMLInputElement
  let value = input.value.replace(/[^0-9]/g, '') // Chỉ giữ số
  if (value === '') {
    filters[field] = undefined // Đặt undefined nếu rỗng
  } else {
    value = Math.max(0, parseInt(value, 10)) // Đảm bảo giá trị không âm
    filters[field] = value
  }
  // Đồng bộ giá trị hiển thị
  input.value = filters[field] ? filters[field].toLocaleString('vi-VN') : ''
}

const applyFilters = () => {
  emit('filter', { ...filters })
}

const resetFilters = () => {
  Object.assign(filters, {
    thuongHieu: [],
    mauSac: [],
    kichCo: [],
    chatLieu: [],
    loaiDe: [],
    danhMuc: [],
    xuatXu: [],
    giaTu: undefined,
    giaDen: undefined
  })
  applyFilters()
}
</script>
<style scoped>
.color-check {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 1px solid #ccc;
  cursor: pointer;
  transition: outline 0.2s ease;
}

.size-box {
  padding: 6px 12px;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  background-color: #f8f9fa;
  transition: all 0.2s ease;
}

.size-box.active {
  background-color: #0d6efd;
  color: white;
  border-color: #0d6efd;
}

.size-box:hover {
  background-color: #e9ecef;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.btn {
  padding: 6px 12px;
  font-size: 14px;
}

.border-bottom {
  border-color: #dee2e6 !important;
}
</style>