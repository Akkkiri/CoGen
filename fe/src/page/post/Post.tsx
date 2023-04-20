import SelectBox from "../../components/SelectBox";
import { useState, useEffect } from "react";
import Pagenation from "../../components/Pagenation";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import PostContainer from "../../components/PostContainer";
import axios from "../../api/axios";
import CategorySwiper from "../../components/CategorySwiper";
import { NavLink } from "react-router-dom";
import { isLogin } from "../../store/modules/authSlice";
import { useAppSelector } from "../../store/hook";
import { useNavigate } from "react-router-dom";

import Swal from "sweetalert2";
export default function PostPlace() {
  const [sort, setSort] = useState<Select>("new");

  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [postProps, setPostProps] = useState<any>();
  const [category, setCategory] = useState<string>(`전체`);
  const isLoginUser = useAppSelector(isLogin);

  const navigate = useNavigate();
  useEffect(() => {
    axios
      .get(
        `/feeds/categories?category=${SelectBoxMatcher(
          category
        )}&sort=${sort}&page=${page}`
      )
      .then((response) => {
        setPostProps(response.data.data);
        setTotalPages(response.data.pageInfo.totalPages);
      });
  }, [category, page, sort]);

  useEffect(() => {
    setPage(1);
  }, [category]);

  const goToLogin = () => {
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
  };
  return (
    <>
      <h1 className="text-center text-xl p-3 border-b border-y-lightGray md:text-2xl">
        게시판
      </h1>
      <div className="p-2 border-b border-y-lightGray">
        <CategorySwiper setSelected={setCategory} checked={category} />
      </div>
      <div className="p-2 border-b border-y-lightGray">
        <SelectBox setSelect={setSort} type={"sort"} />
      </div>
      <div className="mb-20">
        <PostContainer postContainerProps={postProps} />
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
      <div className="relative m-3">
        <div className="fixed bottom-[70px]">
          {isLoginUser ? (
            <NavLink to={"/writepost"}>
              <button className="btn-r">게시글 작성</button>
            </NavLink>
          ) : (
            <button onClick={goToLogin} className="btn-r">
              게시글 작성
            </button>
          )}
        </div>
      </div>
    </>
  );
}
