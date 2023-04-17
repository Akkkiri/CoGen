import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Pagination, Navigation } from "swiper";
interface Props {
  image: any;
}
export default function FeedImage({ image }: Props) {
  return (
    <>
      {image.length === 0 ? (
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
            {image.map((el: any) => (
              <SwiperSlide key={el.pairingImageId} className="">
                {/* <image
                  src={el.imageUrl}
                  alt="imageList"
                  width={300}
                  height={300}
                  className="m-auto h-full w-auto"
                  priority
                /> */}
              </SwiperSlide>
            ))}
          </div>
        </Swiper>
      )}
    </>
  );
}
