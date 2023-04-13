import axios from "api/axios";
import BackBtn from "components/BackBtn";
import QnaContainer from "components/QnaContainer";
import { useState, useEffect } from "react";

export interface QnaElement {
  qnaId: number;
  content: string;
  answerBody: string;
}

export default function MyQna() {
  const [myQnaList, setMyQnaList] = useState<QnaElement[]>([]);

  useEffect(() => {
    axios
      .get("/mypage/myqna")
      .then((res) => {
        setMyQnaList(res.data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      <BackBtn />
      <h1 className="page-title">나의 문답</h1>
      <ul className="flex flex-col gap-3 m-4">
        {myQnaList.map((el, idx) => {
          return (
            <li key={el.qnaId}>
              <QnaContainer
                question={el.content}
                answer={el.answerBody}
                idx={idx}
              />
            </li>
          );
        })}
      </ul>
    </div>
  );
}
