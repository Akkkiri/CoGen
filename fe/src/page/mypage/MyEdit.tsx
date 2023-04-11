import BackBtn from "components/BackBtn";
import SelectBox from "components/SelectBox";
import { useState } from "react";
import { FaChevronRight } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { Select } from "util/SelectUtil";
import { genderList } from "page/signup/Info";
import axios from "api/axios";

export default function MyEdit() {
  const navigate = useNavigate();
  const [nickname, setNickname] = useState("");
  const [profileImage, setProfileImage] = useState("");
  const [genderType, setGenderType] = useState("");
  const [ageType, setAgeType] = useState<Select>("");
  const handleSubmit = () => {
    const postBody = { nickname, profileImage, genderType, ageType };
    //제거
    console.log(postBody);
    // axios
    //   .post("/mypage/patch", postBody)
    //   .then((res) => {
    //     //제거
    //     console.log("회원정보수정 등록하기", res);
    //     navigate("/mypage");
    //   })
    //   .catch((err) => console.log(err));
  };
  return (
    <div>
      <BackBtn />
      <h1 className="page-title">회원정보 수정</h1>
      <div className="flex flex-col justify-center items-center py-3 mt-5 gap-3 border-b border-y-lightGray/30">
        <img
          src={"/images/user.png"}
          alt="profileImage"
          className="rounded-full w-28 h-28"
        ></img>
        <button className="btn-r text-sm">프로필 사진 수정하기</button>
      </div>
      <div>
        <div className="flex justify-between items-center p-4 border-b border-y-lightGray">
          <span>닉네임</span>
          <input
            className="input-basic py-1"
            value={nickname}
            onChange={(e) => {
              setNickname(e.target.value);
            }}
          ></input>
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
          <SelectBox type="ageType" setSelect={setAgeType} curState={""} />
        </div>
        <div className="flex justify-between items-center p-2 border-b border-y-lightGray">
          <button
            className="btn-g flex-1"
            onClick={() => {
              navigate("/mypage");
            }}
          >
            취소하기
          </button>
          <button className="btn-r flex-1" onClick={handleSubmit}>
            등록하기
          </button>
        </div>
        <div className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red">
          비밀번호 수정
          <FaChevronRight />
        </div>
        <div className="flex justify-between items-center p-4 border-b border-y-lightGray hover:text-y-red">
          회원 탈퇴
          <FaChevronRight />
        </div>
      </div>
    </div>
  );
}
