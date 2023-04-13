import { IoExtensionPuzzleOutline } from "react-icons/io5";
import { BsPatchQuestion, BsPerson } from "react-icons/bs";
import { SlSpeech, SlHome } from "react-icons/sl";
import { NavLink } from "react-router-dom";
import { useAppSelector } from "../../store/hook";
import { isLogin } from "../../store/modules/authSlice";

export default function Nav() {
  const isLoginUser = useAppSelector(isLogin);

  return (
    <nav className="w-full m-auto fixed bottom-0 z-[9] border-t border-y-lightGray bg-white">
      <div className="flex justify-between max-w-5xl m-auto  pt-2">
        <div className="w-20">
          <NavLink
            to="/"
            className={({ isActive }) => (isActive ? "text-y-red" : "")}
          >
            <div className="flex flex-col items-center text-sm">
              <SlHome className="text-3xl" />홈
            </div>
          </NavLink>
        </div>
        <div className="w-20">
          <NavLink
            to="/question"
            className={({ isActive }) => (isActive ? "text-y-red" : "")}
          >
            <div className="flex flex-col items-center text-sm">
              <BsPatchQuestion className="text-3xl" />
              이번주 질문
            </div>
          </NavLink>
        </div>
        <div className="w-20">
          <NavLink
            to="/post"
            className={({ isActive }) => (isActive ? "text-y-red" : "")}
          >
            <div className="flex flex-col items-center text-sm">
              <SlSpeech className="text-3xl" />
              게시판
            </div>
          </NavLink>
        </div>
        <div className="w-20">
          <NavLink
            to="/quiz"
            className={({ isActive }) => (isActive ? "text-y-red" : "")}
          >
            <div className="flex flex-col items-center text-sm">
              <IoExtensionPuzzleOutline className="text-3xl" />
              퀴즈
            </div>
          </NavLink>
        </div>
        <div className="w-20">
          <NavLink
            to={isLoginUser ? "/mypage" : "/login"}
            className={({ isActive }) => (isActive ? "text-y-red" : "")}
          >
            <div className="flex flex-col items-center text-sm">
              <BsPerson className="text-3xl" />나
            </div>
          </NavLink>
        </div>
      </div>
    </nav>
  );
}
