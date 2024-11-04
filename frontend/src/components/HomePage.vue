<template>
  <div class="landing-page">
    <header class="hero-section">
      <img src="@/assets/logo.jpg" alt="Panlasang Panahon Logo" class="logo" />
      <h1>Welcome to Panlasang Panahon</h1>
      <p>Your ultimate guide to weather-based meal suggestions.</p>
      <button class="cta-button" @click="navigateToShop">Explore Menus</button>
    </header>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  methods: {
    navigateToShop() {
      this.$router.push('/shop'); // Change this line to redirect to Shop Items
    }
  }
};
</script>

<style scoped>
.landing-page {
  text-align: center;
  font-family: 'Arial', sans-serif;
  color: #333;
  margin: 0;
  padding: 0;
}

.hero-section {
  background-color: #f8f8f8;
  padding: 50px 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.logo {
  width: 150px;
  margin-bottom: 20px;
}

h1 {
  font-size: 2.5em;
  color: #e67e22; /* Matches the logo color */
}

p {
  font-size: 1.2em;
  margin-bottom: 20px;
}

.cta-button {
  background-color: #e67e22; /* Button color matching the logo */
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
}

.cta-button:hover {
  background-color: #d35400; /* Darker shade for hover effect */
}
</style>