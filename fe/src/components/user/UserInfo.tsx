import { ToDateString } from "../../util/TodateString";
interface UserInfoProps {
  nickname: string;
  profileImage: string;
  date: string;
}

export default function UserInfo({
  nickname,
  profileImage,
  date,
}: UserInfoProps) {
  return (
    <div className="flex">
      <img
        src={profileImage}
        alt="profileImage"
        className="rounded-full w-8 h-8 self-center md:w-10 md:h-10"
      ></img>
      <div className="ml-2">
        <div className="text-sm md:text-lg">{nickname}</div>
        <div className="font-light text-xs text-y-lightGray">
          {ToDateString(date)}
        </div>
      </div>
    </div>
  );
}
