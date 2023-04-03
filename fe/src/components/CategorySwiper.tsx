import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
type categoryProps = {
  setSelected: React.Dispatch<React.SetStateAction<string>>;
  checked: string | undefined;
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
      navigation
      pagination={{ clickable: true }}
    >
      {categoryList.map((el: string, idx: number) => (
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
                  ? "border-2 border-y-red text-base block cursor-pointer select-none rounded-xl p-1 text-center"
                  : "text-base block border-y-lightGray cursor-pointer select-none rounded-xl p-1 text-center border"
              }`}
            >
              {el}
            </label>
          </div>
        </SwiperSlide>
      ))}
    </Swiper>
  );
}
