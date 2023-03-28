import { HiOutlineArrowUp } from "react-icons/hi";
import { useState, useEffect } from "react";

export default function ScrollTopBtn() {
  const [scrollY, setScrollY] = useState(0);
  const [show, setShow] = useState(false);

  const handleFollow = () => {
    setScrollY(window.pageYOffset);
    if (scrollY > 50) setShow(true);
    else setShow(false);
  };

  const handleToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
    setScrollY(0);
    setShow(false);
  };

  useEffect(() => {
    const watch = () => {
      window.addEventListener("scroll", handleFollow);
    };
    watch();
    return () => {
      window.removeEventListener("scroll", handleFollow);
    };
  });

  return (
    <button
      className={
        show
          ? "fixed bottom-[70px] rounded-full bg-y-purple text-white p-2 text-xl flex justify-center items-center shadow-lg shadow-y-lightGray/70"
          : "hidden"
      }
      onClick={handleToTop}
    >
      <HiOutlineArrowUp />
    </button>
  );
}
