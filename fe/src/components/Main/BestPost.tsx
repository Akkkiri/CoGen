import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import UserInfo from "../user/UserInfo";
import { IoHeartOutline, IoChatbubbleEllipsesOutline } from "react-icons/io5";
import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";

export default function BestPost({ bestPostProps }: any) {
  const [bestPost, setBestPost] = useState<any>();
  useEffect(() => {
    if (bestPostProps !== undefined) setBestPost(bestPostProps);
  }, [bestPostProps]);
  return (
    <div>
      <div className="flex gap-2">
        <img
          src="/images/cogenlogo-r.png"
          alt="logo"
          className="w-9 h-6 self-center"
        ></img>
        <div className="text-lg"> 인기 게시글</div>
      </div>

      <Swiper
        spaceBetween={3}
        slidesPerView={1.4}
        grabCursor={true}
        scrollbar={{ draggable: true }}
        // navigation
        pagination={{ clickable: true }}
      >
        {bestPost?.map((el: any, idx: number) => (
          <SwiperSlide key={idx}>
            <NavLink to={`/post/${el.feedId}`}>
              <div className="p-2">
                <div
                  className={`${
                    idx % 2 === 0
                      ? "bg-y-sky rounded-2xl"
                      : "bg-y-pink rounded-2xl"
                  } p-4`}
                >
                  <div className="truncate">{el.title}</div>
                  <div className="h-20">
                    <div className="my-2 text-sm font-light break-all overflow-hidden line-clamp-3">
                      {el.body}
                    </div>
                  </div>
                  <div className="flex justify-between">
                    <UserInfo
                      nickname={el.nickname}
                      profileImage={el.profileImage}
                      date={el.createdAt}
                    />
                    <div className="flex text-xs gap-2">
                      <div className="flex self-center">
                        <IoHeartOutline className="self-center text-lg" />
                        {el.likeCount}
                      </div>
                      <div className="flex self-center">
                        <IoChatbubbleEllipsesOutline className="self-center text-lg" />
                        {el.commentCount}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </NavLink>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
}
