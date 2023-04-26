import Level from '../user/Level';
import { isLogin } from '../../store/modules/authSlice';
import { useAppSelector } from '../../store/hook';
import { NavLink } from 'react-router-dom';
import { Point } from 'util/LevelUtil';
import { medal, medalImg } from 'components/user/UserProfile';

export interface UserProfileProps {
  nickname: string;
  hashcode: string;
  profileImage: string;
  level: number;
  ariFactor: Point;
}

export default function MainUser({
  nickname,
  hashcode,
  profileImage,
  level,
  ariFactor,
}: UserProfileProps) {
  const isLoginUser = useAppSelector(isLogin);
  return (
    <div className="p-3 mx-2 my-4 rounded-xl border-2 border-y-lightGray  md:text-lg">
      {isLoginUser ? (
        <NavLink to={`/mypage`}>
          <div className="flex justify-center items-center mx-2">
            <img src={profileImage} alt="profileImage" className="rounded-full w-20 h-20"></img>
            <div className="w-full mx-4 mt-2">
              <div className="flex items-end">
                <span className={`${nickname.length === 8 ? 'text-sm' : ''}`}>{nickname}</span>
                <span className="text-xs text-y-lightGray font-light">{hashcode}</span>
                <img src={medalImg[medal(level)]} alt="level" className="w-5 h-5"></img>
              </div>
              <Level level={level} ariFactor={ariFactor} />
            </div>
          </div>
        </NavLink>
      ) : (
        <div className="p-2 text-center">
          <div className="pb-3 md:text-xl">
            <p>로그인 후 코젠에서</p>
            <p>다양한 활동을 즐겨보세요</p>
          </div>
          <NavLink to="/login">
            <button className="btn-p rounded-lg px-4 py-2 text-white text-sm">로그인</button>
          </NavLink>
        </div>
      )}
    </div>
  );
}
