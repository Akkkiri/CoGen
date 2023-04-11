interface LinearBarProps {
  order: number;
  setOrder: React.Dispatch<React.SetStateAction<number>>;
}

export default function LinearBar({ order, setOrder }: LinearBarProps) {
  return (
    <div className="flex gap-2 mt-12">
      <div
        className={`flex-1 h-4 ${order > 0 ? "bg-y-red" : "bg-y-lightGray"}`}
      ></div>
      <div
        className={`flex-1 h-4 ${order > 1 ? "bg-y-red" : "bg-y-lightGray"}`}
      ></div>
      <div
        className={`flex-1 h-4 ${order > 2 ? "bg-y-red" : "bg-y-lightGray"}`}
      ></div>
      <div
        className={`flex-1 h-4 ${order > 3 ? "bg-y-red" : "bg-y-lightGray"}`}
      ></div>
      <div
        className={`flex-1 h-4 ${order > 4 ? "bg-y-red" : "bg-y-lightGray"}`}
      ></div>
    </div>
  );
}
