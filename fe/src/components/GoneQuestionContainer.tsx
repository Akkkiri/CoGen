import { NavLink } from "react-router-dom";

interface QnaContainerProps {
  questionId: number;
  question: string;
  idx: number;
}

export default function GoneQuestionContainer({
  questionId,
  question,
  idx,
}: QnaContainerProps) {
  return (
    <NavLink to={`/question/${questionId}`}>
      <div
        className={`${
          idx % 2 !== 0 ? "bg-y-sky rounded-r-2xl" : "bg-y-pink rounded-l-2xl"
        } rounded-t-2xl p-4 m-3 md:text-xl`}
      >
        <div>"{question}"</div>
      </div>
    </NavLink>
  );
}
