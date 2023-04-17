import BigInput from "components/Inputs/BigInput";
import { Select, SelectBoxMatcher } from "../../util/SelectUtil";
import { useState, useEffect } from "react";
import SelectBox from "../../components/SelectBox";
import ImageUpload from "components/ImageUpload";
import axios from "../../api/axios";
import { useNavigate } from "react-router-dom";
// import { Input } from "components/Inputs/Input";
// import { useForm } from "react-hook-form";
// interface IFormValues {
//   name: string;
//   text: string;
// }
export default function Writepost() {
  // const {
  //   register,
  //   getValues,
  //   formState: { errors },
  // } = useForm<IFormValues>({ mode: "onChange" });
  const [inputState, setInputState] = useState<string>("");
  const [content, setContent] = useState<string>("");
  const [category, setCategory] = useState<Select>("");
  const [imageData, setImageData] = useState([]);
  const [contentLength, setContentLength] = useState("");
  const [categoryErr, setCategoryErr] = useState("");
  const [titleErr, setTitleErr] = useState("");
  const navigate = useNavigate();
  // const onValid = (data: any) => {
  //   // 기본으로 data 가져오기
  //   // console.log(data);
  //   const { text } = getValues();
  //   postPost(text);
  // };
  const onInputChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputState(e.target.value);
  };
  // useEffect(() => {
  //   if (content.length >= 3) setContentLength(true);
  //   else setContentLength(false);
  // }, [content]);
  // useEffect(() => {
  //   if (category !== "") setCategoryErr(true);
  //   else setCategoryErr(false);
  // }, [category]);
  // useEffect(() => {
  //   if (inputState.length >= 1) setTitleErr(true);
  //   else setTitleErr(false);
  // }, [inputState.length]);
  const postPost = () => {
    if (content.length < 3) setContentLength("세글자 이상 작성해주세요");

    if (category === "") setCategoryErr("카테고리를 선택해주세요");

    if (inputState.length < 1) setTitleErr("한글자 이상 작성해주세요");

    const jsonData = {
      title: inputState,
      body: content,
      category: category,
    };
    // const formData = new FormData();
    // for (const file of imageData) {
    //   formData.append("files", file);
    // }
    // formData.append(
    //   "post",
    //   new Blob([JSON.stringify(jsonData)], { type: "application/json" })
    // );
    // setFinalData(formData);
    axios
      .post(`/feeds/add`, jsonData)
      .then((res) => {
        const PostId = res.data;
        navigate(`/post/${PostId}`);
      })

      .catch((err) => console.log(err));
  };

  return (
    <>
      <div className="p-3 border-b border-y-lightGray">
        <h1 className="text-center text-xl ">글 작성</h1>
      </div>
      <div>
        <div className="m-2">
          <div className="mb-2 mt-6 text-lg font-semibold">카테고리</div>
          {category === "" ? (
            <div className="text-y-red text-sm">{categoryErr}</div>
          ) : null}
          <SelectBox setSelect={setCategory} type={"category"} />
        </div>
        <div className="m-2">
          <div className="mb-2 mt-2 text-lg font-semibold">제목</div>
          {inputState.length < 1 ? (
            <div className="text-y-red text-sm">{titleErr}</div>
          ) : null}

          <section className="font-light block">
            <input
              className="border border-y-lightGray rounded-xl focus:outline-y-red focus:ring-1 block w-full p-2.5 placeholder-slate-300"
              type="text"
              placeholder="제목을 입력해주세요"
              value={inputState}
              onChange={(e: any) => {
                onInputChange(e);
              }}
            />

            {/* <Input
                name="text"
                type="text"
                placeholder="제목을 입력해주세요!"
                register={register}
              /> */}
          </section>
        </div>

        <ImageUpload imageData={imageData} setImageData={setImageData} />
        <div className="m-2">
          <div className="mb-2 mt-4 text-lg font-semibold">본문</div>
          {content.length < 3 ? (
            <div className="text-y-red text-sm">{contentLength}</div>
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

          <button onClick={postPost} className="flex-1 btn-r">
            등록하기
          </button>
        </div>
      </div>
    </>
  );
}