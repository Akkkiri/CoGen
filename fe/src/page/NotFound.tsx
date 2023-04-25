import { useNavigate } from "react-router-dom";
import { IoChevronBack, IoChevronForward } from "react-icons/io5";

export default function NotFound() {
  const navigate = useNavigate();
  return (
    <div className="flex flex-col justify-center items-center mt-44 md:mt-24">
      <img
        src="asset/notfound.png"
        alt="NotFound"
        className="w-60 mb-4 md:w-80"
      />
      <h2 className="text-lg lg:text-2xl mb-2 font-bold">
        페이지를 찾을 수 없습니다
      </h2>
      <h3 className="text-y-gray text-xs lg:text-sm my-0.5">
        존재하지 않은 주소를 입력하셨거나
      </h3>
      <h3 className="text-y-gray text-xs lg:text-sm my-0.5">
        요청하신 페이지의 주소가 변경, 삭제되어 찾을 수 없습니다
      </h3>
      <div className="flex mt-6">
        <button
          className="flex justify-center items-center px-5 text-y-lightGray hover:text-y-red"
          onClick={() => {
            navigate(-1);
          }}
        >
          <IoChevronBack />
          <span className="text-sm">이전 페이지로</span>
        </button>
        <button
          className="flex justify-center items-center px-5 text-y-lightGray hover:text-y-red"
          onClick={() => {
            navigate("/");
          }}
        >
          <span className="text-sm">홈 페이지로</span>
          <IoChevronForward />
        </button>
      </div>
    </div>
  );
}
