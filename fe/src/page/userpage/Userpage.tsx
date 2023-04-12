import axios from "api/axios";
import BackBtn from "components/BackBtn";
import UserProfile, { UserProfileProps } from "components/user/UserProfile";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "store/hook";
import { id } from "store/modules/authSlice";
import UserPost from "./UserPost";
import UserQna from "./UserQna";
import UserQuestion from "./UserQuestion";

export default function Userpage() {
  const userID = Number(window.location.pathname.replace(/[^0-9]/g, ""));
  const ID = useAppSelector(id);
  const navigate = useNavigate();

  useEffect(() => {
    if (ID === userID) {
      navigate("/mypage");
    }
  }, [ID, userID, navigate]);

  const [userInfo, setUserInfo] = useState<UserProfileProps>({
    nickname: "",
    hashcode: "",
    profileImage: "",
    level: 0,
    ariFactor: 0,
    friendsNum: 0,
    isMine: false,
  });
  const [curTab, setCurTab] = useState(0);
  const tabMenu = [
    { name: "문답", content: <UserQna userID={userID} /> },
    { name: "질문", content: <UserQuestion userID={userID} /> },
    { name: "게시글", content: <UserPost userID={userID} /> },
  ];

  useEffect(() => {
    axios
      .get(`/users/${userID}`)
      .then((res) => {
        const obj = {
          userID: userID,
          nickname: res.data.nickname,
          hashcode: res.data.hashcode,
          profileImage: res.data.profileImage,
          level: res.data.level,
          ariFactor: res.data.ariFactor,
          friendsNum: res.data.friendsNum,
          isMine: false,
        };
        setUserInfo(obj);
      })
      .catch((err) => console.log(err));
  }, [userID]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">{userInfo.nickname}님의 페이지</h1>
      <UserProfile {...userInfo} />
      <div className="flex mt-4 border-t-4 border-y-lightGray/30 py-3 border-b">
        {tabMenu.map((el, idx) => {
          return (
            <div
              key={idx}
              onClick={() => {
                setCurTab(idx);
              }}
              className={`flex-1 text-center ${
                curTab === idx ? "text-y-red" : "text-y-lightGray"
              }`}
            >
              {el.name}
            </div>
          );
        })}
      </div>
      {tabMenu[curTab].content}
    </div>
  );
}
