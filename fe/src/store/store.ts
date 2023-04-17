import { combineReducers, configureStore } from "@reduxjs/toolkit";
import authReducer from "./modules/authSlice";
import quizReducer from "./modules/quizSlice";
import searchReducer from "./modules/searchSlice";
import notifyReducer from "./modules/notifySlice";
import { persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["auth", "quiz", "search"],
};

export const rootReducer = combineReducers({
  auth: authReducer,
  quiz: quizReducer,
  search: searchReducer,
  notify: notifyReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [
          "persist/PERSIST",
          "persist/PURGE",
          "auth/getToken/fulfilled",
          "auth/getToken/rejected",
          "auth/getNewToken/fulfilled",
          "auth/getNewToken/rejected",
          "auth/oauth/fulfilled",
          "auth/oauth/rejected",
        ],
      },
    }),
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

// export default persistReducer(persistConfig, rootReducer);
