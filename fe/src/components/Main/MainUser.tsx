import Level from "../user/Level";
import { useState } from "react";
interface UserProfileProps {
  nickname: string;
  profileImage: string;
  level: number;
  point: number;
}

export default function MainUser({
  nickname,
  profileImage,
  level,
  point,
}: UserProfileProps) {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  return (
    <div className="p-2 mx-2 my-4 rounded-xl border-2 border-y-lightGray">
      <div>
        <img
          src="/images/cogenlogo-p.png"
          alt="logo"
          className="w-9 float-right"
        ></img>
      </div>
      {isLogin ? (
        <div className="flex justify-center items-center mx-2">
          <img
            src={profileImage}
            alt="profileImage"
            className="rounded-full w-20 h-20"
          ></img>
          <div className="w-full mx-4 mt-2">
            <div className="flex justify-between">
              <span>{nickname}</span>
            </div>
            <Level level={level} point={point} />
          </div>
        </div>
      ) : (
        <div className="flex flex-col p-2">
          <div className="text-center pb-3">
            로그인 후 답변과 퀴즈를 작성해보세요!!
          </div>
          <button className="bg-y-purple text-white py-1 px-6 rounded-lg w-28 self-center ">
            로그인
          </button>
        </div>
      )}
    </div>
  );
}
