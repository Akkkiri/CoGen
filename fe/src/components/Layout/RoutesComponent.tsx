import { Routes, Route } from "react-router-dom";
import Home from "../../page/Home";
import Login from "../../page/Login";
import Mypage from "../../page/mypage/Mypage";
import Post from "../../page/post/Post";
import PostDetail from "../../page/post/PostDetail";
import Question from "../../page/Question";
import Quiz from "../../page/Quiz";

export default function RoutesComponent() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/question" element={<Question />} />
      <Route path="/post" element={<Post />} />
      <Route path="/post/:PostId" element={<PostDetail />} />
      <Route path="/quiz" element={<Quiz />} />
      <Route path="/mypage" element={<Mypage />} />
      <Route path="/login" element={<Login />} />
    </Routes>
  );
}
