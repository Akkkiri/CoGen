import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";

const TermsModal = withReactContent(Swal);

export function openTermsModal() {
  TermsModal.fire({
    title: <p>이용 약관</p>,
    html: <p>이용 약관 내용이 여기 길게 들어올 예정입니다.</p>,
  });
}

export function openPrivacyTermsModal() {
  TermsModal.fire({
    title: <p>개인정보 수집 및 이용 안내</p>,
    html: (
      <p>
        개인정보 수집 및 이용 동의에 대한 내용이 여기 길게 들어올 예정입니다.
      </p>
    ),
  });
}
