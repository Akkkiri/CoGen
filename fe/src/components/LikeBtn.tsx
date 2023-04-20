import { IoHeartOutline, IoHeart } from "react-icons/io5";
type LikeProps = {
  likeCount: number;
  isLike: boolean;
  onClick: React.MouseEventHandler<HTMLButtonElement> | undefined;
  //onClick이벤트 타입
};
export default function LikeBtn({ onClick, likeCount, isLike }: LikeProps) {
  return (
    <button onClick={onClick} className="flex text-sm md:text-base">
      <div className="flex">
        {!isLike ? (
          <IoHeartOutline className="text-lg md:text-2xl" />
        ) : (
          <IoHeart className="text-lg text-y-red" />
        )}
        <span className="self-center">좋아요 {likeCount}</span>
      </div>
    </button>
  );
}
