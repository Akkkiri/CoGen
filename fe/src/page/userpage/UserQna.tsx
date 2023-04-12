import { useState, useEffect } from "react";
import { QnaElement } from "page/mypage/MyQna";
import axios from "api/axios";
import QnaContainer from "components/QnaContainer";

export default function UserQna({ userID }: { userID: number }) {
  const [userQnaList, setUserQnaList] = useState<QnaElement[]>([]);
  useEffect(() => {
    axios
      .get(`/users/${userID}/qna`)
      .then((res) => {
        setUserQnaList(res.data);
      })
      .catch((err) => console.log(err));
  }, [userID]);
  return (
    <div>
      <ul>
        {userQnaList.map((el, idx) => {
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
