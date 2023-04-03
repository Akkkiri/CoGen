import { useState } from "react";
import BestPost from "../components/Main/BestPost";
import MainQnaContainer from "../components/Main/MainQnaContainer";
import MainQuizContainer from "../components/Main/MainQuizContainer";
import MainUser from "../components/Main/MainUser";
import { NavLink } from "react-router-dom";
import axios from "api/axios";
import { useAppDispatch } from "store/hook";
import { getNewTokenAsync } from "store/modules/authSlice";
import { logout } from "store/modules/authSlice";

export default function Home() {
  const dispatch = useAppDispatch();
  return (
    <>
      <button
        className="btn-r"
        onClick={() => {
          dispatch(getNewTokenAsync()).then((res) =>
            console.log(res, "리프레쉬")
          );
        }}
      >
        리프레쉬
      </button>
      <button
        className="btn-p"
        onClick={() => {
          dispatch(logout());
        }}
      >
        로그아웃
      </button>
      <div className="border-b-8">
        <MainUser
          nickname={"닉네임이열글자가넘으"}
          profileImage={"/images/user.png"}
          level={1}
          point={33}
        />
      </div>
      <div className="border-b-8">
        <MainQnaContainer question="인생 영화는 무엇인가요?" />
      </div>
      <div className="border-b-8">
        <MainQuizContainer question="[스불재]의 뜻이 무엇일까요?" />
      </div>
      <div className="py-4 px-2">
        <NavLink to="/post">
          <BestPost />
        </NavLink>
      </div>
    </>
  );
}
