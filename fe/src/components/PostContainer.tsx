import { IoHeartOutline, IoChatbubbleEllipsesOutline } from "react-icons/io5";
import { BsDot } from "react-icons/bs";
interface PostContainerProps {
  title: string;
  contents: string;
  tag: string;
}

export default function PostContainer({
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
        <div>{title}</div>
        <div className="my-2 text-sm font-light">{contents}</div>
        <div className="flex justify-between text-xs">
          <div className="flex">
            이름 <BsDot className="self-center" /> 1시간전
            <BsDot className="self-center" /> 조회 8
          </div>
          <div className="flex gap-2">
            <div className="flex">
              <IoHeartOutline className="text-lg" />
              <div className="self-center">좋아요 1</div>
            </div>
            <div className="flex ">
              <IoChatbubbleEllipsesOutline className="text-lg" />
              <div className="self-center">댓글 1</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
