import SelectBox from "../../components/SelectBox";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import Pagenation from "../../components/Pagenation";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import PostDetailContainer from "../../components/PostDetailContainer";
import CommentContainer from "../../components/CommentContainer";
import axios from "../../api/axios";
import SmallInput from "../../components/Inputs/SmallInput";
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
  useEffect(() => {
    axios.get(`/feeds/${PostId}`).then((response) => {
      setTitle(response.data.title);
      setPostContents(response.data.body);
      setTag(response.data.category);
      setPostNickName(response.data.userInfo.nickname);
      setPostProfileImage(response.data.userInfo.profileImage);
      SetView(response.data.viewCount);
      setPostDate(response.data.createdAt);
    });
  }, [PostId, page]);
  useEffect(() => {
    axios
      .get(`/feeds/${PostId}/comments?sort=${comment}&page=${page}`)
      .then((response) => {
        console.log(response.data);
        setPostComments(response.data.data);
        setTotalPages(response.data.pageInfo.totalPages);
      });
  }, [PostId, page, comment]);
  const postComment = () => {
    const reqBody = { content: inputState };
    axios
      .post(`/feeds/1/comments/add`, reqBody)
      .then((response) => {})
      .catch((err) => console.log(err));
  };
  return (
    <>
      <h1 className="text-center text-xl p-3 border-b border-y-lightGray">
        게시판
      </h1>
      <div>
        <PostDetailContainer
          title={title}
          contents={postContents}
          tag={tag}
          nickname={PostNickname}
          profileImage={postProfileImage}
          date={postDate}
          view={viwe}
        />
        <div className="p-2">
          <SmallInput
            inputState={inputState}
            setInputState={setInputState}
            placeholder={"답변을 작성해주세요."}
            postFunc={postComment}
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
              />
            </div>
          ))}

          <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
        </div>
      </div>
    </>
  );
}
