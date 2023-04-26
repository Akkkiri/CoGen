import { IoHeartOutline, IoChatbubbleEllipsesOutline } from "react-icons/io5";
import { BsDot } from "react-icons/bs";
import { useEffect, useState } from "react";
import { ToDateString } from "../util/TodateString";
import { Category } from "../util/CategoryUtil";
import { NavLink } from "react-router-dom";
import BookmarkBtn from "./user/BookmarkBtn";
import Empty from "./Empty";

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
    <div className="p-2">
      {containerProps?.length === 0 ? (
        <Empty str={"게시글이"} />
      ) : (
        containerProps?.map((el: any, idx: number) => (
          <div key={idx}>
            {bookmark ? <BookmarkBtn feedId={el.feedId} /> : null}
            <NavLink to={`/post/${el.feedId}`}>
              <div className="p-2 border-b border-y-lightGray">
                <div className="p-2">
                  <div className="bg-y-red text-white p-1 w-16 text-center text-xs rounded-md mb-2 md:text-base md:w-20">
                    {Category(el.category)}
                  </div>
                  <div className="md:text-xl">{el.title}</div>
                  <div className="h-20 md:h-24">
                    <div className="my-2 text-sm font-light line-clamp-3 md:text-lg">
                      {el.body}
                    </div>
                  </div>
                  <div className="flex justify-between text-xs md:text-base">
                    <div className="flex text-y-gray">
                      {el.nickname} <BsDot className="self-center" />
                      {ToDateString(el.createdAt)}
                      <BsDot className="self-center" /> 조회 {el.viewCount}
                    </div>
                    <div className="flex gap-1 text-y-gray text-xs md:text-base">
                      <div className="flex">
                        <IoHeartOutline className="text-base md:text-2xl" />
                        <div className="self-center">{el.likeCount}</div>
                      </div>
                      <div className="flex ">
                        <IoChatbubbleEllipsesOutline className="text-base md:text-2xl" />
                        <div className="self-center">{el.commentCount}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </NavLink>
          </div>
        ))
      )}
    </div>
  );
}
