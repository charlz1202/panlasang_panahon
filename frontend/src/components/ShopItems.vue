<template>
  <div class="shop-page">
    <!-- Logo -->
    <img src="@/assets/logo.jpg" alt="Panlasang Panahon Logo" class="logo" />

    <h2>Shop Items</h2>

    <!-- Hamburger Menu -->
    <div class="hamburger-menu" @click="toggleMenu">
      <i class="fas fa-bars"></i>
    </div>
    <transition name="fade">
      <div v-if="isMenuOpen" class="menu-overlay" @click.self="toggleMenu">
        <div class="menu">
          <ul>
            <li><a @click="goToOrderHistory">Order History</a></li>
            <li><a @click="logout">Logout</a></li>
          </ul>
        </div>
      </div>
    </transition>

    <!-- Cart Icon -->
    <div class="cart-icon" @click="toggleCartDrawer">
      <img src="/images/cart.png" alt="Cart" class="cart-image" />
    </div>

    <!-- Filters -->
    <div class="filters">
      <label for="category">Category:</label>
      <select v-model="selectedCategory">
        <option value="">All</option>
        <option value="MAIN_DISH">Main Dish</option>
        <option value="DESSERT">Dessert</option>
        <option value="DRINK">Drink</option>
      </select>

      <label for="weather">Weather:</label>
      <select v-model="selectedWeather">
        <option value="HOT">Summer</option>
        <option value="COLD">Cold</option>
        <option value="RAINY">Rainy</option>
      </select>

      <label for="location">Location:</label>
      <select v-model="selectedLocation">
        <option value="">All</option>
        <option v-for="location in availableLocations" :key="location" :value="location">
          {{ location }}
        </option>
      </select>
    </div>

    <!-- Items Display -->
    <div v-if="filteredItems.length > 0" class="card-container">
      <div v-for="item in filteredItems" :key="item.id" class="card">
        <img :src="getItemImage(item)" alt="Item Image" class="item-image" />
        <div class="card-content">
          <h3>{{ item.name }}</h3>
          <p>{{ item.description }}</p>
          <p>Available at <b>{{ item.restaurant.name }}</b></p>

          <div class="card-bottom">
            <p class="price"><strong>${{ item.price }}</strong></p>
            <button @click="addToCart(item)">Add to Cart</button>
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      <p>No items match your criteria.</p>
    </div>

    <!-- Cart Drawer -->
    <transition name="slide">
      <div v-if="isCartDrawerOpen" class="cart-drawer">
        <div class="cart-drawer-content">
          <p><b>Total: ${{ totalCartPrice }}</b></p>
          <button class="btn btn-primary btn-sm" @click="placeOrder">Go to cart</button>
          <ul class="cart-items">
            <li v-for="(item, index) in cart" :key="index" class="cart-item">
              <div class="cart-item-details">
                <img :src="getItemImage(item)" alt="Item Image" class="cart-item-image" />
                <p class="price">${{ item.price }}</p>
                <div>
                  <input
                    class="qty"
                    type="number"
                    v-model.number="item.quantity"
                    min="1"
                    @change="updateCartItemQuantity(index)"
                  />
                  <a href="#" @click.prevent="removeFromCart(index)" class="remove-button">
                    <i class="fas fa-trash-alt"></i>
                  </a>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      items: [],
      cart: [],
      selectedCategory: "",
      selectedWeather: "HOT",
      selectedLocation: "",
      availableLocations: [],
      userLocation: "Vancouver",
      isCartDrawerOpen: false,
      isMenuOpen: false,
    };
  },
  computed: {
    filteredItems() {
      return this.items.filter((item) => {
        const matchesCategory = !this.selectedCategory || item.category === this.selectedCategory;
        const matchesWeather = !this.selectedWeather || item.weather === this.selectedWeather;
        const matchesLocation = !this.selectedLocation || item.restaurant.city === this.selectedLocation;
        return matchesCategory && matchesWeather && matchesLocation;
      });
    },
    totalCartPrice() {
      return this.cart.reduce((total, item) => total + item.price * item.quantity, 0);
    },
  },
  methods: {
    async fetchItems() {
      try {
        const response = await axios.get("http://localhost:8080/api/items");
        this.items = response.data;
        this.availableLocations = [...new Set(this.items.map((item) => item.restaurant.city))];

        if (this.availableLocations.includes(this.userLocation)) {
          this.selectedLocation = this.userLocation;
        } else {
          this.selectedLocation = "Vancouver";
        }
      } catch (error) {
        console.error("Error fetching items:", error);
      }
    },
    logout() {
      localStorage.removeItem("authToken");
      this.cart = [];
      this.selectedLocation = "";
      window.location.href = "/login";
    },
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    goToOrderHistory() {
      this.$router.push("/order-history");
      this.toggleMenu();
    },
    async placeOrder() {
      const authToken = localStorage.getItem("authToken");
      if (!authToken) {
        alert("You must be logged in to place an order.");
        this.$router.push("/login");
        return;
      }

      const orderData = {
        items: this.cart.map((item) => ({ id: item.id, quantity: item.quantity })),
        price: this.totalCartPrice,
        weather: this.selectedWeather,
      };

      try {
        await axios.post("http://localhost:8080/api/orders", orderData, {
          headers: { Authorization: `Bearer ${authToken}` },
        });
        alert("Order placed successfully!");
        this.cart = [];
        this.toggleCartDrawer(false);
      } catch (error) {
        console.error("Error placing order:", error);
      }
    },
    getItemImage(item) {
      return `/images/${item.filename}`;
    },
    addToCart(item) {
      const authToken = localStorage.getItem("authToken");

      if (!authToken) {
        alert("You must be logged in to add items to the cart.");
        window.location.href = "/login";
        return;
      }

      const existingItem = this.cart.find((cartItem) => cartItem.id === item.id);
      if (existingItem) {
        existingItem.quantity += 1;
      } else {
        this.cart.push({ ...item, quantity: 1 });
      }

      this.isCartDrawerOpen = true;
    },
    updateCartItemQuantity(index) {
      if (this.cart[index].quantity <= 0) {
        this.removeFromCart(index);
      }
    },
    removeFromCart(index) {
      this.cart.splice(index, 1);
    },
    toggleCartDrawer(state) {
      if (typeof state === "boolean") {
        this.isCartDrawerOpen = state;
      } else {
        this.isCartDrawerOpen = !this.isCartDrawerOpen;
      }
    },
  },
  mounted() {
    this.fetchItems();
  },
};
</script>

<style scoped>
/* General Styles */
.shop-page {
  position: relative;
  padding: 20px;
}

h2 {
  text-align: center;
  margin-bottom: 20px;
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

/* Logo Styling */
.logo {
  display: block;
  margin: 0 auto 20px;
  width: 150px; /* Adjust the width of the logo */
}

/* Floating Cart Icon */
.cart-icon {
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 1000;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.cart-icon img {
  width: 24px;
  height: 24px;
}

/* Filters */
.filters {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}

.filters label {
  font-weight: bold;
}

/* Card Grid */
.card-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease;
}

.card:hover {
  transform: scale(1.05);
}

.card-content {
  padding: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.card-bottom {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.price {
  font-weight: bold;
  font-size: 1.2em;
  margin: 10px 0;
}

/* Item Images */
.item-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-bottom: 1px solid #ddd;
}

/* Cart Drawer */
.cart-drawer {
  position: fixed;
  top: 0;
  right: 0;
  width: 300px;
  height: 100%;
  background-color: white;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.5);
  overflow-y: auto;
  z-index: 1000;
  transition: transform 0.3s ease;
}

.cart-drawer-content {
  padding: 20px;
}

.cart-items {
  list-style: none;
  padding: 0;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.cart-item-details {
  display: flex;
  align-items: center;
  gap: 10px;
}

.cart-item-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
}

.qty {
  width: 50px;
  text-align: center;
}

.remove-button {
  color: #ff4d4d;
  cursor: pointer;
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

/* Transition Styles */
.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}

.slide-enter,
.slide-leave-to {
  transform: translateX(100%);
}
</style>
