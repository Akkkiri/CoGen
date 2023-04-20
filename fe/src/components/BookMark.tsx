import { IoBookmarkOutline, IoBookmark } from "react-icons/io5";
type LikeProps = {
  isSavedFeed: boolean;
  onClick: React.MouseEventHandler<HTMLButtonElement> | undefined;
  //onClick이벤트 타입
};
export default function BookMarkBtn({ onClick, isSavedFeed }: LikeProps) {
  return (
    <button onClick={onClick} className="flex text-sm md:text-base">
      <div className="flex">
        {!isSavedFeed ? (
          <>
            <IoBookmarkOutline className="text-lg self-center md:text-2xl" />
            <span>저장하기</span>
          </>
        ) : (
          <>
            <IoBookmark className="text-lg self-cente md:text-2xl" />
            <span>저장완료</span>
          </>
        )}
      </div>
    </button>
  );
}
