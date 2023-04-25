import { Link, useNavigate } from "react-router-dom";
import { useForm, SubmitHandler } from "react-hook-form";
import axios from "api/axios";
import { useState } from "react";
import { useAppDispatch } from "store/hook";
import { signInAsync } from "store/modules/authSlice";
import { KakaoBtn, NaverBtn } from "components/signup/OauthBtn";
import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import { FaChevronRight } from "react-icons/fa";

interface IFormInput {
  userId: string;
  password: string;
}

export default function Login() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [loginError, setLoginError] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const { register, handleSubmit } = useForm<IFormInput>();
  const onSubmit: SubmitHandler<IFormInput> = (data) => {
    dispatch(signInAsync(data)).then((res) => {
      if (res.type === "auth/getToken/fulfilled") {
        // console.log("auth/getToken/fulfilled", res);
        axios.defaults.headers.common["Authorization"] =
          res.payload.headers.authorization;
        navigate("/");
      } else if (res.type === "auth/getToken/rejected") {
        setLoginError(true);
      }
    });
  };

  return (
    <div className="max-w-md m-auto flex flex-col justify-center items-center mt-8 px-4">
      <img src="/images/logo_2.png" alt="logo" width={90}></img>
      <h1 className="font-bold text-2xl mt-3 mb-6">로그인</h1>
      <form
        className="w-full flex flex-col gap-2"
        onSubmit={handleSubmit(onSubmit)}
      >
        <input
          placeholder="전화번호"
          className="input-basic"
          maxLength={11}
          onKeyDown={(e) => {
            if (!/^[0-9]+$/.test(e.key) && e.key.length === 1) {
              e.preventDefault();
            }
          }}
          {...register("userId", { required: true })}
        />
        <div className="flex justify-end items-center">
          <input
            placeholder="비밀번호"
            type={showPassword ? "text" : "password"}
            autoComplete={showPassword ? "off" : "on"}
            className="input-basic flex-1"
            maxLength={12}
            {...register("password", { required: true })}
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
        {loginError ? (
          <div className="text-xs text-y-red font-light text-center">
            <p>전화번호 또는 비밀번호가 일치하지 않습니다.</p>
            <p>입력하신 내용을 다시 확인해 주세요.</p>
          </div>
        ) : null}
        <div className="flex justify-end mx-1">
          <button
            type="button"
            className="flex items-center text-xs text-y-lightGray/70 -mb-2 -mt-1 w-fit"
            onClick={() => {
              navigate("/help/pw");
            }}
          >
            비밀번호찾기
            <FaChevronRight className="mx-1" />
          </button>
        </div>
        <button className="btn-r">로그인</button>
      </form>
      <Link to="/signup" className="flex w-full">
        <button className="btn-p flex-1">회원가입</button>
      </Link>
      <div className="h-px w-98% bg-gray-300 flex justify-center items-center my-4">
        <span className="text-y-gray text-sm font-light bg-white px-2 absolute">
          간편 로그인
        </span>
      </div>
      <div className="flex gap-x-8 justify-center">
        <NaverBtn />
        <KakaoBtn />
      </div>
    </div>
  );
}
