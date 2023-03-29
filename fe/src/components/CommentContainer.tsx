import { IoHeartOutline } from "react-icons/io5";

import UserInfo from "./user/UserInfo";
interface CommentContainerProps {
  contents: string;
}

export default function CommentContainer({ contents }: CommentContainerProps) {
  return (
    <div className="p-2">
      <div className="p-4 border border-y-lightGray rounded-xl">
        <UserInfo
          nickname={"닉네임"}
          profileImage={"/images/user.png"}
          date={"2023.03.22"}
        />
        <div className="mt-2 text-sm font-light">{contents}</div>
        <div className="flex justify-end text-xs">
          <div className="flex ">
            <IoHeartOutline className="text-lg" />
            <div className="self-center">좋아요 1</div>
          </div>
        </div>
      </div>
    </div>
  );
}
