import UserInfo from "./user/UserInfo";
import { IoHeartOutline, IoChatbubbleEllipsesOutline } from "react-icons/io5";

interface MainPostContainerProps {
  title: string;
  contents: string;
  idx: number;
}

export default function MainPostContainer({
  title,
  contents,
  idx,
}: MainPostContainerProps) {
  return (
    <div className="p-2">
      <div
        className={`${
          idx % 2 === 0 ? "bg-y-sky rounded-2xl" : "bg-y-pink rounded-2xl"
        } p-4`}
      >
        <div>{title}</div>
        <div className="my-2 text-sm font-light">{contents}</div>
        <div className="flex justify-between">
          <UserInfo
            nickname={"닉네임"}
            profileImage={"/images/user.png"}
            date={"2023.03.22"}
            userId={1}
          />
          <div className="flex text-xs gap-2">
            <div className="flex self-center">
              <IoHeartOutline className="self-center text-lg" />1
            </div>
            <div className="flex self-center">
              <IoChatbubbleEllipsesOutline className="self-center text-lg" />1
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
