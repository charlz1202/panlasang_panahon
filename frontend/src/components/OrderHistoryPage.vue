<template>
  <div>
    <h1>Your Order History</h1>
    
    <!-- Show message if no orders are placed yet -->
    <div v-if="orderHistory.length === 0 && isLoggedIn">
      <p>No orders placed yet.</p>
    </div>
    
    <!-- Show message if user is not logged in -->
    <div v-else-if="!isLoggedIn">
      <p>User ID not found. Please log in.</p>
    </div>
    
    <!-- Show order history if user is logged in -->
    <div v-else>
      <div v-for="order in orderHistory" :key="order.id">
        <p><strong>Date:</strong> {{ formatDate(order.dateTime) }}</p>
        <p><strong>Weather:</strong> {{ order.weather }}</p>
        <p><strong>Price:</strong> ${{ order.price.toFixed(2) }}</p>
        <ul>
          <li v-for="item in order.items" :key="item.id">
            {{ item.name }} (x{{ item.quantity }})
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      orderHistory: [],
      isLoggedIn: false,
    };
  },
  async created() {
    const userId = this.getLoggedInUserId();

    if (userId) {
      this.isLoggedIn = true;
      try {
        const token = localStorage.getItem('authToken');
        if (!token) {
          throw new Error("Token not found");
        }

        const response = await axios.get(`http://localhost:8080/api/order_history/${userId}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
          },
        });
        this.orderHistory = response.data;
      } catch (error) {
        if (error.response && error.response.status === 401) {
          alert("Your session has expired. Please log in again.");
          this.$router.push('/login');
        } else {
          console.error("Error fetching order history:", error);
          alert("Failed to fetch order history. Please try again.");
        }
      }
    } else {
      this.isLoggedIn = false;
      alert("Please log in to view order history.");
      this.$router.push('/login');
    }
  },
  methods: {
    formatDate(dateTime) {
      const date = new Date(dateTime);
      return date.toLocaleDateString() + " " + date.toLocaleTimeString();
    },
    getLoggedInUserId() {
      const user = JSON.parse(localStorage.getItem('user'));
      return user ? user.id : null;
    },
  },
};
</script>