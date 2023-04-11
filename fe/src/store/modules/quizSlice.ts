import { createSlice } from "@reduxjs/toolkit";
import { RootState } from "store/store";

const initialState = {
  score: -1,
  firstQuiz: "",
};

export const quizSlice = createSlice({
  name: "quiz",
  initialState,
  reducers: {
    saveScore: (state, action) => {
      state.score = action.payload;
    },
    saveFirstQuiz: (state, action) => {
      state.firstQuiz = action.payload;
    },
  },
});

export const quizScore = (state: RootState) => state.quiz.score;
export const firstQuiz = (state: RootState) => state.quiz.firstQuiz;
export const { saveScore, saveFirstQuiz } = quizSlice.actions;

export default quizSlice.reducer;
