import BigInput from "components/Inputs/BigInput";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import { useState, useEffect } from "react";
import SelectBox from "../../components/SelectBox";
import ImageUpload from "components/ImageUpload";
import axios from "../../api/axios";
import { useNavigate, useParams } from "react-router-dom";
import Swal from "sweetalert2";
import AWS from "aws-sdk";
import {
  AWS_ACCESS_KEY,
  AWS_SECRET_KEY,
  AWS_S3_BUCKET,
  AWS_S3_BUCKET_REGION,
} from "util/AWSInfo";
import { userId } from "store/modules/authSlice";
export default function EditPost() {
  const { PostId } = useParams();
  const [inputState, setInputState] = useState<string>("");
  const [content, setContent] = useState<string>("");
  const [category, setCategory] = useState<Select>("");
  const [imageData, setImageData] = useState<string[]>([]);
  const [contentLength, setContentLength] = useState("");
  const [categoryErr, setCategoryErr] = useState("");
  const [titleErr, setTitleErr] = useState("");
  const [progress, setProgress] = useState(0);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const navigate = useNavigate();
  const [imgData, setImgData] = useState<string[]>([]);
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
  // const handleSubmit = () => {
  //   if (imgData) {
  //     postPost(imgData);
  //   } else {
  //     postPost([]);
  //   }
  // };
  // const handleUpLoad = () => {
  //   if (selectedFile) {
  //     uploadFile(selectedFile);
  //     const url = `https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/feedImages/_${selectedFile.name.replace(
  //       / /g,
  //       ""
  //     )}`;
  //     setImgData([...imgData, ...[url]]);
  //   }
  // };
  // const handleSubmit = () => {
  //   if (selectedFile) {
  //     uploadFile(selectedFile);
  //     const url = `https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/feedImages/_${selectedFile.name.replace(
  //       / /g,
  //       ""
  //     )}`;
  //     setImgData([...imgData, ...[url]]);
  //     postPost(imgData);
  //   } else {
  //     postPost([]);
  //   }
  // };
  useEffect(() => {
    axios.get(`/feeds/${PostId}`).then((response) => {
      setInputState(response.data.title);
      setContent(response.data.body);
      setCategory(response.data.category);

      // setImage(response.data.imagePath);
      // setImage2(response.data.imagePath2);
      // setImage3(response.data.imagePath3);
    });
  }, [PostId]);
  const handleSubmit = () => {
    if (selectedFile) {
      uploadFile(selectedFile);
      const url = `https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/feedImages/_${selectedFile.name.replace(
        / /g,
        ""
      )}`;

      editPost(url);
    } else {
      editPost();
    }
  };
  const uploadFile = async (file: File) => {
    setImgData([
      `https://ewha-image-bucket.s3.ap-northeast-2.amazonaws.com/feedImages/_${file.name.replace(
        / /g,
        ""
      )}`,
    ]);

    const params = {
      ACL: "public-read",
      Body: file,
      Bucket: AWS_S3_BUCKET,
      Key: "feedImages/" + "_" + file.name.replace(/ /g, ""),
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
  // console.log(imgData);
  const editPost = (url?: string) => {
    if (content.length < 3) setContentLength("세글자 이상 작성해주세요");

    if (category === "") setCategoryErr("카테고리를 선택해주세요");

    if (inputState.length < 1) setTitleErr("한글자 이상 작성해주세요");

    const jsonData = {
      title: inputState,
      body: content,
      category: category,
      imagePath: url ? url : imgData[0],
      // imagePath2: url ? url : imgData[1],
      // imagePath3: url ? url : imgData[2],
    };

    axios
      .patch(`/feeds/${PostId}/edit`, jsonData)
      .then((res) => {
        navigate(`/post/${PostId}`);
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
          setImgData={setImgData}
          imgData={imgData}
          // handleUpLoad={handleUpLoad}
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
