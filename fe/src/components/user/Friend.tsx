import axios from "api/axios";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAppSelector } from "store/hook";
import { isLogin } from "store/modules/authSlice";
import Swal from "sweetalert2";

export interface FriendProps {
  userId: number;
  nickname: string;
  hashcode: string;
  profileImage: string;
  thumbnailPath: string;
  isFollowing: boolean;
}

export default function Friend({
  userId,
  nickname,
  hashcode,
  profileImage,
  isFollowing,
}: FriendProps) {
  const navigate = useNavigate();
  const isLoginUser = useAppSelector(isLogin);
  const [isFollow, setIsFollow] = useState<boolean>(isFollowing);
  const handleFollow = () => {
    axios
      .post(`/follows/${userId}`)
      .then((res) => {
        setIsFollow(!isFollow);
      })
      .catch((err) => console.log(err));
  };

  return (
    <div className="flex justify-between">
      <div className="flex items-center">
        <Link to={`/user/${userId}`}>
          <img
            src={profileImage}
            alt="userprofileImage"
            className="w-12 h-12 rounded-full mr-2"
          />
        </Link>
        <div>
          <span>{nickname}</span>
          <span className="text-xs text-y-lightGray font-light">
            {hashcode}
          </span>
        </div>
      </div>
      {isFollow ? (
        <button
          className="w-28 btn-r"
          onClick={
            isLoginUser
              ? handleFollow
              : () => {
                  Swal.fire({
                    title: "CoGen",
                    text: "로그인이 필요한 서비스 입니다.",
                    showCancelButton: true,
                    reverseButtons: true,
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
        >
          친구 삭제
        </button>
      ) : (
        <button
          className="w-28 btn-r bg-y-pink text-black hover:bg-red-200"
          onClick={
            isLoginUser
              ? handleFollow
              : () => {
                  Swal.fire({
                    text: "로그인이 필요한 서비스 입니다.",
                    showCancelButton: true,
                    reverseButtons: true,
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
        >
          친구하기
        </button>
      )}
    </div>
  );
}
