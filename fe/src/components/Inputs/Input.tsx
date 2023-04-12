import { Path, UseFormRegister, RegisterOptions } from "react-hook-form";

interface IFormValues {
  name: string;
  text: string;
}

type InputProps = {
  type: string;
  name: Path<IFormValues>;
  placeholder: string;
  register: UseFormRegister<IFormValues>;
  rules?: RegisterOptions;
};

const inputContainerClassName = "font-light block";
const inputClassName =
  "border border-y-lightGray rounded-xl focus:outline-y-red focus:ring-1 block w-full p-2.5 placeholder-slate-300";

export const Input = ({
  name,
  rules,
  type,
  placeholder,
  register,
}: InputProps) => {
  return (
    <section className={inputContainerClassName}>
      <input
        className={inputClassName}
        type={type}
        placeholder={placeholder}
        {...(register && register(name, rules))}
      />
    </section>
  );
};
