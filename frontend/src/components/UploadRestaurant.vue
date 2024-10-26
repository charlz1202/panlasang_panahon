<template>
  <div>
    <h2>Upload Restaurants</h2>
    <input type="file" @change="handleFileUpload" accept=".csv" />
    <button @click="uploadCSV">Upload CSV</button>

    <div v-if="errorMessage" style="color: red;">
      {{ errorMessage }}
    </div>
    <div v-if="successMessage" style="color: green;">
      {{ successMessage }}
    </div>
  </div>
</template>

<script>
import axios from "axios"; // Ensure axios is installed

export default {
  name: "UploadRestaurant",
  data() {
    return {
      file: null,
      successMessage: "",
      errorMessage: "",
    };
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      // Check file extension instead of MIME type
      if (file && file.name.endsWith(".csv")) {
        this.file = file;
        this.successMessage = ""; // Clear previous success message
        this.errorMessage = ""; // Clear previous error message
      } else {
        this.errorMessage = "Please upload a valid CSV file.";
        this.file = null;
      }
    },

    async uploadCSV() {
      if (!this.file) {
        this.errorMessage = "No file selected.";
        return;
      }

      const formData = new FormData();
      formData.append("file", this.file);

      try {
        await axios.post(
          "http://localhost:8080/api/restaurants/upload", // Ensure this matches your backend endpoint
          formData,
          { headers: { "Content-Type": "multipart/form-data" } }
        );
        this.successMessage = "Restaurants uploaded successfully!";
        this.errorMessage = "";
      } catch (error) {
        // Log full error for debugging
        console.error("Error uploading restaurants:", error);
        this.errorMessage = "Failed to upload restaurants: " + (error.response?.data || error.message);
      }
    }
  }
};
</script>

<style scoped>
input {
  margin: 10px;
}

button {
  margin-top: 10px;
  background-color: blue;
  color: white;
  padding: 5px 10px;
}

h2 {
  margin-bottom: 15px;
}
</style>