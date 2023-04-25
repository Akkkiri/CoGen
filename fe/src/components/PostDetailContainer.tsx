import { MdModeEdit } from "react-icons/md";
import { HiTrash } from "react-icons/hi";
import UserInfo from "./user/UserInfo";
import { Category } from "../util/CategoryUtil";
import axios from "../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import WarningBtn from "./ WarningBtn";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { Pagination, Navigation } from "swiper";
import { isLogin } from "../store/modules/authSlice";
import { useAppSelector } from "../store/hook";
interface PostContainerProps {
  title: string;
  contents: string;
  tag: string;
  nickname: string;
  profileImage: string;
  date: string;
  view: number;
  isMine: boolean;
  image: string;
  image2: string;
  image3: string;
  userId: number;
  userid: number;
}

export default function PostDetailContainer({
  title,
  contents,
  tag,
  nickname,
  profileImage,
  date,
  view,
  isMine,
  userid,
  image,
  image2,
  image3,
  userId,
}: PostContainerProps) {
  const { PostId } = useParams();
  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();
  const deletepost = () => {
    axios.delete(`/feeds/${PostId}/delete`);
    navigate(-1);
  };
  const warningComment = () => {
    axios.patch(`/feeds/${PostId}/report`).catch((err) => console.log(err));
  };
  const goToLogin = () => {
    Swal.fire({
      title: "CoGen",
      text: "로그인이 필요한 서비스 입니다.",
      showCancelButton: true,
      confirmButtonColor: "#E74D47",
      cancelButtonColor: "#A7A7A7",
      confirmButtonText: "로그인",
      cancelButtonText: "취소",
    }).then((result) => {
      if (result.isConfirmed) {
        navigate("/login");
      }
    });
  };
  return (
    <div className="p-2 border-b border-y-lightGray">
      <div className="p-2">
        <div className="bg-y-red text-white p-1 w-16 text-center text-xs rounded-md mb-2 md:text-base md:w-20">
          {Category(tag)}
        </div>
        <div className="flex justify-between pb-2">
          <UserInfo
            nickname={nickname}
            profileImage={profileImage}
            date={date}
            userId={userId}
          />

          {userId === userid && isLoginUser ? (
            <div className="flex gap-1 px-4 text-sm md:text-base self-center">
              <button
                onClick={
                  isLoginUser
                    ? () => navigate(`/editpost/${PostId}`)
                    : goToLogin
                }
              >
                <MdModeEdit className="text-y-red inline -mr-0.5" /> 수정
              </button>
              <button
                onClick={
                  isLoginUser
                    ? () => {
                        Swal.fire({
                          title: "게시글을 삭제하시겠습니까?",
                          text: "삭제하시면 다시 복구시킬 수 없습니다.",
                          showCancelButton: true,
                          confirmButtonColor: "#E74D47",
                          cancelButtonColor: "#A7A7A7",
                          confirmButtonText: "삭제",
                          cancelButtonText: "취소",
                        }).then((result) => {
                          if (result.isConfirmed) {
                            deletepost();
                          }
                        });
                      }
                    : goToLogin
                }
              >
                <HiTrash className="text-y-red inline -mr-0.5" />
                삭제
              </button>
            </div>
          ) : (
            <WarningBtn
              onClick={() => {
                Swal.fire({
                  title: "CoGen",
                  text: "게시글을 신고하시겠습니까?",
                  showCancelButton: true,
                  confirmButtonColor: "#E74D47",
                  cancelButtonColor: "#A7A7A7",
                  confirmButtonText: "신고",
                  cancelButtonText: "취소",
                }).then((result) => {
                  if (result.isConfirmed) {
                    warningComment();
                  }
                });
              }}
            />
          )}
        </div>
        <div className="text-lg md:text-2xl">{title}</div>
        <div className="my-5">
          <Swiper
            pagination={true}
            navigation={true}
            // loop={true}
            modules={[Pagination, Navigation]}
            className="overflow-clip"
          >
            {!image ? null : (
              <div>
                <SwiperSlide key={1}>
                  <img
                    src={image}
                    alt="imageList1"
                    width={300}
                    height={300}
                    className="m-auto w-auto h-72 md:h-96"
                  />
                </SwiperSlide>
              </div>
            )}
            {!image2 ? null : (
              <div>
                <SwiperSlide key={2}>
                  <img
                    src={image2}
                    alt="imageList2"
                    width={300}
                    height={300}
                    className="m-auto h-72 w-auto md:h-96"
                  />
                </SwiperSlide>
              </div>
            )}
            {!image3 ? null : (
              <div>
                <SwiperSlide key={3}>
                  <img
                    src={image3}
                    alt="imageList3"
                    width={300}
                    height={300}
                    className="m-auto h-72 w-auto md:h-96"
                  />
                </SwiperSlide>
              </div>
            )}
          </Swiper>
        </div>
        <div className="my-2">
          <div className="font-light whitespace-pre-line md:text-xl">
            {contents}
          </div>
        </div>
        <div className="text-sm text-y-gray md:text-lg">조회 {view}</div>
      </div>
    </div>
  );
}
