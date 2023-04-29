import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "store/store";

export interface NotifyProps {
  id: number;
  type: "LIKE" | "COMMENT" | "FOLLOW";
  message: string;
  url: string;
}

const initialState: { notifyList: NotifyProps[] } = {
  notifyList: [],
};

export const notifySlice = createSlice({
  name: "notify",
  initialState,
  reducers: {
    saveNotify: (state, action) => {
      state.notifyList = [...state.notifyList, action.payload];
    },
    deleteNotify: (state) => {
      state.notifyList = state.notifyList.slice(1);
    },
  },
});

export const notifyList = (state: RootState) => state.notify.notifyList;
export const { saveNotify, deleteNotify } = notifySlice.actions;

export default notifySlice.reducer;
