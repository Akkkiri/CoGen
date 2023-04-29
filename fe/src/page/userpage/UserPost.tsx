import axios from "api/axios";
import { useEffect, useState } from "react";
import PostContainer from "components/PostContainer";
import Pagenation from "components/Pagenation";

export default function UserPost({ userID }: { userID: number }) {
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [postProps, setPostProps] = useState<any>();

  useEffect(() => {
    axios
      .get(`/users/${userID}/feeds?page=${page}`)
      .then((res) => {
        setPostProps(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [userID, page]);

  return (
    <div>
      <PostContainer postContainerProps={postProps} />
      <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
    </div>
  );
}
