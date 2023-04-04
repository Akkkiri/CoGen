import SmallInput from "../components/Inputs/SmallInput";
import { useState, useEffect } from "react";
import SelectBox from "../components/SelectBox";
import { Select } from "../util/SelectUtil";
import CommentContainer from "../components/CommentContainer";
import Pagenation from "../components/Pagenation";
import axios from "../api/axios";
export default function Question() {
  const [weeklyQuestions, SetWeeklyQuestions] = useState<string>("");
  const [questionId, setQuestionId] = useState<number>(0);
  const [questComment, setQuestComment] = useState([]);
  const [inputState, setInputState] = useState<string>("");
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const postQuestion = () => {
    const reqBody = { content: inputState };
    axios
      .post(`/questions/${questionId}/answer/add`, reqBody)
      .then((response) => {})
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    axios.get(`/questions/weekly`).then((response) => {
      SetWeeklyQuestions(response.data.content);
      setQuestionId(response.data.questionId);
    });
  }, []);
  useEffect(() => {
    axios
      .get(
        `questions/${questionId}/answer/list?sort=${sort}&page=
      ${page}`
      )
      .then((response) => {
        setQuestComment(response.data.data);
        setTotalPages(response.data.pageInfo.totalPages);
      });
  }, [page, questionId, sort]);

  return (
    <>
      <h1 className="text-center text-xl p-3 border-b border-y-lightGray">
        이번주 질문
      </h1>
      <div className="py-6 text-center border-b border-y-lightGray p-2">
        <div className="text-lg">"{weeklyQuestions}"</div>
        <SmallInput
          inputState={inputState}
          setInputState={setInputState}
          placeholder={"답변을 작성해주세요."}
          postFunc={postQuestion}
        />
      </div>
      <div className="p-2">
        <SelectBox setSelect={setSort} type={"comment"} />
        {questComment.map((el: any) => (
          <div key={el.answerId}>
            <CommentContainer
              contents={el.answerBody}
              nickname={el.userNickname}
              profileImage={el.profileImage}
              date={el.modifiedAt}
              like={el.likeCount}
            />
          </div>
        ))}
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </>
  );
}
