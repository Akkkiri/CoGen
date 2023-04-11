import { useState } from "react";

export interface QuizBtnProps {
  answer: string;
  bodies: [string, string, string];
  content: string;
  explanation: string;
  showAnswer: boolean;
  setShowAnswer: React.Dispatch<React.SetStateAction<boolean>>;
  score: number;
  setScore: React.Dispatch<React.SetStateAction<number>>;
}

export default function QuizBtn({
  answer,
  bodies,
  content,
  explanation,
  showAnswer,
  setShowAnswer,
  score,
  setScore,
}: QuizBtnProps) {
  const [choice, setChoice] = useState("");
  const handleAnswer = (e: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
    setShowAnswer(true);
    setChoice(e.currentTarget.value);
    if (e.currentTarget.value === answer) {
      setScore(score + 20);
    }
  };
  return (
    <div className="flex flex-col text-center gap-4">
      <h1 className="mt-10">{content}</h1>
      {showAnswer ? (
        <p className="text-y-purple -my-2">정답은 "{answer}"입니다</p>
      ) : null}
      {bodies?.map((el, idx) => {
        return (
          <button
            key={idx}
            value={el}
            onClick={showAnswer ? () => {} : handleAnswer}
            className={`flex justify-between items-center bg-y-pink rounded-lg py-2 ${
              showAnswer ? (el === answer ? "bg-y-purple text-y-pink" : "") : ""
            } ${
              showAnswer
                ? choice === el
                  ? el === answer
                    ? ""
                    : "border-4 border-y-red"
                  : ""
                : ""
            }`}
          >
            <span
              className={`text-3xl font-bold text-y-red ml-4 ${
                showAnswer ? (el === answer ? "text-y-pink" : "") : ""
              }`}
            >
              {idx + 1}
            </span>
            {el}
            <span className="mr-4"></span>
          </button>
        );
      })}
      {showAnswer ? (
        <p className="text-y-lightGray -mb-2"> {explanation}</p>
      ) : null}
    </div>
  );
}
