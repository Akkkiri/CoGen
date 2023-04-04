import axios from "../../api/axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Timer from "../../components/signup/Timer";
import { useAppDispatch } from "store/hook";
import { saveNumber } from "store/modules/authSlice";
import { CgFileDocument } from "react-icons/cg";
import {
  openTermsModal,
  openPrivacyTermsModal,
} from "components/signup/TermsModal";

export default function Signup() {
  const [timerActive, setTimerActive] = useState(false);
  const [reset, setReset] = useState(false);

  const [phoneNumber, setPhoneNumber] = useState("");
  const [certificationNumber, setCertificationNumber] = useState("");
  const [checkBox, setCheckBox] = useState<number[]>([]);
  const [checkList, setCheckList] = useState({
    phoneNumber: false,
    certificationNumber: false,
    checkBox: false,
  });

  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (reset) {
      setTimeout(() => {
        setReset(false);
      }, 1000);
    }
  }, [reset]);

  useEffect(() => {
    if (checkBox.length === 2) {
      setCheckList((prev) => {
        return { ...prev, checkBox: true };
      });
    } else {
      setCheckList((prev) => {
        return { ...prev, checkBox: false };
      });
    }
  }, [checkBox.length]);

  const submitPhoneNumber = (phoneNumber: string) => {
    if (phoneNumber.length === 11) {
      const postBody = { phoneNumber };
      //제거
      // console.log(postBody);
      axios
        .post("/sms/send", postBody)
        .then((_) => setCheckList({ ...checkList, phoneNumber: true }))
        .catch((err) => console.log(err));
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
      //제거
      // console.log(postBody);
      axios
        .post("/sms/verification", postBody)
        .then((res) => {
          setCheckList({ ...checkList, certificationNumber: true });
          //제거
          // console.log(res);
          dispatch(saveNumber(res.data));
        })
        .catch((err) => console.log(err));
    }
  };

  const handleSingleCheck = (checked: boolean, id: number) => {
    if (checked) {
      setCheckBox((prev) => [...prev, id]);
    } else {
      setCheckBox(checkBox.filter((el) => el !== id));
    }
  };

  const handleAllCheck = (checked: boolean) => {
    if (checked) {
      let idArray: number[] = [];
      [{ id: 0 }, { id: 1 }].forEach((el) => idArray.push(el.id));
      setCheckBox(idArray);
    } else {
      setCheckBox([]);
    }
  };

  return (
    <div className="max-w-md m-auto flex flex-col justify-center items-center mt-12 px-4">
      <img src="/images/logo.png" alt="logo" width={90}></img>
      <h1 className="font-bold text-2xl mt-3 mb-6">회원가입</h1>
      <div className="w-full flex flex-col gap-2">
        <form className="flex justify-end items-center">
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

        <div>
          <label className="bg-y-pink p-2 pl-6 rounded-lg flex items-center cursor-pointer">
            <input
              type="checkbox"
              className="mr-2 cursor-pointer"
              id="allcheck"
              name="agree"
              onChange={(e) => handleAllCheck(e.target.checked)}
              checked={checkBox.length === 2 ? true : false}
            />
            전체 약관 동의
          </label>
          <label className="flex items-center px-2 py-1.5 pl-6 cursor-pointer">
            <input
              type="checkbox"
              name="agree"
              className="mr-2 cursor-pointer"
              onChange={(e) => handleSingleCheck(e.target.checked, 0)}
              checked={checkBox.includes(0) ? true : false}
            ></input>
            이용 약관
            <span className="text-sm ml-1 text-y-lightGray font-light">
              {" "}
              (필수)
            </span>
            <button
              type="button"
              className="ml-1 cursor-pointer hover:text-y-red"
              onClick={(e) => {
                e.preventDefault();
                openTermsModal();
              }}
            >
              <CgFileDocument />
            </button>
          </label>
          <label className="flex items-center px-2 py-1.5 pl-6 cursor-pointer">
            <input
              type="checkbox"
              name="agree"
              className="mr-2 cursor-pointer"
              onChange={(e) => handleSingleCheck(e.target.checked, 1)}
              checked={checkBox.includes(1) ? true : false}
            ></input>
            개인정보 수집 및 이용 안내
            <span className="text-sm ml-1 text-y-lightGray font-light">
              (필수)
            </span>
            <button
              type="button"
              className="ml-1 cursor-pointer hover:text-y-red"
              onClick={(e) => {
                e.preventDefault();
                openPrivacyTermsModal();
              }}
            >
              <CgFileDocument />
            </button>
          </label>
        </div>
        <button
          className="btn-r"
          onClick={(e) => {
            e.preventDefault();
            if (
              checkList.phoneNumber &&
              checkList.certificationNumber &&
              checkList.checkBox
            ) {
              navigate("/signup/nickname");
            }
            //제거
            // console.log(checkList);
          }}
        >
          가입하기
        </button>
      </div>
    </div>
  );
}
