import { useState, useEffect } from "react";
import { GoTriangleDown } from "react-icons/go";
import { Select, SelectBoxMatcher } from "../util/SelectUtil";

type SortBoxProps = {
  type: "sort" | "category" | "comment";
  setSelect: React.Dispatch<React.SetStateAction<Select>>;
};

export default function SelectBox({ type, setSelect }: SortBoxProps) {
  const [list, setList] = useState<string[]>();
  const [curChoice, setCurChoice] = useState<string>();
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    if (type === "sort") {
      setCurChoice("최신순");
      setList(["최신순", "공감순", "댓글많은순"]);
    } else if (type === "comment") {
      setCurChoice("최신순");
      setList(["최신순", "공감순"]);
    } else if (type === "category") {
      setCurChoice("카테고리");
      setList(["고민", "꿀팁", "장소공유", "명언", "유머", "일상", "기타"]);
    }
  }, [type]);

  const handleChoice = (el: string) => {
    setCurChoice(el);
    setShowModal(false);
    const newChoice = SelectBoxMatcher(el);
    if (newChoice !== undefined) {
      setSelect(newChoice);
    }
  };

  return (
    <div className="">
      <button
        onClick={() => setShowModal(!showModal)}
        className="flex items-center w-28 text-xs border border-y-lightGray rounded-lg py-1.5 px-2"
      >
        <GoTriangleDown className="w-3 h-3 mt-[1px] mr-1 text-y-gray" />
        <span>{curChoice}</span>
      </button>
      {showModal ? (
        <div className="relative w-28 h-0">
          <ul className="bg-white border border-y-lightGray/40 rounded-lg text-xs z-10 absolute w-full">
            {list?.map((el, idx) => (
              <li
                key={idx}
                className="pl-5 py-0.5 hover:bg-y-pink rounded-lg"
                onClick={() => {
                  handleChoice(el);
                }}
              >
                {el}
              </li>
            ))}
          </ul>
          <button
            className="inset-0 fixed cursor-default"
            onClick={() => setShowModal(false)}
          ></button>
        </div>
      ) : null}
    </div>
  );
}
