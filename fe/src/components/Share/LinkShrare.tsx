import { HiOutlineLink } from 'react-icons/hi';
import Swal from 'sweetalert2';
import { useLocation } from 'react-router-dom';
export default function LinkShare() {
  const location = useLocation();
  const baseUrl = 'https://www.akkkiri.co.kr';
  const handleCopyClipBoard = async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
      Swal.fire({
        title: 'CoGen',
        text: '링크복사가 완료되었습니다. 게시글을 공유해보세요!',
        showCancelButton: true,
        confirmButtonColor: '#E74D47',
        cancelButtonColor: '#A7A7A7',
        confirmButtonText: '확인',
        cancelButtonText: '취소',
      });
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <HiOutlineLink
      onClick={() => handleCopyClipBoard(`${baseUrl}${location.pathname}`)}
      className="w-10 h-10 cursor-pointer bg-y-pink p-2 rounded-xl m-2"
    />
  );
}
