import { useState } from "react";
import CommentContainer from "../components/CommentContainer";
import BigInput from "../components/Inputs/BigInput";
import SmallInput from "../components/Inputs/SmallInput";
import MainPostContainer from "../components/MainPostContainer";
import Pagenation from "../components/Pagenation";
import PostContainer from "../components/PostContainer";
import QnaContainer from "../components/QnaContainer";
import SelectBox from "../components/SelectBox";
import Level from "../components/user/Level";
import UserInfo from "../components/user/UserInfo";
import UserProfile from "../components/user/UserProfile";
import { Select } from "../util/SelectUtil";

export default function Home() {
  const [sort, setSort] = useState<Select>("new");
  const [category, setCategory] = useState<Select>("WORRY");
  const [page, setPage] = useState<number>(7);
  const [content, setContent] = useState<string>("");
  const [acontent, setaContent] = useState<string>("");
  const dd = () => {};
  return (
    <>
      <h1>홈입니다.</h1>
      <div className="flex">
        <button className="flex-1 btn-g hover:bg-gray-500">취소하기</button>
        <button className="flex-1 btn-r hover:bg-red-700">등록하기</button>
      </div>
      <button className="btn-r">일반 버튼1</button>
      <button className="btn-g">일반 버튼2</button>
      <Level level={2} point={33} />
      <UserInfo
        nickname={"닉네임"}
        profileImage={"/images/user.png"}
        date={"2023.03.22"}
      />
      <UserProfile
        nickname={"닉네임이열글자가넘으"}
        profileImage={"/images/user.png"}
        level={1}
        point={33}
        friendsNum={10}
        // isMine={true}
      />
      <QnaContainer
        question={"살면서 가장 감명 깊게 본 영화를 세 개 추천해 주세요"}
        answer={[
          "헤어질결심, 지구 최후의 밤, 서유기 선리기연",
          "벼랑 위의 포뇨, 마녀 배달부 키키, 센과 치히로의 행방불명",
        ]}
        idx={1}
      />
      <QnaContainer
        question={"살면서 가장 감명 깊게 본 영화를 세 개 추천해 주세요"}
        answer={[
          "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ",
        ]}
        idx={2}
      />
      <BigInput
        placeholder="세글자 이상 적어주세요"
        inputState={content}
        setInputState={setContent}
      />
      <SmallInput
        placeholder="세글자 이상 적어주세요"
        inputState={acontent}
        setInputState={setaContent}
        postFunc={dd}
      />
      <MainPostContainer
        title={"내일 점심에 뭘 먹는게 좋을까요?"}
        contents={
          "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ"
        }
        idx={2}
      />
      <MainPostContainer
        title={"내일 점심에 뭘 먹는게 좋을까요?"}
        contents={
          "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ"
        }
        idx={1}
      />
      <PostContainer
        title={"내일 점심에 뭘 먹는게 좋을까요?"}
        contents={
          "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ"
        }
        tag={"고민"}
      />
      <CommentContainer
        contents={
          "본문은 이렇게 길게 쓸 수도 있으니까요! 내일 점심에 다들 맛있는거 드시길 바라요! 저는 개인적으로 김치볶음밥이 먹고싶은데 과연 엄마와 마음이 통할지 모르겠어요!! 우리 이거 다 먹고살자고 하는 일이니까 부디 밥 꼭 챙겨드시길 바라요. 내일 만나요!ㅎㅎ"
        }
      />
      <SelectBox setSelect={setSort} type={"sort"} />
      <SelectBox setSelect={setCategory} type={"category"} />
      <Pagenation page={page} setPage={setPage} totalPages={8} />
    </>
  );
}
