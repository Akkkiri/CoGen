import SelectBox from "../../components/SelectBox";
import { useState, useEffect } from "react";
import Pagenation from "../../components/Pagenation";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import PostContainer from "../../components/PostContainer";
import axios from "../../api/axios";
import CategorySwiper from "../../components/CategorySwiper";
export default function Post() {
  const [sort, setSort] = useState<Select>("new");
  const [page, setPage] = useState<number>(1);
  const [totalPages, setTotalPages] = useState<number>(1);
  const [postProps, setPostProps] = useState<any>();
  const [category, setCategory] = useState<string>("전체");
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

  return (
    <>
      <h1 className="text-center text-xl p-3 border-b border-y-lightGray">
        게시판
      </h1>
      <div className="p-2 border-b border-y-lightGray">
        <CategorySwiper setSelected={setCategory} />
      </div>
      <div className="p-2 border-b border-y-lightGray">
        <SelectBox setSelect={setSort} type={"sort"} />
      </div>
      <div className="mb-3">
        <PostContainer postContainerProps={postProps} />
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </>
  );
}
