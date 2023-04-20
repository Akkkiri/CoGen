import { IoHeart, IoChatbubbleEllipses, IoPersonAdd } from "react-icons/io5";
import { HiTrash } from "react-icons/hi";
import { ToDateString } from "util/TodateString";

export interface NotifyContainerProps {
  notificationId: number;
  type: "LIKE" | "COMMENT" | "FOLLOW";
  receiverBody: string;
  body: string;
  isRead: boolean;
  createdAt: string;
  url?: string;
  deleteNotify: (notificationId: number) => void;
}

export default function NotifyContainer({
  notificationId,
  body,
  type,
  receiverBody,
  createdAt,
  deleteNotify,
}: NotifyContainerProps) {
  const icon = {
    LIKE: <IoHeart />,
    COMMENT: <IoChatbubbleEllipses />,
    FOLLOW: <IoPersonAdd />,
  };

  return (
    <li className="flex justify-center items-center p-2">
      <div className="w-12 h-12 rounded-full bg-y-red flex justify-center items-center text-white text-2xl">
        {icon[type]}
      </div>
      <div>
        <p className="text-sm max-w-[220px] mx-2">{body}</p>
        <p className="text-xs text-y-lightGray mx-2 font-light">
          {ToDateString(createdAt)}
        </p>
      </div>
      <button
        className="text-y-lightGray hover:text-y-red text-lg"
        onClick={() => {
          deleteNotify(notificationId);
        }}
      >
        <HiTrash />
      </button>
    </li>
  );
}
