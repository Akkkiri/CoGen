import { Point, ConvertPoint } from "util/LevelUtil";
import { AiOutlineQuestionCircle } from "react-icons/ai";
import Swal from "sweetalert2";
export default function Level({
  ariFactor,
  level,
}: {
  ariFactor: Point;
  level: number;
}) {
  return (
    <div className="my-2 text-xs">
      <div className="flex justify-between mb-1">
        <span className="flex justify-center">
          레벨 {level}
          <button
            className="text-y-purple text-sm ml-1"
            onClick={() => {
              Swal.fire({
                title: "CoGen",
                text: "레벨은 총 50까지 있으며, 코젠 포인트가 50 쌓일때마다 레벨 up! 코젠 포인트는 게시글, 댓글, 퀴즈 풀기 등 활동을 통해 쌓을 수 있어요! 레벨이 올라가면 멋진 메달을 받을 수 있어요 :)",
                confirmButtonColor: "#7254E9",
                confirmButtonText: "확인",
              });
            }}
          >
            <AiOutlineQuestionCircle />
          </button>
        </span>
        <span>
          <span className="text-y-purple">{ariFactor}</span>/50
        </span>
      </div>
      <div className="w-full h-2 bg-y-lightGray/50 rounded-lg">
        <div
          className={`${ConvertPoint[ariFactor]} h-2 bg-y-purple rounded-lg`}
        ></div>
      </div>
    </div>
  );
}
