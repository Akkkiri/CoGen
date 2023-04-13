import BackBtn from "components/BackBtn";

export default function IdentityVerification({
  type,
}: {
  type: "find" | "change" | "signout";
}) {
  const title = {
    find: "비밀번호 찾기",
    change: "비밀번호 변경",
    signout: "회원 탈퇴",
  };
  return (
    <div>
      <BackBtn />
      <h1 className="page-title">{title[type]}</h1>
      <form></form>
    </div>
  );
}
