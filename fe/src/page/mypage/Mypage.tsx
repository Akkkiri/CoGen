import axios from "api/axios";
import UserProfile, { UserProfileProps } from "components/user/UserProfile";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaChevronRight } from "react-icons/fa";
import {
  IoChatbubbleEllipsesOutline,
  IoLogOutOutline,
  IoLeafOutline,
} from "react-icons/io5";
import { BsBookmark, BsQuestionCircle, BsPencil } from "react-icons/bs";
import { useAppDispatch } from "store/hook";
import { logout } from "store/modules/authSlice";
import authAPI from "api/authAPI";

export default function Mypage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [userprofile, setUserprofile] = useState<UserProfileProps>({
    nickname: "",
    hashcode: "",
    profileImage: "",
    level: 0,
    ariFactor: 0,
    friendsNum: 0,
    isMine: true,
  });
  useEffect(() => {
    axios
      .get("/mypage")
      .then((res) => {
        const obj = {
          nickname: res.data.nickname,
          hashcode: res.data.hashcode,
          profileImage: res.data.profileImage,
          level: res.data.level,
          ariFactor: res.data.ariFactor,
          friendsNum: res.data.friendsNum,
          isMine: true,
        };
        setUserprofile(obj);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [dispatch, navigate]);

  return (
    <>
      <h1 className="page-title">마이페이지</h1>
      <UserProfile {...userprofile} />
      <div className="mt-4 border-t-4 border-y-lightGray/30">
        <Link
          to="/mypage/qna"
          className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red"
        >
          <div className="flex items-center md:text-lg">
            <IoLeafOutline className="mr-2 text-3xl rotate-90" />
            나의 문답
          </div>
          <FaChevronRight />
        </Link>
        <Link
          to="/mypage/question"
          className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red"
        >
          <div className="flex items-center md:text-lg">
            <BsQuestionCircle className="mr-2 text-3xl" />
            내가 답한 질문
          </div>
          <FaChevronRight />
        </Link>
        <Link
          to="/mypage/bookmark"
          className="flex justify-between items-center p-4 border-b border-y-lightGray  hover:text-y-red"
        >
          <div className="flex items-center md:text-lg">
            <BsBookmark className="mr-2 text-3xl" />
            내가 저장한 글
          </div>
          <FaChevronRight />
        </Link>
        <Link
          to="/mypage/post"
          className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red"
        >
          <div className="flex items-center md:text-lg">
            <BsPencil className="mr-2 text-3xl" />
            나의 게시글
          </div>
          <FaChevronRight />
        </Link>
        <Link
          to="/mypage/comment"
          className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red"
        >
          <div className="flex items-center md:text-lg">
            <IoChatbubbleEllipsesOutline className="mr-2 text-3xl " />
            나의 댓글
          </div>
          <FaChevronRight />
        </Link>
        <button
          onClick={() => {
            authAPI.logout();
            dispatch(logout());
            navigate("/");
          }}
          className="flex w-full items-center p-4 md:text-lg hover:text-y-red"
        >
          <IoLogOutOutline className="mr-2 text-3xl" />
          로그아웃
        </button>
      </div>
    </>
  );
}
