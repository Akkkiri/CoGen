import { MdModeEdit, MdOutlineCancel } from "react-icons/md";
import { HiTrash } from "react-icons/hi";
import UserInfo from "./user/UserInfo";
import Swal from "sweetalert2";
import axios from "../api/axios";
import SmallInput from "../components/Inputs/SmallInput";
import { myid, isLogin } from "../store/modules/authSlice";
import { useAppSelector } from "../store/hook";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import WarningBtn from "./ WarningBtn";
import LikeBtn from "./LikeBtn";
import {
  AgeTypeMatcherToKor,
  GenderTypeMatcherToKor,
} from "../util/SelectUtil";
export interface CommentContainerProps {
  contents: string;
  nickname: string;
  profileImage: string;
  date: string;
  like: string | number;
  userid: number;
  answerId: number;
  isLiked: boolean;
  deleteAnswer: Function;
  genderType: string;
  ageType: string;
}

export default function QuestionCommentContainer({
  contents,
  nickname,
  profileImage,
  date,
  like,
  userid,
  answerId,
  isLiked,
  deleteAnswer,
  genderType,
  ageType,
}: CommentContainerProps) {
  const myId = useAppSelector(myid);
  const [isEditMode, setIsEditMode] = useState(false);
  const [inputState, setInputState] = useState<string>(contents);
  const isLoginUser = useAppSelector(isLogin);
  const [isLike, setIsLike] = useState<boolean>(isLiked);
  const [likeCount, setLikeCount] = useState<number>(Number(like));
  const navigate = useNavigate();

  const editAnswer = () => {
    setIsEditMode(true);
  };
  const deleteComment = () => {
    deleteAnswer(answerId);
  };
  const patchComment = () => {
    axios
      .patch(`/answers/${answerId}/edit`, {
        body: inputState,
      })
      .catch((err) => console.log(err));

    setIsEditMode(false);
  };
  const warningAnswer = () => {
    axios.patch(`/answers/${answerId}/report`).catch((err) => console.log(err));
  };
  const LikeAnswer = () => {
    axios
      .patch(`/answers/${answerId}/like`)
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
        <div className="flex gap-1 self-center pb-3">
          {genderType === "NOBODY" ? null : (
            <div className="text-xs md:text-base md:w-16 bg-y-pink p-1 w-12 text-center rounded-lg">
              {GenderTypeMatcherToKor(genderType)}
            </div>
          )}
          <div
            className={` ${
              ageType === "OTHERS"
                ? "text-[10px] md:text-sm"
                : "text-xs md:text-base "
            } md:w-16  bg-y-sky p-1 w-12 text-center rounded-lg`}
          >
            {AgeTypeMatcherToKor(ageType)}
          </div>
        </div>
        <div className="flex justify-between pb-2">
          <div className="">
            <UserInfo
              nickname={nickname}
              profileImage={profileImage}
              date={date}
              userId={userid}
            />
          </div>
          {isEditMode ? (
            <div className="flex px-4 text-sm self-center md:text-base">
              <button onClick={() => setIsEditMode(false)}>
                <MdOutlineCancel className="text-y-red inline mr-1" />
                취소
              </button>
            </div>
          ) : (
            <div>
              {myId === userid ? (
                <div className="flex gap-1 text-sm self-center md:text-base">
                  <button onClick={editAnswer}>
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
              ) : (
                <div className="self-center">
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
                          warningAnswer();
                        }
                      });
                    }}
                  />
                </div>
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
          <div className="mt-2 text-sm font-light whitespace-pre-line md:text-lg">
            {inputState}
          </div>
        )}
        <div className="flex w-full justify-end">
          <LikeBtn
            onClick={
              isLoginUser
                ? LikeAnswer
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
