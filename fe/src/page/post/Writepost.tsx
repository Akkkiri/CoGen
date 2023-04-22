import BigInput from "components/Inputs/BigInput";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import { useState, useEffect } from "react";
import SelectBox from "../../components/SelectBox";
import ImageUpload from "components/ImageUpload";
import axios from "../../api/axios";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import AWS from "aws-sdk";
import {
  AWS_ACCESS_KEY,
  AWS_SECRET_KEY,
  AWS_S3_BUCKET,
  AWS_S3_BUCKET_REGION,
} from "util/AWSInfo";
export default function Writepost() {
  const [inputState, setInputState] = useState<string>("");
  const [content, setContent] = useState<string>("");
  const [category, setCategory] = useState<Select>("");
  const [imageData, setImageData] = useState<File[]>([]);
  const [contentLength, setContentLength] = useState("");
  const [categoryErr, setCategoryErr] = useState("");
  const [titleErr, setTitleErr] = useState("");
  const [progress, setProgress] = useState(0);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [imgList, setImgList] = useState<string[]>([]);

  const navigate = useNavigate();
  const onInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputState(e.target.value);
  };
  AWS.config.update({
    accessKeyId: AWS_ACCESS_KEY,
    secretAccessKey: AWS_SECRET_KEY,
  });

  const s3Bucket = new AWS.S3({
    params: { Bucket: AWS_S3_BUCKET },
    region: AWS_S3_BUCKET_REGION,
  });

  const isVaild = () => {
    if (category === "" || inputState === "" || content.length < 3) {
      return false;
    } else {
      return true;
    }
  };

  const handleSubmit = async () => {
    if (isVaild()) {
      if (imageData.length) {
        let imageList = [];
        for (let i = 0; i < imageData.length; i++) {
          const uuid = crypto.randomUUID();
          let url = `https://${AWS_S3_BUCKET}.s3.${AWS_S3_BUCKET_REGION}.amazonaws.com/${
            "feedImages/" + uuid + "_" + imageData[i].name.replace(/ /g, "")
          }`;
          imageList.push(url);
          uploadFile(imageData[i], uuid);
        }
        postPost(imageList);
      } else {
        postPost(["", "", ""]);
      }
    }
  };

  const uploadFile = async (file: File | null, uuid: string) => {
    if (!file) {
      return "";
    }
    // return new Promise<string>((resolve, reject) => {
    const params = {
      ACL: "public-read",
      Body: file,
      Bucket: AWS_S3_BUCKET,
      Key: "feedImages/" + uuid + "_" + file.name.replace(/ /g, ""),
    };
    s3Bucket
      .putObject(params)
      .on("httpUploadProgress", (e) => {
        setProgress(Math.round((e.loaded / e.total) * 100));
      })
      .send((err) => {
        console.log(err);
      });
  };

  const postPost = (urls: string[]) => {
    if (content.length < 3) setContentLength("세글자 이상 작성해주세요");

    if (category === "") setCategoryErr("카테고리를 선택해주세요");

    if (inputState.length < 1) setTitleErr("한글자 이상 작성해주세요");

    const jsonData = {
      title: inputState,
      body: content,
      category: category,
      imagePath: urls[0] || "",
      imagePath2: urls[1] || "",
      imagePath3: urls[2] || "",
    };

    axios
      .post(`/feeds/add`, jsonData)
      .then((res) => {
        navigate(`/post/ALL`);
        navigate(`/post/${res.data}`);
      })

      .catch((err) => console.log(err));
  };

  return (
    <>
      <div className="p-3 border-b border-y-lightGray">
        <h1 className="text-center text-xl md:text-2xl">글 작성</h1>
      </div>
      <div>
        <div className="m-2">
          <div className="mb-2 mt-6 text-lg font-semibold md:text-xl">
            카테고리
          </div>
          {category === "" ? (
            <div className="text-y-red text-sm">{categoryErr}</div>
          ) : null}
          <SelectBox setSelect={setCategory} type={"category"} />
        </div>
        <div className="m-2">
          <div className="mb-2 mt-2 text-lg font-semibold md:text-xl">제목</div>
          {inputState.length < 1 ? (
            <div className="text-y-red text-sm md:text-base">{titleErr}</div>
          ) : null}

          <section className="font-light block">
            <input
              className="border border-y-lightGray rounded-xl focus:outline-y-red focus:ring-1 block w-full p-2.5 placeholder-slate-300 md:text-lg"
              type="text"
              placeholder="제목을 입력해주세요"
              value={inputState}
              onChange={(e: any) => {
                onInputChange(e);
              }}
            />
          </section>
        </div>

        <ImageUpload
          imageData={imageData}
          setImageData={setImageData}
          setSelectedFile={setSelectedFile}
        />
        <div className="m-2">
          <div className="mb-2 mt-4 text-lg font-semibold md:text-xl">본문</div>
          {content.length < 3 ? (
            <div className="text-y-red text-sm md:text-base">
              {contentLength}
            </div>
          ) : null}

          <BigInput
            placeholder="세글자 이상 적어주세요"
            inputState={content}
            setInputState={setContent}
          />
        </div>
        <div className="flex m-2">
          <button
            onClick={() => {
              navigate(-1);
            }}
            className="flex-1 btn-g"
          >
            취소하기
          </button>

          <button onClick={handleSubmit} className="flex-1 btn-r">
            등록하기
          </button>
        </div>
      </div>
    </>
  );
}
