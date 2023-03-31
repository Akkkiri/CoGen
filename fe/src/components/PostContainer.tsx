import { IoHeartOutline, IoChatbubbleEllipsesOutline } from "react-icons/io5";
import { BsDot } from "react-icons/bs";
import { useEffect, useState } from "react";
import { ToDateString } from "../util/TodateString";
import { Category } from "../util/CategoryUtil";
import { NavLink } from "react-router-dom";
export default function PostContainer({ postContainerProps }: any) {
  const [containerProps, setContainerProps] = useState<any>();
  useEffect(() => {
    if (postContainerProps !== undefined) setContainerProps(postContainerProps);
  }, [postContainerProps]);
  return (
    <div>
      {containerProps?.map((el: any, idx: number) => (
        <div key={idx}>
          <NavLink to={`/post/${el.feedId}`}>
            <div className="p-2 border-b border-y-lightGray">
              <div className="p-2">
                <div className="bg-y-red text-white p-1 w-16 text-center text-xs rounded-md mb-2">
                  {Category(el.category)}
                </div>
                <div>{el.title}</div>
                <div className="h-20">
                  <div className="my-2 text-sm font-light line-clamp-3">
                    {el.body}
                  </div>
                </div>
                <div className="flex justify-between text-xs">
                  <div className="flex text-y-gray">
                    이름 <BsDot className="self-center" />{" "}
                    {ToDateString(el.createdAt)}
                    <BsDot className="self-center" /> 조회 {el.viewCount}
                  </div>
                  <div className="flex gap-2 text-y-gray text-xs">
                    <div className="flex">
                      <IoHeartOutline className="text-base" />
                      <div className="self-center">좋아요 {el.likeCount}</div>
                    </div>
                    <div className="flex ">
                      <IoChatbubbleEllipsesOutline className="text-base" />
                      <div className="self-center">댓글 {el.commentCount}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </NavLink>
        </div>
      ))}
    </div>
  );
}
