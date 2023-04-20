import GoneQuestionContainer from "components/GoneQuestionContainer";
import axios from "api/axios";
import Pagenation from "../../components/Pagenation";
import { useState, useEffect } from "react";
import CloseBtn from "../../components/Layout/CloseBtn";
export default function GonQuestion() {
  const [questions, setQuestions] = useState([]);
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  useEffect(() => {
    axios
      .get(`/questions/list?page=${page}`)
      .then((res) => {
        // console.log(res.data);
        setQuestions(res.data.data);
        setTotalPages(res.data.pageInfo.totalPages);
      })
      .catch((err) => console.log(err));
  }, [page]);

  return (
    <>
      <div className="p-3 border-b border-y-lightGray">
        <CloseBtn />
        <h1 className="text-center text-xl md:text-2xl">지나간 질문</h1>
      </div>
      {questions.map((el: any, idx: number) => (
        <div key={idx}>
          <GoneQuestionContainer
            questionId={el.questionId}
            question={el.content}
            idx={idx}
          />
        </div>
      ))}
      <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
    </>
  );
}
