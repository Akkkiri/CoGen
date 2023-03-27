/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        "y-red": "#E74D47",
        "y-purple": "#7254E9",
        "y-pink": "#FCEAE9",
        "y-sky": "#EEE9FC",
        "y-gray": "#545454",
        "y-lightGray": "#A19E9E",
        "y-black": "#000000",
      },
    },
  },
  plugins: [],
};
