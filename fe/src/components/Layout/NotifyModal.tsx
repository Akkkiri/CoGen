export default function NotifyModal({
  setIsNotifying,
}: {
  setIsNotifying: React.Dispatch<React.SetStateAction<boolean>>;
}) {
  return (
    <div className="fixed inset-0 w-full h-full z-50 bg-black/50">
      <div className="flex justify-between bg-white max-w-xl m-auto">
        <h1>알림 모달</h1>
        <button onClick={() => setIsNotifying(false)}>지우기</button>
      </div>
    </div>
  );
}
