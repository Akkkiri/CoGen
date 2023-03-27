import { BiSearch } from "react-icons/bi";
import { HiOutlineBell } from "react-icons/hi";
import { useState } from "react";
import SearchModal from "./SearchModal";
import NotifyModal from "./NotifyModal";
import { Link } from "react-router-dom";

export default function Header() {
  const [isSearching, setIsSearching] = useState(false);
  const [isNotifying, setIsNotifying] = useState(false);

  return (
    <header className="p-4 border-b border-y-lightGray">
      <div className="flex justify-between items-center max-w-5xl m-auto">
        <Link to="/">
          <img src="images/logo.png" alt="CoGen logo" width={60}></img>
        </Link>
        <div className="flex justify-center items-center">
          <button
            className="hover:text-y-red"
            onClick={() => setIsSearching(true)}
          >
            <BiSearch size={30} />
          </button>
          {isSearching ? <SearchModal setIsSearching={setIsSearching} /> : null}
          <button
            className="hover:text-y-red"
            onClick={() => setIsNotifying(true)}
          >
            <HiOutlineBell size={30} />
          </button>
          {isNotifying ? <NotifyModal setIsNotifying={setIsNotifying} /> : null}
        </div>
      </div>
    </header>
  );
}
