import { useAppSelector } from "store/hook";
import { notifyList } from "store/modules/notifySlice";
import Snackbar from "./Snackbar";

export default function NofiticationCenter() {
  const notificationList = useAppSelector(notifyList);

  return (
    <div className="fixed bottom-20 right-4 z-50 ">
      {notificationList.map((el) => (
        <Snackbar key={el.id} text={el.message} type={el.type} url={el.url} />
      ))}
    </div>
  );
}
