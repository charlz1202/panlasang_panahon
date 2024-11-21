<template>
  <div id="app">
    <!-- Hamburger Menu (Visible for logged-in users) -->
    <div v-if="isLoggedIn" class="hamburger-menu">
      <button class="hamburger-button" @click="toggleMenu">
        &#9776; <!-- Hamburger Icon -->
      </button>
      <div v-if="menuOpen" class="menu-content">
        <ul>
          <li><router-link to="/order-history">Order History</router-link></li>
          <li><button @click="logoutUser">Logout</button></li>
        </ul>
      </div>
    </div>

    <!-- Main Content -->
    <router-view />
  </div>
</template>

<script>
import { ref, watchEffect } from "vue";

export default {
  name: "App",
  setup() {
    // Track login status based on 'authToken' in localStorage
    const isLoggedIn = ref(!!localStorage.getItem("authToken"));

    // Watch localStorage to reactively update isLoggedIn
    watchEffect(() => {
      isLoggedIn.value = !!localStorage.getItem("authToken");
    });

    // Menu state for hamburger menu
    const menuOpen = ref(false);

    // Toggle menu visibility
    const toggleMenu = () => {
      menuOpen.value = !menuOpen.value;
    };

    // Log out the user
    const logoutUser = () => {
      localStorage.removeItem("authToken");
      localStorage.removeItem("user");
      isLoggedIn.value = false;
      menuOpen.value = false; // Close the menu
      window.location.href = "/login"; // Redirect to login
    };

    return { isLoggedIn, menuOpen, toggleMenu, logoutUser };
  },
};
</script>

<style scoped>
body {
  margin: 0;
  font-family: Arial, sans-serif;
}

#app {
  text-align: center;
}

.hamburger-menu {
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 1000;
}

.hamburger-button {
  font-size: 24px;
  background: none;
  border: none;
  cursor: pointer;
  color: #333;
}

.menu-content {
  position: absolute;
  top: 50px;
  right: 10px;
  background-color: #fff;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  border-radius: 8px;
  overflow: hidden;
}

.menu-content ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.menu-content ul li {
  padding: 10px 15px;
  text-align: left;
}

.menu-content ul li a,
.menu-content ul li button {
  text-decoration: none;
  color: #333;
  font-size: 16px;
  background: none;
  border: none;
  cursor: pointer;
  width: 100%;
  display: block;
  text-align: left;
}

.menu-content ul li button:hover,
.menu-content ul li a:hover {
  background-color: #f2f2f2;
}
</style>