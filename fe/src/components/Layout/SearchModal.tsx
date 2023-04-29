import { useState, useEffect, useRef } from "react";
import { AiOutlineCloseCircle } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import { MdCancel } from "react-icons/md";
import { IoSearchOutline } from "react-icons/io5";
import { useAppDispatch, useAppSelector } from "store/hook";
import {
  saveSearchHistory,
  saveSearchMode,
  searchHistory,
  searchMode,
} from "store/modules/searchSlice";

interface searchWord {
  id: number;
  mode: string;
  word: string;
}

export default function SearchModal({
  setIsSearching,
}: {
  setIsSearching: React.Dispatch<React.SetStateAction<boolean>>;
}) {
  const mode = useAppSelector(searchMode);
  const [userSearch, setUserSearch] = useState(mode === "user" ? true : false);
  const [inputState, setInputState] = useState<string>("");
  const [searchHistoryList, setSearchHistoryList] = useState<searchWord[]>([]);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const history = useAppSelector(searchHistory);
  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputState(e.target.value);
  };

  const inputRef = useRef<HTMLInputElement>(null);
  useEffect(() => {
    if (inputRef.current !== null) inputRef.current.focus();
  }, []);

  useEffect(() => {
    setSearchHistoryList(history);
  }, [history]);

  const onSearch = () => {
    if (inputState !== "") {
      navigate(`/search/${userSearch ? "user" : "post"}?query=${inputState}`, {
        state: { mode: userSearch ? "user" : "post", query: inputState },
      });
      addSearchHistory(inputState, userSearch);
      dispatch(saveSearchMode(userSearch ? "user" : "post"));
      setIsSearching(false);
    }
  };

  const addSearchHistory = (word: string, userSearch: boolean) => {
    const newSearchWord = {
      id: Date.now(),
      mode: userSearch ? "user" : "post",
      word,
    };
    setSearchHistoryList([newSearchWord, ...searchHistoryList]);
    dispatch(saveSearchHistory([newSearchWord, ...searchHistoryList]));
  };

  const removeSearchHistory = (id: number) => {
    const filtered = searchHistoryList.filter((el: searchWord) => {
      return el.id !== id;
    });
    setSearchHistoryList(filtered);
    dispatch(
      saveSearchHistory(
        searchHistoryList.filter((el: searchWord) => {
          return el.id !== id;
        })
      )
    );
    return null;
  };

  const clearSearchHistory = () => {
    setSearchHistoryList([]);
    dispatch(saveSearchHistory([]));
  };

  return (
    <div className="fixed inset-0 w-full h-full z-50 bg-black/50">
      <div className="flex flex-col bg-white max-w-5xl m-auto">
        <div className="flex justify-between mx-2 mt-3">
          <div>
            <button
              className={`w-28 border-2 px-2 py-1 rounded-2xl ${
                userSearch
                  ? "text-y-lightGray border-y-lightGray "
                  : "text-y-red border-y-red"
              }`}
              onClick={() => {
                setUserSearch(false);
                dispatch(saveSearchMode("post"));
              }}
            >
              게시글 검색
            </button>
            <button
              className={`w-28 ml-2 border-2 px-2 py-1 rounded-2xl ${
                userSearch
                  ? "text-y-red border-y-red"
                  : "text-y-lightGray border-y-lightGray "
              }`}
              onClick={() => {
                setUserSearch(true);
                dispatch(saveSearchMode("user"));
              }}
            >
              유저 검색
            </button>
          </div>
          <button onClick={() => setIsSearching(false)}>
            <AiOutlineCloseCircle className="text-2xl text-y-lightGray" />
          </button>
        </div>
        <div>
          <form
            className="flex items-center h-12 rounded-xl p-2 bg-y-lightGray/20 mx-2 my-3"
            onSubmit={(e) => {
              e.preventDefault();
            }}
          >
            <IoSearchOutline className="w-[25px] h-[25px] mr-0.5" />
            <input
              autoFocus
              ref={inputRef}
              type="text"
              placeholder={
                userSearch
                  ? "닉네임 혹은 #고유번호로 유저를 검색해보세요"
                  : "게시글을 검색해보세요"
              }
              className="w-full outline-none bg-transparent"
              value={inputState}
              onChange={(e) => {
                onInputChange(e);
              }}
              onKeyUp={(e) => {
                if (e.key === "Enter") {
                  onSearch();
                }
              }}
            ></input>
            {inputState !== "" ? (
              <button
                type="button"
                onClick={() => {
                  setInputState("");
                }}
              >
                <MdCancel className="w-5 h-5 mx-1" />
              </button>
            ) : null}
          </form>
          <div className="flex justify-between px-3 pb-2 border-b border-y-lightGray/30">
            <h4 className="text-y-gray">최근검색어</h4>
            {searchHistoryList.length ? (
              <button
                className="text-y-gray/80 font-light text-sm"
                onClick={clearSearchHistory}
              >
                전체 삭제
              </button>
            ) : null}
          </div>
          <ul>
            {searchHistoryList.length
              ? searchHistoryList.map((el, idx) => {
                  return idx > 5 ? (
                    removeSearchHistory(el.id)
                  ) : (
                    <li
                      key={el.id}
                      className="flex justify-between py-1 px-3 hover:bg-y-lightGray/20"
                    >
                      <div
                        className="w-full"
                        onClick={() => {
                          navigate(`/search/${el.mode}?query=${el.word}`, {
                            state: {
                              mode: el.mode,
                              query: el.word,
                            },
                          });
                          dispatch(saveSearchMode(el.mode));
                          setIsSearching(false);
                        }}
                      >
                        {el.word}
                      </div>
                      <button
                        className="text-y-gray z-1"
                        onClick={() => removeSearchHistory(el.id)}
                      >
                        <MdCancel className="mx-1 w-5 h-4" />
                      </button>
                    </li>
                  );
                })
              : null}
          </ul>
        </div>
      </div>
      <button
        className="inset-0 fixed cursor-default -z-10"
        onClick={() => setIsSearching(false)}
      ></button>
    </div>
  );
}
