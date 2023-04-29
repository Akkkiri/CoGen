import { BsCaretLeftFill, BsCaretRightFill } from "react-icons/bs";
import { IoPlayBack, IoPlayForward } from "react-icons/io5";
import { useAppDispatch } from "store/hook";
import { savePage } from "store/modules/postSlice";

type pageInfoProps = {
  page: number;
  totalPages: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
};

export default function Pagenation({
  page,
  totalPages,
  setPage,
}: pageInfoProps) {
  const dispatch = useAppDispatch();
  const pagenation = new Array(totalPages).fill(1);
  return totalPages < 2 ? null : (
    <div className="flex justify-center items-center mt-2">
      <button
        className="px-2 bg-y-pink w-8 h-8 md:w-11 md:h-11 rounded-[6px] mx-0.5"
        onClick={() => {
          setPage(1);
          dispatch(savePage(1));
        }}
      >
        <IoPlayBack />
      </button>
      <button
        className="px-2 bg-y-pink w-8 h-8 md:w-11 md:h-11 rounded-[6px] mx-0.5 text-sm md:text-base"
        onClick={() => {
          if (page > 1) {
            setPage(page - 1);
            dispatch(savePage(page - 1));
          }
        }}
      >
        <BsCaretLeftFill />
      </button>
      {pagenation.map((el: number, idx) => {
        return (
          <button
            key={idx}
            value={el + idx}
            onClick={(e) => {
              setPage(Number(e.currentTarget.value));
              dispatch(savePage(Number(e.currentTarget.value)));
            }}
            className={`w-8 h-8 md:w-11 md:h-11 rounded-[6px] text-sm mx-0.5 md:text-base ${
              page === el + idx ? "bg-y-red text-white" : "bg-y-pink"
            }
            ${
              page < 3
                ? el + idx < 6
                  ? ""
                  : "hidden"
                : page === el + idx - 2 ||
                  page === el + idx - 1 ||
                  page === el + idx ||
                  page === el + idx + 1 ||
                  page === el + idx + 2
                ? ""
                : page >= totalPages - 2
                ? el + idx > totalPages - 5
                  ? ""
                  : "hidden"
                : "hidden"
            }`}
          >
            {el + idx}
          </button>
        );
      })}
      <button
        className="px-2 bg-y-pink w-8 h-8 md:w-11 md:h-11 rounded-[6px] mx-0.5 text-sm md:text-base"
        onClick={() => {
          if (totalPages > page) {
            setPage(page + 1);
            dispatch(savePage(page + 1));
          }
        }}
      >
        <BsCaretRightFill />
      </button>
      <button
        className="px-2 bg-y-pink w-8 h-8 md:w-11 md:h-11 rounded-[6px] mx-0.5"
        onClick={() => {
          setPage(totalPages);
          dispatch(savePage(totalPages));
        }}
      >
        <IoPlayForward />
      </button>
    </div>
  );
}
