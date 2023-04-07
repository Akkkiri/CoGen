import axios from "api/axios";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import TextareaAutosize from "react-textarea-autosize";
import { useAppSelector } from "store/hook";
import { id } from "store/modules/authSlice";

export default function SelfQna() {
  const { register, getValues, handleSubmit } = useForm();
  const [error, setError] = useState(false);
  const navigate = useNavigate();
  const ID = useAppSelector(id);

  const isVaild = () => {
    const answered = [];
    for (let i = 1; i <= 10; i++) {
      if (getValues(String(i)).length > 0) {
        answered.push(i);
      }
    }
    if (answered.length >= 3) {
      return true;
    } else {
      return false;
    }
  };

  const onSubmit = () => {
    const postBody = [
      { qnaId: 1, answerBody: getValues("1") },
      { qnaId: 2, answerBody: getValues("2") },
      { qnaId: 3, answerBody: getValues("3") },
      { qnaId: 4, answerBody: getValues("4") },
      { qnaId: 5, answerBody: getValues("5") },
      { qnaId: 6, answerBody: getValues("6") },
      { qnaId: 7, answerBody: getValues("7") },
      { qnaId: 8, answerBody: getValues("8") },
      { qnaId: 9, answerBody: getValues("9") },
      { qnaId: 10, answerBody: getValues("10") },
    ];
    if (isVaild()) {
      setError(false);
      axios
        .patch(`/users/${ID}/firstqna`, postBody)
        .then((res) => {
          //제거
          // navigate("/");
          navigate("/login");
        })
        .catch((err) => console.log(err));
    } else {
      setError(true);
    }
  };

  const questionList = [
    { qnaId: 1, question: "나의 성격 (MBTI 등)은?" },
    { qnaId: 2, question: "나를 표현할 수 있는 한 단어는?" },
    { qnaId: 3, question: "내가 좋아하는 연예인 또는 유튜버는?" },
    { qnaId: 4, question: "내가 좋아하는 계절과 이유는?" },
    { qnaId: 5, question: "가장 가보고 싶은 여행지는?" },
    { qnaId: 6, question: "무인도에 가져갈 3가지는?" },
    { qnaId: 7, question: "나는 아침형인간 vs 저녁형인간?" },
    { qnaId: 8, question: "내 인생의 음악(bgm)을 고른다면?" },
    { qnaId: 9, question: "내 인생 최고의 추억은?" },
    { qnaId: 10, question: "나의 묘비명을 지어본다면?" },
  ];
  return (
    <div className="max-w-md m-auto flex flex-col justify-center items-center mt-12 px-4">
      <img src="/images/logo.png" alt="logo" width={90}></img>
      <h1 className="font-bold text-2xl my-3">회원가입</h1>
      <form onSubmit={handleSubmit(onSubmit)} className="w-full">
        <p className="text-y-lightGray text-center mb-3">
          원하는 질문을 3개 이상 골라 답변해주세요
        </p>
        <div className="flex flex-col gap-2">
          {questionList.map((el) => {
            return (
              <div
                key={el.qnaId}
                className={`${
                  el.qnaId % 2 === 0
                    ? "bg-y-sky rounded-r-3xl"
                    : "bg-y-pink rounded-l-3xl"
                } cursor-pointer rounded-t-3xl px-4 py-7 text-center`}
              >
                <h1>{el.question}</h1>
                <TextareaAutosize
                  {...register(`${el.qnaId}`)}
                  minRows={1}
                  maxRows={6}
                  maxLength={1000}
                  className={`${"rounded-lg mt-3 -mb-2 w-full px-2 text-lg font-light resize-none focus:outline-y-lightGray/20"}`}
                />
              </div>
            );
          })}
        </div>
        {error ? (
          <p className="text-center text-y-red/80 mt-3">
            질문 3개 이상 답변을 작성하고 다시 버튼을 눌러주세요
          </p>
        ) : null}
        <button className="btn-r w-full mt-2">등록하기</button>
      </form>
    </div>
  );
}
