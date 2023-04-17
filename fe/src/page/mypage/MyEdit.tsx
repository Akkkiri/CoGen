import BackBtn from "components/BackBtn";
import SelectBox from "components/SelectBox";
import { useState, useEffect } from "react";
import { FaChevronRight } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { Select, AgeTypeMatcherToKor } from "util/SelectUtil";
import { genderList } from "page/signup/Info";
import Swal from "sweetalert2";
import axios from "api/axios";

export default function MyEdit() {
  const navigate = useNavigate();
  const [error, setError] = useState(false);
  const [nickname, setNickname] = useState("");
  const [hashcode, setHashcode] = useState("");
  const [profileImage, setProfileImage] = useState("");
  const [genderType, setGenderType] = useState("");
  const [ageType, setAgeType] = useState<Select>("");

  useEffect(() => {
    axios
      .get("/mypage")
      .then((res) => {
        setNickname(res.data.nickname);
        setHashcode(res.data.hashcode);
        setProfileImage(res.data.profileImage);
        setGenderType(res.data.genderType);
        setAgeType(res.data.ageType);
      })
      .catch((err) => console.log(err));
  }, []);

  const isVaild = (inputState: string) => {
    if (
      inputState.length > 1 &&
      inputState.match(/^(?=.*\S)[a-zA-Z0-9가-힣]+$/i)
    ) {
      return false;
    } else {
      return true;
    }
  };

  const handleSubmit = () => {
    if (error) {
      Swal.fire({
        text: "닉네임을 다시 입력해주세요",
        confirmButtonColor: "#E74D47",
        confirmButtonText: "확인",
      });
    } else {
      const patchBody = {
        nickname: nickname + hashcode,
        profileImage,
        genderType,
        ageType,
      };
      axios
        .patch("/mypage/patch", patchBody)
        .then((res) => {
          navigate("/mypage");
        })
        .catch((err) => console.log(err));
    }
  };
  return (
    <div>
      <BackBtn />
      <h1 className="page-title">회원정보 수정</h1>
      <div className="flex flex-col justify-center items-center py-3 mt-2 gap-3 border-b border-y-lightGray/30">
        <p className="text-y-lightGray text-sm mb-2">
          등록하기 버튼을 눌러야 변경이 저장됩니다
        </p>
        <img
          src={profileImage}
          alt="profileImage"
          className="rounded-full w-28 h-28"
        ></img>
        <button className="btn-r text-sm">프로필 사진 수정하기</button>
      </div>
      <div>
        <div className="flex justify-between items-center p-4 border-b border-y-lightGray">
          <span>닉네임</span>
          <div>
            <input
              className="input-basic py-1"
              value={nickname}
              maxLength={8}
              onChange={(e) => {
                setNickname(e.target.value);
                if (isVaild(e.target.value)) {
                  setError(true);
                } else {
                  setError(false);
                }
              }}
            ></input>
            {error ? (
              <p className="text-xs text-y-red font-light m-1 ml-2 -mb-3">
                2~8글자 (특수문자,공백X)
              </p>
            ) : null}
          </div>
        </div>
        <div className="flex justify-between items-center px-4 py-2 border-b border-y-lightGray">
          <span>성별</span>
          <div className="w-52 flex gap-1 text-sm border border-y-lightGray rounded-lg p-1">
            {genderList.map((el, idx) => {
              return (
                <div key={idx} className="flex-1 flex">
                  <input
                    type="radio"
                    id={el.eng}
                    name="genderType"
                    value={el.eng}
                    checked={genderType === el.eng ? true : false}
                    className="peer hidden"
                    onChange={(e) => {
                      setGenderType(e.target.value);
                    }}
                  />
                  <label htmlFor={el.eng} className="flex-1 radio-peer">
                    {el.kor}
                  </label>
                </div>
              );
            })}
          </div>
        </div>
        <div className="flex justify-between items-center px-4 py-3 border-b border-y-lightGray">
          <span>연령대</span>
          <SelectBox
            type="ageType"
            setSelect={setAgeType}
            curState={AgeTypeMatcherToKor(ageType)}
          />
        </div>
        <div className="flex justify-between items-center p-2 border-b border-y-lightGray">
          <button
            className="btn-g flex-1"
            onClick={() => {
              navigate("/mypage");
            }}
          >
            돌아가기
          </button>
          <button className="btn-r flex-1" onClick={handleSubmit}>
            등록하기
          </button>
        </div>
        <div
          className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red"
          onClick={() => {
            navigate("pw");
          }}
        >
          비밀번호 수정
          <FaChevronRight />
        </div>
        <div
          className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red"
          onClick={() => {
            navigate("/mypage/edit/signout");
          }}
        >
          회원 탈퇴
          <FaChevronRight />
        </div>
      </div>
    </div>
  );
}
