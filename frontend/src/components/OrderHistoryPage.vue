<template>
  <div class="order-history-page">
    <!-- Hamburger Menu -->
    <div class="hamburger-menu" @click="toggleMenu">
      <i class="fas fa-bars"></i>
    </div>
    <transition name="fade">
      <div v-if="isMenuOpen" class="menu-overlay" @click.self="toggleMenu">
        <div class="menu">
          <ul>
            <li><a @click="goToShop">Go to Shop</a></li>
            <li><a @click="logout">Logout</a></li>
          </ul>
        </div>
      </div>
    </transition>

    <h2 class="page-title">Your Order History</h2>
    <div v-if="orders.length > 0" class="order-list">
      <div v-for="order in orders" :key="order.id" class="order-card">
        <h3>Order #{{ order.id }}</h3>
        <p><strong>Date:</strong> {{ formatDate(order.datetime) }}</p>
        <p><strong>Total:</strong> ${{ order.price.toFixed(2) }}</p>
        <ul class="order-items">
          <li v-for="item in order.items" :key="item.id">
            <strong>Quantity:</strong> {{ item.quantity }} - {{ item.name }} - ${{ item.price.toFixed(2) }}
          </li>
        </ul>
      </div>
    </div>
    <div v-else class="no-orders">
      <p>No orders found.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "OrderHistoryPage",
  data() {
    return {
      orders: [], // Store order history
      isMenuOpen: false, // Track menu open/close state
    };
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    goToShop() {
      this.$router.push("/shop"); // Navigate to the shop page
      this.toggleMenu(); // Close the menu
    },
    logout() {
      localStorage.removeItem("authToken"); // Remove the auth token
      window.location.href = "/login"; // Redirect to login page
    },
    formatDate(datetime) {
      return new Date(datetime).toLocaleString(); // Format datetime as readable string
    },
  },
  async created() {
    try {
      const authToken = localStorage.getItem("authToken");
      const response = await fetch("http://localhost:8080/api/orders/history", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${authToken}`,
        },
      });

      if (response.ok) {
        this.orders = await response.json();
      } else {
        console.error("Failed to fetch order history:", response.status, response.statusText);
      }
    } catch (error) {
      console.error("Error fetching order history:", error);
    }
  },
};
</script>

<style scoped>
/* General Styles */
.order-history-page {
  padding: 20px;
  font-family: Arial, sans-serif;
  position: relative;
  max-width: 800px;
  margin: 0 auto;
}

/* Hamburger Menu */
.hamburger-menu {
  position: fixed;
  top: 10px;
  left: 10px;
  z-index: 1000;
  cursor: pointer;
  font-size: 24px;
}

/* Menu Overlay */
.menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 900;
}

.menu {
  position: absolute;
  top: 50px;
  left: 10px;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.menu ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

.menu ul li {
  margin-bottom: 10px;
}

.menu ul li a {
  color: #007bff;
  cursor: pointer;
}

.menu ul li a:hover {
  text-decoration: underline;
}

/* Page Title */
.page-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
  font-weight: bold;
  color: #333;
}

/* Order List Styles */
.order-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  transition: transform 0.2s ease;
}

.order-card:hover {
  transform: scale(1.02);
}

.order-items {
  list-style: none;
  margin: 10px 0 0;
  padding: 0;
}

.order-items li {
  margin: 5px 0;
}

/* No Orders Found */
.no-orders {
  text-align: center;
  font-size: 18px;
  color: #999;
}
</style>
