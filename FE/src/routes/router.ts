import { ROUTES_CONSTANTS } from '@/constants/path'
import { ROLES } from '@/constants/roles'
import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  // Not Found route
  {
    path: ROUTES_CONSTANTS.NOT_FOUND.path,
    name: ROUTES_CONSTANTS.NOT_FOUND.name,
    component: () => import('@/pages/404/NotFound.vue')
  },
  // 403 route
  {
    path: ROUTES_CONSTANTS.FORBIDDEN.path,
    name: ROUTES_CONSTANTS.FORBIDDEN.name,
    component: () => import('@/pages/403/Forbidden.vue')
  },
  // 401 route
  {
    path: ROUTES_CONSTANTS.UNAUTHORIZED.path,
    name: ROUTES_CONSTANTS.UNAUTHORIZED.name,
    component: () => import('@/pages/401/Unauthorized.vue')
  },
  {
    path: ROUTES_CONSTANTS.LOGIN.path,
    name: ROUTES_CONSTANTS.LOGIN.name,
    component: () => import('@/components/ui/login/Login.vue')
  },

  
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.MAUSAC.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.MAUSAC.path,
        name: ROUTES_CONSTANTS.ADMIN.children.MAUSAC.name,
        component: () => import('@/pages/admin/mausac/MauSac.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.HOA_DON.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.HOA_DON.path,
        name: ROUTES_CONSTANTS.ADMIN.children.HOA_DON.name,
        component: () => import('@/pages/admin/hoadon/HoaDon.vue'),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.HOA_DON_DETAIL.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.HOA_DON_DETAIL.path,
        name: ROUTES_CONSTANTS.ADMIN.children.HOA_DON_DETAIL.name,
        component: () => import('@/pages/admin/hoadon/HoaDonModal.vue'),
        meta: {
          requiresRole: ROLES.ADMIN,
          requiresAuth: true
        }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SIZE.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SIZE.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SIZE.name,
        component: () => import('@/pages/admin/size/Size.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.THUONG_HIEU.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.THUONG_HIEU.path,
        name: ROUTES_CONSTANTS.ADMIN.children.THUONG_HIEU.name,
        component: () => import('@/pages/admin/thuonghieu/ThuongHieu.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.XUAT_XU.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.XUAT_XU.path,
        name: ROUTES_CONSTANTS.ADMIN.children.XUAT_XU.name,
        component: () => import('@/pages/admin/xuatxu/XuatXu.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.KHACH_HANG.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.KHACH_HANG.path,
        name: ROUTES_CONSTANTS.ADMIN.children.KHACH_HANG.name,
        component: () => import('@/pages/admin/khachhang/KhachHang.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.NHAN_VIEN.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.NHAN_VIEN.path,
        name: ROUTES_CONSTANTS.ADMIN.children.NHAN_VIEN.name,
        component: () => import('@/pages/admin/nhanvien/NhanVien.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM.name,
        component: () => import('@/pages/admin/sanpham/SanPham.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },

  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.DOT_GIAM_GIA.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.DOT_GIAM_GIA.path,
        name: ROUTES_CONSTANTS.ADMIN.children.DOT_GIAM_GIA.name,
        component: () => import('@/pages/admin/dotgiamgia/DotGiamGia.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },

  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.ADD_DOT_GIAM_GIA.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.ADD_DOT_GIAM_GIA.path,
        name: ROUTES_CONSTANTS.ADMIN.children.ADD_DOT_GIAM_GIA.name,
        component: () => import('@/pages/admin/dotgiamgia/DotGiamGiaModal.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },

  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.UPDATE_DOT_GIAM_GIA.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.UPDATE_DOT_GIAM_GIA.path,
        name: ROUTES_CONSTANTS.ADMIN.children.UPDATE_DOT_GIAM_GIA.name,
        component: () => import('@/pages/admin/dotgiamgia/DotGiamGiaUpdate.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },

  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM_CHI_TIET.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM_CHI_TIET.path,
        name: ROUTES_CONSTANTS.ADMIN.children.SAN_PHAM_CHI_TIET.name,
        component: () => import('@/pages/admin/sanphamchitiet/SanPhamChiTiet.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.CHAT_LIEU.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.CHAT_LIEU.path,
        name: ROUTES_CONSTANTS.ADMIN.children.CHAT_LIEU.name,
        component: () => import('@/pages/admin/chatlieu/ChatLieu.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.LOAI_DE.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.LOAI_DE.path,
        name: ROUTES_CONSTANTS.ADMIN.children.LOAI_DE.name,
        component: () => import('@/pages/admin/loaide/LoaiDe.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.LOAI_GIAY.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.LOAI_GIAY.path,
        name: ROUTES_CONSTANTS.ADMIN.children.LOAI_GIAY.name,
        component: () => import('@/pages/admin/loaigiay/LoaiGiay.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.THEM_SAN_PHAM_CHI_TIET.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.THEM_SAN_PHAM_CHI_TIET.path,
        name: ROUTES_CONSTANTS.ADMIN.children.THEM_SAN_PHAM_CHI_TIET.name,
        component: () => import('@/pages/admin/sanphamchitiet/createsanpham/CreateSanPhamChiTiet.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
  {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.path,
        name: ROUTES_CONSTANTS.ADMIN.children.BAN_HANG.name,
        component: () => import('@/pages/admin/banhang/BanHang.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
    {
    path: ROUTES_CONSTANTS.ADMIN.path,
    redirect: `${ROUTES_CONSTANTS.ADMIN.path}/${ROUTES_CONSTANTS.ADMIN.children.VOUCHER.path}`,
    component: () => import('@/layout/Admin.vue'),
    children: [
      {
        path: ROUTES_CONSTANTS.ADMIN.children.VOUCHER.path,
        name: ROUTES_CONSTANTS.ADMIN.children.VOUCHER.name,
        component: () => import('@/pages/admin/voucher/Voucher.vue'),
        // meta: {
        //   requiresRole: ROLES.ADMIN,
        //   requiresAuth: true
        // }
      }
    ]
  },
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})
