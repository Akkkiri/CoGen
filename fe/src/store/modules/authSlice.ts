import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { PURGE } from "redux-persist";
import authAPI from "api/authAPI";
import { RootState } from "store/store";

const initialState = {
  loading: false,
  isLogin: false,
  isFirstLogin: false,
  userId: "",
  id: 0,
  token: "",
};

export const signInAsync = createAsyncThunk(
  "auth/getToken",
  async (params: any, thunkAPI) => {
    try {
      const response: any = await authAPI.signIn(params);
      return response;
    } catch (err) {
      return thunkAPI.rejectWithValue(err);
    }
  }
);

export const getNewTokenAsync = createAsyncThunk(
  "auth/getNewToken",
  async (_, thunkAPI) => {
    try {
      const response: any = await authAPI.refreshToken();
      return response;
    } catch (err) {
      return thunkAPI.rejectWithValue(err);
    }
  }
);

export const oauthAsync = createAsyncThunk(
  "auth/oauth",
  async ({ path, code }: { path: string; code: string }, thunkAPI) => {
    try {
      const response: any = await authAPI.oauth({ path, code });
      //제거
      // console.log("로그인 성공하면 오는 res", response.data);
      return response;
    } catch (error) {
      return thunkAPI.rejectWithValue(error);
    }
  }
);

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    saveNumber: (state, action) => {
      state.userId = action.payload;
    },
    saveId: (state, action) => {
      state.id = action.payload;
    },
    saveToken: (state, action) => {
      state.token = action.payload;
    },
    logout: (state) => {
      state.userId = "";
      state.token = "";
      state.isLogin = false;
      state.id = 0;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(signInAsync.pending, (state) => {
        state.loading = true;
        state.isLogin = false;
      })
      .addCase(signInAsync.fulfilled, (state, { payload }) => {
        state.loading = false;
        state.isLogin = true;
        state.id = payload.data.id;
        state.userId = payload.data.userId;
        state.token = payload.headers.authorization;
      })
      .addCase(signInAsync.rejected, (state) => {
        state.loading = false;
        state.isLogin = false;
        state.id = 0;
        state.userId = "";
        state.token = "";
      })
      .addCase(getNewTokenAsync.fulfilled, (state, { payload }) => {
        state.loading = false;
        state.isLogin = true;
        state.token = payload.headers.authorization;
      })
      .addCase(getNewTokenAsync.rejected, (state, { payload }) => {
        state.loading = false;
        state.isLogin = false;
        state.id = 0;
        state.userId = "";
        state.token = "";
      })
      .addCase(oauthAsync.fulfilled, (state, { payload }) => {
        state.loading = false;
        state.isLogin = true;
        state.token = payload.headers.authorization;
      })
      // .addCase(oauthAsync.rejected, (state, { payload }) => {
      //   state.loading = false;
      //   state.isLogin = false;
      //   state.id = 0;
      //   state.userId = "";
      //   state.token = "";
      // })
      .addCase(PURGE, () => initialState);
  },
});

export const isLogin = (state: RootState) => state.auth.isLogin;
export const userId = (state: RootState) => state.auth.userId;
export const myid = (state: RootState) => state.auth.id;
export const accessToken = (state: RootState) => state.auth.token;
export const authState = (state: RootState) => state.auth;
export const { saveNumber, saveId, saveToken, logout } = authSlice.actions;

export default authSlice.reducer;
