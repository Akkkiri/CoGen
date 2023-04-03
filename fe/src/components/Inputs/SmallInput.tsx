import TextareaAutosize from "react-textarea-autosize";
type InputProps = {
  placeholder: string;
  inputState: string;
  setInputState: React.Dispatch<React.SetStateAction<string>>;
  postFunc: Function;
};

export default function SmallInput({
  placeholder,
  inputState,
  postFunc,
  setInputState,
}: InputProps) {
  const onInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputState(e.target.value);
  };
  const handleSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    postFunc();
  };
  return (
    <>
      <form className="flex font-light py-2">
        <TextareaAutosize
          minRows={1}
          maxRows={6}
          className="w-full rounded-l-xl p-2 border-l border-t border-b border-y-lightGray focus:outline-y-red placeholder-slate-300 font-light resize-none"
          placeholder={placeholder}
          value={inputState}
          maxLength={1000}
          onChange={(e) => {
            onInputChange(e);
          }}
          onKeyUp={(e) => {
            if (e.key === "Enter") {
              postFunc();
            }
          }}
        />
        <button
          className="w-20 bg-y-red rounded-r-xl p-1 text-white"
          onClick={handleSubmit}
        >
          등록
        </button>
      </form>
    </>
  );
}
