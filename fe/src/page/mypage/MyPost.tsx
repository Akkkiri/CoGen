import BackBtn from "components/BackBtn";
import { useEffect, useState } from "react";
import PostContainer from "components/PostContainer";
import Pagenation from "components/Pagenation";
import axios from "api/axios";
import { useAppSelector } from "store/hook";
import { id } from "store/modules/authSlice";

export default function MyPost() {
  const USERID = useAppSelector(id);
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [postProps, setPostProps] = useState<any>();

  useEffect(() => {
    axios
      .get(`/users/${USERID}/feeds?page=${page}`)
      .then((res) => {
        setPostProps(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [USERID, page]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">나의 게시글</h1>
      <div>
        <PostContainer postContainerProps={postProps} />
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </div>
  );
}
