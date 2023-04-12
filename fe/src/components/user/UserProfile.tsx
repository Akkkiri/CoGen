import { useNavigate } from "react-router-dom";
import Level from "./Level";
import axios from "api/axios";
import { useAppSelector } from "store/hook";
import { isLogin } from "store/modules/authSlice";
import Swal from "sweetalert2";
import { useEffect, useState } from "react";

export interface UserProfileProps {
  userID?: number;
  nickname: string;
  hashcode: string;
  profileImage: string;
  level: number;
  ariFactor: number;
  friendsNum: number;
  isMine?: boolean;
}

export default function UserProfile({
  userID,
  nickname,
  hashcode,
  profileImage,
  level,
  ariFactor,
  friendsNum,
  isMine,
}: UserProfileProps) {
  const navigate = useNavigate();
  const isLoginUser = useAppSelector(isLogin);
  const [isFollowing, setIsFollowing] = useState(false);
  const medal = (level: number) => {
    if (level === 50) return 50;
    else if (level >= 40) return 40;
    else if (level >= 30) return 30;
    else if (level >= 20) return 20;
    else if (level >= 10) return 10;
    else return 1;
  };
  const medalImg: any = {
    1: "/images/level1.png",
    10: "/images/level10.png",
    20: "/images/level20.png",
    30: "/images/level30.png",
    40: "/images/level40.png",
    50: "/images/level50.png",
  };

  useEffect(() => {
    //setIsFollowing 으로 isFollowing 가져오는 로직 필요
  }, []);

  const handleFollowing = () => {
    axios
      .post(`/follows/${userID}`)
      .then((res) => {
        //제거
        console.log(res);
        setIsFollowing(!isFollowing);
      })
      .catch((err) => console.log(err));
  };
  return (
    <div className="flex justify-center items-center mx-2">
      <img
        src={profileImage}
        alt="profileImage"
        className="rounded-full w-20 h-20"
      ></img>
      <div className="w-full mx-4 mt-2">
        <div className="flex justify-between">
          <div className="flex items-end">
            <span>{nickname}</span>
            <span className="text-xs text-y-lightGray font-light">
              {hashcode}
            </span>
            <img
              src={medalImg[medal(level)]}
              alt="level"
              className="w-5 h-5"
            ></img>
          </div>
          <button
            className="rounded-lg bg-y-sky py-0.5 px-4 text-xs"
            onClick={() => {
              navigate(`friend`);
            }}
          >
            친구 {friendsNum}
          </button>
        </div>
        <Level level={level} ariFactor={ariFactor} />
        {isMine ? (
          <button
            className="w-full bg-y-red rounded-lg text-white text-xs py-1"
            onClick={() => navigate("/mypage/edit")}
          >
            회원정보 수정하기
          </button>
        ) : isFollowing ? (
          <button
            className="w-full bg-y-red rounded-lg text-white text-xs py-1"
            onClick={handleFollowing}
          >
            친구 삭제
          </button>
        ) : (
          <button
            className="w-full bg-y-pink rounded-lg text-black text-xs py-1"
            onClick={
              isLoginUser
                ? handleFollowing
                : () => {
                    Swal.fire({
                      text: "로그인이 필요한 서비스입니다",
                      showCancelButton: true,
                      reverseButtons: true,
                      confirmButtonColor: "#E74D47",
                      confirmButtonText: "로그인하러 가기",
                      cancelButtonColor: "#A19E9E",
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
    </div>
  );
}
