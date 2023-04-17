import axios from "api/axios";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import Pagenation from "components/Pagenation";
import Friend, { FriendProps } from "components/user/Friend";
import Empty from "components/Empty";
import BackBtn from "components/BackBtn";

export default function UserSearch() {
  const location = useLocation();
  const query = location.state.query;
  const mode = location.state.mode;
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [friendsList, setFriendsList] = useState<FriendProps[]>([]);

  useEffect(() => {
    if (mode === "user") {
      axios
        .get(`/search/user?query=${encodeURIComponent(query)}&page=${page}`)
        .then((res) => {
          console.log(res);
          setFriendsList(res.data.data);
          setTotalPages(res.data.pageInfo.totalPages);
        })
        .catch((err) => console.log(err));
    }
  }, [mode, query, page]);

  return (
    <>
      <BackBtn />
      <h1 className="page-title">"{query}" 검색 결과</h1>
      <ul className="max-w-2xl m-auto">
        {friendsList.length === 0 ? (
          <Empty str={"유저가"} />
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
    </>
  );
}
