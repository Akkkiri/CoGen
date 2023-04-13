import Pagenation from "components/Pagenation";
import { useState, useEffect } from "react";
import axios from "api/axios";
import QnaContainer from "components/QnaContainer";
import { QuestionElement } from "page/mypage/MyQuestion";
import { Link } from "react-router-dom";
import Empty from "components/Empty";

export default function UserQuestion({ userID }: { userID: number }) {
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [userQuestionList, setUserQuestionList] = useState<QuestionElement[]>(
    []
  );
  useEffect(() => {
    axios
      .get(`/users/${userID}/questions?page=${page}`)
      .then((res) => {
        setUserQuestionList(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [userID, page]);
  return (
    <div>
      <ul className="flex flex-col gap-3 m-4">
        {userQuestionList.length === 0 ? (
          <Empty str={"등록된 답변이"} />
        ) : (
          userQuestionList.map((el, idx) => {
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
