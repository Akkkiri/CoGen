import axios from "api/axios";
import { useAppDispatch } from "store/hook";
import { saveScore } from "store/modules/quizSlice";
import Swal from "sweetalert2";

export default function QuizScore({ score }: { score: number }) {
  const dispatch = useAppDispatch();

  const cheerMent = (score: number) => {
    switch (score) {
      case 0:
        return "다시 도전해보세요!";
      case 20:
        return "너무 아쉬워요!";
      case 40:
        return "살짝 아쉬워요!";
      case 60:
        return "대단해요!";
      case 80:
        return "훌륭해요!";
      case 100:
        return "완벽해요!";
    }
  };
  const scoreImg = (score: number) => {
    switch (score) {
      case 0:
        return "/images/quiz0.png";
      case 20:
        return "/images/quiz20.png";
      case 40:
        return "/images/quiz40.png";
      case 60:
        return "/images/quiz60.png";
      case 80:
        return "/images/quiz80.png";
      case 100:
        return "/images/quiz100.png";
    }
  };
  return (
    <div className="text-center mt-4">
      <p>당신의 점수는</p>
      <p className="text-4xl font-bold text-y-red mb-4">{score}점</p>
      <img src={scoreImg(score)} alt="humanImage" className="w-52 m-auto"></img>
      <p className="text-3xl font-bold mt-4">{cheerMent(score)}</p>
      <p className="text-y-lightGray">완료하지 않으면 저장되지 않습니다</p>
      <div className="flex gap-3">
        <button
          onClick={() => {
            window.location.replace("/quiz");
            dispatch(saveScore(-1));
          }}
          className="btn-g flex-1"
        >
          다시풀기
        </button>
        <button
          onClick={() => {
            axios.post("/api/quizzes/weekly/clear");
            dispatch(saveScore(score));
            Swal.fire({
              title: "CoGen",
              text: "저장되었습니다",
              confirmButtonColor: "#E74D47",
              confirmButtonText: "확인",
            });
          }}
          className="btn-r flex-1"
        >
          완료하기
        </button>
      </div>
    </div>
  );
}
