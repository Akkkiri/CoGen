import SelectBox from "../../components/SelectBox";
import { useState, useEffect } from "react";
import { IoMdClose, IoMdShare } from "react-icons/io";
import { useParams, useNavigate } from "react-router-dom";
import Pagenation from "../../components/Pagenation";
import { Select } from "../../util/SelectUtil";
import PostDetailContainer from "../../components/PostDetailContainer";
import CommentContainer, {
  CommentContainerProps,
} from "../../components/CommentContainer";
import axios from "../../api/axios";
import SmallInput from "../../components/Inputs/SmallInput";
import { isLogin } from "../../store/modules/authSlice";
import { useAppSelector } from "../../store/hook";
import Swal from "sweetalert2";
import { myid } from "../../store/modules/authSlice";
import { IoChatbubbleEllipsesOutline } from "react-icons/io5";
import LikeBtn from "components/LikeBtn";
import Empty from "components/Empty";
import BookMarkBtn from "components/BookMark";
import ShareModal from "components/Share/ShareModal";
import Loading from "components/Loading";

export default function PostDetail() {
  const { PostId } = useParams();
  const [sort, setSort] = useState<Select>("new");
  const [title, setTitle] = useState<string>("");
  const [postContents, setPostContents] = useState<string>("");
  const [tag, setTag] = useState<string>("");
  const [PostNickname, setPostNickName] = useState<string>("");
  const [postProfileImage, setPostProfileImage] = useState<string>("");
  const [viwe, SetView] = useState<number>(0);
  const [postDate, setPostDate] = useState<string>("");
  const [postComments, setPostComments] = useState<
    CommentContainerProps[] | null
  >(null);
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [inputState, setInputState] = useState<string>("");
  const [commentCount, setCommentCount] = useState<number>(0);
  const [isLike, setIsLike] = useState<boolean>(false);
  const [userId, setUserID] = useState<number>(0);
  const [likeCounts, setLikeCounts] = useState<number>(0);
  const [savePost, setSavePost] = useState<boolean>(false);
  const [isMine, setIsMine] = useState(false);
  const [image, setImage] = useState<string>("");
  const [image2, setImage2] = useState<string>("");
  const [image3, setImage3] = useState<string>("");
  const [modalOpen, setModalOpen] = useState(false);
  const userid = useAppSelector(myid);
  const isLoginUser = useAppSelector(isLogin);
  const navigate = useNavigate();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  useEffect(() => {
    axios
      .get(`/feeds/${PostId}`)
      .then((response) => {
        setTitle(response.data.title);
        setPostContents(response.data.body);
        setTag(response.data.category);
        setPostNickName(response.data.userInfo.nickname);
        setPostProfileImage(response.data.userInfo.profileImage);
        setUserID(response.data.userInfo.id);
        SetView(response.data.viewCount);
        setPostDate(response.data.createdAt);
        setCommentCount(response.data.commentCount);
        setLikeCounts(response.data.likeCount);
        setIsLike(response.data.isLiked);
        setSavePost(response.data.isSavedFeed);
        setImage(response.data.imagePath);
        setImage2(response.data.imagePath2);
        setImage3(response.data.imagePath3);
      })
      .catch((err) => {
        if (
          err.response.data.status === 404 ||
          err.response.data.status === 500
        ) {
          navigate("/404");
        }
      });
  }, [PostId, navigate]);

  useEffect(() => {
    if (PostId !== undefined) {
      axios
        .get(`/feeds/${PostId}/comments?sort=${sort}&page=${page}`)
        .then((response) => {
          setPostComments(response.data.data);
          setTotalPages(response.data.pageInfo.totalPages);
        });
    }
  }, [PostId, page, sort]);

  const postComment = () => {
    const reqBody = { body: inputState };
    axios
      .post(`/feeds/${PostId}/comments/add`, reqBody)
      .then((response) => {
        if (postComments === null) {
          setPostComments([response.data]);
          setCommentCount(commentCount + 1);
        } else {
          setPostComments([response.data, ...postComments]);
          setCommentCount(commentCount + 1);
        }

        setInputState("");
      })
      .catch((err) => console.log(err));
  };
  const LikePost = () => {
    axios
      .patch(`/feeds/${PostId}/like`)
      .then(() => {
        setIsLike(!isLike);
        if (isLike) {
          setLikeCounts(likeCounts - 1);
        } else {
          setLikeCounts(likeCounts + 1);
        }
      })
      .catch((err) => console.log(err));
  };
  const SavePost = () => {
    axios
      .patch(`/feeds/${PostId}/save`)
      .then(() => {
        setSavePost(!savePost);
      })
      .catch((err) => console.log(err));
  };
  const deleteComment = (commentId: number) => {
    axios
      .delete(`/comments/${commentId}/delete`)
      .then(() => {
        if (postComments !== null) {
          const filtered = postComments.filter((el) => {
            return el.commentId !== commentId;
          });
          setPostComments(filtered);
          setCommentCount(commentCount - 1);
        }
      })
      .catch((err) => console.log(err));
  };
  const showModal = () => {
    setModalOpen(true);
  };
  return (
    <>
      <div className="p-3 border-b border-y-lightGray">
        <IoMdClose
          onClick={() => navigate(-1)}
          className="w-6 h-6 cursor-pointer absolute "
        />
        <h1 className="text-center text-xl">함께 나눠요</h1>
      </div>

      <div className="w-full relative">
        <PostDetailContainer
          title={title}
          contents={postContents}
          tag={tag}
          nickname={PostNickname}
          profileImage={postProfileImage}
          date={postDate}
          view={viwe}
          userId={userId}
          userid={userid}
          isMine={isMine}
          image={image}
          image2={image2}
          image3={image3}
        />

        <div className="flex p-4 text-sm justify-between border-b border-y-lightGray">
          <LikeBtn
            onClick={
              isLoginUser
                ? LikePost
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
            likeCount={likeCounts}
            isLike={isLike}
          />
          <BookMarkBtn
            onClick={
              isLoginUser
                ? SavePost
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
            isSavedFeed={savePost}
          />
          <div className="flex" onClick={showModal}>
            <IoMdShare className="self-center text-lg md:text-2xl" />
            <span className="self-center md:text-base">공유하기</span>
          </div>
        </div>
        {modalOpen ? (
          <ShareModal
            setModalOpen={setModalOpen}
            title={title}
            img={image}
            contents={postContents}
          />
        ) : null}
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
          <div className="flex justify-between">
            <SelectBox setSelect={setSort} type={"comment"} />

            <div className="flex self-center mr-2 gap-1">
              <IoChatbubbleEllipsesOutline className="text-lg self-center" />
              댓글 {commentCount}
            </div>
          </div>
          {postComments === null ? (
            <Empty str={"댓글이"} />
          ) : (
            <>
              {postComments.map((el: any) => (
                <div key={el.commentId}>
                  <CommentContainer
                    isLiked={el.isLiked}
                    contents={el.body}
                    nickname={el.userInfo.nickname}
                    profileImage={el.userInfo.profileImage}
                    date={el.modifiedAt}
                    like={el.likeCount}
                    userid={el.userInfo.id}
                    commentId={el.commentId}
                    deleteComment={deleteComment}
                  />
                </div>
              ))}
            </>
          )}
          <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
        </div>
      </div>
    </>
  );
}
