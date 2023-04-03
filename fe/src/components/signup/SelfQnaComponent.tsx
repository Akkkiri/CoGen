import { useState } from "react";
import { useForm } from "react-hook-form";

interface SelfQnaComponentProps {
  q: string;
  idx: number;
}

export default function SelfQnaComponent({ q, idx }: SelfQnaComponentProps) {
  const { register, getValues } = useForm();

  const [inputOpen, setInputOpen] = useState(false);
  return (
    <div
      className={`${
        idx % 2 === 0 ? "bg-y-sky" : "bg-y-pink"
      } rounded-lg cursor-pointer`}
    >
      <h1
        onClick={() => {
          setInputOpen(!inputOpen);
        }}
      >
        {q}
      </h1>
      <input
        {...register(`${idx}`)}
        className={`${inputOpen ? "" : "hidden"}`}
      />
    </div>
  );
}
