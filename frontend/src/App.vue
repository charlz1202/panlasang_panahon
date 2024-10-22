<template>
  <div id="app">
    <nav v-if="showNavBar">
      <router-link to="/shop">Shop Items</router-link> | 
      <!-- Conditionally render Login/Logout based on user's login status -->
      <router-link v-if="!isLoggedIn" to="/login">
        <a href="#">Login</a>
      </router-link>
      <a v-if="isLoggedIn" href="#" @click="logoutUser">Logout</a>
    </nav>
    <router-view :isLoggedIn="isLoggedIn" @login="handleLogin" />
  </div>
</template>

<script>
import { ref, watchEffect } from 'vue';
import { useRoute } from 'vue-router';

export default {
  name: 'App',
  setup() {
    const route = useRoute();
    // Track login status based on 'user' in localStorage
    const isLoggedIn = ref(!!localStorage.getItem('user'));

    // Watch localStorage to reactively update isLoggedIn
    watchEffect(() => {
      isLoggedIn.value = !!localStorage.getItem('user');
    });

    // Function to logout the user
    const logoutUser = () => {
      try {
        // Clear user data from localStorage
        localStorage.removeItem('user');

        // Set isLoggedIn to false to hide logout and show login
        isLoggedIn.value = false;

        // Optionally, redirect to login page after logout
        window.location.href = '/login'; // Reload page to update state
      } catch (error) {
        console.error('Error during logout:', error);
      }
    };

    // Handle successful login
    const handleLogin = () => {
      isLoggedIn.value = true;
    };

    // show or hide the navbar based on the current route
    const showNavBar = ref(true);
    watchEffect(() => {
      showNavBar.value = (route.path !== '/login' && route.path !== '/register'); // Hide navbar if the route is '/login'
    });

    return { isLoggedIn, logoutUser, handleLogin, showNavBar };
  }
};
</script>

<style>
body {
  margin: 10px;
}
#app {
  text-align: center;
}
nav {
  margin-bottom: 20px;
}
nav a {
  text-decoration: none;
  color: #42b983;
  cursor: pointer;
}
nav a:hover {
  text-decoration: underline;
}
</style>
