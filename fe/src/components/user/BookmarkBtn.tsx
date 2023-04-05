import { useState } from "react";
import { BsBookmark, BsBookmarkFill } from "react-icons/bs";

export default function BookmarkBtn() {
  const [isBookmark, setIsBookmark] = useState(true);
  return (
    <button
      className="w-full flex justify-end absolute p-3 text-2xl"
      onClick={() => {
        //저장하기 취소 + 다시 저장하기 로직 필요함
        setIsBookmark(!isBookmark);
      }}
    >
      {isBookmark ? <BsBookmarkFill /> : <BsBookmark />}
    </button>
  );
}
