import { createApp } from 'vue';
import App from './App.vue';
import router from './router';  // Import the router configuration

// Import Bootstrap and Bootstrap Icons
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js'; // Optionally import JS for components like modals, tooltips

// Optional: Import Bootstrap Icons if you need them
import 'bootstrap-icons/font/bootstrap-icons.css';

createApp(App).use(router).mount('#app');
