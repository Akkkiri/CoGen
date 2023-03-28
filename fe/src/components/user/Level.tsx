export default function Level({
  point,
  level,
}: {
  point: number;
  level: number;
}) {
  const convertPercent = (point: number) => {
    if (point === 0) {
      return "w-0";
    }
    return `w-${point * 2}%`;
  };
  return (
    <div className="my-2 text-xs">
      <div className="flex justify-between mb-1">
        <span>레벨 {level}</span>
        <span>
          <span className="text-y-purple">{point}</span>/50
        </span>
      </div>
      <div className="w-full h-2 bg-y-lightGray/50 rounded-lg">
        <div
          className={`${convertPercent(point)} h-2 bg-y-purple rounded-lg`}
        ></div>
      </div>
    </div>
  );
}
