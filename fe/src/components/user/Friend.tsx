import axios from "api/axios";
import { useState } from "react";
import { Link } from "react-router-dom";

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
  const [isFollow, setIsFollow] = useState<boolean>(isFollowing);
  const handleFollow = () => {
    axios
      .post(`/follows/${userId}`)
      .then((res) => {
        //제거
        // console.log(res);
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
            className="w-12 h-12 mr-2"
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
        <button className="w-28 btn-r" onClick={handleFollow}>
          친구 삭제
        </button>
      ) : (
        <button
          className="w-28 btn-r bg-y-pink text-black hover:bg-red-200"
          onClick={handleFollow}
        >
          친구하기
        </button>
      )}
    </div>
  );
}
