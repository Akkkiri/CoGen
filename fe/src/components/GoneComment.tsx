import { IoHeartOutline } from "react-icons/io5";
import { MdModeEdit, MdOutlineCancel } from "react-icons/md";
import { HiTrash } from "react-icons/hi";
import UserInfo from "./user/UserInfo";
import Swal from "sweetalert2";
import axios from "../api/axios";
import WarningBtn from "./ WarningBtn";
import { id, isLogin } from "../store/modules/authSlice";
import { useAppSelector } from "../store/hook";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import LikeBtn from "./LikeBtn";
export interface CommentContainerProps {
  contents: string;
  nickname: string;
  profileImage: string;
  date: string;
  like: string | number;
  userid: number;
  commentId: number;
  isLiked: boolean;
}

export default function GoneComment({
  contents,
  nickname,
  profileImage,
  date,
  like,
  userid,
  commentId,
  isLiked,
}: CommentContainerProps) {
  const myId = useAppSelector(id);
  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();
  const [isLike, setIsLike] = useState<boolean>(isLiked);
  const [likeCount, setLikeCount] = useState<number>(Number(like));
  const deleteComment = () => {
    axios
      .delete(`/answers/${commentId}/delete`)
      .then(() => window.location.reload())
      .catch((err) => console.log(err));
  };
  const warningComment = () => {
    axios
      .patch(`/answers/${commentId}/report`)
      .catch((err) => console.log(err));
  };
  const LikeComment = () => {
    axios
      .patch(`/comments/${commentId}/like`)
      .then(() => {
        setIsLike(!isLike);
        if (isLike) {
          setLikeCount(likeCount - 1);
        } else {
          setLikeCount(likeCount + 1);
        }
      })
      .catch((err) => console.log(err));
  };
  return (
    <div className="pb-2">
      <div className="p-4 border border-y-lightGray rounded-xl">
        <div className="flex justify-between pb-2">
          <UserInfo
            nickname={nickname}
            profileImage={profileImage}
            date={date}
          />

          <div>
            {myId === userid ? (
              <div className="flex gap-1 px-4 text-sm self-center">
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
                        deleteComment();
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
        </div>
        <div className="mt-2 text-sm font-light whitespace-pre-line">
          {contents}
        </div>
        <div className="flex w-full justify-end">
          <LikeBtn
            onClick={
              isLoginUser
                ? LikeComment
                : () => {
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
                  }
            }
            likeCount={likeCount}
            isLike={isLike}
          />
        </div>
      </div>
    </div>
  );
}
