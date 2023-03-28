export type Select =
  | "new"
  | "likes"
  | "comments"
  | "WORRY"
  | "TIP"
  | "PLACE"
  | "QUOTE"
  | "HUMOR"
  | "DAILY"
  | "ETC";

export const SelectBoxMatcher = (str: string) => {
  switch (str) {
    case "최신순":
      return "new";
    case "공감순":
      return "likes";
    case "댓글많은순":
      return "comments";
    case "고민":
      return "WORRY";
    case "꿀팁":
      return "TIP";
    case "장소공유":
      return "PLACE";
    case "명언":
      return "QUOTE";
    case "유머":
      return "HUMOR";
    case "일상":
      return "DAILY";
    case "기타":
      return "ETC";
  }
};
