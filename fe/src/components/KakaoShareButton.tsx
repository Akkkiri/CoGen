import { useEffect } from "react";
import kakao from "asset/kakao.png";

// 참고로, JS SDK는 PC 또는 모바일에 따라 동작이 변경되는 부분들이 있어서
// user agent가 임의로 변경된 환경 (크롬 브라우저 > 개발자모드 > 모바일 설정)을 지원하지 않음
const KakaoShareButton = () => {
  useEffect(() => {
    createKakaoButton();
  }, []);
  const createKakaoButton = () => {
    if (window.Kakao) {
      const kakao = window.Kakao;
      // 중복 initialization 방지
      if (!kakao.isInitialized()) {
        kakao.init(process.env.REACT_APP_KAKAO_API_KEY);
      }

      kakao.Link.createDefaultButton({
        container: "#kakao-link-btn",
        objectType: "feed",
        content: {
          title: `코젠`,
          description: `세대 공감 프로젝트 CoGen`,
          imageUrl: "",
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
    <button type="button" id="kakao-link-btn" className="rounded-full mx-[1px]">
      <img
        className="w-6 h-6 rounded-full"
        src={kakao}
        alt="kakao"
        width={100}
        height={100}
      />
    </button>
  );
};
export default KakaoShareButton;
