import axios from "axios";

const instance = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

// instance.interceptors.request.use(
//   (config) => {
//     const storage = localStorage.getItem("persist:root");
//     if (storage) {
//       const auth = JSON.parse(storage).auth;
//       const token = JSON.parse(auth).token;
//       config.headers.Authorization = token;
//     }
//     return config;
//   },
//   (error) => {
//     return Promise.reject(error);
//   }
// );

export default instance;
