<template>
  <div>
    <h2>Shop Items</h2>

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
        <option value="HOT">Hot</option>
        <option value="COLD">Cold</option>
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
          <p><strong>${{ item.price }}</strong></p>
          <button @click="addToCart(item)">Add to Cart</button>
        </div>
      </div>
    </div>
    <div v-else>
      <p>No items match your criteria.</p>
    </div>

    <!-- Display Cart -->
    <div v-if="cart.length > 0" class="cart">
      <h3>Cart</h3>
      <ul>
        <li v-for="(item, index) in cart" :key="index">
          <p>{{ item.name }} - ${{ item.price }}</p>
          <button @click="removeFromCart(index)">Remove</button>
        </li>
      </ul>
      <p>Total: ${{ totalCartPrice }}</p>
      <button @click="placeOrder">Place Order</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      items: [],              // All items loaded from the backend
      cart: [],               // Cart items
      selectedCategory: '',   // Filter category
      selectedWeather: '',    // Filter weather
      selectedLocation: '',   // Selected location
      availableLocations: [], // Available locations
      userLocation: '',       // Saved location of the logged-in user
      orderSuccess: false,    // To display success message on order
    };
  },
  computed: {
    // Filter items based on selected category, weather, and location
    filteredItems() {
      return this.items.filter(item => {
        const matchesCategory = !this.selectedCategory || item.category === this.selectedCategory;
        const matchesWeather = !this.selectedWeather || item.weather === this.selectedWeather || item.weather === "BOTH";
        const matchesLocation = !this.selectedLocation || item.restaurant.location === this.selectedLocation;
        return matchesCategory && matchesWeather && matchesLocation;
      });
    },
    totalCartPrice() {
      return this.cart.reduce((total, item) => total + item.price, 0);
    }
  },
  methods: {
    // Fetch items from backend
    async fetchItems() {
      try {
        const response = await axios.get('http://localhost:8080/api/items');
        this.items = response.data;

        // Extract locations from the items
        this.availableLocations = [...new Set(this.items.map(item => item.restaurant.location))];


        // If user is logged in, get user location and filter items
        if (localStorage.getItem('user')) {
          const user = JSON.parse(localStorage.getItem('user'));
          if (user && user.location) {
            this.userLocation = user.location;
            this.selectedLocation = user.location; // Set location based on the user's saved location
          }
        }

      } catch (error) {
        console.error('Error fetching items:', error);
      }
    },
    // Get the item's image path
    getItemImage(item) {
      return `/images/${item.filename}`; // Adjust this path based on your server
    },
    // Add item to cart
    addToCart(item) {
      this.cart.push(item);
    },
    // Remove item from cart
    removeFromCart(index) {
      this.cart.splice(index, 1);
    },
    // Place an order
    async placeOrder() {
      if (this.cart.length === 0) {
        alert('Cart is empty. Please add items.');
        return;
      }

      // Prepare the order data to send to the backend
      const orderData = {
        user: { id: 1 }, // Assuming the logged-in user has ID 1 for now
        dateTime: new Date().toISOString(),
        weather: this.selectedWeather || 'both',
        items: this.cart.map(item => ({ id: item.id })),
        price: this.totalCartPrice
      };

      try {
        const response = await axios.post('http://localhost:8080/api/orders', orderData);
        console.log('Order placed successfully:', response.data);
        alert('Order placed successfully!');
        this.cart = []; // Clear the cart after successful order
      } catch (error) {
        console.error('Error placing order:', error);
        alert('Failed to place the order. Please try again.');
      }
    }
  },
  mounted() {
    this.fetchItems(); // Load items when component is mounted
  }
};
</script>

<style scoped>
/* General container */
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
  height: 100%; /* Ensure the card takes up full height */
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

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  border-radius: 5px;
  width: fit-content;
  align-self: center;
  margin-top: auto; /* This ensures the button stays at the bottom */
}

button:hover {
  background-color: #0056b3;
}


h2, h3 {
  margin-bottom: 10px;
}
</style>
