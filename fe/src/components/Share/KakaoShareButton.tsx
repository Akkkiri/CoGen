import { useEffect } from "react";
import kakao from "asset/kakao.png";

// 참고로, JS SDK는 PC 또는 모바일에 따라 동작이 변경되는 부분들이 있어서
// user agent가 임의로 변경된 환경 (크롬 브라우저 > 개발자모드 > 모바일 설정)을 지원하지 않음
type props = {
  title: string;
  img: string;
  contents: string;
};
const KakaoShareButton = ({ title, img, contents }: props) => {
  useEffect(() => {
    createKakaoButton();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const API_KEY = process.env.REACT_APP_KAKAO_API_KEY;
  const createKakaoButton = () => {
    if (window.Kakao) {
      const kakao = window.Kakao;
      // 중복 initialization 방지
      if (!kakao.isInitialized()) {
        kakao.init(API_KEY);
      }

      kakao.Link.createDefaultButton({
        container: "#kakao-link-btn",
        objectType: "feed",
        content: {
          title: `${title}`,
          description: `세대 공감 프로젝트 CoGen`,
          imageUrl: `${img}`,
          link: {
            mobileWebUrl: window.location.href,
            webUrl: window.location.href,
          },
        },
        buttons: [
          {
            title: "웹으로 보기",
            link: {
              mobileWebUrl: window.location.href,
              webUrl: window.location.href,
            },
          },
          {
            title: "앱으로 보기",
            link: {
              mobileWebUrl: window.location.href,
              webUrl: window.location.href,
            },
          },
        ],
      });
    }
  };

  return (
    <button type="button" id="kakao-link-btn" className="rounded-xl">
      <img
        className="w-10 h-10 rounded-xl"
        src={kakao}
        alt="kakao"
        width={100}
        height={100}
      />
    </button>
  );
};
export default KakaoShareButton;
