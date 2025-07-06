<template>
    <div class="container-fluid d-flex flex-column align-items-center">
        <!-- Breadcrumb + tiêu đề -->
        <div class="container py-3">
            <div class="row align-items-center">
                <BreadCrumbUser :routes="breadcrumbRoutes" title="Danh sách sản phẩm" />
            </div>
        </div>

        <div class="container d-flex flex-column flex-lg-row gap-4">
            <!-- Bộ lọc  -->
            <div class="col-12 col-lg-3">
                <FilterBox />
            </div>
            <!-- Sản phẩm  -->
            <div class="col flex-fill">
                <div class="d-flex flex-column flex-md-row justify-content-between align-items-center">
                    <p class="fw-bold">
                        Showing {{ sanPhamList.length }} results for:
                        <span class="fw-normal">{{ keyword }}</span>
                    </p>
                    <div class="d-flex gap-3">
                        <select class="form-select form-select-sm text-secondary border-secondary">
                            <option value="">Sort by</option>
                            <option value="CreatedAt">Created at</option>
                            <option value="Price">Price</option>
                            <option value="Rating">Rating</option>
                            <option value="Sold">Sold</option>
                        </select>
                        <select class="form-select form-select-sm text-secondary border-secondary">
                            <option value="">Ascending</option>
                            <option value="true">Descending</option>
                        </select>
                    </div>
                </div>

                <div class="row g-3 mt-3">
                    <div v-for="(item, index) in sanPhamList" :key="index" class="col-6 col-md-4 col-lg-3">
                        <div class="card h-100 product-card" @click="handleClick(item)">
                            <div class="img-wrapper position-relative">
                                <img :src="item.imageUrl" class="card-img-top product-img" alt="product image" />
                                <div class="hover-overlay">
                                    <div class="mb-2">
                                        <strong>Màu sắc:</strong>
                                        <div class="d-flex gap-1 mt-1">
                                            <span v-for="(color, i) in item.mauList" :key="i" class="color-dot"
                                                :style="{ backgroundColor: color.maMau }" :title="color.tenMau"></span>
                                        </div>
                                    </div>
                                    <div>
                                        <strong>Kích cỡ:</strong>
                                        <div class="d-flex gap-2 flex-wrap mt-1">
                                            <span class="size-box" v-for="(size, i) in item.kichCoList" :key="i">{{ size
                                            }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="card-body">
                                <h6 class="card-title text-truncate" :title="item.tenSanPham">{{ item.tenSanPham }}</h6>
                                <p class="card-text mb-1">Brand: {{ item.thuongHieu }}</p>
                                <p class="card-text">Price: {{ item.giaBan.toLocaleString() }}₫</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import FilterBox from './FilterBox.vue'

const keyword = ref<string>('Cold Weather')

const breadcrumbRoutes = [
    { name: 'Trang chủ', path: '/' },
    { name: 'Sản phẩm', path: '/san-pham' }
];

const sanPhamList = ref([
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },

    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },
    {
        id: 1,
        tenSanPham: 'Sneaker Trắng Basic Nam',
        imageUrl: 'https://tse2.mm.bing.net/th/id/OIP.sZYIBcS6g7FQJFJemXZ4PQHaHa?pid=Api&P=0&h=180',
        thuongHieu: 'Nike',
        giaBan: 1250000,
        mauList: [
            { tenMau: 'Trắng', maMau: '#ffffff' },
            { tenMau: 'Đen', maMau: '#000000' }
        ],
        kichCoList: ['40', '41', '42']
    },

    {
        id: 2,
        tenSanPham: 'Adidas Nữ Hồng Pastel',
        imageUrl: 'https://tse1.mm.bing.net/th/id/OIP.kHHSVIvcAzElFc54Nb0MxQHaFc?pid=Api&P=0&h=180',
        thuongHieu: 'Adidas',
        giaBan: 1320000,
        mauList: [
            { tenMau: 'Hồng', maMau: '#ffc0cb' },
            { tenMau: 'Trắng', maMau: '#ffffff' }
        ],
        kichCoList: ['36', '37', '38']
    },
    {
        id: 3,
        tenSanPham: 'Boot Nam Da Nâu Cao Cổ',
        imageUrl: 'https://vn-test-11.slatic.net/p/23248c8fb9647b30f8013baff44c8029.jpg',
        thuongHieu: 'Dr.Martens',
        giaBan: 1650000,
        mauList: [
            { tenMau: 'Nâu', maMau: '#6e4b2a' }
        ],
        kichCoList: ['42', '43', '44']
    }
])

import { useRouter } from 'vue-router'
import BreadCrumbUser from '@/components/ui/Breadcrumbs/BreadCrumbUser.vue'

const router = useRouter()

const handleClick = (product: any) => {
    console.log('Bạn đã click vào sản phẩm:', product)
    router.push({
        name: 'san-pham-chi-tiet',
        params: { id: product.id }
    })
}

</script>
<style scoped>
.img-wrapper {
    width: 100%;
    height: 200px;
    overflow: hidden;
    background-color: #f8f8f8;
    position: relative;
}

.product-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
}

.product-card {
    cursor: pointer;
}

.hover-overlay {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background: rgba(255, 255, 255, 0.95);
    padding: 0.75rem;
    font-size: 14px;
    transform: translateY(100%);
    transition: transform 0.3s ease-in-out;
    color: #333;
}

.img-wrapper:hover .hover-overlay {
    transform: translateY(0%);
}

.color-dot {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    border: 1px solid #ccc;
    display: inline-block;
}

.size-box {
    padding: 2px 6px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 12px;
    background-color: #f9f9f9;
}
</style>
