import { createRouter, createWebHistory } from 'vue-router';
import { useAdminStore } from '../stores/admin';

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/Login.vue')
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/Dashboard.vue')
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('../views/product/Product.vue')
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/category/Category.vue')
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/order/Order.vue')
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const adminStore = useAdminStore();
  if (to.path !== '/login' && !adminStore.token) {
    next('/login');
  } else {
    next();
  }
});

export default router;
