import axios from "api/axios";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import SelectBox from "components/SelectBox";
import PostContainer from "components/PostContainer";
import Pagenation from "components/Pagenation";
import BackBtn from "components/BackBtn";
import SearchCategorySwiper from "components/SearchCategorySwiper";
import { useAppSelector } from "store/hook";
import {
  beforeCategory,
  beforePage,
  beforeSort,
} from "store/modules/postSlice";
import useDidMountEffect from "util/useDidMountEffect";

export default function PostSearch() {
  const location = useLocation();
  const query = location.state.query;
  const mode = location.state.mode;
  const savedCategory = useAppSelector(beforeCategory);
  const savedSort = useAppSelector(beforeSort);
  const savedPage = useAppSelector(beforePage);
  const [category, setCategory] = useState(savedCategory);
  const [sort, setSort] = useState<Select>(savedSort);
  const [page, setPage] = useState(savedPage);
  const [totalPages, setTotalPages] = useState(1);
  const [searchProps, setSearchProps] = useState<any>();

  useEffect(() => {
    if (mode === "post") {
      axios
        .get(
          `/search?category=${SelectBoxMatcher(
            category
          )}&query=${query}&sort=${sort}&page=${page}`
        )
        .then((res) => {
          setSearchProps(res.data.data);
          setTotalPages(res.data.pageInfo.totalPages);
        })
        .catch((err) => console.log(err));
    }
  }, [mode, category, query, sort, page]);

  useDidMountEffect(() => {
    setPage(1);
  }, [category]);

  return (
    <>
      <BackBtn />
      <h1 className="page-title">"{query}" 검색 결과</h1>
      <div className="p-2 border-b border-y-lightGray">
        <SearchCategorySwiper setSelected={setCategory} checked={category} />
      </div>
      <div className="p-2 border-b border-y-lightGray">
        <SelectBox setSelect={setSort} type={"sort"} />
      </div>
      <div className="mb-3">
        <PostContainer postContainerProps={searchProps} />
        <Pagenation page={page} setPage={setPage} totalPages={totalPages} />
      </div>
    </>
  );
}
