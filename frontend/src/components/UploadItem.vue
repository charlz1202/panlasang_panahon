<template>
  <div>
    <h2>Upload Items</h2>
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
import Papa from "papaparse"; // Ensure PapaParse is installed
import axios from "axios"; // Ensure axios is installed

export default {
  name: "UploadItem",
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
      if (file && file.type === "text/csv") {
        this.file = file;
        this.errorMessage = ""; // Clear any previous error message
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
        Papa.parse(this.file, {
          complete: (result) => {
            // Remove the first row (header)
            const dataWithoutHeader = result.data.slice(1); // Skip the first row
            const items = dataWithoutHeader.map(row => ({
              id: parseInt(row[0]),        // Assuming ID is in column 1
              name: row[1],                // Assuming Name is in column 2
              description: row[2],         // Assuming Description is in column 3
              price: parseFloat(row[3]),   // Assuming Price is in column 4
              category: row[4],            // Assuming Category is in column 5
              weather: row[5],             // Assuming Weather is in column 6
              filename: row[7],            // Assuming Filename is in column 8
              restaurant: { id: parseInt(row[6]) } // Assuming Restaurant ID is in column 7
            }));

            // Send bulk data to the backend
            this.saveItems(items);
          },
          header: false,
          skipEmptyLines: true
        });
      } catch (error) {
        this.errorMessage = "Error parsing the CSV: " + error.message;
      }
    },

    async saveItems(items) {
      try {
        await axios.post(
          "http://localhost:8080/api/items/bulk-upload", // Ensure this matches your backend endpoint
          items,
          { headers: { "Content-Type": "application/json" } }
        );
        this.successMessage = "Items uploaded successfully!";
      } catch (error) {
        this.errorMessage = "Failed to upload items: " + error.message;
      }
    }
  }
};
</script>