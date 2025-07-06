<template>
  <div id="bannerCarousel" class="carousel slide w-100" data-bs-ride="carousel">
    <div class="carousel-inner rounded overflow-hidden">
      <div
        v-for="(item, index) in banners"
        :key="item.id"
        class="carousel-item"
        :class="{ active: index === 0 }"
      >
        <img
          :src="item.image"
          class="d-block w-100"
          alt="Banner"
          style="height: 380px; object-fit: cover"
        />
      </div>
    </div>

    <!-- Dots -->
    <div class="carousel-indicators">
      <button
        v-for="(item, index) in banners"
        :key="'dot-' + index"
        type="button"
        data-bs-target="#bannerCarousel"
        :data-bs-slide-to="index"
        :class="{ active: index === 0 }"
        :aria-current="index === 0"
        :aria-label="'Slide ' + (index + 1)"
      ></button>
    </div>

    <!-- Controls -->
    <button
      class="carousel-control-prev"
      type="button"
      data-bs-target="#bannerCarousel"
      data-bs-slide="prev"
    >
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Previous</span>
    </button>
    <button
      class="carousel-control-next"
      type="button"
      data-bs-target="#bannerCarousel"
      data-bs-slide="next"
    >
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Next</span>
    </button>
  </div>
</template>

<script setup lang="ts">
const banners = [
  {
    id: 1,
    image: 'https://intphcm.com/data/upload/poster-giay-den.jpg', // sneaker trắng đơn giản
  },
  {
    id: 2,
    image: 'https://www.elleman.vn/wp-content/uploads/2018/08/13/gi%C3%A0y-sneakers-2-elle-man-8.jpg', // phong cách thể thao, sàn màu tối
  },
  {
    id: 3,
    image: 'https://laruki.com/wp-content/uploads/2021/05/bitis-hunter-street-vietnam-arising.jpg', // giày da cổ điển sang trọng
  },
]

</script>
