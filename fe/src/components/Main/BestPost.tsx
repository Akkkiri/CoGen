import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import MainPostContainer from "../MainPostContainer";
export default function BestPost() {
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
        spaceBetween={10}
        slidesPerView={1.5}
        grabCursor={true}
        scrollbar={{ draggable: true }}
        navigation
        pagination={{ clickable: true }}
      >
        <SwiperSlide>
          <MainPostContainer
            title={"내일 점심에 뭘 먹는게 좋을까요?"}
            contents={
              "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ"
            }
            idx={1}
          />
        </SwiperSlide>
      </Swiper>
    </div>
  );
}
