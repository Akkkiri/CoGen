import { IoMdClose } from "react-icons/io";
import { useNavigate } from "react-router-dom";
export default function CloseBtn() {
  const navigate = useNavigate();
  return (
    <IoMdClose
      onClick={() => navigate(-1)}
      className="w-6 h-6 cursor-pointer absolute "
    />
  );
}
