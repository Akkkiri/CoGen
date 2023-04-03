import SelectBox from "../../components/SelectBox";
import { useState, useEffect } from "react";
import Pagenation from "../../components/Pagenation";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import PostDetailContainer from "../../components/PostDetailContainer";
import CommentContainer from "../../components/CommentContainer";
export default function PostDetail() {
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(7);

  return (
    <>
      <h1 className="text-center text-xl p-3 border-b border-y-lightGray">
        게시판
      </h1>
      <div>
        <PostDetailContainer
          title={"내일 점심에 뭘 먹는게 좋을까요?"}
          contents={
            "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ"
          }
          tag={"고민"}
        />
        <div className="p-2">
          <SelectBox setSelect={setSort} type={"sort"} />
          <CommentContainer contents="안녕" />
          <CommentContainer contents="안녕" />
          <CommentContainer contents="안녕" />
          <Pagenation page={page} setPage={setPage} totalPages={8} />
        </div>
      </div>
    </>
  );
}
