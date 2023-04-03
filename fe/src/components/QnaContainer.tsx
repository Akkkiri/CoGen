interface QnaContainerProps {
  question: string;
  answer: string[];
  idx: number;
}

export default function QnaContainer({
  question,
  answer,
  idx,
}: QnaContainerProps) {
  return (
    <div
      className={`${
        idx % 2 !== 0 ? "bg-y-sky rounded-r-2xl" : "bg-y-pink rounded-l-2xl"
      } rounded-t-2xl p-4`}
    >
      <div>"{question}"</div>
      {answer.map((el, idx) => (
        <div
          key={idx}
          className="bg-white text-sm rounded-lg p-2 mt-2 font-light"
        >
          {el}
        </div>
      ))}
    </div>
  );
}
