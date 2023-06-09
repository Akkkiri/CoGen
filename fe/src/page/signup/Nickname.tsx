import { useRef, useState } from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { useAppDispatch, useAppSelector } from "store/hook";
import { saveId, userId } from "store/modules/authSlice";
import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import axios from "api/axios";
import { useNavigate } from "react-router-dom";

interface IFormInput {
  userId: string;
  nickname: string;
  password: string;
  passwordRepeat: string;
}

export default function Nickname() {
  const [showPassword, setShowPassword] = useState(false);
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const userPhoneNumber = useAppSelector(userId);

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<IFormInput>({
    defaultValues: { userId: userPhoneNumber },
  });
  const onSubmit: SubmitHandler<IFormInput> = (data) => {
    axios
      .post("/users/signup", data)
      .then((res) => {
        dispatch(saveId(res.data.id));
        navigate("/signup/info");
      })
      .catch((err) => console.log(err));
  };

  const password = useRef<string>();
  password.current = watch("password");

  return (
    <div className="max-w-md m-auto flex flex-col justify-center items-center mt-12 px-4">
      <img src="/images/logo.png" alt="logo" width={90}></img>
      <h1 className="font-bold text-2xl mt-3 mb-6">회원가입</h1>
      <form
        className="w-full flex flex-col gap-2"
        onSubmit={handleSubmit(onSubmit)}
      >
        <input className="input-basic" disabled {...register("userId")} />
        <input
          placeholder="닉네임 (2~8글자)"
          className="input-basic"
          maxLength={8}
          {...register("nickname", {
            required: true,
            minLength: 2,
            maxLength: 8,
            pattern: /^(?=.*\S)[a-zA-Z0-9가-힣]+$/i,
          })}
          aria-invalid={errors.nickname ? "true" : "false"}
        />
        {errors.nickname?.type === "required" && (
          <p role="alert" className="text-y-red font-light">
            닉네임은 필수로 입력해주셔야합니다.
          </p>
        )}
        {errors.nickname?.type === "minLength" && (
          <p role="alert" className="text-y-red font-light">
            2글자 이상 8글자 이하로 설정해야 합니다.
          </p>
        )}
        {errors.nickname?.type === "pattern" && (
          <p role="alert" className="text-y-red font-light">
            닉네임은 숫자와 문자만 입력 가능합니다
          </p>
        )}
        <div className="flex justify-end items-center">
          <input
            type={showPassword ? "text" : "password"}
            autoComplete={showPassword ? "off" : "on"}
            placeholder="비밀번호 (6~12글자)"
            className="input-basic flex-1"
            {...register("password", {
              required: true,
              maxLength: 12,
              minLength: 6,
              pattern: /^(?=.*\S)(?=.*\d)(?=.*[a-zA-ZS])[A-Za-z0-9]{6,12}$/,
            })}
            aria-invalid={errors.password ? "true" : "false"}
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
        {errors.password?.type === "required" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 필수로 입력해주셔야 합니다
          </p>
        )}
        {errors.password?.type === "minLength" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 6자리 이상 설정하셔야 합니다
          </p>
        )}
        {errors.password?.type === "maxLength" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 12자리까지만 입력 가능합니다
          </p>
        )}
        {errors.password?.type === "pattern" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호는 숫자와 문자 조합으로 입력하셔야 합니다 (특수문자,공백 X)
          </p>
        )}
        <div className="flex justify-end items-center">
          <input
            type={showPassword ? "text" : "password"}
            autoComplete={showPassword ? "off" : "on"}
            placeholder="비밀번호 확인"
            maxLength={12}
            className="input-basic flex-1"
            {...register("passwordRepeat", {
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
        {errors.passwordRepeat?.type === "validate" && (
          <p role="alert" className="text-y-red font-light">
            비밀번호가 일치하지 않습니다
          </p>
        )}
        <button className="btn-r">다음</button>
      </form>
    </div>
  );
}
