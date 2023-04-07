import { RiKakaoTalkFill } from "react-icons/ri";
import { SiNaver } from "react-icons/si";

export const KakaoBtn = () => {
  const REST_API_KEY = process.env.REACT_APP_KAKAO_REST_KEY;
  const REDIRECT_URI = "https://www.akkkiri.co.kr/oauth/kakao";
  const LINK = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;

  const handleKakaoLogin = () => {
    return window.location.assign(LINK);
  };

  return (
    <button
      onClick={handleKakaoLogin}
      className="flex justify-center items-center w-10 h-10 rounded-md bg-yellow-400 hover:bg-yellow-500 text-3xl"
    >
      <RiKakaoTalkFill />
    </button>
  );
};

export const NaverBtn = () => {
  const REST_API_KEY = process.env.REACT_APP_NAVER_CLIENT_KEY;
  const REDIRECT_URI = "https://www.akkkiri.co.kr/oauth/naver";
  const LINK = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}`;

  const handleNaverLogin = () => {
    return window.location.assign(LINK);
  };

  return (
    <button
      onClick={handleNaverLogin}
      className="flex justify-center items-center w-10 h-10 rounded-md bg-green-500 hover:bg-green-600 text-white text-xl"
    >
      <SiNaver />
    </button>
  );
};
