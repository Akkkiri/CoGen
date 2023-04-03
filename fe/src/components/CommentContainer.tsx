import { IoHeartOutline } from "react-icons/io5";

import UserInfo from "./user/UserInfo";
interface CommentContainerProps {
  contents: string;
  nickname: string;
  profileImage: string;
  date: string;
  like: string;
}

export default function CommentContainer({
  contents,
  nickname,
  profileImage,
  date,
  like,
}: CommentContainerProps) {
  return (
    <div className="pb-2">
      <div className="p-4 border border-y-lightGray rounded-xl">
        <UserInfo nickname={nickname} profileImage={profileImage} date={date} />
        <div className="mt-2 text-sm font-light">{contents}</div>
        <div className="flex justify-end text-xs">
          <div className="flex ">
            <IoHeartOutline className="text-lg" />
            <div className="self-center">좋아요 {like}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
