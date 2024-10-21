<template>
  <div>
    <h1>Login</h1>
    <form @submit.prevent="loginUser">
      <input type="email" v-model="email" placeholder="Email" required />
      <input type="password" v-model="password" placeholder="Password" required />
      <button type="submit">Login</button>
    </form>
    <p>Don't have an account?</p>
    <button @click="redirectToRegistration">Register here</button>
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
