import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "store/hook";
import { accessToken, isLogin, logout } from "store/modules/authSlice";
import authAPI from "./authAPI";
const instance = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

export const AxiosInterceptor = ({ children }: any) => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [isSet, setIsSet] = useState(false);
  const isLoginUser = useAppSelector(isLogin);
  const TOKEN = useAppSelector(accessToken);
  useEffect(() => {
    const reqInterceptor = instance.interceptors.request.use(
      (config) => {
        if (isLoginUser) {
          config.headers["Authorization"] = TOKEN;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    const resInterceptor = instance.interceptors.response.use(
      (response) => {
        return response;
      },
      (error) => {
        console.log("hh", error);
        if (error.response.status === 404 || error.response.status >= 500) {
          navigate("/404");
        }
        if (error.response.status === 401) {
          console.log("리프레쉬 필요");
          authAPI
            .refreshToken()
            .then((res) => {
              console.log("47: 리프레쉬 완료");
            })
            .catch((err) => {
              console.log(err);
              // dispatch(logout());
            });
        }
        return Promise.reject(error);
      }
    );
    setIsSet(true);
    return () => {
      instance.interceptors.response.eject(resInterceptor);
      instance.interceptors.request.eject(reqInterceptor);
    };
  }, [navigate, dispatch, isLoginUser, TOKEN]);
  return isSet && children;
};

export default instance;

// const reqInterceptor = instance.interceptors.request.use(
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
