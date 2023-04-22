import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "store/store";
import { Select } from "util/SelectUtil";

const initialState: {
  category: string;
  sort: Select;
  page: number;
} = {
  category: "전체",
  sort: "new",
  page: 1,
};

export const postSlice = createSlice({
  name: "post",
  initialState,
  reducers: {
    saveCategory: (state, action) => {
      state.category = action.payload;
    },
    saveSort: (state, action) => {
      state.sort = action.payload;
    },
    savePage: (state, action) => {
      state.page = action.payload;
    },
  },
});

export const beforeCategory = (state: RootState) => state.post.category;
export const beforeSort = (state: RootState) => state.post.sort;
export const beforePage = (state: RootState) => state.post.page;
export const { saveCategory, saveSort, savePage } = postSlice.actions;

export default postSlice.reducer;
