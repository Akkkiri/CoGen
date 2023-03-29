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
        className="rounded-full w-8 h-8"
      ></img>
      <div className="ml-2">
        <div className="text-sm">{nickname}</div>
        <div className="font-light text-[8px] text-y-lightGray">{date}</div>
      </div>
    </div>
  );
}
