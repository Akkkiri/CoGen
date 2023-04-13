import Level from "../user/Level";
import { isLogin } from "../../store/modules/authSlice";
import { useAppSelector } from "../../store/hook";
import { NavLink } from "react-router-dom";
import { Point } from "util/LevelUtil";

export interface UserProfileProps {
  nickname: string;
  profileImage: string;
  level: number;
  ariFactor: Point;
}

export default function MainUser({
  nickname,
  profileImage,
  level,
  ariFactor,
}: UserProfileProps) {
  const isLoginUser = useAppSelector(isLogin);
  return (
    <div className="p-2 mx-2 my-4 rounded-xl border-2 border-y-lightGray">
      <div>
        <img
          src="/images/cogenlogo-p.png"
          alt="logo"
          className="w-9 float-right"
        ></img>
      </div>
      {isLoginUser ? (
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
            <Level level={level} ariFactor={ariFactor} />
          </div>
        </div>
      ) : (
        <div className="p-2 text-center">
          <div className="pb-3">로그인 후 답변과 퀴즈를 작성해보세요!!</div>
          <NavLink to="/login">
            <button className="btn-p rounded-lg px-4 py-2 text-white text-sm">
              로그인
            </button>
          </NavLink>
        </div>
      )}
    </div>
  );
}
