import axios from "api/axios";
import BackBtn from "components/BackBtn";
import Timer from "../../components/signup/Timer";
import { useAppSelector } from "store/hook";
import { userId } from "store/modules/authSlice";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import MyEditPassword from "page/mypage/MyEditPassword";
import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "store/hook";
import { logout } from "store/modules/authSlice";
import Swal from "sweetalert2";

export default function IdentityVerification({
  type,
}: {
  type: "find" | "change" | "signout";
}) {
  const title = {
    find: "비밀번호 찾기",
    change: "비밀번호 수정",
    signout: "회원 탈퇴",
  };

  const userPhoneNumber = useAppSelector(userId);
  const [passed, setPassed] = useState(false);
  const [notUser, setNotUser] = useState(false);
  const [timerActive, setTimerActive] = useState(false);
  const [reset, setReset] = useState(false);
  const [phoneNumber, setPhoneNumber] = useState("");
  const [certificationNumber, setCertificationNumber] = useState("");
  const [error, setError] = useState(false);
  const [checkList, setCheckList] = useState({
    phoneNumber: false,
    certificationNumber: false,
  });

  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (userPhoneNumber !== "") {
      setPhoneNumber(userPhoneNumber);
    }
  }, [userPhoneNumber]);

  useEffect(() => {
    if (reset) {
      setTimeout(() => {
        setReset(false);
      }, 1000);
    }
  }, [reset]);

  const submitPhoneNumber = (phoneNumber: string) => {
    if (phoneNumber.length === 11) {
      const postBody = { phoneNumber };
      axios
        .post("/find/password/sms/send", postBody)
        .then((_) => setCheckList({ ...checkList, phoneNumber: true }))
        .catch((err) => {
          if (err.response.data.status === 404) {
            setNotUser(true);
          } else {
            console.log(err);
          }
        });
    }
  };

  const submitCertificationNumber = (
    phoneNumber: string,
    certificationNumber: string
  ) => {
    if (checkList.phoneNumber) {
      const postBody = {
        phoneNumber,
        certificationNumber,
      };
      axios
        .post("/sms/verification", postBody)
        .then((_) => {
          setCheckList({ ...checkList, certificationNumber: true });
          setError(false);
        })
        .catch((err) => setError(true));
    }
  };

  return (
    <div>
      {passed ? (
        <MyEditPassword phoneNumber={phoneNumber} type={type} />
      ) : (
        <>
          <BackBtn />
          <h1 className="page-title">{title[type]}</h1>
          <div className="max-w-md m-auto mt-10">
            <div className="w-full flex flex-col gap-2 p-4 text-center">
              {notUser ? (
                <p className="text-y-red">
                  유저가 아닙니다.
                  <Link
                    to="/signup"
                    className="font-bold text-y-purple border-b-2 border-y-purple mx-1"
                  >
                    회원가입
                  </Link>
                  을 진행해주세요
                </p>
              ) : null}
              <form className="flex justify-end items-center">
                {type === "find" ? (
                  <input
                    placeholder="전화번호"
                    className="input-basic flex-1"
                    value={phoneNumber}
                    maxLength={11}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    onKeyDown={(e) => {
                      if (!/^[0-9]+$/.test(e.key) && e.key.length === 1) {
                        e.preventDefault();
                      }
                    }}
                  />
                ) : (
                  <input
                    className="input-basic flex-1 text-y-lightGray"
                    disabled
                    value={phoneNumber}
                  />
                )}
                <button
                  type="submit"
                  className="btn-r text-xs absolute"
                  onClick={(e) => {
                    e.preventDefault();
                    submitPhoneNumber(phoneNumber);
                    setTimerActive(true);
                    setReset(true);
                  }}
                >
                  인증번호 받기
                </button>
              </form>

              <form className="flex justify-end items-center">
                <input
                  placeholder="인증번호"
                  className="input-basic flex-1"
                  value={certificationNumber}
                  onChange={(e) => setCertificationNumber(e.target.value)}
                  onKeyDown={(e) => {
                    if (!/^[0-9]+$/.test(e.key) && e.key.length === 1) {
                      e.preventDefault();
                    }
                  }}
                />
                <Timer active={timerActive} reset={reset} />
                <button
                  className="btn-r text-xs px-10 absolute"
                  onClick={(e) => {
                    e.preventDefault();
                    submitCertificationNumber(phoneNumber, certificationNumber);
                    setTimerActive(false);
                  }}
                >
                  확인
                </button>
              </form>
              {error ? (
                <p className="text-sm text-y-red font-light">
                  인증번호를 다시 확인해주세요
                </p>
              ) : null}
            </div>

            <div className="flex mx-3">
              <button
                className="btn-r flex-1"
                onClick={
                  type === "signout"
                    ? () => {
                        Swal.fire({
                          title: "회원탈퇴",
                          text: "탈퇴시, 작성했던 모든 글이 삭제되고 계정을 복구할 수 없습니다. Cogen을 탈퇴하시겠습니까?",
                          showCancelButton: true,
                          confirmButtonColor: "#E74D47",
                          confirmButtonText: "탈퇴하기",
                          cancelButtonColor: "#A19E9E",
                          cancelButtonText: "계속같이하기",
                        }).then((result) => {
                          if (result.isConfirmed) {
                            axios
                              .delete(`/mypage/signout`)
                              .then((res) => {
                                dispatch(logout());
                                navigate("/");
                              })
                              .catch((err) => console.log(err));
                          } else {
                            navigate("/");
                          }
                        });
                      }
                    : () => {
                        if (
                          checkList.phoneNumber &&
                          checkList.certificationNumber
                        ) {
                          setPassed(true);
                        }
                      }
                }
              >
                다음으로
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
