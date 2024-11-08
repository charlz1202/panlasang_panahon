<template>
  <div class="landing-page">
    <header class="hero-section">
      <img src="@/assets/logo.jpg" alt="Panlasang Panahon Logo" class="logo" />
      <h1>Welcome to Panlasang Panahon</h1>
      <p>Your ultimate guide to weather-based meal suggestions.</p>
      <div class="button-group">
        <button class="cta-button" @click="navigateToLogin">Login to Explore More</button>
        <button class="guest-button" @click="navigateToShop">Explore as Guest</button>
      </div>
    </header>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  methods: {
    navigateToLogin() {
      this.$router.push('/login'); // Direct to LoginPage.vue
    },
    navigateToShop() {
      this.$router.push('/shop'); // Direct to ShopItems.vue for non-registered users
    }
  }
};
</script>

<style scoped>
.landing-page {
  font-family: 'Arial', sans-serif;
  color: #333;
  height: 100vh; /* Full viewport height */
  display: flex;
  justify-content: center;
  align-items: center; /* Vertically center content */
  margin: 0;
  padding: 0;
  background-color: #f2f2f2; /* Soft background color for the page */
}

.hero-section {
  background: linear-gradient(145deg, #f8f8f8, #e2e2e2); /* Subtle gradient */
  padding: 50px 40px; /* Increased padding */
  text-align: center;
  border-radius: 15px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  width: 85%; /* Increased width to make the card larger */
  max-width: 750px; /* Set larger max-width */
  min-width: 500px; /* Minimum width to maintain proportions on smaller screens */
}

.logo {
  width: 150px; /* Increased logo size */
  margin-bottom: 30px; /* Increased space between logo and text */
}

h1 {
  font-size: 3em; /* Larger font size */
  color: #e67e22; /* Matches the logo color */
  margin-bottom: 20px; /* Increased spacing between heading and text */
}

p {
  font-size: 1.4em; /* Larger text */
  margin-bottom: 40px;
  line-height: 1.6;
  color: #555;
}

.button-group {
  display: flex;
  gap: 25px; /* Increased gap between buttons */
  justify-content: center;
}

.cta-button, .guest-button {
  background-color: #e67e22;
  color: #fff;
  padding: 15px 30px; /* Increased padding */
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1.2em; /* Larger font for buttons */
  transition: background-color 0.3s ease;
  width: 200px; /* Fixed width for buttons */
}

.cta-button:hover, .guest-button:hover {
  background-color: #d35400; /* Darker shade for hover effect */
}
</style>