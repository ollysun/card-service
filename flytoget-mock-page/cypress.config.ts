import { defineConfig } from "cypress";

export default defineConfig({
  chromeWebSecurity : false,

  e2e: {
    baseUrl: process.env.REACT_APP_BASE_URL || "http://localhost:3000",
  },
});
