import axios from "api/axios";
import { persistor } from "index";

// 만료 시간 (밀리초)
const ACCESS_EXPIRY_TIME = 3 * 60 * 60 * 1000; // 3시간
const REFRESH_EXPIRY_TIME = 24 * 60 * 60 * 1000; // 24시간
// const ACCESS_EXPIRY_TIME = 3 * 60 * 1000; // 3분
// const REFRESH_EXPIRY_TIME = 5 * 60 * 1000; // 5분

const setAxiosHeaderAuth = (value: any) =>
  (axios.defaults.headers.common["Authorization"] = value);

const signInSuccess = (response: any) => {
  setAxiosHeaderAuth(response.headers.authorization);
  // accessToken 만료하기 1분 전에 로그인 연장
  setTimeout(authAPI.refreshToken, ACCESS_EXPIRY_TIME - 60000);
};

const resetUserData = () => {
  setAxiosHeaderAuth("");
  purge();
};

const purge = async () => {
  await persistor.purge();
};

const authAPI = {
  signIn: (params: any) => {
    return new Promise((resolve, reject) => {
      return axios
        .post("/login", params)
        .then((res) => {
          signInSuccess(res);
          // refresh token 만료되면 로그아웃
          setTimeout(authAPI.logout, REFRESH_EXPIRY_TIME);
          return resolve(res);
        })
        .catch((err) => {
          return reject(err);
        });
    });
  },
  logout: () => {
    return new Promise((resolve, reject) => {
      return axios
        .post("/logout")
        .then((_) => resolve(_))
        .catch((err) => reject(err))
        .finally(() => {
          resetUserData();
        });
    });
  },
  refreshToken: () => {
    return new Promise((resolve) => {
      return axios
        .get("/token/refresh")
        .then((res) => {
          signInSuccess(res);
          //제거
          // console.log("리프레시 완료");
          return resolve(res);
        })
        .catch(() => authAPI.logout());
    });
  },
  oauth: ({ path, code }: { path: string; code: string }) => {
    return new Promise((resolve, reject) => {
      return axios
        .get(`${path}${code}`)
        .then((response) => {
          signInSuccess(response);
          setTimeout(authAPI.logout, REFRESH_EXPIRY_TIME);
          return resolve(response);
        })
        .catch((error) => {
          return reject(error);
        });
    });
  },
};

export default authAPI;
