import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Pagination, Navigation } from "swiper";

export default function ImageCarousel(imageList: any) {
  return (
    <>
      {imageList?.length === 0 ? (
        <></>
      ) : (
        <Swiper
          pagination={true}
          navigation={true}
          // loop={true}
          modules={[Pagination, Navigation]}
          className="overflow-clip h-72"
        >
          <div>
            {imageList?.map((el: any, idx: number) => (
              <SwiperSlide key={idx} className="">
                <img
                  src={el}
                  alt="imageList"
                  width={300}
                  height={300}
                  className="m-auto h-full w-auto"
                />
              </SwiperSlide>
            ))}
          </div>
        </Swiper>
      )}
    </>
  );
}
