<template>
  <div
    class="container d-flex flex-column min-vh-100 justify-content-center align-items-center"
    style="max-width: 600px"
  >
    <!-- Logo -->
    <img src="@/assets/logo.jpg" alt="Panlasang Panahon Logo" class="logo mb-3" />
    <h2 class="mb-3">Panlasang Panahon</h2>
    <div class="row" style="max-width: 300px">
      <form class="form-login" @submit.prevent="loginUser">
        <input
          class="form-control"
          type="email"
          v-model="email"
          placeholder="Email"
          required
        />
        <input
          class="form-control"
          type="password"
          v-model="password"
          placeholder="Password"
          required
        />
        <button class="btn btn-primary w-100 mt-2" type="submit">Login</button>
      </form>
      <p class="mt-3">
        Don't have an account?
        <a href="#" @click.prevent="redirectToRegistration">Register here</a>
      </p>
      <p v-if="loginError" class="text-danger">{{ loginError }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "LoginPage",
  data() {
    return {
      email: "",
      password: "",
      loginError: "", // Stores login error messages
    };
  },
  beforeRouteEnter(to, from, next) {
    const isAuthenticated = localStorage.getItem("authToken");
    if (isAuthenticated) {
      next("/shop"); // Redirect to /shop if already logged in
    } else {
      next(); // Proceed to login page
    }
  },
  methods: {
    async loginUser() {
      try {
        const response = await fetch("http://localhost:8080/api/users/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email: this.email,
            password: this.password,
          }),
        });

        const data = await response.json();

        if (response.ok) {
          // Save user and token in localStorage
          localStorage.setItem("user", JSON.stringify(data.user));
          localStorage.setItem("authToken", data.token);

          // Redirect to the intended page or /shop
          const redirectTo = this.$route.query.redirect || "/shop";
          this.$router.push(redirectTo);
        } else {
          // Show error message from server or default
          this.loginError = data.message || "Invalid email or password.";
        }
      } catch (error) {
        console.error("Login failed:", error);
        this.loginError = "An unexpected error occurred. Please try again.";
      }
    },
    redirectToRegistration() {
      this.$router.push("/register");
    },
  },
};
</script>

<style scoped>
/* Style for the logo */
.logo {
  width: 150px; /* Adjust the size of the logo */
  margin-bottom: 20px; /* Space between the logo and the heading */
}

.form-login input:focus {
  z-index: 1;
}
.form-login .form-control {
  position: relative;
}
</style>
