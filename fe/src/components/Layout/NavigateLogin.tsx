import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function NavigateLogin({ url }: { url: string }) {
  const navigate = useNavigate();
  useEffect(() => {
    navigate(url);
  }, [navigate, url]);
  return <></>;
}
