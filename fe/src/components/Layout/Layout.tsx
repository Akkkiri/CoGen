import Header from "./Header";
import Nav from "./Nav";
import ScrollTopBtn from "./ScrollTopBtn";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <Header />
      <div className="max-w-5xl m-auto">
        {children}
        <div className="flex justify-end mr-2">
          <ScrollTopBtn />
        </div>
        <div className="h-[62px]"></div>
      </div>
      <Nav />
    </div>
  );
}
