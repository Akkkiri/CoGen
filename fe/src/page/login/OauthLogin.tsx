import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "store/hook";
import { oauthAsync, saveId } from "store/modules/authSlice";
import axios from "api/axios";
import Loading from "components/Loading";

export default function OauthLogin() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    const path = window.location.pathname;
    const code = window.location.search;
    // if (
    //   code !== undefined &&
    //   code !== "" &&
    //   (path === "/oauth/naver" || path === "/oauth/kakao")
    // ) {
    getOauthCode(path, code);
    // }
  }, []);

  const getOauthCode = (path: string, code: string) => {
    dispatch(oauthAsync({ path, code }))
      .then((res) => {
        if (res.type === "auth/oauth/fulfilled") {
          //제거
          console.log("auth/oauth/fulfilled", res);
          axios.defaults.headers.common["Authorization"] =
            res.payload.headers.authorization;
          if (res.payload.data.isFirstLogin) {
            //제거
            console.log("첫 로그인 여부", res.payload.data.isFirstLogin);
            dispatch(saveId(res.payload.data.id));
            navigate("/signup/info");
          } else {
            navigate("/");
          }
        } else if (res.type === "auth/oauth/rejected") {
          //제거
          console.log("auth/oauth/rejected", res);
          navigate("/login");
        }
      })
      .catch((err) => console.log(err));
  };
  return (
    <div className="text-center mt-36">
      <Loading />
      <h1 className="mt-4">로그인중입니다</h1>
      <h3 className="text-sm mt-1 text-y-lightGray">잠시만 기다려주세요</h3>
    </div>
  );
}
