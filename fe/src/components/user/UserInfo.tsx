import { ToDateString } from "../../util/TodateString";
import { useAppSelector } from "../../store/hook";
import { myid } from "../../store/modules/authSlice";
import { useNavigate } from "react-router-dom";
interface UserInfoProps {
  nickname: string;
  profileImage: string;
  date: string;
  userId: number;
}

export default function UserInfo({
  nickname,
  profileImage,
  date,
  userId,
}: UserInfoProps) {
  const userid = useAppSelector(myid);
  const navigate = useNavigate();
  const userCheck = () => {
    if (userid || userid !== userId) {
      navigate(`/user/${userId}`);
    } else {
      navigate(`/mypage`);
    }
  };
  return (
    <div className="flex">
      <img
        src={profileImage}
        alt="profileImage"
        className="rounded-full w-8 h-8 self-center md:w-10 md:h-10 cursor-pointer"
        onClick={userCheck}
      ></img>
      <div className="ml-2">
        <div className="text-sm md:text-lg cursor-pointer" onClick={userCheck}>
          {nickname}
        </div>
        <div className="font-light text-xs text-y-lightGray">
          {ToDateString(date)}
        </div>
      </div>
    </div>
  );
}
