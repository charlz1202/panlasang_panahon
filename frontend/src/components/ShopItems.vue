<template>
  <div class="shop-page">
    <h2>Shop Items</h2>

    <!-- Cart Icon (Always Visible) -->
    <div class="cart-icon" :class="{ 'cart-button-active': isCartDrawerOpen }" @click="toggleCartDrawer">
      <img src="/images/cart.png" alt="Cart" class="cart-image"/>
      <input type="number" class="cart-count text-center" v-model.number="displayItemCount" disabled/>
    </div>

    <!-- Filter by Category, Weather, and Location -->
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
        <option value="">All</option>
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

    <!-- Display items as cards based on filters -->
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
                  <input class="qty" type="number" v-model.number="item.quantity" min="1" @change="updateCartItemQuantity(index)" />
                  <a href='#' @click="removeFromCart(index)" class="remove-button">
                    <i class="fas fa-trash-alt"></i> <!-- FontAwesome icon for remove -->
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
import axios from 'axios';

export default {
  data() {
    return {
      items: [],
      cart: [],
      selectedCategory: '',
      selectedWeather: '',
      selectedLocation: '',
      availableLocations: [],
      userLocation: '',
      isCartDrawerOpen: false,
      orderSuccess: false,
    };
  },
  computed: {
    filteredItems() {
      return this.items.filter(item => {
        const matchesCategory = !this.selectedCategory || item.category === this.selectedCategory;
        const matchesWeather = !this.selectedWeather || item.weather === this.selectedWeather || item.weather === "BOTH";
        const matchesLocation = !this.selectedLocation || item.restaurant.location === this.selectedLocation;
        return matchesCategory && matchesWeather && matchesLocation;
      });
    },
    totalCartPrice() {
        return this.cart.reduce((total, item) => total + item.price * item.quantity, 0);
    },
    totalItemsCount() {
      return this.cart.reduce((count, item) => count + item.quantity, 0);
    },
    displayItemCount() {
      return this.totalItemsCount === 0 ? '' : this.totalItemsCount;
    }
  },
  methods: {
    async fetchItems() {
      try {
        const response = await axios.get('http://localhost:8080/api/items');
        this.items = response.data;
        this.availableLocations = [...new Set(this.items.map(item => item.restaurant.location))];
        if (localStorage.getItem('user')) {
          const user = JSON.parse(localStorage.getItem('user'));
          if (user && user.location) {
            this.userLocation = user.location;
            this.selectedLocation = user.location;
          }
        }
      } catch (error) {
        console.error('Error fetching items:', error);
      }
    },
    getItemImage(item) {
      return `/images/${item.filename}`;
    },
    addToCart(item) {
      const existingItem = this.cart.find(cartItem => cartItem.id === item.id);
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
      if (this.cart.length === 0) {
        this.isCartDrawerOpen = false;
      }
    },
    
    async placeOrder() {
      if (this.cart.length === 0) {
        alert('Cart is empty. Please add items.');
        return;
      }
      const orderData = {
        user: { id: 1 }, 
        dateTime: new Date().toISOString(),
        weather: this.selectedWeather || 'both',
        items: this.cart.map(item => ({ id: item.id })),
        price: this.totalCartPrice
      };

      try {
        const response = await axios.post('http://localhost:8080/api/orders', orderData);
        console.log('Order placed successfully:', response.data);
        alert('Order placed successfully!');
        this.cart = [];
        this.isCartDrawerOpen = false;
      } catch (error) {
        console.error('Error placing order:', error);
        alert('Failed to place the order. Please try again.');
      }
    },
    toggleCartDrawer() {
      if (this.cart.length > 0) {
        this.isCartDrawerOpen = !this.isCartDrawerOpen;
      }
    }
  },
  mounted() {
    this.fetchItems(); // Load items when component is mounted
  }
};
</script>

<style>
.shop-page {
  position: relative; 
}

.cart-button {
  position: fixed; 
  top: 10px; 
  right: 10px; 
  z-index: 10; 
  background-color: #007bff; 
  color: white; 
  border: none; 
  padding: 8px 12px; 
  border-radius: 5px; 
  cursor: pointer; 
}

.cart-drawer {
  position: fixed; 
  top: 0;
  right: 0; 
  width: 300px; 
  height: 100%; 
  background-color: white; 
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.5); 
  overflow-y: auto; 
  z-index: 5; 
  transition: transform 0.3s ease; 
}

.cart-drawer-content {
  padding: 20px; 
}

.filters {
  margin-bottom: 20px;
}

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
  height: 100%;
}

.card:hover {
  transform: scale(1.05);
}

.card-content {
  padding: 15px;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.item-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}


.card-bottom {
  margin-top: auto; 
  display: flex;
  flex-direction: column;
  align-items: center; 
}

.price {
  margin-bottom: 10px;
  font-size: 1.2em;
  color: #333;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 5px;
  width: fit-content;
  align-self: center;
}

button:hover {
  background-color: #0056b3;
}

h2,
h3 {
  margin-bottom: 10px;
}

.slide-enter-active,
.slide-leave-active {
  transition: transform 0.3s ease;
}
.slide-enter,
.slide-leave-to {
  transform: translateX(100%); 
}

.cart-button-active {
  transform: translateX(-300px); 
}

.cart-items {
  list-style: none;
  padding: 0;
}

.cart-item {
  display: flex;
  align-items: center; 
  border-bottom: 1px solid #ddd; 
  padding: 10px 0; 
}

.cart-item-image {
  width: 200px; 
  height: 150px; 
  object-fit: cover; 
}

.cart-item-details {
  flex-grow: 1; 
  display: flex;
  justify-content: space-between; 
  align-items: center; 
  flex-direction: column;
}

.remove-button {
  background: none; 
  border: none; 
  color: #ff0000; 
  cursor: pointer; 
}


input[type="number"].qty {
  width: 40px; 
  margin-right: 10px; 
  text-align: center;
}

.cart-icon {
  position: fixed; 
  top: 10px; 
  right: 10px; 
  z-index: 10; 
  cursor: pointer; 
}

.cart-count {
  font-size: 18px;
  position: absolute;
  top: 8px;
  left: 13px;
  transform: translate(0%, -50%);
  width: 40px;
  border: none 0px;
  background: transparent;
  font-weight: bold;
  color: orange;
}

.cart-image {
  width: 50px; 
  height: auto; 
}
</style>
