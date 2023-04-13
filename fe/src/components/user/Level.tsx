import { Point, ConvertPoint } from "util/LevelUtil";

export default function Level({
  ariFactor,
  level,
}: {
  ariFactor: Point;
  level: number;
}) {
  return (
    <div className="my-2 text-xs">
      <div className="flex justify-between mb-1">
        <span>레벨 {level}</span>
        <span>
          <span className="text-y-purple">{ariFactor}</span>/50
        </span>
      </div>
      <div className="w-full h-2 bg-y-lightGray/50 rounded-lg">
        <div
          className={`${ConvertPoint[ariFactor]} h-2 bg-y-purple rounded-lg`}
        ></div>
      </div>
    </div>
  );
}
