export const Category = (str: string) => {
  switch (str) {
    case "ALL":
      return "전체";
    case "WORRY":
      return "고민";
    case "TIP":
      return "꿀팁";
    case "PLACE":
      return "장소공유";
    case "QUOTE":
      return "명언";
    case "HUMOR":
      return "유머";
    case "DAILY":
      return "일상";
    case "ETC":
      return "기타";
  }
};
export const CategoryEg = (str: string) => {
  switch (str) {
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
  }
};
