import { useAppSelector } from "store/hook";
import { isLogin } from "store/modules/authSlice";
import { NavLink } from "react-router-dom";
import { useEffect, useState } from "react";
import { AiOutlineCloseCircle } from "react-icons/ai";
import { VscBellSlash } from "react-icons/vsc";
import axios from "api/axios";
import NotifyContainer, {
  NotifyContainerProps,
} from "components/user/NotifyContainer";

export default function NotifyModal({
  setIsNotifying,
}: {
  setIsNotifying: React.Dispatch<React.SetStateAction<boolean>>;
}) {
  const isLoginUser = useAppSelector(isLogin);
  const [notifyList, setNotifyList] = useState<NotifyContainerProps[]>([]);

  useEffect(() => {
    if (isLoginUser) {
      axios
        .get("/notifications")
        .then((res) => {
          // console.log(res);
          setNotifyList(res.data.data);
        })
        .catch((err) => console.log(err));
    }
  }, [isLoginUser]);

  const sample: NotifyContainerProps[] = [
    {
      notificationId: 1,
      type: "LIKE",
      receiverBody: "누구누구씨가 누구님의 게시글에 좋아요를 눌렀습니다",
      isRead: false,
      createdAt: "2023-04-14T21:19:05.228188",
    },
    {
      notificationId: 2,
      type: "COMMENT",
      receiverBody: "누구누구씨가 누구님의 게시글에 댓글을 남겼습니다",
      isRead: true,
      createdAt: "2023-04-14T21:19:05.228188",
    },
    {
      notificationId: 3,
      type: "FOLLOW",
      receiverBody: "누구누구씨가 누구님과 친구하기를 눌렀습니다",
      isRead: false,
      createdAt: "2023-04-14T21:19:05.228188",
    },
    {
      notificationId: 4,
      type: "LIKE",
      receiverBody: "누구누구씨가 누구님의 게시글에 좋아요를 눌렀습니다",
      isRead: false,
      createdAt: "2023-04-14T21:19:05.228188",
    },
    {
      notificationId: 5,
      type: "COMMENT",
      receiverBody: "누구누구씨가 누구님의 게시글에 댓글을 남겼습니다",
      isRead: true,
      createdAt: "2023-04-14T21:19:05.228188",
    },
    {
      notificationId: 6,
      type: "FOLLOW",
      receiverBody: "누구누구씨가 누구님과 친구하기를 눌렀습니다",
      isRead: false,
      createdAt: "2023-04-14T21:19:05.228188",
    },
  ];

  return (
    <div className="relative">
      <div className="absolute top-8 right-0 w-80 bg-white rounded-lg z-50">
        {isLoginUser ? (
          <div className="flex flex-col">
            <div className="flex justify-between items-center border-b pb-2 border-y-lightGray/30 p-2">
              <h1 className="text-y-red">나의 알림</h1>
              <button onClick={() => setIsNotifying(false)}>
                <AiOutlineCloseCircle className="text-xl text-y-lightGray" />
              </button>
            </div>
            <div className="flex justify-end mt-1 pb-1 text-xs font-light border-b border-y-lightGray/30">
              <button className="text-y-lightGray hover:text-y-red mr-3">
                전체 삭제
              </button>
            </div>
            <div className="flex flex-col">
              {notifyList.length === 0 ? (
                <div className="text-y-lightGray flex flex-col justify-center items-center">
                  <VscBellSlash className="text-3xl" />
                  <p className="text-sm mt-1">알람이 없습니다</p>
                </div>
              ) : (
                <ul className="flex flex-col max-h-[400px] overflow-scroll cursor-pointer">
                  {notifyList.map((el) => {
                    return (
                      <div
                        key={el.notificationId}
                        className="hover:bg-y-pink rounded-lg"
                      >
                        <NotifyContainer {...el} />
                      </div>
                    );
                  })}
                </ul>
              )}
            </div>
          </div>
        ) : (
          <div className="flex flex-col justify-center items-center bg-white p-2 rounded-lg z-50">
            <h1 className="m-2">로그인 후 이용 가능한 서비스입니다</h1>
            <NavLink to="/login">
              <button
                className="btn-p rounded-lg px-4 py-2 text-white text-sm"
                onClick={() => setIsNotifying(false)}
              >
                로그인하러가기
              </button>
            </NavLink>
          </div>
        )}
      </div>
      <button
        className="inset-0 fixed cursor-default bg-black/50"
        onClick={() => setIsNotifying(false)}
      ></button>
    </div>
  );
}
