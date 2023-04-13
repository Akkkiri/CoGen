export default function Empty({ str }: { str: string }) {
  return (
    <div className=" flex flex-col justify-center items-center border rounded-2xl border-y-lightGray/30 p-8 mx-2 mt-8 shadow-sm">
      <img src="/images/favicon.png" alt="Loading" width={66} height={66} />
      <p className="text-y-lightGray">{str} 없습니다</p>
    </div>
  );
}
