import BackBtn from "components/BackBtn";
import Pagenation from "components/Pagenation";
import { useEffect, useState } from "react";
import Friend, { FriendProps } from "components/user/Friend";
import axios from "api/axios";
import { useAppSelector } from "store/hook";
import { id } from "store/modules/authSlice";
import Empty from "components/Empty";

export default function MyFriend() {
  const ID = useAppSelector(id);
  const [friendsList, setFriendsList] = useState<FriendProps[]>([]);
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);

  useEffect(() => {
    axios
      .get(`/follows/${ID}/followings?page=${page}`)
      .then((res) => {
        setFriendsList(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [ID, page]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">나의 친구</h1>
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
