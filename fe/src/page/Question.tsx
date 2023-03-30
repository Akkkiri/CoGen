import SmallInput from "../components/Inputs/SmallInput";
import { useState, useEffect } from "react";
import SelectBox from "../components/SelectBox";
import { Select } from "../util/SelectUtil";
import CommentContainer from "../components/CommentContainer";
import Pagenation from "../components/Pagenation";
export default function Question() {
  const [inputState, setInputState] = useState<string>("");
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(7);
  const postQuestion = () => {};
  return (
    <>
      <h1 className="text-center text-xl p-3 border-b border-y-lightGray">
        이번주 질문
      </h1>
      <div className="py-6 text-center border-b border-y-lightGray">
        <div className="text-lg">"최근 가장 큰 고민은 무엇인가요?"</div>
        <SmallInput
          inputState={inputState}
          setInputState={setInputState}
          placeholder={"답변을 작성해주세요."}
          postFunc={postQuestion}
        />
      </div>
      <div className="p-2">
        <SelectBox setSelect={setSort} type={"sort"} />
        <CommentContainer contents="안녕" />
        <CommentContainer contents="안녕" />
        <CommentContainer contents="안녕" />
        <Pagenation page={page} setPage={setPage} totalPages={8} />
      </div>
    </>
  );
}
