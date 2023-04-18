import BackBtn from "components/BackBtn";
import SelectBox from "components/SelectBox";
import { useState, useEffect } from "react";
import { FaChevronRight } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { Select, AgeTypeMatcherToKor } from "util/SelectUtil";
import { genderList } from "page/signup/Info";
import Swal from "sweetalert2";
import axios from "api/axios";
import imageCompression from "browser-image-compression";
import AWS from "aws-sdk";
import {
  AWS_ACCESS_KEY,
  AWS_SECRET_KEY,
  AWS_S3_BUCKET,
  AWS_S3_BUCKET_REGION,
} from "util/AWSInfo";

export default function MyEdit() {
  const navigate = useNavigate();
  const [error, setError] = useState(false);
  const [nickname, setNickname] = useState("");
  const [hashcode, setHashcode] = useState("");
  const [profileImage, setProfileImage] = useState("");
  const [preProfileImage, setPreProfileImage] = useState("");
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [progress, setProgress] = useState(0);
  const [showLoading, setShowLoading] = useState(false);
  const [genderType, setGenderType] = useState("");
  const [ageType, setAgeType] = useState<Select>("");

  AWS.config.update({
    accessKeyId: AWS_ACCESS_KEY,
    secretAccessKey: AWS_SECRET_KEY,
  });

  const s3Bucket = new AWS.S3({
    params: { Bucket: AWS_S3_BUCKET },
    region: AWS_S3_BUCKET_REGION,
  });

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

  const patchRequest = (url?: string) => {
    const patchBody = {
      nickname: nickname + hashcode,
      profileImage: url ? url : profileImage,
      genderType,
      ageType,
    };
    axios
      .patch("/mypage/patch", patchBody)
      .then((res) => {
        if (progress === 0 || progress === 100) {
          navigate("/mypage");
        } else {
          setTimeout(() => navigate("/mypage"), 1000);
        }
      })
      .catch((err) => console.log(err));
  };

  const handleSubmit = () => {
    if (error) {
      Swal.fire({
        text: "닉네임을 다시 입력해주세요",
        confirmButtonColor: "#E74D47",
        confirmButtonText: "확인",
      });
    } else {
      if (selectedFile) {
        uploadFile(selectedFile);
        patchRequest(
          `https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/profileImages/${hashcode.slice(
            1
          )}_${selectedFile.name.replace(/ /g, "")}`
        );
      } else {
        patchRequest();
      }
    }
  };

  const uploadFile = async (file: File) => {
    setProfileImage(
      `https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/profileImages/${hashcode.slice(
        1
      )}_${file.name.replace(/ /g, "")}`
    );
    const params = {
      ACL: "public-read",
      Body: file,
      Bucket: AWS_S3_BUCKET,
      Key:
        "profileImages/" +
        hashcode.slice(1) +
        "_" +
        file.name.replace(/ /g, ""),
    };
    s3Bucket
      .putObject(params)
      .on("httpUploadProgress", (e) => {
        setProgress(Math.round((e.loaded / e.total) * 100));
      })
      .send((err) => {
        if (err) console.log(err);
      });
  };

  const handleImageFile = async (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      let file = e.target.files[0];
      const options = {
        maxSizeMB: 1,
        maxWidthOrHeight: 320,
      };
      if (file.type.toLowerCase() === "image/heic") {
        const heic2any = require("heic2any");
        heic2any({ blob: file, toType: "image/jpeg", quality: 1 }).then(
          async (res: File) => {
            file = new File([res], file.name.split(".")[0] + ".jpeg", {
              type: "image/jpeg",
              lastModified: new Date().getTime(),
            });
            try {
              const compressedFile = await imageCompression(file, options);
              setSelectedFile(compressedFile);
              const preUrl = URL.createObjectURL(compressedFile);
              setPreProfileImage(preUrl);
            } catch (err) {
              Swal.fire({
                title: "Sorry!",
                text: "사진 업로드 중 오류가 발생했습니다. 지원하지 않는 형식의 파일이거나 오류로 인해 업로드가 불가합니다",
                confirmButtonColor: "#E74D47",
                confirmButtonText: "확인",
              });
            }
          }
        );
      } else {
        try {
          const compressedFile = await imageCompression(file, options);
          setSelectedFile(compressedFile);
          const preUrl = URL.createObjectURL(compressedFile);
          setPreProfileImage(preUrl);
        } catch (err) {
          Swal.fire({
            title: "Sorry!",
            text: "사진 업로드 중 오류가 발생했습니다. 지원하지 않는 형식의 파일이거나 오류로 인해 업로드가 불가합니다",
            confirmButtonColor: "#E74D47",
            confirmButtonText: "확인",
          });
        }
      }
      setProgress(0);
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
          src={preProfileImage ? preProfileImage : profileImage}
          alt="profileImage"
          className="rounded-full w-28 h-28"
        ></img>
        <label className="btn-r text-sm">
          프로필 사진 수정하기
          <input
            type="file"
            accept=".jpeg, .jpg, .png, .heic"
            className="hidden"
            onChange={handleImageFile}
          />
        </label>
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
            {progress === 0 || progress === 100
              ? "등록하기"
              : `등록중(${progress})`}
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
