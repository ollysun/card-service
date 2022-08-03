import { defineConfig } from "cypress";

export default defineConfig({
  chromeWebSecurity : false,
  retries: {
    runMode: 5
  },
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    baseUrl: process.env.REACT_APP_BASE_URL || "http://localhost:3000",
  },
});
