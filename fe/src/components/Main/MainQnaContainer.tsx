interface QnaContainerProps {
  question: string;
}

export default function MainQnaContainer({ question }: QnaContainerProps) {
  return (
    <div className="px-2 py-4">
      <div className="flex gap-2">
        <img
          src="/images/cogenlogo-r.png"
          alt="logo"
          className="w-9 h-6 self-center"
        ></img>
        <div className="text-lg">이주의 질문</div>
      </div>
      <div className="bg-y-pink rounded-l-3xl rounded-t-3xl p-4 text-center m-1">
        <div className="p-3">"{question}"</div>
        <button className="btn-r text-sm">답변 하러가기</button>
      </div>
    </div>
  );
}
