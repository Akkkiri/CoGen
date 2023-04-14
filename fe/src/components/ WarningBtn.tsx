import { RiAlarmWarningFill } from "react-icons/ri";
type ButtonProps = {
  onClick: React.MouseEventHandler<HTMLButtonElement> | undefined;
  //onClick이벤트 타입
};
export default function WarningBtn({ onClick }: ButtonProps) {
  return (
    <div className="flex-1 flex justify-end items-center  text-y-brown mr-3 text-sm">
      <button className="flex items-center mr-1" onClick={onClick}>
        <RiAlarmWarningFill className="mb-[1px] text-y-red" />
        <span className="text-y-black ml-[1px]">신고하기</span>
      </button>
    </div>
  );
}
