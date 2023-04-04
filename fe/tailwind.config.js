/** @type {import('tailwindcss').Config} */
const percentageWidth = require("tailwindcss-percentage-width");
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["GmarketSans", "Arial", "sans-serif"],
        // sans가 제일 기본 상속 폰트이므로 전체 폰트바꾸려면 sans재지정후 맨앞에 원하는 폰트 넣기
      },
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

  plugins: [percentageWidth, require("@tailwindcss/line-clamp")],
};
