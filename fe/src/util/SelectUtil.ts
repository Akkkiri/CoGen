export type Select =
  | "new"
  | "likes"
  | "view"
  | "ALL"
  | "WORRY"
  | "TIP"
  | "PLACE"
  | "QUOTE"
  | "HUMOR"
  | "DAILY"
  | "ETC"
  | "TEENAGER"
  | "TWENTIES"
  | "THIRTIES"
  | "FORTIES"
  | "FIFTIES"
  | "SIXTIES"
  | "SEVENTIES"
  | "EIGHTIES"
  | "OTHERS"
  | "";

export const SelectBoxMatcher = (str: string) => {
  switch (str) {
    case "최신순":
      return "new";
    case "공감순":
      return "likes";
    case "조회순":
      return "view";
    case "전체":
      return "ALL";
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
    case "10대":
      return "TEENAGER";
    case "20대":
      return "TWENTIES";
    case "30대":
      return "THIRTIES";
    case "40대":
      return "FORTIES";
    case "50대":
      return "FIFTIES";
    case "60대":
      return "SIXTIES";
    case "70대":
      return "SEVENTIES";
    case "80대 이상":
      return "EIGHTIES";
    case "공개안함":
      return "OTHERS";
    case "":
      return "";
  }
};

export const AgeTypeMatcherToKor = (str: string) => {
  switch (str) {
    case "TEENAGER":
      return "10대";
    case "TWENTIES":
      return "20대";
    case "THIRTIES":
      return "30대";
    case "FORTIES":
      return "40대";
    case "FIFTIES":
      return "50대";
    case "SIXTIES":
      return "60대";
    case "SEVENTIES":
      return "70대";
    case "EIGHTIES":
      return "80대 이상";
    case "OTHERS":
      return "공개안함";
  }
};
