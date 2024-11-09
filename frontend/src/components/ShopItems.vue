<template>
  <div class="shop-page">
    <h2>Shop Items</h2>

    <!-- Logout Button (At the top-right corner) -->
    <button class="logout-button" @click="logout">Logout</button>

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
        const matchesWeather = !this.selectedWeather || item.weather === this.selectedWeather;
        const matchesLocation = !this.selectedLocation || item.restaurant.city === this.selectedLocation;
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

  logout() {
    // Clear any authentication data (e.g., tokens)
    localStorage.removeItem('authToken');
    
    // Redirect to Login Page
    this.$router.push('/login');
  },

async placeOrder() {
  // Check if the cart is empty
  if (this.cart.length === 0) {
    alert('Cart is empty. Please add items.');
    return;
  }

  // Validate selected weather
  const validWeatherValues = ['COLD', 'RAINY', 'HOT'];
  const weather = validWeatherValues.includes(this.selectedWeather) ? this.selectedWeather : null;

  if (!weather && this.selectedWeather !== '') {
    alert('Select the weather from the drop-down.');
    return;
  }

  // Check if the user is already logged in by checking for a stored token
let userId = 1;  // Default to guest user if not logged in
let token = localStorage.getItem('authToken') || '';  // Retrieve token from localStorage if available

if (!token) {
  // User is not logged in; attempt to log in
  try {
    const userResponse = await axios.post('http://localhost:8080/api/users/login', {
      email: this.email,
      password: this.password
    });

    if (userResponse.data && userResponse.data.id && userResponse.data.token) {
      userId = userResponse.data.id; // Use the logged-in user's ID
      token = userResponse.data.token;  // Store the JWT token
      localStorage.setItem('authToken', token); // Save token in localStorage for future requests
    } else {
      alert('Invalid login credentials. Please check your email and password.');
      return;
    }
  } catch (error) {
    console.error('Error fetching user info:', error);
    alert('An error occurred while logging in. Please try again later.');
    return;
  }
}

  // Prepare order data
  const orderData = {
    user: { id: userId },
    dateTime: new Date().toISOString(),
    weather: weather,
    items: this.cart.map(item => ({ id: item.id, quantity: item.quantity })),
    price: this.totalCartPrice
  };

  // Place the order with token in the header
  try {
    const response = await axios.post('http://localhost:8080/api/orders', orderData, {
      headers: {
        'Authorization': `Bearer ${token}`  // Include the token in the Authorization header
      }
    });
    console.log('Order saved successfully:', response.data);

    // After placing the order, redirect to the Order History page
    this.$router.push('/order-history');  // Use Vue Router to navigate

    // Clear the cart and close the cart drawer
    this.cart = [];
    this.isCartDrawerOpen = false;
  } catch (error) {
    console.error('Error placing order:', error);
    alert('Failed to place the order. Please try again.');
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

.logout-button {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 8px 12px;
  background-color: red;
  color: white;
  border: none;
  cursor: pointer;
}

.logout-button:hover {
  background-color: darkred;
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
  font-weight: bold;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.cart-item-details {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.cart-item-image {
  width: 50px;
  height: 50px;
  object-fit: cover;
}

.qty {
  width: 50px;
}

.remove-button {
  color: #ff4d4d;
  cursor: pointer;
}

.cart-items {
  list-style: none;
  padding-left: 0;
}

.cart-icon {
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 10;
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px; /* Reduced padding to avoid large icon */
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px; /* Set a fixed width */
  height: 50px; /* Set a fixed height */
}

.cart-icon img {
  width: 25px; /* Control the size of the cart icon image */
  height: 25px;
}

.cart-count {
  position: absolute;
  top: -5px;
  right: -5px;
  background-color: red;
  color: white;
  font-size: 12px; /* Make the count smaller */
  padding: 2px 5px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 18px;
  height: 18px;
  font-weight: bold;
}

.cart-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 300px;
  background-color: white;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.5);
  padding: 20px;
  z-index: 20;
}
.cart-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.3);
  z-index: 15;
}

</style>