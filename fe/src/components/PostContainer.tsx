import { IoHeartOutline, IoChatbubbleEllipsesOutline } from "react-icons/io5";
import { BsDot } from "react-icons/bs";
import { useEffect, useState } from "react";
import { ToDateString } from "../util/TodateString";
import { Category } from "../util/CategoryUtil";
import { NavLink } from "react-router-dom";
import BookmarkBtn from "./user/BookmarkBtn";

interface PostContainerProps {
  postContainerProps: any;
  bookmark?: boolean;
}

export default function PostContainer({
  postContainerProps,
  bookmark,
}: PostContainerProps) {
  const [containerProps, setContainerProps] = useState<any>();
  useEffect(() => {
    if (postContainerProps !== undefined) setContainerProps(postContainerProps);
  }, [postContainerProps]);
  return (
    <div>
      {containerProps?.map((el: any, idx: number) => (
        <div key={idx}>
          {bookmark ? <BookmarkBtn /> : null}
          <NavLink to={`/post/${el.feedId}`}>
            <div className="p-2 border-b border-y-lightGray">
              <div>
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
                    {el.nickname}
                    <BsDot className="self-center" />
                    {ToDateString(el.createdAt)}
                    <BsDot className="self-center" />
                    조회{el.viewCount}
                  </div>
                  <div className="flex gap-1 text-y-gray text-xs">
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
