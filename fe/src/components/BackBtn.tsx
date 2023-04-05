import { HiXMark } from "react-icons/hi2";
import { useNavigate } from "react-router-dom";

export default function BackBtn() {
  const navigate = useNavigate();
  return (
    <button
      className="text-2xl font-bold absolute p-3"
      onClick={() => {
        navigate(-1);
      }}
    >
      <HiXMark />
    </button>
  );
}
