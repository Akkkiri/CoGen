import { combineReducers, configureStore } from "@reduxjs/toolkit";
import authReducer from "./modules/authSlice";
import { persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";
import { persistStore } from "redux-persist";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["auth"],
};

export const rootReducer = combineReducers({
  auth: authReducer,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        ignoredActions: [
          "persist/PERSIST",
          "auth/getToken/fulfilled",
          "auth/getToken/rejected",
        ],
      },
    }),
});

export const persistor = persistStore(store);

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

// export default persistReducer(persistConfig, rootReducer);
