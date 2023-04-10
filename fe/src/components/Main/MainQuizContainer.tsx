import { NavLink } from "react-router-dom";
interface QnaContainerProps {
  question: string;
}

export default function MainQuizContainer({ question }: QnaContainerProps) {
  return (
    <div className="px-2 py-4">
      <div className="flex gap-2">
        <img
          src="/images/cogenlogo-p.png"
          alt="logo"
          className="w-9 h-6 self-center"
        ></img>
        <div className="text-lg">퀴즈</div>
      </div>
      <div className="bg-y-sky rounded-r-3xl rounded-t-3xl p-4 text-center m-1">
        <div className="p-3">"{question}"</div>
        <NavLink to="/quiz">
          <button className="btn-p rounded-lg px-4 py-2 text-white text-sm">
            퀴즈 풀러가기
          </button>
        </NavLink>
      </div>
    </div>
  );
}
