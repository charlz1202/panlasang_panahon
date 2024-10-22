<template>
  <div>
    <h2>Upload Restaurant</h2>
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
import Papa from "papaparse";
import axios from "axios";

export default {
  name: 'UploadRestaurant',
  data() {
    return {
      file: null,
      successMessage: "",
      errorMessage: ""
    };
  },
  methods: {
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file && file.type === "text/csv") {
        this.file = file;
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

      try {
        // Parse the CSV file using papaparse
        Papa.parse(this.file, {
          complete: async (result) => {
             // Remove the first row (header)
            const dataWithoutHeader = result.data.slice(1); // Skip the first row
            const restaurants = dataWithoutHeader.map(row => ({
              id: row[0],
              name: row[1],
              address: row[2],
              city: row[3],
              province: row[4],
              postalcode: row[5],
              phone: row[6]
            }));

            // Send the parsed data to the Spring Boot backend
            await axios.post("http://localhost:8080/api/restaurants/upload", restaurants, {
              headers: {
                "Content-Type": "application/json"
              }
            });

            this.successMessage = "Restaurants uploaded successfully!";
            this.errorMessage = "";
          },
          header: false
        });
      } catch (error) {
        this.errorMessage = "Failed to upload CSV: " + error.message;
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
