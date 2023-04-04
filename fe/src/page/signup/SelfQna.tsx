import SelfQnaComponent from "components/signup/SelfQnaComponent";
import { useForm } from "react-hook-form";

export default function SelfQna() {
  const { getValues, handleSubmit } = useForm();
  const onSubmit = (data: any) => alert(JSON.stringify(data));

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
      <h1 className="font-bold text-2xl mt-3 mb-6">회원가입</h1>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          {questionList.map((el) => {
            return (
              <SelfQnaComponent key={el.qnaId} q={el.question} idx={el.qnaId} />
            );
          })}
        </div>
        <button
          type="button"
          onClick={() => {
            // const multipleValues = getValues([`${1}`, `${2}`]);
          }}
        >
          mm
        </button>
        <button className="btn-r w-full">등록하기</button>
      </form>
    </div>
  );
}
