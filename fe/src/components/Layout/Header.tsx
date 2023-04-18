import logo from "asset/logo.png";
import { IoSearchOutline } from "react-icons/io5";
import { VscBell, VscBellDot } from "react-icons/vsc";
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
import { EventSourcePolyfill } from "event-source-polyfill";
import {
  deleteNotify,
  NotifyProps,
  saveNotify,
} from "store/modules/notifySlice";
const EventSource = EventSourcePolyfill;

export default function Header() {
  const [isSearching, setIsSearching] = useState(false);
  const [isNotifying, setIsNotifying] = useState(false);
  const [hasNewNotify, setHasNewNotify] = useState(false);

  const TOKEN = useSelector(accessToken);
  const isLoginUser = useSelector(isLogin);
  const dispatch = useAppDispatch();

  useEffect(() => {
    axios.defaults.headers.common["Authorization"] = TOKEN;
  }, [TOKEN]);

  useEffect(() => {
    if (isLoginUser) {
      checkNotify();
      setInterval(() => {
        dispatch(getNewTokenAsync());
      }, 1 * 60 * 60 * 1000);
    }
  }, [isLoginUser, dispatch]);

  useEffect(() => {
    if (isLoginUser) {
      // SSE 알림
      let evtSource: EventSourcePolyfill;
      const sse = async () => {
        try {
          evtSource = new EventSource(
            `${process.env.REACT_APP_API_URL}/subscribe`,
            {
              headers: {
                Authorization: TOKEN,
              },
              heartbeatTimeout: 180000,
              withCredentials: true,
            }
          );
          evtSource.onopen = (e: any) => {
            // console.log("Connection was opened1.");
            // console.log("onopen", e);
          };
          evtSource.addEventListener("sse", (e: any) => {
            // console.log("sse", e);
            if (e.data.slice(0, 9) !== "Connected") {
              setHasNewNotify(true);
              const sseData = JSON.parse(e.data);
              handleNotify({
                id: sseData.id,
                type: sseData.type,
                message: sseData.body,
                url: sseData.url,
              });
            }
          });
          evtSource.onerror = (e: any) => {
            console.log(e);
          };
        } catch (error) {}
      };
      sse();
      return () => {
        return evtSource.close();
      };
    } else {
      setHasNewNotify(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isLoginUser, TOKEN]);

  const checkNotify = () => {
    axios
      .get("/notifications/check")
      .then((res) => setHasNewNotify(res.data))
      .catch((err) => console.log(err));
  };

  const handleNotify = ({ id, type, message, url }: NotifyProps) => {
    dispatch(saveNotify({ id, type, message, url }));
    setTimeout(() => {
      dispatch(deleteNotify());
    }, 6000);
  };

  return (
    <header className="sticky top-0 z-10 bg-white w-full px-4 py-2 border-b border-y-lightGray">
      <div className="flex justify-between items-center max-w-5xl m-auto">
        <Link to="/">
          <img src={logo} alt="CoGen logo" width={50}></img>
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
            {hasNewNotify ? (
              <VscBellDot size={30} className="text-y-red" />
            ) : (
              <VscBell size={30} />
            )}
          </button>
          {isNotifying ? (
            <NotifyModal
              setIsNotifying={setIsNotifying}
              setHasNewNotify={setHasNewNotify}
            />
          ) : null}
        </div>
      </div>
    </header>
  );
}
