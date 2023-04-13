import SelectBox from "../../components/SelectBox";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Pagenation from "../../components/Pagenation";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import PostDetailContainer from "../../components/PostDetailContainer";
import CommentContainer from "../../components/CommentContainer";
import axios from "../../api/axios";
import SmallInput from "../../components/Inputs/SmallInput";
import CloseBtn from "../../components/Layout/CloseBtn";
import { isLogin } from "../../store/modules/authSlice";
import { useAppSelector } from "../../store/hook";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { id } from "../../store/modules/authSlice";
export default function PostDetail() {
  const { PostId } = useParams();
  const [comment, setComment] = useState<Select>("new");
  const [title, setTitle] = useState<string>("");
  const [postContents, setPostContents] = useState<string>("");
  const [tag, setTag] = useState<string>("");
  const [PostNickname, setPostNickName] = useState<string>("");
  const [postProfileImage, setPostProfileImage] = useState<string>("");
  const [viwe, SetView] = useState<number>(0);
  const [postDate, setPostDate] = useState<string>("");
  const [postComments, setPostComments] = useState([]);
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [inputState, setInputState] = useState<string>("");

  const [isMine, setIsMine] = useState(false);
  const userid = useAppSelector(id);

  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();
  useEffect(() => {
    axios.get(`/feeds/${PostId}`).then((response) => {
      // console.log(response.data);
      setTitle(response.data.title);
      setPostContents(response.data.body);
      setTag(response.data.category);
      setPostNickName(response.data.userInfo.nickname);
      setPostProfileImage(response.data.userInfo.profileImage);
      SetView(response.data.viewCount);
      setPostDate(response.data.createdAt);
      if (response.data.userInfo.id === userid) {
        setIsMine(true);
      }
    });
  }, [PostId, page, userid]);
  useEffect(() => {
    axios
      .get(`/feeds/${PostId}/comments?sort=${comment}&page=${page}`)
      .then((response) => {
        //제거
        console.log(response.data);
        setPostComments(response.data.data);
        setTotalPages(response.data.pageInfo.totalPages);
      });
  }, [PostId, page, comment]);
  const postComment = () => {
    const reqBody = { body: inputState };
    axios
      .post(`/feeds/${PostId}/comments/add`, reqBody)
      .then(() => window.location.reload())
      .catch((err) => console.log(err));
  };
  return (
    <>
      <div className="p-3 border-b border-y-lightGray">
        <CloseBtn />
        <h1 className="text-center text-xl">게시판</h1>
      </div>

      <div>
        <PostDetailContainer
          title={title}
          contents={postContents}
          tag={tag}
          nickname={PostNickname}
          profileImage={postProfileImage}
          date={postDate}
          view={viwe}
          isMine={isMine}
        />
        <div className="p-2">
          <SmallInput
            inputState={inputState}
            setInputState={setInputState}
            placeholder={"댓글을 작성해주세요."}
            postFunc={
              isLoginUser
                ? postComment
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
          <SelectBox setSelect={setComment} type={"comment"} />
          {postComments.map((el: any) => (
            <div key={el.commentId}>
              <CommentContainer
                contents={el.body}
                nickname={el.userInfo.nickname}
                profileImage={el.userInfo.profileImage}
                date={el.modifiedAt}
                like={el.likeCount}
                userid={el.userInfo.id}
                commentId={el.commentId}
              />
            </div>
          ))}

          <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
        </div>
      </div>
    </>
  );
}
