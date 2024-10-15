<template>
  <div id="app">
    <nav>
      <router-link to="/shop">Shop Items</router-link> |
      <router-link to="/register">Register</router-link> | 
      <router-link to="/login">Login</router-link>
    </nav>
    <router-view></router-view> <!-- Will render the routed components -->
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style>
#app {
  text-align: center;
}
nav {
  margin-bottom: 20px;
}
nav a {
  margin: 0 10px;
  text-decoration: none;
  color: #42b983;
}
nav a.router-link-exact-active {
  font-weight: bold;
}
</style>