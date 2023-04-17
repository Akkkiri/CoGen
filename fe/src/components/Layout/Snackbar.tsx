import { useEffect, useState } from "react";
import { IoHeart, IoChatbubbleEllipses, IoPersonAdd } from "react-icons/io5";
import { Link } from "react-router-dom";

export default function Snackber({
  text,
  type,
  url,
}: {
  text: string;
  type: "LIKE" | "COMMENT" | "FOLLOW";
  url: string;
}) {
  const [isFading, setIsFading] = useState(false);

  useEffect(() => {
    let mounted = true;
    setTimeout(() => {
      if (mounted) {
        setIsFading(true);
      }
    }, 5000);

    return () => {
      mounted = false;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  const icon = {
    LIKE: <IoHeart className="w-2.5" />,
    COMMENT: <IoChatbubbleEllipses className="w-2.5" />,
    FOLLOW: <IoPersonAdd className="w-3" />,
  };

  return (
    <Link
      onClick={() => {
        setIsFading(true);
      }}
      to={url}
      className={`flex justify-center items-center bg-y-pink py-1 px-2 my-1 rounded-lg shadow-lg ${
        isFading ? "opacity-0 transition ease-in-out delay-200 " : ""
      }`}
    >
      <div className="w-4 h-4 rounded-full bg-y-red flex justify-center items-center text-white mr-1">
        {icon[type]}
      </div>
      <div>{text}</div>
    </Link>
  );
}
