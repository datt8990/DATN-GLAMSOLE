<template>
  <div class="border rounded p-3 bg-light">
    <h5 class="fw-bold mb-3">Bộ lọc sản phẩm</h5>

    <!-- Accordion Block -->
    <div v-for="section in sections" :key="section.key" class="mb-3">
      <div
        class="d-flex justify-content-between align-items-center border-bottom pb-1 mb-2 cursor-pointer"
        @click="toggleSection(section.key)"
        style="user-select: none;"
      >
        <span class="fw-semibold">{{ section.label }}</span>
        <i class="bi" :class="sectionOpen[section.key] ? 'bi-chevron-up' : 'bi-chevron-down'"></i>
      </div>

      <div v-show="sectionOpen[section.key]">
        <!-- Thương hiệu -->
        <div v-if="section.key === 'thuongHieu'">
          <div v-for="item in thuongHieuList" :key="item" class="form-check">
            <input class="form-check-input" type="checkbox" :value="item" v-model="filters.thuongHieu" />
            <label class="form-check-label">{{ item }}</label>
          </div>
        </div>

        <!-- Chất liệu -->
        <div v-else-if="section.key === 'chatLieu'">
          <div v-for="item in chatLieuList" :key="item" class="form-check">
            <input class="form-check-input" type="checkbox" :value="item" v-model="filters.chatLieu" />
            <label class="form-check-label">{{ item }}</label>
          </div>
        </div>

        <!-- Loại đế -->
        <div v-else-if="section.key === 'loaiDe'">
          <div v-for="item in loaiDeList" :key="item" class="form-check">
            <input class="form-check-input" type="checkbox" :value="item" v-model="filters.loaiDe" />
            <label class="form-check-label">{{ item }}</label>
          </div>
        </div>

        <!-- Danh mục -->
        <div v-else-if="section.key === 'danhMuc'">
          <div v-for="item in danhMucList" :key="item" class="form-check">
            <input class="form-check-input" type="checkbox" :value="item" v-model="filters.danhMuc" />
            <label class="form-check-label">{{ item }}</label>
          </div>
        </div>

        <!-- Xuất xứ -->
        <div v-else-if="section.key === 'xuatXu'">
          <div v-for="item in xuatXuList" :key="item" class="form-check">
            <input class="form-check-input" type="checkbox" :value="item" v-model="filters.xuatXu" />
            <label class="form-check-label">{{ item }}</label>
          </div>
        </div>

        <!-- Màu sắc -->
        <div v-else-if="section.key === 'mauSac'">
          <div class="d-flex flex-wrap gap-2 mt-1">
            <div
              v-for="color in mauSacList"
              :key="color.maMau"
              class="color-check"
              :title="color.tenMau"
              :style="{
                backgroundColor: color.maMau,
                outline: filters.mauSac.includes(color.maMau) ? '2px solid black' : 'none'
              }"
              @click="toggleMauSac(color.maMau)"
            ></div>
          </div>
        </div>

        <!-- Kích cỡ -->
        <div v-else-if="section.key === 'kichCo'">
          <div class="d-flex flex-wrap gap-2 mt-1">
            <span
              v-for="size in kichCoList"
              :key="size"
              class="size-box"
              :class="{ active: filters.kichCo.includes(size) }"
              @click="toggleKichCo(size)"
            >{{ size }}</span>
          </div>
        </div>

        <!-- Khoảng giá -->
        <div v-else-if="section.key === 'gia'">
          <label class="form-label small">Từ: {{ filters.giaTu.toLocaleString() }}₫</label>
          <input type="range" class="form-range" v-model="filters.giaTu" :min="0" :max="filters.giaDen" step="50000" />
          <label class="form-label small">Đến: {{ filters.giaDen.toLocaleString() }}₫</label>
          <input type="range" class="form-range" v-model="filters.giaDen" :min="filters.giaTu" :max="5000000" step="50000" />
        </div>
      </div>
    </div>

    <!-- Nút áp dụng & xóa -->
    <div class="d-flex justify-content-between mt-3 gap-2">
      <button class="btn btn-outline-danger btn-sm" @click="resetFilters" title="Xóa lọc">
        <i class="bi bi-x-circle"></i> Xóa
      </button>
      <button class="btn btn-outline-success btn-sm" @click="applyFilters" title="Áp dụng bộ lọc">
        <i class="bi bi-funnel-fill"></i> Lọc
      </button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, defineEmits } from 'vue'

const emit = defineEmits(['filter'])

const filters = reactive({
  thuongHieu: [] as string[],
  mauSac: [] as string[],
  kichCo: [] as string[],
  chatLieu: [] as string[],
  loaiDe: [] as string[],
  danhMuc: [] as string[],
  xuatXu: [] as string[],
  giaTu: 0,
  giaDen: 5000000
})

const sectionOpen = reactive<Record<string, boolean>>({
  thuongHieu: false,
  mauSac: false,
  kichCo: false,
  chatLieu: false,
  loaiDe: false,
  danhMuc: false,
  xuatXu: false,
  gia: false
})

const sections = [
  { key: 'thuongHieu', label: 'Thương hiệu' },
  { key: 'chatLieu', label: 'Chất liệu' },
  { key: 'loaiDe', label: 'Loại đế' },
  { key: 'danhMuc', label: 'Danh mục' },
  { key: 'xuatXu', label: 'Xuất xứ' },
  { key: 'mauSac', label: 'Màu sắc' },
  { key: 'kichCo', label: 'Kích cỡ' },
  { key: 'gia', label: 'Khoảng giá' }
]

// dữ liệu giả
const thuongHieuList = ['Nike', 'Adidas', 'Converse', 'Puma']
const chatLieuList = ['Vải', 'Da', 'Giả da', 'Nhựa']
const loaiDeList = ['Đế bằng', 'Đế cao su', 'Đế cao']
const danhMucList = ['Thể thao', 'Đi học', 'Đi chơi', 'Đi làm']
const xuatXuList = ['Việt Nam', 'Trung Quốc', 'Hàn Quốc']
const kichCoList = ['36', '37', '38', '39', '40', '41', '42', '43', '44']
const mauSacList = [
  { tenMau: 'Trắng', maMau: '#ffffff' },
  { tenMau: 'Đen', maMau: '#000000' },
  { tenMau: 'Đỏ', maMau: '#ff0000' },
  { tenMau: 'Hồng', maMau: '#ffc0cb' },
  { tenMau: 'Nâu', maMau: '#6e4b2a' }
]

// hàm toggle
const toggleSection = (key: string) => {
  sectionOpen[key] = !sectionOpen[key]
}
function toggleMauSac(maMau: string) {
  const index = filters.mauSac.indexOf(maMau)
  index >= 0 ? filters.mauSac.splice(index, 1) : filters.mauSac.push(maMau)
}
function toggleKichCo(size: string) {
  const index = filters.kichCo.indexOf(size)
  index >= 0 ? filters.kichCo.splice(index, 1) : filters.kichCo.push(size)
}

function applyFilters() {
  emit('filter', { ...filters })
}
function resetFilters() {
  Object.assign(filters, {
    thuongHieu: [],
    mauSac: [],
    kichCo: [],
    chatLieu: [],
    loaiDe: [],
    danhMuc: [],
    xuatXu: [],
    giaTu: 0,
    giaDen: 5000000
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
}
.size-box {
  padding: 3px 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  background-color: #f8f8f8;
  transition: 0.2s;
}
.size-box.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}
</style>
