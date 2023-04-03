import Level from "./Level";

export interface UserProfileProps {
  nickname: string;
  profileImage: string;
  level: number;
  ariFactor: number;
  friendsNum: number;
  isMine?: boolean;
}

export default function UserProfile({
  nickname,
  profileImage,
  level,
  ariFactor,
  friendsNum,
  isMine,
}: UserProfileProps) {
  return (
    <div className="flex justify-center items-center mx-2">
      <img
        src={profileImage}
        alt="profileImage"
        className="rounded-full w-20 h-20"
      ></img>
      <div className="w-full mx-4 mt-2">
        <div className="flex justify-between">
          <span>{nickname}</span>
          <button
            className="rounded-lg bg-y-sky py-0.5 px-4 text-xs"
            // onClick={() => console.log("친구목록보기 필요")}
          >
            친구 {friendsNum}
          </button>
        </div>
        <Level level={level} ariFactor={ariFactor} />
        {/* 추후 isMine import 필요 */}
        {isMine ? (
          <button
            className="w-full bg-y-red rounded-lg text-white text-xs py-1"
            // onClick={() => console.log("회원정보수정 필요")}
          >
            회원정보 수정하기
          </button>
        ) : (
          <button
            className="w-full bg-y-pink rounded-lg text-black text-xs py-1"
            // onClick={() => console.log("친구하기 필요")}
          >
            친구하기
          </button>
        )}
      </div>
    </div>
  );
}
