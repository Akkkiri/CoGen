// import QuestionCommentContainer, {
//   CommentContainerProps,
// } from "../../components/QuestionCommentContainer";
import Pagenation from "../../components/Pagenation";
import { Select } from "../../util/SelectUtil";
import axios from "../../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import SelectBox from "../../components/SelectBox";
import CloseBtn from "../../components/Layout/CloseBtn";
import GoneComment, { CommentContainerProps } from "components/GoneComment";
export default function GoneDetail() {
  const { QuestionId } = useParams();
  const navigate = useNavigate();
  const [content, setContent] = useState<string>("");
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [img, setImg] = useState<string>("");
  const [questComment, setQuestComment] = useState<
    CommentContainerProps[] | null
  >(null);
  useEffect(() => {
    axios
      .get(`/questions/${QuestionId}`)
      .then((response) => {
        if (response.data.length === 0) {
          navigate("/404");
        }
        setContent(response.data.content);
        setImg(response.data.imagePath);
      })
      .catch((err) => {
        if (
          err.response.data.status === 404 ||
          err.response.data.status === 500
        ) {
          navigate("/404");
        }
      });
  }, [QuestionId, sort, navigate]);
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
        <h1 className="text-center text-xl md:text-2xl">지나간 질문</h1>
      </div>
      <div className="py-6 text-center border-b border-y-lightGray p-2">
        <div className="text-lg pb-3 md:text-2xl">"{content}"</div>
        {img ? (
          <div>
            <img
              src={img}
              alt="questionimg"
              width={300}
              height={300}
              className="m-auto w-auto h-72 md:h-96"
            />
          </div>
        ) : null}
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
                  isLiked={el.isLiked}
                />
              </div>
            ))}
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </>
  );
}
