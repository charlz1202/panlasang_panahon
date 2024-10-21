import { createRouter, createWebHistory } from 'vue-router';
import RegistrationPage from '../components/RegistrationPage.vue';
import LoginPage from '../components/LoginPage.vue';
import HomePage from '../components/HomePage.vue';
import UploadRestaurant from '../components/UploadRestaurant.vue';
import UploadItem from '../components/UploadItem.vue';
import ShopItems from '../components/ShopItems.vue';

const routes = [
  { path: '/', component: HomePage },
  { path: '/login', name: 'login', component: LoginPage },
  { path: '/register', component: RegistrationPage },
  { path: '/upload/restaurant', component: UploadRestaurant },
  { path: '/upload/item', component: UploadItem },
  { path: '/shop', component: ShopItems },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
