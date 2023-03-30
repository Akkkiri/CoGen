import { MdModeEdit } from "react-icons/md";
import { HiTrash } from "react-icons/hi";
import UserInfo from "./user/UserInfo";
interface PostContainerProps {
  title: string;
  contents: string;
  tag: string;
}

export default function PostDetailContainer({
  title,
  contents,
  tag,
}: PostContainerProps) {
  return (
    <div className="p-2 border-b border-y-lightGray">
      <div className="p-2">
        <div className="bg-y-red text-white p-1 w-10 text-center text-xs rounded-md mb-2">
          {tag}
        </div>
        <div className="flex justify-between pb-2">
          <UserInfo
            nickname={"닉네임"}
            profileImage={"/images/user.png"}
            date={"2023.03.22"}
          />
          <div className="flex px-4 text-sm self-center">
            <div>
              <MdModeEdit className="text-y-red inline -mr-0.5" /> 수정
            </div>
            <div>
              <HiTrash className="text-y-red ml-1 inline" />
              <span>삭제</span>
            </div>
          </div>
        </div>
        <div>{title}</div>
        <div className="my-2 text-sm font-light">{contents}</div>
        <div className="text-sm text-y-gray">조회 9</div>
      </div>
    </div>
  );
}
