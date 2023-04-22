import { useState, useEffect } from "react";
import { GoTriangleDown } from "react-icons/go";
import { useAppDispatch, useAppSelector } from "store/hook";
import { beforeSort, saveSort } from "store/modules/postSlice";
import {
  Select,
  SelectBoxMatcher,
  SortSelectBoxMatcher,
} from "../util/SelectUtil";

type SortBoxProps = {
  type: "sort" | "category" | "comment" | "ageType";
  setSelect: React.Dispatch<React.SetStateAction<Select>>;
  curState?: string;
};

export default function SelectBox({ type, setSelect, curState }: SortBoxProps) {
  const [list, setList] = useState<string[]>();
  const [curChoice, setCurChoice] = useState<string>();
  const [showModal, setShowModal] = useState(false);
  const savedSort = useAppSelector(beforeSort);
  useEffect(() => {
    if (type === "sort") {
      setCurChoice(SortSelectBoxMatcher(savedSort));
      setList(["최신순", "공감순", "조회순"]);
    } else if (type === "comment") {
      setCurChoice("최신순");
      setList(["최신순", "공감순"]);
    } else if (type === "category") {
      setCurChoice("카테고리");
      setList(["고민", "꿀팁", "장소공유", "명언", "유머", "일상", "기타"]);
    } else if (type === "ageType") {
      setCurChoice(curState);
      setList([
        "10대",
        "20대",
        "30대",
        "40대",
        "50대",
        "60대",
        "70대",
        "80대 이상",
        "공개안함",
      ]);
    }
  }, [type, curState, savedSort]);

  const dispatch = useAppDispatch();
  const handleChoice = (el: string) => {
    setCurChoice(el);
    setShowModal(false);
    const newChoice = SelectBoxMatcher(el);
    if (newChoice !== undefined) {
      setSelect(newChoice);
      if (type === "sort") {
        dispatch(saveSort(newChoice));
      }
    }
  };

  return (
    <div className={`${type === "ageType" ? "" : "pb-2"}`}>
      <button
        onClick={() => setShowModal(!showModal)}
        className={`flex items-center ${
          type === "ageType" ? "w-52" : "w-28"
        } border border-y-lightGray rounded-xl py-1.5 px-2 md:text-lg`}
      >
        <GoTriangleDown className="w-3 h-3 mt-[1px] mr-1 text-y-gray" />
        <span>{curChoice}</span>
      </button>
      {showModal ? (
        <div className={`relative ${type === "ageType" ? "w-52" : "w-28"} h-0`}>
          <ul className="bg-white border border-y-lightGray/40 rounded-lg text-base z-10 absolute w-full">
            {list?.map((el, idx) => (
              <li
                key={idx}
                className="pl-5 py-1 hover:bg-y-pink rounded-lg md:text-lg"
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
