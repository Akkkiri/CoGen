import LinkShare from "./LinkShrare";
import { IoMdClose } from "react-icons/io";
import KakaoShareButton from "./KakaoShareButton";
import { useState, useEffect } from "react";
type props = {
  setModalOpen: any;
  title: string;
  img: string;
  contents: string;
};
export default function ShareModal({
  title,
  img,
  contents,
  setModalOpen,
}: props) {
  const closeModal = () => {
    setModalOpen(false);
  };
  const [shareButton, setShareButton] = useState(false);

  useEffect(() => {
    const script = document.createElement("script");
    script.src = "https://developers.kakao.com/sdk/js/kakao.js";
    script.async = true;
    document.body.appendChild(script);

    // 스크립트가 로드 된 후 쉐어버튼 렌더링
    script.onload = () => {
      setShareButton(true);
    };

    return () => {
      document.body.removeChild(script);
    };
  }, []);

  return (
    <div className="bg-white border w-56 rounded-xl absolute z-[100] left-1/2">
      <div className="p-3 border-b border-y-lightGray">
        <IoMdClose
          onClick={closeModal}
          className="w-6 h-6 cursor-pointer absolute "
        />
        <h1 className="text-center text-xl">공유하기</h1>
      </div>
      <div className="flex py-5 px-12 justify-between">
        <LinkShare />
        {shareButton && (
          <KakaoShareButton title={title} img={img} contents={contents} />
        )}
      </div>
    </div>
  );
}
