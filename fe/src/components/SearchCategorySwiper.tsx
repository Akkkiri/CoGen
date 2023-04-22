import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import { useAppDispatch } from "store/hook";
import { saveCategory } from "store/modules/postSlice";
type categoryProps = {
  setSelected: React.Dispatch<React.SetStateAction<string>>;
  checked: string;
};

export default function SearchCategorySwiper({
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
  const dispatch = useAppDispatch();
  const onClick = (e: React.MouseEvent<HTMLInputElement>) => {
    setSelected((e.target as HTMLInputElement).value);
    dispatch(saveCategory((e.target as HTMLInputElement).value));
  };

  return (
    <Swiper
      spaceBetween={10}
      slidesPerView={3.5}
      grabCursor={true}
      scrollbar={{ draggable: true }}
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
                  ? "border border-y-red text-y-red text-base block cursor-pointer select-none rounded-xl p-1 text-center"
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
