<template>
  <div class="order-summary-page">
    <h2>Order Summary</h2>
    <!-- Display Order Summary -->
    <div v-if="cart.length > 0">
      <div v-for="(item, index) in cart" :key="index" class="order-item">
        <img :src="getItemImage(item)" alt="Item Image" class="order-item-image" />
        <div class="order-item-details">
          <h3>{{ item.name }}</h3>
          <p>{{ item.description }}</p>
          <p>Quantity: {{ item.quantity }}</p>
          <p>Price: ${{ item.price }}</p>
          <p>Total: ${{ item.price * item.quantity }}</p>
        </div>
      </div>
      <!-- Total Price -->
      <div class="order-total">
        <p><strong>Total Price: ${{ totalCartPrice }}</strong></p>
      </div>
      <!-- Save Order History Button -->
      <button @click="saveOrderHistory" class="btn btn-primary">Save as Order History</button>
    </div>
    <div v-else>
      <p>Your cart is empty. Add items to proceed.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    cart: {
      type: Array,
      default: () => []
    },
    selectedWeather: {
      type: String,
      default: 'both'
    }
  },
  data() {
    return {
      localCart: [...this.cart]  // Initialize localCart with the prop value
    };
  },
  computed: {
    totalCartPrice() {
      return this.localCart.reduce((total, item) => total + item.price * item.quantity, 0);
    }
  },
  methods: {
    // Get item image
    getItemImage(item) {
      return `/images/${item.filename}`;
    },

    // Save Order as History Logic
    async saveOrderHistory() {
  if (this.localCart.length === 0) {
    alert('Cart is empty. Please add items.');
    return;
  }

  // Ensure selectedWeather is one of the valid weather values
  const validWeatherValues = ['COLD', 'RAINY', 'HOT'];
  const weatherValue = validWeatherValues.includes(this.selectedWeather) ? this.selectedWeather : 'COLD';  // Default to 'COLD' if invalid

  // Make sure to define weatherValue in the correct scope
  const orderData = {
    user: { id: 1 },  // Modify this according to your user authentication
    dateTime: new Date().toISOString(),
    weather: weatherValue,  // Use the valid weather value
    items: this.localCart.map(item => ({
      id: item.id,
      name: item.name,
      quantity: item.quantity,
      price: item.price
    })),
    price: this.totalCartPrice
  };

  try {
    const response = await axios.post('http://localhost:8080/api/orders/order-history', orderData);
    console.log('Order history saved successfully', response.data);
  } catch (error) {
    console.error('Failed to save order history', error);
    }
  }
},
  mounted() {
    // Use localCart and avoid directly mutating the prop
    if (!this.localCart.length && localStorage.getItem('cart')) {
      this.localCart = JSON.parse(localStorage.getItem('cart'));
    }
  }
};
</script>

<style scoped>
.order-summary-page {
  padding: 20px;
}

.order-item {
  display: flex;
  margin-bottom: 20px;
}

.order-item-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 20px;
}

.order-item-details {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.order-total {
  margin-top: 20px;
  font-size: 1.2em;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 5px;
  margin-top: 20px;
}

button:hover {
  background-color: #0056b3;
}
</style>