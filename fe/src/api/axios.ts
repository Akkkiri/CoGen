import axios from "axios";
import { useAppSelector } from "store/hook";
import { accessToken } from "store/modules/authSlice";

const instance = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

instance.interceptors.request.use(
  function (config) {
    //요청 보내기 전에 수행할 일
    const token = useAppSelector(accessToken);
    config.headers.Authorization = token;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;
