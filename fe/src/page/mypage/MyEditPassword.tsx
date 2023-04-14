import axios from "api/axios";
import BackBtn from "components/BackBtn";
import { useRef, useState } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

interface IFormInput {
  userId: string;
  newPassword: string;
  newPasswordRepeat: string;
}

export default function MyEditPassword({
  phoneNumber,
  type,
}: {
  phoneNumber: string;
  type: "find" | "change" | "signout";
}) {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<IFormInput>({
    defaultValues: { userId: phoneNumber },
  });

  const password = useRef<string>();
  password.current = watch("newPassword");

  const onSubmit: SubmitHandler<IFormInput> = (data) => {
    if (type === "change") {
      axios
        .patch("/mypage/patch/password", data)
        .then((res) => {
          Swal.fire({
            text: "비밀번호가 변경되었습니다",
            confirmButtonColor: "#E74D47",
            confirmButtonText: "확인",
          }).then((result) => {
            if (result.isConfirmed) {
              navigate("/mypage");
            }
          });
        })
        .catch((err) => console.log(err));
    } else {
      axios
        .patch("/find/password/change", data)
        .then((res) => {
          Swal.fire({
            text: "비밀번호가 변경되었습니다",
            confirmButtonColor: "#E74D47",
            confirmButtonText: "로그인하기",
          }).then((result) => {
            if (result.isConfirmed) {
              navigate("/login");
            }
          });
        })
        .catch((err) => console.log(err));
    }
  };
  return (
    <div>
      <BackBtn />
      <h1 className="page-title">비밀번호 변경</h1>
      <form
        className="w-full flex flex-col gap-2 mt-8 px-4"
        onSubmit={handleSubmit(onSubmit)}
      >
        <input className="input-basic" disabled {...register("userId")} />
        <div className="flex justify-end items-center">
          <input
            type={showPassword ? "text" : "password"}
            autoComplete={showPassword ? "off" : "on"}
            placeholder="새 비밀번호 (6~12글자)"
            className="input-basic flex-1"
            {...register("newPassword", {
              required: true,
              maxLength: 12,
              minLength: 6,
              pattern: /^(?=.*\d)(?=.*[a-zA-ZS])[A-Za-z0-9]{6,12}$/,
            })}
            aria-invalid={errors.newPassword ? "true" : "false"}
          />
          <button
            type="button"
            className="mr-3 absolute"
            onClick={() => {
              setShowPassword(!showPassword);
            }}
          >
            {showPassword ? <AiFillEye /> : <AiFillEyeInvisible />}
          </button>
        </div>
        {errors.newPassword?.type === "required" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 필수로 입력해주셔야 합니다
          </p>
        )}
        {errors.newPassword?.type === "minLength" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 6자리 이상 설정하셔야 합니다
          </p>
        )}
        {errors.newPassword?.type === "maxLength" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 12자리까지만 입력 가능합니다
          </p>
        )}
        {errors.newPassword?.type === "pattern" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 숫자와 문자 조합으로 입력하셔야 합니다 (특수문자,공백 X)
          </p>
        )}
        <div className="flex justify-end items-center">
          <input
            type={showPassword ? "text" : "password"}
            autoComplete={showPassword ? "off" : "on"}
            placeholder="새 비밀번호 확인"
            maxLength={12}
            className="input-basic flex-1"
            {...register("newPasswordRepeat", {
              required: true,
              validate: (value) => value === password.current,
            })}
          />
          <button
            type="button"
            className="mr-3 absolute"
            onClick={() => {
              setShowPassword(!showPassword);
            }}
          >
            {showPassword ? <AiFillEye /> : <AiFillEyeInvisible />}
          </button>
        </div>
        {errors.newPasswordRepeat?.type === "validate" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호가 일치하지 않습니다
          </p>
        )}
        <button className="btn-r">변경하기</button>
      </form>
    </div>
  );
}
