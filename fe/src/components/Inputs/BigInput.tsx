import { useState } from "react";

type InputProps = {
  placeholder: string;
  inputState: string;
  setInputState: React.Dispatch<React.SetStateAction<string>>;
};

export default function BigInput({
  placeholder,
  inputState,
  setInputState,
}: InputProps) {
  const [inputLen, setInputLen] = useState<number | undefined>(0);
  const onInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputState(e.target.value);
    setInputLen(e.target.value.length);
  };

  return (
    <>
      <form className="font-light relative">
        <textarea
          className="w-full h-36 rounded-xl p-3 border border-y-lightGray md:text-lg focus:outline-y-red placeholder-slate-300 resize-none"
          placeholder={placeholder}
          value={inputState}
          maxLength={1000}
          onChange={(e) => {
            onInputChange(e);
          }}
        />
        <div className="text-right text-slate-300 right-4 absolute top-28 md:text-lg">
          {`(${inputLen}/1000)`}
        </div>
      </form>
    </>
  );
}
