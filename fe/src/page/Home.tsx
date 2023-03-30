import { useState } from "react";
import BestPost from "../components/Main/BestPost";
import MainQnaContainer from "../components/Main/MainQnaContainer";
import MainQuizContainer from "../components/Main/MainQuizContainer";
import MainUser from "../components/Main/MainUser";

export default function Home() {
  return (
    <>
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
      <BestPost />
    </>
  );
}
