import axios from "api/axios";
import { FaChevronRight } from "react-icons/fa";
import LinearBar from "components/quiz/LinearBar";
import QuizBtn, { QuizBtnProps } from "components/quiz/QuizBtn";
import QuizScore from "components/quiz/QuizScore";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "store/hook";
import {
  firstQuiz,
  quizScore,
  saveFirstQuiz,
  saveScore,
} from "store/modules/quizSlice";
import { logout } from "store/modules/authSlice";
import authAPI from "api/authAPI";
import { useNavigate } from "react-router-dom";

export default function Quiz() {
  const [order, setOrder] = useState(1);
  const [score, setScore] = useState(0);
  const [quizList, setQuizList] = useState<QuizBtnProps[]>([]);
  const [showAnswer, setShowAnswer] = useState(false);
  const savedScore = useAppSelector(quizScore);
  const savedFirstQuiz = useAppSelector(firstQuiz);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    if (savedScore >= 0) {
      setOrder(6);
      setScore(savedScore);
    }
  }, [savedScore]);

  useEffect(() => {
    axios
      .get("/quizzes/weekly")
      .then((res) => {
        setQuizList(res.data);
        if (savedFirstQuiz !== res.data[0].content) {
          dispatch(saveScore(-1));
          dispatch(saveFirstQuiz(res.data[0].content));
        }
      })
      .catch((err) => {
        //에러
        // if (err.response.data.status === 401 && err.config.url !== "/logout") {
        //   authAPI
        //     .refreshToken()
        //     .then((res) => {})
        //     .catch((err) => {
        //       authAPI.logout();
        //       dispatch(logout());
        //       navigate("/");
        //     });
        // }
      });
  }, [dispatch, navigate, savedFirstQuiz]);

  return (
    <div>
      <h1 className="page-title">함께 배워요</h1>
      <div className="mx-4">
        <LinearBar order={order} setOrder={setOrder} />
        {order < 6 ? (
          <QuizBtn
            {...quizList[order - 1]}
            showAnswer={showAnswer}
            setShowAnswer={setShowAnswer}
            score={score}
            setScore={setScore}
          />
        ) : (
          <QuizScore score={score} setOrder={setOrder} />
        )}
      </div>
      <div className="flex justify-end mx-4">
        {showAnswer ? (
          <button
            className="btn-r mt-10 flex items-center"
            onClick={() => {
              if (order < 6) {
                setShowAnswer(false);
                setOrder(order + 1);
              }
            }}
          >
            <FaChevronRight className="mr-2" />
            다음문제
          </button>
        ) : null}
      </div>
    </div>
  );
}
