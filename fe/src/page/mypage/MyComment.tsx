import BackBtn from "components/BackBtn";
import { useState, useEffect } from "react";
import axios from "api/axios";
import CommentContainer from "components/CommentContainer";
import Pagenation from "components/Pagenation";
import Empty from "components/Empty";

interface CommentElement {
  userid: number;
  commentId: number;
  feedId: number;
  nickname: string;
  profileImage: string;
  thumbnailPath: string;
  body: string;
  createdAt: string;
  modifiedAt: string;
  likeCount: number;
}

export default function MyComment() {
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [myCommentsList, setMyCommentsList] = useState<CommentElement[]>([]);
  const deleteComment = (commentId: number) => {
    axios
      .delete(`/comments/${commentId}/delete`)
      .then(() => {
        if (myCommentsList !== null) {
          const filtered = myCommentsList.filter((el) => {
            // console.log(el.answerId);
            return el.commentId !== commentId;
          });
          setMyCommentsList(filtered);
        }
      })
      .catch((err) => console.log(err));
  };
  useEffect(() => {
    axios
      .get(`/mypage/mycomments?page=${page}`)
      .then((res) => {
        setMyCommentsList(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [page]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">나의 댓글</h1>
      <div className="mt-2 p-3">
        {myCommentsList.length === 0 ? (
          <Empty str={"댓글이"} />
        ) : (
          myCommentsList.map((el) => {
            return (
              <ul key={el.commentId}>
                <CommentContainer
                  isLiked={true}
                  contents={el.body}
                  nickname={el.nickname}
                  profileImage={el.profileImage}
                  date={el.modifiedAt}
                  like={el.likeCount}
                  userid={el.userid}
                  commentId={el.commentId}
                  deleteComment={deleteComment}
                />
              </ul>
            );
          })
        )}
      </div>
      <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
    </div>
  );
}
