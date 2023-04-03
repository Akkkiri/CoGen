import axios from "api/axios";

const setAxiosHeaderAuth = (value: any) =>
  (axios.defaults.headers.common["Authorization"] = value);

const signInSuccess = (response: any) => {
  setAxiosHeaderAuth(response.headers.authorization);
  // accessToken 만료하기 1분 전에 로그인 연장
  // setTimeout(authApi.refreshToken, ACCESS_EXPIRY_TIME - 60000);
};

const authAPI = {
  signIn: (params: any) => {
    return new Promise((resolve, reject) => {
      return axios
        .post("/api/login", params)
        .then((res) => {
          signInSuccess(res);
          return resolve(res);
        })
        .catch((err) => {
          return reject(err);
        });
    });
  },
};

export default authAPI;
