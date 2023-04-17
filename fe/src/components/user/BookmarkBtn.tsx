import axios from "api/axios";
import { useState } from "react";
import { BsBookmark, BsBookmarkFill } from "react-icons/bs";

export default function BookmarkBtn({ feedId }: { feedId: number }) {
  const [isBookmark, setIsBookmark] = useState(true);
  return (
    <button
      className="w-full flex justify-end absolute py-3 px-8 text-2xl"
      onClick={() => {
        //저장하기 취소 + 다시 저장하기 로직 필요함
        axios
          .patch(`/feeds/${feedId}/save`)
          .then(() => {
            setIsBookmark(!isBookmark);
          })
          .catch((err) => console.log(err));
      }}
    >
      {isBookmark ? <BsBookmarkFill /> : <BsBookmark />}
    </button>
  );
}
