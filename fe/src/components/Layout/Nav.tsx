import { IoExtensionPuzzleOutline } from "react-icons/io5";
import { BsPatchQuestion, BsPerson } from "react-icons/bs";
import { SlSpeech, SlHome } from "react-icons/sl";
import { NavLink } from "react-router-dom";
import { useState } from "react";

export default function Nav() {
  const [isLogin, setIsLogin] = useState<boolean>(false);
  return (
    <nav className="w-full m-auto fixed bottom-0 z-[9] border-t border-y-lightGray bg-white">
      <div className="flex justify-between max-w-5xl m-auto px-4 pt-2">
        <NavLink
          to="/"
          className={({ isActive }) => (isActive ? "text-y-red" : "")}
        >
          <div className="flex flex-col items-center">
            <SlHome className="text-3xl" />홈
          </div>
        </NavLink>

        <NavLink
          to="/question"
          className={({ isActive }) => (isActive ? "text-y-red" : "")}
        >
          <div className="flex flex-col items-center">
            <BsPatchQuestion className="text-3xl" />
            이번주 질문
          </div>
        </NavLink>

        <NavLink
          to="/post"
          className={({ isActive }) => (isActive ? "text-y-red" : "")}
        >
          <div className="flex flex-col items-center">
            <SlSpeech className="text-3xl" />
            게시판
          </div>
        </NavLink>

        <NavLink
          to="/quiz"
          className={({ isActive }) => (isActive ? "text-y-red" : "")}
        >
          <div className="flex flex-col items-center">
            <IoExtensionPuzzleOutline className="text-3xl" />
            퀴즈
          </div>
        </NavLink>

        <NavLink
          to={isLogin ? "/mypage" : "/login"}
          className={({ isActive }) => (isActive ? "text-y-red" : "")}
        >
          <div className="flex flex-col items-center">
            <BsPerson className="text-3xl" />나
          </div>
        </NavLink>
      </div>
    </nav>
  );
}
