import BackBtn from "components/BackBtn";
import Pagenation from "components/Pagenation";
import { useEffect, useState } from "react";
import Friend, { FriendProps } from "components/user/Friend";
import axios from "api/axios";
import Empty from "components/Empty";

export default function UserFriend() {
  const userID = Number(window.location.pathname.replace(/[^0-9]/g, ""));
  const [userName, setUserName] = useState("");
  const [friendsList, setFriendsList] = useState<FriendProps[]>([]);
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);

  useEffect(() => {
    axios
      .get(`/follows/${userID}/followings?page=${page}`)
      .then((res) => {
        setFriendsList(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
        setUserName(res.data.pageInfo.nickname);
      })
      .catch((err) => console.log(err));
  }, [userID, page]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">{userName}님의 친구</h1>
      <ul className="max-w-2xl m-auto">
        {friendsList.length === 0 ? (
          <Empty str={"친구가"} />
        ) : (
          friendsList.map((el) => {
            return (
              <li
                key={el.hashcode}
                className="border-b border-y-lightGray/50 px-2 py-4"
              >
                <Friend {...el} />
              </li>
            );
          })
        )}
      </ul>
      <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
    </div>
  );
}
