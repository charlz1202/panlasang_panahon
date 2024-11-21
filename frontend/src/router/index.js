import { createRouter, createWebHistory } from 'vue-router';
import RegistrationPage from '../components/RegistrationPage.vue';
import LoginPage from '../components/LoginPage.vue';
import HomePage from '../components/HomePage.vue';
import UploadRestaurant from '../components/UploadRestaurant.vue';
import UploadItem from '../components/UploadItem.vue';
import ShopItems from '../components/ShopItems.vue';
import OrderHistoryPage from '../components/OrderHistoryPage.vue';


const routes = [
  { path: '/', name: 'home', component: HomePage, meta: { requiresAuth: false } },
  { path: '/login', name: 'login', component: LoginPage, meta: { requiresAuth: false } },
  { path: '/register', name: 'register', component: RegistrationPage, meta: { requiresAuth: false } },
  { path: '/upload/restaurant', name: 'upload-restaurant', component: UploadRestaurant, meta: { requiresAuth: true } },
  { path: '/upload/item', name: 'upload-item', component: UploadItem, meta: { requiresAuth: true } },
  { path: '/shop', name: 'shop', component: ShopItems, meta: { requiresAuth: false } },
  { 
    path: '/order-history', 
    name: 'order-history', 
    component: () => import('@/components/OrderHistoryPage.vue'), 
    meta: { requiresAuth: true },
  },
  // Catch-all for unknown paths
  { path: '/:pathMatch(.*)*', redirect: '/' },
  { path: '/order-history', name: 'order-history', component: OrderHistoryPage, meta: { requiresAuth: true } },

];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation guard
router.beforeEach((to, from, next) => {
  const authToken = localStorage.getItem('authToken');

  console.log(`Navigating to: ${to.path}`);
  console.log(`From: ${from.path}`);
  console.log(`AuthToken Exists: ${!!authToken}`);

  // Allow access to public routes without authentication
  if (to.meta.requiresAuth === false) {
    console.log(`Public route detected: ${to.path}`);
    next(); // Allow navigation
    return;
  }

  // Redirect unauthenticated users trying to access private routes
  if (to.meta.requiresAuth === true && !authToken) {
    console.log(`Unauthenticated user trying to access a protected route: ${to.path}`);
    alert('You must be logged in to access this page.');
    next('/login'); // Redirect to login
    return;
  }

  // Prevent authenticated users from accessing the login page
  if (authToken && to.path === '/login') {
    console.log('Authenticated user trying to access /login. Redirecting to /shop.');
    next('/shop'); // Redirect to shop
    return;
  }

  // Allow authenticated users to proceed
  console.log(`Authenticated user accessing private route: ${to.path}`);
  next();
});

export default router;
