import axios from "api/axios";
import { useState } from "react";
import { useForm, SubmitHandler, FieldValues } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "store/hook";
import { id } from "store/modules/authSlice";

export const genderList = [
  { kor: "남자", eng: "MALE" },
  { kor: "여자", eng: "FEMALE" },
  { kor: "공개안함", eng: "NOBODY" },
];
const agegroupList = [
  { kor: "10대", eng: "TEENAGER" },
  { kor: "20대", eng: "TWENTIES" },
  { kor: "30대", eng: "THIRTIES" },
  { kor: "40대", eng: "FORTIES" },
  { kor: "50대", eng: "FIFTIES" },
  { kor: "60대", eng: "SIXTIES" },
  { kor: "70대", eng: "SEVENTIES" },
  { kor: "80대 이상", eng: "EIGHTIES" },
  { kor: "공개안함", eng: "OTHERS" },
];

export default function Info() {
  const { register, getValues, handleSubmit } = useForm();
  const navigate = useNavigate();
  const ID = useAppSelector(id);
  const onSubmit: SubmitHandler<FieldValues> = (data) => {
    axios
      .patch(`/users/${ID}/firstlogin`, data)
      .then((res) => {
        navigate("/signup/qna");
      })
      .catch((err) => console.log(err));
  };

  const [next, setNext] = useState(false);

  return (
    <div className="max-w-md m-auto flex flex-col justify-center items-center mt-12 px-4">
      <img src="/images/logo.png" alt="logo" width={90}></img>
      <h1 className="font-bold text-2xl mt-3 mb-6">회원가입</h1>
      <form className="flex flex-col w-full" onSubmit={handleSubmit(onSubmit)}>
        {next ? null : (
          <>
            <div className="flex gap-2 mx-1 mb-24">
              {genderList.map((el, idx) => {
                return (
                  <div key={idx} className="flex-1 flex">
                    <input
                      type="radio"
                      id={el.eng}
                      value={el.eng}
                      className="peer hidden"
                      {...register("genderType", {
                        required: true,
                      })}
                    />
                    <label htmlFor={el.eng} className="flex-1 radio-peer">
                      {el.kor}
                    </label>
                  </div>
                );
              })}
            </div>
            <button
              type="button"
              className="btn-r"
              onClick={() => {
                const singleValue = getValues("genderType");
                if (singleValue) {
                  setNext(true);
                }
              }}
            >
              다음
            </button>
          </>
        )}
        {next ? (
          <>
            <div className="grid grid-cols-3 gap-2 mx-1 mb-4">
              {agegroupList.map((el, idx) => {
                return (
                  <div key={idx} className="flex-1 flex">
                    <input
                      type="radio"
                      id={el.eng}
                      value={el.eng}
                      className="peer hidden"
                      {...register("ageType", {
                        required: true,
                      })}
                    />
                    <label className="flex-1 radio-peer" htmlFor={el.eng}>
                      {el.kor}
                    </label>
                  </div>
                );
              })}
            </div>
            <button className="btn-r">다음</button>
          </>
        ) : null}
      </form>
    </div>
  );
}
