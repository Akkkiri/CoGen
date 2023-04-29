import BackBtn from "components/BackBtn";
import { useEffect, useState } from "react";
import PostContainer from "components/PostContainer";
import Pagenation from "components/Pagenation";
import axios from "api/axios";

export default function MyBookmark() {
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [postProps, setPostProps] = useState<any>();

  useEffect(() => {
    axios
      .get(`/mypage/bookmarks?page=${page}`)
      .then((res) => {
        setPostProps(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [page]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">내가 저장한 글</h1>
      <div className="p-2">
        <PostContainer postContainerProps={postProps} bookmark={true} />
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </div>
  );
}
