import { useEffect, useState } from "react";
import BestPost from "../components/Main/BestPost";
import MainQnaContainer from "../components/Main/MainQnaContainer";
import MainQuizContainer from "../components/Main/MainQuizContainer";
import MainUser from "../components/Main/MainUser";
import { NavLink } from "react-router-dom";
import axios from "../api/axios";
export default function Home() {
  const [weeklyQuestions, SetWeeklyQuestions] = useState<string>("");
  const [weeklyquiz, SetWeeklyquiz] = useState<string>("");
  const [username, setUsername] = useState("");
  const [userimg, setUserimg] = useState("");
  const [level, setLevel] = useState();
  const [point, setPoint] = useState();
  useEffect(() => {
    axios
      .get(`/questions/weekly`)
      .then((response) => SetWeeklyQuestions(response.data.content));
  }, []);
  // 이번주 질문
  // useEffect(() => {
  //   axios.get(`/mypage`).then((response) => {
  //     console.log(response.data);
  //     setUsername(response.data);
  //     setUserimg(response.data[0].beerId);
  //   });
  // }, []);

  useEffect(() => {
    axios
      .get(`/quizzes/weekly`)
      .then((response) => SetWeeklyquiz(response.data[0].content));
  }, []);
  // 이번주 퀴즈
  // console.log(weeklyQuestions);
  return (
    <>
      <MainUser
        nickname={"닉네임이열글자가넘으"}
        profileImage={"/images/user.png"}
        level={1}
        point={33}
      />
      <MainQnaContainer question={weeklyQuestions} />
      <MainQuizContainer question={weeklyquiz} />
      <div className="py-4 px-2">
        <NavLink to="/post">
          <BestPost />
        </NavLink>
      </div>
    </>
  );
}
