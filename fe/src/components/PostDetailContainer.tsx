import { MdModeEdit } from "react-icons/md";
import { HiTrash } from "react-icons/hi";
import UserInfo from "./user/UserInfo";
import { Category } from "../util/CategoryUtil";
import axios from "../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import WarningBtn from "./ WarningBtn";
interface PostContainerProps {
  title: string;
  contents: string;
  tag: string;
  nickname: string;
  profileImage: string;
  date: string;
  view: number;
  isMine: boolean;
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
}: PostContainerProps) {
  const { PostId } = useParams();
  const navigate = useNavigate();
  const deletepost = () => {
    axios.delete(`/feeds/${PostId}/delete`);
    navigate(-1);
  };
  const warningComment = () => {
    axios.patch(`/feeds/${PostId}/report`).catch((err) => console.log(err));
  };
  return (
    <div className="p-2 border-b border-y-lightGray">
      <div className="p-2">
        <div className="bg-y-red text-white p-1 w-16 text-center text-xs rounded-md mb-2">
          {Category(tag)}
        </div>
        <div className="flex justify-between pb-2">
          <UserInfo
            nickname={nickname}
            profileImage={profileImage}
            date={date}
          />

          {isMine ? (
            <div className="flex gap-1 px-4 text-sm self-center">
              <button>
                <MdModeEdit className="text-y-red inline -mr-0.5" /> 수정
              </button>
              <button
                onClick={() => {
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
                }}
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
        <div>{title}</div>
        <div className="my-2 text-sm font-light">{contents}</div>
        <div className="text-sm text-y-gray">조회 {view}</div>
      </div>
    </div>
  );
}
