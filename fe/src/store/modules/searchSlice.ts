import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "store/store";

const initialState = {
  mode: "",
  historyList: [],
};

export const searchSlice = createSlice({
  name: "search",
  initialState,
  reducers: {
    saveSearchMode: (state, action) => {
      state.mode = action.payload;
    },
    saveSearchHistory: (state, action) => {
      state.historyList = action.payload;
    },
  },
});

export const searchMode = (state: RootState) => state.search.mode;
export const searchHistory = (state: RootState) => state.search.historyList;
export const { saveSearchMode, saveSearchHistory } = searchSlice.actions;

export default searchSlice.reducer;
