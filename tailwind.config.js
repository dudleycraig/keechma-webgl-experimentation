const defaultTheme = require("tailwindcss/defaultTheme");

module.exports = {
  content: process.env.NODE_ENV == "production" ? ["./public/js/package.js"] : ["./public/js/cljs-runtime/**/*.js"],
  plugins: [require("@tailwindcss/line-clamp")],
  future: { removeDeprecatedGapUtilities: true, },
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        gray: { 50: "#43505c", 100: "#B1BFD0", 200: "#2f3c48", 300: "#879AAE", 400: "#5E768F", 500: "#111e2a", 600: "#070F17", 700: "#000a16", 800: "#00000c", 900: "#000002", },
        whitegray: { 50: "#F9FAFE", },
        bluegray: { 50: "#E6EEF8", 100: "#5e7893", 200: "#546e89", 300: "#4a647f", 400: "#405a75", 500: "#36506b", 600: "#2c4661", 700: "#243b53", 800: "#1A2D3F", 900: "#0e2843", },
        blue: { 50: "#D6E2FF", 100: "#B4C2E4", 200: "#92A2C9", 300: "#7184AF", 400: "#516795", 500: "#2F4B7C", 600: "#468BFF", 700: "#70A0FF", 800: "#0F59E8", 900: "#284F9A", },
      },
    },
    fontFamily: {
      inter: ["Inter", "sans-serif"],
    },
    fontSize: {
      display: ["48px", "56px"],
      "heading-xl": ["38px", "46px"],
      "heading-lg": ["32px", "39px"],
      "heading-md": ["28px", "34px"],
      "body-lg": ["18px", "22px"],
      "body-md": ["16px", "19px"],
      "body-sm": ["14px", "17px"],
      "body-xs": ["10px", "12px"],
    },
  },
};
