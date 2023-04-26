import { useEffect, useState } from "react";
import BestPost from "../components/Main/BestPost";
import { useAppDispatch, useAppSelector } from "../store/hook";
import { isLogin, logout } from "../store/modules/authSlice";
import MainQnaContainer from "../components/Main/MainQnaContainer";
import MainQuizContainer from "../components/Main/MainQuizContainer";
import MainUser, { UserProfileProps } from "../components/Main/MainUser";
import axios from "../api/axios";
import Footer from "components/Layout/Footer";
import authAPI from "api/authAPI";
import banner from "asset/banner.png";
import { useNavigate } from "react-router-dom";

export default function Home() {
  const isLoginUser = useAppSelector(isLogin);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [weeklyQuestions, SetWeeklyQuestions] = useState<string>("");
  const [weeklyquiz, SetWeeklyquiz] = useState<string>("");
  const [bestPostProps, setBestPostProps] = useState<any>();
  const [userprofile, setUserprofile] = useState<UserProfileProps>({
    nickname: "",
    hashcode: "",
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
            hashcode: res.data.hashcode,
            profileImage: res.data.profileImage,
            level: res.data.level,
            ariFactor: res.data.ariFactor,
          };
          setUserprofile(obj);
        })
        .catch((err) => {
          //에러
          // if (
          //   err.response.data.status === 401 &&
          //   err.config.url !== "/logout"
          // ) {
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
    }
  }, [isLoginUser, dispatch]);
  useEffect(() => {
    axios
      .get(`/questions/weekly`)
      .then((response) => SetWeeklyQuestions(response.data.content))
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
  }, [dispatch]);

  useEffect(() => {
    axios.get(`/feeds/weekly`).then((response) => {
      setBestPostProps(response.data);
    });
  }, []);

  useEffect(() => {
    axios
      .get(`/quizzes/weekly`)
      .then((response) => SetWeeklyquiz(response.data[0].content))
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
  }, [dispatch]);

  return (
    <>
      <img
        src={banner}
        alt="kakao"
        // width={100}
        // height={100}
      />
      <MainUser {...userprofile} />
      <MainQnaContainer question={weeklyQuestions} />
      <MainQuizContainer question={weeklyquiz} />
      <div className="py-4 px-2">
        <BestPost bestPostProps={bestPostProps} />
      </div>
      <Footer />
    </>
  );
}
