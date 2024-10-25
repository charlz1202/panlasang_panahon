const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: [
    'my-dep'
  ], // No dependencies to transpile
});
