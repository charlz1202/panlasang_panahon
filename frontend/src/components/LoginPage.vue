<template>
  <div class="container d-flex flex-column min-vh-100 justify-content-center align-items-center" style="max-width: 600px">
    <h2 class="mb-3">Panlasang Panahon</h2>
    <div class="row" style="max-width: 300px">
      <form class="form-login" @submit.prevent="loginUser">
        <input class="form-control" type="email" v-model="email" placeholder="Email" required />
        <input class="form-control" type="password" v-model="password" placeholder="Password" required />
        <button class="btn btn-primary w-100 mt-2" type="submit">Login</button>
      </form>
      <p class="mt-3">Don't have an account? <a href="#" @click="redirectToRegistration">Register here</a></p>
      <p v-if="loginError" class="text-danger">{{ loginError }}</p> <!-- Error message for invalid login -->
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginPage',

   beforeRouteEnter(to, from, next) {
    // Check if the user is already logged in
    if (localStorage.getItem('user')) {
      next('/shop'); // Redirect to shop if the user is already logged in
    } else {
      next(); // Proceed normally to login page
    }
  },
  
  data() {
    return {
      email: '',
      password: '',
      loginError: '',  // Error message variable
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
          console.log('Login successful, redirecting...');
          const redirectTo = this.$route.query.redirect || '/shop'; // Check if there is a redirect query param
          console.log(`Redirecting to: ${redirectTo}`);
          this.$router.push(redirectTo);  // Redirect to the appropriate page
        } else {
          this.loginError = data.message || 'Invalid credentials'; // Display error message on the page
        }
      } catch (error) {
        console.error('Error during login:', error);
        this.loginError = 'Failed to login. Please check the console for details.';
      }
    },

    redirectToRegistration() {
      this.$router.push('/register');
    },
  },
};
</script>

<style scoped>
.form-login input:focus {
  z-index: 1;
}
.form-login .form-control {
  position: relative;
}
</style>