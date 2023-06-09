import SmallInput from "../../components/Inputs/SmallInput";
import { useState, useEffect } from "react";
import SelectBox from "../../components/SelectBox";
import { Select } from "../../util/SelectUtil";
import QuestionCommentContainer, {
  CommentContainerProps,
} from "../../components/QuestionCommentContainer";
import Pagenation from "../../components/Pagenation";
import axios from "../../api/axios";
import { isLogin } from "../../store/modules/authSlice";
import { useAppSelector } from "../../store/hook";
import Swal from "sweetalert2";
import { useNavigate, NavLink } from "react-router-dom";
import { TiArrowBack } from "react-icons/ti";
export default function Question() {
  const [weeklyQuestions, SetWeeklyQuestions] = useState<string>("");
  const [questionId, setQuestionId] = useState<number>();
  const [questComment, setQuestComment] = useState<
    CommentContainerProps[] | null
  >(null);
  const [inputState, setInputState] = useState<string>("");
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [img, setImg] = useState<string>("");
  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();
  const postQuestion = () => {
    if (inputState !== "") {
      const reqBody = { answerBody: inputState };
      axios
        .post(`/questions/${questionId}/answer/add`, reqBody)
        .then((response) => {
          if (questComment === null) {
            setQuestComment([response.data]);
          } else {
            setQuestComment([response.data, ...questComment]);
          }
          setInputState("");
        })
        // .then(() => window.location.reload())
        .catch((err) => console.log(err));
    }
  };

  useEffect(() => {
    axios.get(`/questions/weekly`).then((response) => {
      setImg(response.data.imagePath);
      SetWeeklyQuestions(response.data.content);
      setQuestionId(response.data.questionId);
    });
  }, []);
  useEffect(() => {
    if (questionId !== undefined) {
      axios
        .get(
          `questions/${questionId}/answer/list?sort=${sort}&page=
      ${page}`
        )
        .then((response) => {
          setQuestComment(response.data.data);
          setTotalPages(response.data.pageInfo.totalPages);
        })
        .catch((err) => console.log(err));
    }
  }, [page, questionId, sort]);

  const deleteAnswer = (answerId: number) => {
    axios
      .delete(`/answers/${answerId}/delete`)
      .then(() => {
        if (questComment !== null) {
          const filtered = questComment.filter((el) => {
            return el.answerId !== answerId;
          });
          setQuestComment(filtered);
        }
      })
      .catch((err) => console.log(err));
  };
  return (
    <div className="mb-16">
      <h1 className="text-center text-xl md:text-2xl  p-3 border-b border-y-lightGray">
        함께 이해해요
      </h1>
      <div className="py-6 text-center border-b border-y-lightGray p-2 ">
        <div className="text-lg md:text-2xl whitespace-pre-line">
          "{weeklyQuestions}"
        </div>
        {img ? (
          <div>
            <img
              src={img}
              alt="questionimg"
              width={300}
              height={300}
              className="m-auto w-auto h-72"
            />
          </div>
        ) : null}
        <SmallInput
          inputState={inputState}
          setInputState={setInputState}
          placeholder={"답변을 작성해주세요."}
          postFunc={
            isLoginUser
              ? postQuestion
              : () => {
                  Swal.fire({
                    title: "CoGen",
                    text: "로그인이 필요한 서비스 입니다.",
                    showCancelButton: true,
                    confirmButtonColor: "#E74D47",
                    cancelButtonColor: "#A7A7A7",
                    confirmButtonText: "로그인",
                    cancelButtonText: "취소",
                  }).then((result) => {
                    if (result.isConfirmed) {
                      navigate("/login");
                    }
                  });
                }
          }
        />
      </div>
      <div className="p-2">
        <SelectBox setSelect={setSort} type={"comment"} />

        {questComment === null
          ? null
          : questComment.map((el: any) => (
              <div key={el.answerId}>
                <QuestionCommentContainer
                  ageType={el.ageType}
                  genderType={el.genderType}
                  contents={el.answerBody}
                  nickname={el.nickname}
                  profileImage={el.profileImage}
                  date={el.modifiedAt}
                  like={el.likeCount}
                  userid={el.userId}
                  answerId={el.answerId}
                  isLiked={el.isLiked}
                  deleteAnswer={deleteAnswer}
                />
              </div>
            ))}
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
        <div className="relative m-1">
          <div className="fixed bottom-[70px]">
            <NavLink to={"/question/all"}>
              <button className="btn-r shadow-xl flex items-center justify-center">
                <TiArrowBack className="mr-1 text-xl" />
                지나간 질문
              </button>
            </NavLink>
          </div>
        </div>
      </div>
    </div>
  );
}
