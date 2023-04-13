import { IoHeartOutline } from "react-icons/io5";
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
export interface CommentContainerProps {
  contents: string;
  nickname: string;
  profileImage: string;
  date: string;
  like: string | number;
  userid: number;
  commentId: number;
}

export default function QuestionCommentContainer({
  contents,
  nickname,
  profileImage,
  date,
  like,
  userid,
  commentId,
}: CommentContainerProps) {
  const myId = useAppSelector(id);
  const [isEditMode, setIsEditMode] = useState(false);
  const [inputState, setInputState] = useState<string>(contents);
  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();
  const deleteComment = () => {
    axios
      .delete(`/answers/${commentId}/delete`)
      .then(() => window.location.reload())
      .catch((err) => console.log(err));
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
                <div className="flex gap-1 px-4 text-sm self-center">
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
                          deleteComment();
                        }
                      });
                    }}
                  >
                    <HiTrash className="text-y-red inline -mr-0.5" />
                    삭제
                  </button>
                </div>
              ) : null}
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
          <div className="mt-2 text-sm font-light">{inputState}</div>
        )}
        <div className="flex justify-end text-xs">
          <div className="flex ">
            <IoHeartOutline className="text-lg" />
            <div className="self-center">좋아요 {like}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
