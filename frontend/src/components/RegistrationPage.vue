<template>
  <div>
    <h1>Register</h1>
    <form @submit.prevent="registerUser">
      <input type="email" v-model="email" placeholder="Email" required />
      <input type="password" v-model="password" placeholder="Password" required />
      <input type="text" v-model="location" placeholder="Location" required />
      <input type="text" v-model="phone" placeholder="Phone" required />
      <button type="submit">Register</button>
    </form>
    <p>Already have an account?</p>
    <button @click="redirectToLogin">Login here</button>
  </div>
</template>

<script>
export default {
  name: 'RegistrationPage', // Changed to multi-word name
  data() {
    return {
      email: '',
      password: '',
      location: '',
      phone: '',
    };
  },
  methods: {
    async registerUser() {
      try {
        const response = await fetch(`http://localhost:8080/api/users/register`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            email: this.email,
            password: this.password,
            location: this.location,
            phone: this.phone,
          }),
        });

        const data = await response.json(); // Parse response as JSON

        if (response.ok) {
          alert('Registration successful');
          this.$router.push('/login');
        } else {
          alert(`Failed to register: ${data.message || 'Unknown error'}`);
        }
      } catch (error) {
        console.error('Error during registration:', error);
        alert('Failed to register. Check the console for more details.');
      }
    }

  },
};
</script>
