import { RiKakaoTalkFill } from "react-icons/ri";
import { SiNaver } from "react-icons/si";
import { Link, useNavigate } from "react-router-dom";
import { useForm, SubmitHandler } from "react-hook-form";
import axios from "api/axios";
import { useState } from "react";
import { useAppDispatch } from "store/hook";
import { signInAsync } from "store/modules/authSlice";

interface IFormInput {
  userId: string;
  password: string;
}

export default function Login() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const [loginError, setLoginError] = useState(false);
  const { register, handleSubmit } = useForm<IFormInput>();
  const onSubmit: SubmitHandler<IFormInput> = (data) => {
    //제거
    console.log(data);
    dispatch(signInAsync(data)).then((res) => {
      if (res.type === "auth/getToken/fulfilled") {
        console.log("auth/getToken/fulfilled", res);
        axios.defaults.headers.common["Authorization"] =
          res.payload.headers.authorization;
        navigate("/");
      } else if (res.type === "auth/getToken/rejected") {
        //제거
        console.log("auth/getToken/rejected", res);
        setLoginError(true);
      }
    });
  };

  return (
    <div className="max-w-md m-auto flex flex-col justify-center items-center mt-12 px-4">
      <img src="/images/logo.png" alt="logo" width={90}></img>
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
        <input
          placeholder="비밀번호"
          type="password"
          autoComplete="on"
          className="input-basic"
          maxLength={12}
          {...register("password", { required: true })}
        />
        {loginError ? (
          <div className="text-xs text-y-red font-light text-center">
            <p>전화번호 또는 비밀번호가 일치하지 않습니다.</p>
            <p>입력하신 내용을 다시 확인해 주세요.</p>
          </div>
        ) : null}
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
        <button className="flex justify-center items-center w-10 h-10 rounded-md bg-green-500 hover:bg-green-600 text-white text-xl">
          <SiNaver />
        </button>
        <button className="flex justify-center items-center w-10 h-10 rounded-md bg-yellow-400 hover:bg-yellow-500 text-3xl">
          <RiKakaoTalkFill />
        </button>
      </div>
    </div>
  );
}
