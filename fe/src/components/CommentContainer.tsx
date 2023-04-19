import { MdModeEdit, MdOutlineCancel } from "react-icons/md";
import { HiTrash } from "react-icons/hi";
import UserInfo from "./user/UserInfo";
import Swal from "sweetalert2";
import axios from "../api/axios";
import SmallInput from "../components/Inputs/SmallInput";
import { id, isLogin } from "../store/modules/authSlice";
import { useAppSelector } from "../store/hook";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import WarningBtn from "./ WarningBtn";
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
  deleteComment: Function;
}

export default function CommentContainer({
  contents,
  nickname,
  profileImage,
  date,
  like,
  userid,
  commentId,
  isLiked,
  deleteComment,
}: CommentContainerProps) {
  const myId = useAppSelector(id);
  const [isEditMode, setIsEditMode] = useState(false);
  const [inputState, setInputState] = useState<string>(contents);
  const [isLike, setIsLike] = useState<boolean>(isLiked);
  const [likeCount, setLikeCount] = useState<number>(Number(like));
  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();
  const deleteComments = () => {
    deleteComment(commentId);
  };
  const editComment = () => {
    setIsEditMode(true);
  };
  const patchComment = () => {
    axios
      .patch(`/comments/${commentId}/edit`, {
        body: inputState,
      })
      .catch((err) => console.log(err));

    setIsEditMode(false);
  };
  const warningComment = () => {
    axios
      .patch(`/comments/${commentId}/report`)
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
          {isEditMode ? (
            <div className="flex px-4 text-sm self-center">
              <button onClick={() => setIsEditMode(false)}>
                <MdOutlineCancel className="text-y-red inline mr-1" />
                취소
              </button>
            </div>
          ) : (
            <div>
              {myId === userid ? (
                <div className="flex gap-1 text-sm self-center">
                  <button onClick={editComment}>
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
                          deleteComments();
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
          )}
        </div>
        {isEditMode ? (
          <SmallInput
            inputState={inputState}
            setInputState={setInputState}
            placeholder={"답변을 작성해주세요."}
            postFunc={
              isLoginUser
                ? patchComment
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
          />
        ) : (
          <div className="mt-2 text-sm font-light whitespace-pre-line">
            {inputState}
          </div>
        )}
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
