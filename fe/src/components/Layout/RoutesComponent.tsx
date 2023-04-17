import { Routes, Route } from "react-router-dom";
import Home from "page/Home";
import Login from "page/login/Login";
import MyBookmark from "page/mypage/MyBookmark";
import MyComment from "page/mypage/MyComment";
import MyEdit from "page/mypage/MyEdit";
import MyFriend from "page/mypage/MyFriend";
import Mypage from "page/mypage/Mypage";
import MyPost from "page/mypage/MyPost";
import MyQna from "page/mypage/MyQna";
import MyQuestion from "page/mypage/MyQuestion";
import Post from "page/post/Post";
import PostDetail from "page/post/PostDetail";
import Question from "page/Question/Question";
import Quiz from "page/Quiz";
import Info from "page/signup/Info";
import Nickname from "page/signup/Nickname";
import SelfQna from "page/signup/SelfQna";
import Signup from "page/signup/Signup";
import OauthLogin from "page/login/OauthLogin";
import Userpage from "page/userpage/Userpage";
import UserFriend from "page/userpage/UserFriend";
import IdentityVerification from "page/login/IdentityVerification";
import GonQuestion from "page/Question/GoneQuestion";
import Writepost from "page/post/Writepost";
import GoneDetail from "page/Question/GoneDetail";
import PostSearch from "page/search/PostSearch";
import UserSearch from "page/search/UserSearch";

export default function RoutesComponent() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/question" element={<Question />} />
      <Route path="/question/all" element={<GonQuestion />} />
      <Route path="/question/:QuestionId" element={<GoneDetail />} />
      <Route path="/post" element={<Post />} />
      <Route path="/post/:PostId" element={<PostDetail />} />
      <Route path="/writepost" element={<Writepost />} />
      <Route path="/quiz" element={<Quiz />} />
      <Route path="/mypage" element={<Mypage />} />
      <Route path="/login" element={<Login />} />
      <Route path="/oauth/:site" element={<OauthLogin />} />

      <Route path="/signup" element={<Signup />} />
      <Route path="/signup/nickname" element={<Nickname />} />
      <Route path="/signup/info" element={<Info />} />
      <Route path="/signup/qna" element={<SelfQna />} />

      <Route path="/mypage/qna" element={<MyQna />} />
      <Route path="/mypage/question" element={<MyQuestion />} />
      <Route path="/mypage/bookmark" element={<MyBookmark />} />
      <Route path="/mypage/post" element={<MyPost />} />
      <Route path="/mypage/comment" element={<MyComment />} />
      <Route path="/mypage/edit" element={<MyEdit />} />
      <Route
        path="/mypage/edit/pw"
        element={<IdentityVerification type="change" />}
      />
      <Route
        path="/mypage/edit/signout"
        element={<IdentityVerification type="signout" />}
      />
      <Route path="/mypage/friend" element={<MyFriend />} />
      <Route path="/user/:id" element={<Userpage />} />
      <Route path="/user/:id/friend" element={<UserFriend />} />
      <Route path="/help/pw" element={<IdentityVerification type="find" />} />

      <Route path="/search/post" element={<PostSearch />} />
      <Route path="/search/user" element={<UserSearch />} />
    </Routes>
  );
}
