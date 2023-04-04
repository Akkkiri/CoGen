import { IoSearchOutline } from "react-icons/io5";
import { VscBell } from "react-icons/vsc";
import { useEffect, useState } from "react";
import SearchModal from "./SearchModal";
import NotifyModal from "./NotifyModal";
import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import {
  accessToken,
  getNewTokenAsync,
  isLogin,
} from "store/modules/authSlice";
import axios from "api/axios";
import { useAppDispatch } from "store/hook";

export default function Header() {
  const [isSearching, setIsSearching] = useState(false);
  const [isNotifying, setIsNotifying] = useState(false);

  const TOKEN = useSelector(accessToken);
  const isLoginUser = useSelector(isLogin);
  const dispatch = useAppDispatch();

  useEffect(() => {
    axios.defaults.headers.common["Authorization"] = TOKEN;
  }, [TOKEN]);

  useEffect(() => {
    if (isLoginUser) {
      setInterval(() => {
        dispatch(getNewTokenAsync());
      }, 2 * 60 * 60 * 1000);
    }
  }, [isLoginUser, dispatch]);

  return (
    <header className="sticky top-0 z-10 bg-white w-full px-4 py-2 border-b border-y-lightGray">
      <div className="flex justify-between items-center max-w-5xl m-auto">
        <Link to="/">
          <img src="images/logo.png" alt="CoGen logo" width={50}></img>
        </Link>
        <div className="flex justify-center items-center">
          <button
            className="hover:text-y-red"
            onClick={() => setIsSearching(true)}
          >
            <IoSearchOutline size={30} />
          </button>
          {isSearching ? <SearchModal setIsSearching={setIsSearching} /> : null}
          <button
            className="hover:text-y-red"
            onClick={() => setIsNotifying(true)}
          >
            <VscBell size={30} />
          </button>
          {isNotifying ? <NotifyModal setIsNotifying={setIsNotifying} /> : null}
        </div>
      </div>
    </header>
  );
}
