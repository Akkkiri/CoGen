export default function SearchModal({
  setIsSearching,
}: {
  setIsSearching: React.Dispatch<React.SetStateAction<boolean>>;
}) {
  return (
    <div className="fixed inset-0 w-full h-full z-50 bg-black/50">
      <div className="flex justify-between bg-white max-w-5xl m-auto">
        <h1>검색 모달</h1>
        <button onClick={() => setIsSearching(false)}>지우기</button>
      </div>
    </div>
  );
}
