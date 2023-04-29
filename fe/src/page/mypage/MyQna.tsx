import axios from "api/axios";
import BackBtn from "components/BackBtn";
import QnaContainer from "components/QnaContainer";
import { useState, useEffect } from "react";
import { MdModeEdit } from "react-icons/md";
import MyQnaEdit from "./MyQnaEdit";

export interface QnaElement {
  qnaId: number;
  content: string;
  answerBody: string;
}

export default function MyQna() {
  const [myQnaList, setMyQnaList] = useState<QnaElement[]>([]);
  const [isEditMode, setIsEditMode] = useState(false);
  const [qnaData, setQnaData] = useState<string[]>([]);

  useEffect(() => {
    axios
      .get("/mypage/myqna")
      .then((res) => {
        setMyQnaList(res.data);
        const q1 = res.data.filter((el: QnaElement) => el.qnaId === 1);
        const q2 = res.data.filter((el: QnaElement) => el.qnaId === 2);
        const q3 = res.data.filter((el: QnaElement) => el.qnaId === 3);
        const q4 = res.data.filter((el: QnaElement) => el.qnaId === 4);
        const q5 = res.data.filter((el: QnaElement) => el.qnaId === 5);
        const q6 = res.data.filter((el: QnaElement) => el.qnaId === 6);
        const q7 = res.data.filter((el: QnaElement) => el.qnaId === 7);
        const q8 = res.data.filter((el: QnaElement) => el.qnaId === 8);
        const q9 = res.data.filter((el: QnaElement) => el.qnaId === 9);
        const q10 = res.data.filter((el: QnaElement) => el.qnaId === 10);
        const data = [
          q1.length === 0 ? "" : q1[0].answerBody,
          q2.length === 0 ? "" : q2[0].answerBody,
          q3.length === 0 ? "" : q3[0].answerBody,
          q4.length === 0 ? "" : q4[0].answerBody,
          q5.length === 0 ? "" : q5[0].answerBody,
          q6.length === 0 ? "" : q6[0].answerBody,
          q7.length === 0 ? "" : q7[0].answerBody,
          q8.length === 0 ? "" : q8[0].answerBody,
          q9.length === 0 ? "" : q9[0].answerBody,
          q10.length === 0 ? "" : q10[0].answerBody,
        ];
        setQnaData(data);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <>
      {isEditMode ? (
        <MyQnaEdit qnaData={qnaData} setIsEditMode={setIsEditMode} />
      ) : (
        <div>
          <BackBtn />
          <h1 className="page-title">나의 문답</h1>
          <div className="flex justify-end mr-4">
            <button
              onClick={() => {
                setIsEditMode(true);
              }}
            >
              <MdModeEdit className="text-y-red inline -mr-0.5" /> 수정
            </button>
          </div>
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
      )}
    </>
  );
}
