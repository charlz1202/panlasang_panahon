<template>
  <h2 class="mt-5 mb-3"><b>Panlasang Panahon</b></h2>
  <div class="container border shadow" style="max-width: 640px">
    <div class="ms-3 mt-3 me-3">
      <h2>Create account</h2>
      <form class="row g-3" @submit.prevent="registerUser">
        <div class="col-md-6 mt-4">
          <label for="fullname" class="form-label">Your name</label>
          <input type="text" class="form-control" v-model="fullname" placeholder="First and last name">
        </div>
        <div class="col-md-6 mt-4">
          <label for="phone" class="form-label">Phone</label>
          <input type="text" class="form-control" v-model="phone">
        </div>
        <div class="col-md-6">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" v-model="email">
        </div>
        <div class="col-md-6">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" v-model="password">
        </div>
        <div class="col-12">
          <label for="address1" class="form-label">Address</label>
          <input type="text" class="form-control" v-model="address1" placeholder="Street address or P.O. Box">
        </div>
        <div class="col-12">
          <label for="address2" class="form-label">Address 2</label>
          <input type="text" class="form-control" v-model="address2" placeholder="Apartment, Suite, Unit, Builing">
        </div>
        <div class="col-md-5">
          <label for="city" class="form-label">City</label>
          <input type="text" class="form-control" v-model="city">
        </div>
        <div class="col-md-4">
          <label for="province" class="form-label">State</label>
          <select v-model="province" class="form-select">
            <option value="" selected disabled>Select</option>
            <option value="Alberta">Alberta</option>
            <option value="British Columbia">British Columbia</option>
            <option value="Manitoba">Manitoba</option>
            <option value="New Brunswick">New Brunswick</option>
            <option value="Newfoundland">Newfoundland</option>
            <option value="Northwest Territories">Northwest Territories</option>
            <option value="Nova Scotia">Nova Scotia</option>
            <option value="Nunavut">Nunavut</option>
            <option value="Ontario">Ontario</option>
            <option value="Prince Edward Island">Prince Edward Island</option>
            <option value="Quebec">Quebec</option>
            <option value="Saskatchewan">Saskatchewan</option>
            <option value="Yukon">Yukon</option>
          </select>
        </div>
        <div class="col-md-3">
          <label for="postalcode" class="form-label">Postal code</label>
          <input type="text" class="form-control" v-model="postalcode">
        </div>
        <div class="d-grid gap-2 mb-4">
          <button type="submit" class="btn btn-warning">Register</button>
        </div>
      </form>

    </div>
  </div>
</template>

<script>
export default {
  name: 'RegistrationPage', // Changed to multi-word name
  data() {
    return {
      fullname: '',
      email: '',
      password: '',
      address1: '',
      address2: '',
      city: '',
      province: '',
      postalcode: '',
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
            fullname: this.fullname,
            email: this.email,
            password: this.password,
            address1: this.address1,
            address2: this.address1,
            city: this.city,
            province: this.province,
            postalcode: this.postalcode,
            phone: this.phone,
          }),
        });
        const a = {
            fullname: this.fullname,
            email: this.email,
            password: this.password,
            address1: this.address1,
            address2: this.address1,
            city: this.city,
            province: this.province,
            postalcode: this.postalcode,
            phone: this.phone,
          };
        console.log(a);
        const data = await response.json(); // Parse response as JSON
        console.log(data);
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
    },
    redirectToLogin() {
      this.$router.push('/login');
    },
  },
};
</script>

<style>
  .form-label {
    font-weight: bold;
  }
  div.container {
    text-align: left;
  }
</style>