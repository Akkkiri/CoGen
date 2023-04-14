// import QuestionCommentContainer, {
//   CommentContainerProps,
// } from "../../components/QuestionCommentContainer";
import Pagenation from "../../components/Pagenation";
import { Select } from "../../util/SelectUtil";
import axios from "../../api/axios";
import Swal from "sweetalert2";
import { useNavigate, NavLink, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import SelectBox from "../../components/SelectBox";
import CloseBtn from "../../components/Layout/CloseBtn";
import GoneComment, { CommentContainerProps } from "components/GoneComment";
export default function GoneDetail() {
  const { QuestionId } = useParams();
  const [content, setContent] = useState<string>("");
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [questComment, setQuestComment] = useState<
    CommentContainerProps[] | null
  >(null);
  useEffect(() => {
    axios
      .get(`/questions/${QuestionId}`)
      .then((response) => {
        setContent(response.data.content);
        // console.log(response.data);
      })
      .catch((err) => console.log(err));
  }, [QuestionId, sort]);
  useEffect(() => {
    axios
      .get(
        `questions/${QuestionId}/answer/list?sort=${sort}&page=
      ${page}`
      )
      .then((res) => {
        setQuestComment(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [QuestionId, page, sort]);
  return (
    <>
      <div className="p-3 border-b border-y-lightGray">
        <CloseBtn />
        <h1 className="text-center text-xl">지나간 질문</h1>
      </div>
      <div className="py-6 text-center border-b border-y-lightGray p-2">
        <div className="text-lg">"{content}"</div>
      </div>
      <div className="p-2">
        <SelectBox setSelect={setSort} type={"comment"} />

        {questComment === null
          ? null
          : questComment.map((el: any) => (
              <div key={el.answerId}>
                <GoneComment
                  contents={el.answerBody}
                  nickname={el.nickname}
                  profileImage={el.profileImage}
                  date={el.modifiedAt}
                  like={el.likeCount}
                  userid={el.userId}
                  commentId={el.answerId}
                />
              </div>
            ))}
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </>
  );
}
