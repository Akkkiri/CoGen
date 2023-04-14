import BackBtn from "components/BackBtn";
import Pagenation from "components/Pagenation";
import { useState, useEffect } from "react";
import axios from "api/axios";
import QnaContainer from "components/QnaContainer";
import Empty from "components/Empty";
import { Link } from "react-router-dom";

export interface QuestionElement {
  questionId: number;
  content: string;
  answerList: {
    answerBody: string[];
  };
}

export default function MyQuestion() {
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [myQuestionList, setMyQuestionList] = useState<QuestionElement[]>([]);

  useEffect(() => {
    axios
      .get(`/mypage/myquestions?page=${page}`)
      .then((res) => {
        setMyQuestionList(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [page]);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">내가 답한 질문</h1>
      <ul className="flex flex-col gap-3 m-4">
        {myQuestionList.length === 0 ? (
          <Empty str={"등록된 답변이"} />
        ) : (
          myQuestionList.map((el, idx) => {
            return (
              <li key={el.questionId}>
                <Link to={`/question/${el.questionId}`}>
                  <QnaContainer
                    question={el.content}
                    answer={el.answerList.answerBody}
                    idx={idx}
                  />
                </Link>
              </li>
            );
          })
        )}
      </ul>
      <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
    </div>
  );
}
