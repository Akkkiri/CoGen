import { useEffect, useState } from "react";
import BestPost from "../components/Main/BestPost";
import { useAppSelector } from "../store/hook";
import { isLogin } from "../store/modules/authSlice";
import MainQnaContainer from "../components/Main/MainQnaContainer";
import MainQuizContainer from "../components/Main/MainQuizContainer";
import MainUser, { UserProfileProps } from "../components/Main/MainUser";
import axios from "../api/axios";

export default function Home() {
  const isLoginUser = useAppSelector(isLogin);
  const [weeklyQuestions, SetWeeklyQuestions] = useState<string>("");
  const [weeklyquiz, SetWeeklyquiz] = useState<string>("");
  const [bestPostProps, setBestPostProps] = useState<any>();
  const [userprofile, setUserprofile] = useState<UserProfileProps>({
    nickname: "",
    profileImage: "",
    level: 0,
    ariFactor: 0,
  });
  useEffect(() => {
    if (isLoginUser) {
      axios
        .get("/mypage")
        .then((res) => {
          const obj = {
            nickname: res.data.nickname,
            profileImage: res.data.profileImage,
            level: res.data.level,
            ariFactor: res.data.ariFactor,
          };
          setUserprofile(obj);
        })
        .catch((err) => console.log(err));
    }
  }, [isLoginUser]);
  useEffect(() => {
    axios
      .get(`/questions/weekly`)
      .then((response) => SetWeeklyQuestions(response.data.content));
  }, []);
  // 이번주 질문
  useEffect(() => {
    axios.get(`/feeds/weekly`).then((response) => {
      // console.log(response.data);
      setBestPostProps(response.data);
    });
  }, []);

  useEffect(() => {
    axios
      .get(`/quizzes/weekly`)
      .then((response) => SetWeeklyquiz(response.data[0].content));
  }, []);
  // 이번주 퀴즈
  // console.log(weeklyQuestions);
  return (
    <>
      <MainUser {...userprofile} />
      <MainQnaContainer question={weeklyQuestions} />
      <MainQuizContainer question={weeklyquiz} />
      <div className="py-4 px-2">
        <BestPost bestPostProps={bestPostProps} />
      </div>
    </>
  );
}
