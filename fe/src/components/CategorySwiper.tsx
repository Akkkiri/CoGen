import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import { NavLink } from "react-router-dom";
type categoryProps = {
  setSelected: React.Dispatch<React.SetStateAction<string>>;
  checked: string;
};

export default function CategorySwiper({
  setSelected,
  checked,
}: categoryProps) {
  const categoryList = [
    "전체",
    "고민",
    "꿀팁",
    "장소공유",
    "명언",
    "유머",
    "일상",
    "기타",
  ];

  const onClick = (e: React.MouseEvent<HTMLInputElement>) => {
    setSelected((e.target as HTMLInputElement).value);
  };
  return (
    <Swiper
      spaceBetween={10}
      slidesPerView={3.5}
      grabCursor={true}
      scrollbar={{ draggable: true }}
      // navigation
      pagination={{ clickable: true }}
    >
      {/* {categoryList.map((el: string, idx: number) => (
        <SwiperSlide key={idx}>
          <div key={idx.toString()}>
            <input
              type="radio"
              name="category"
              id={el}
              value={el}
              onClick={onClick}
              className="peer hidden"
            />
            <label
              htmlFor={el}
              className={`${
                checked === el
                  ? "border border-y-red text-y-red text-base block cursor-pointer select-none rounded-xl p-1 text-center"
                  : "text-base block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
              }`}
            >
              {el}
            </label>
          </div>
        </SwiperSlide>
      ))} */}

      <SwiperSlide>
        <NavLink
          to="/post/ALL"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>전체</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/WORRY"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>고민</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/TIP"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>꿀팁</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/PLACE"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>장소공유</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/QUOTE"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>명언</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/HUMOR"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>유머</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/DAILY"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>일상</div>
        </NavLink>
      </SwiperSlide>
      <SwiperSlide>
        <NavLink
          to="/post/ETC"
          className={({ isActive }) =>
            isActive
              ? "border border-y-red text-y-red text-base md:text-lg block cursor-pointer select-none rounded-xl p-1 text-center"
              : "text-base md:text-lg block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
          }
        >
          <div>기타</div>
        </NavLink>
      </SwiperSlide>
    </Swiper>
  );
}
