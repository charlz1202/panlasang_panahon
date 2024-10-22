<template>
  <div class="container d-flex flex-column min-vh-100 justify-content-center align-items-center " style="max-width: 600px">
    <h2 class="mb-3">Panlasang Panahon</h2>
    <div class="row" style="max-width: 300px">
      <form class="form-login" @submit.prevent="loginUser">
        <input class="form-control" type="email" v-model="email" placeholder="Email" required />
        <input class="form-control" type="password" v-model="password" placeholder="Password" required />
        <button class="btn btn-primary w-100 mt-2" type="submit">Login</button>
      </form>
      <p>Don't have an account? <a href="#" @click="redirectToRegistration">Register here</a></p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginPage',
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async loginUser() {
      try {
        const response = await fetch('http://localhost:8080/api/users/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: this.email,
            password: this.password,
          }),
        });

        const data = await response.json();

        if (response.ok) {
          // Save user data in localStorage
          const user = {
            phone: data.id,
            email: data.email,
            fullname: data.fullname,
            location: data.location,  // Assuming location is returned from the backend
          };

          localStorage.setItem('user', JSON.stringify(user));  // Store user data
          this.$emit('login');
          this.$router.push('/shop');  // Redirect to shop page after login
        } else {
          alert(data.message || 'Invalid credentials');
        }
      } catch (error) {
        console.error('Error during login:', error);
        alert('Failed to login. Please check the console for details.');
      }
    },

    redirectToRegistration() {
      this.$router.push('/register');
    },
  },
};
</script>

<style>
  .form-login input:focus {z-index: 1;}.form-login .form-control {position: relative;}
</style>